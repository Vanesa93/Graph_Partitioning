package algorithms;	

import java.util.LinkedList;

import graph.Edge;
import graph.Graph;
import graph.Vertex;

/**
 * Kernighan-Lin - splitting a graph into 
 * two groups where the weights of the edges between groups (cutting cost) is minimised
 * @author Vanesa Georgieva
 *
 */

public class KernighanLinAlgorithm {
  
  /** Performs KerninghanLin on the given graph **/
  public static KernighanLinAlgorithm process(Graph g) {
    return new KernighanLinAlgorithm(g);
  }

  
  final private VertexGroup A, B;
  final private VertexGroup unswappedA, unswappedB;
  public VertexGroup getGroupA() { return A; }
  public VertexGroup getGroupB() { return B; }
  
  final private Graph graph;
  public Graph getGraph() { return graph; }
  final private int partitionSize;
  
  
  private KernighanLinAlgorithm(Graph g) {
    this.graph = g;
    this.partitionSize = g.getVertices().size() / 2;
    if (g.getVertices().size() != partitionSize * 2) 
      throw new RuntimeException("Size of vertices must be even");
    
    A = new VertexGroup();
    B = new VertexGroup();
    // Split vertices into A and B
    int i = 0;
    for (Vertex v : g.getVertices()) {
      (++i > partitionSize ? B : A).add(v);
    }
    
    // create new unSwapped groups for processing
    unswappedA = new VertexGroup(A);
    unswappedB = new VertexGroup(B);
    
    doAllSwaps();
  }
  
  /** Performs swaps(half of graph vertices) and chooses the one with least cut cost one **/
  private void doAllSwaps() {

    LinkedList<Edge> swaps = new LinkedList<Edge>();
    double minCost = Double.POSITIVE_INFINITY;
    int minId = -1;
    
    for (int i = 0; i < partitionSize; i++) {
      double cost = doSingleSwap(swaps);
      if (cost < minCost) {
        minCost = cost; 
        minId = i; 
      }
    }
    // Unwind swaps
    while (swaps.size()-1 > minId) {
      Edge pair = swaps.pop();
      // unswap
      swapVertices(A, pair.two, B, pair.one);
    }
  }
  
  /** Chooses the least cost swap and performs it **/
  private double doSingleSwap(LinkedList<Edge> swaps) {
   
    Edge maxPair = null;
    double maxGain = Double.NEGATIVE_INFINITY;    
    for (Vertex v_a : unswappedA) {
      for (Vertex v_b : unswappedB) {        
        Edge e = graph.findConnected(v_a, v_b);
        double edge_cost = (e != null) ? e.weight : 0;
        // Calculate the gain in cost if these vertices were swappeds
        double gain = getVertexCost(v_a) + getVertexCost(v_b) - 2 * edge_cost;
        
        if (gain > maxGain) {
          maxPair = new Edge(v_a, v_b);
          maxGain = gain;
        }
      }
    }
    
    swapVertices(A, maxPair.one, B, maxPair.two);
    swaps.push(maxPair);
    unswappedA.remove(maxPair.one);
    unswappedB.remove(maxPair.two);
    
    return getCutCost();
  }

  /** Returns the difference of external cost and internal cost of this vertex.
   *  When moving a vertex from within group A, all internal edges become external 
   *  edges and the opposite **/
  private double getVertexCost(Vertex v) {
    
    double cost = 0;

    boolean v1isInA = A.contains(v);
    
    for (Vertex v2 : graph.getVertices()) {      
      boolean v2isInA = A.contains(v2);
      Edge edge = graph.findConnected(v, v2);
      double edge_cost = (edge != null) ? edge.weight : 0;
      if (v1isInA != v2isInA) // external
        cost += edge_cost;
      else
        cost -= edge_cost;
    }
    return cost;
  }
  
  /** Returns the sum of the costs of all edges between A and B **/
  public double getCutCost() {
    double cost = 0;
    for (Edge edge : graph.getEdges()) {     
      boolean firstInA = A.contains(edge.one);
      boolean secondInA= A.contains(edge.two);
      if (firstInA != secondInA) // external
        cost += edge.weight;
    }
    return cost;
  }
  
  /** Swaps va and vb in groups a and b **/
  private static void swapVertices(VertexGroup a, Vertex va, VertexGroup b, Vertex vb) {
    if (!a.contains(va) || a.contains(vb) ||
        !b.contains(vb) || b.contains(va)) throw new RuntimeException("Invalid swap");
    a.remove(va); a.add(vb);
    b.remove(vb); b.add(va);
  }
}