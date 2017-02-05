package vvgeorgieva;	

import java.util.LinkedList;

/**
 * An implentation of the Kernighan-Lin heuristic algorithm for splitting a graph into 
 * two groups where the weights of the edges between groups (cutting cost) is minimised.
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
    unswappedA = new VertexGroup(A);
    unswappedB = new VertexGroup(B);
    
    System.out.println(A.size()+" "+B.size());
    
    doAllSwaps();
  }
  
  /** Performs |V|/2 swaps and chooses the one with least cut cost one **/
  private void doAllSwaps() {

    LinkedList<Edge> swaps = new LinkedList<Edge>();
    double minCost = Double.POSITIVE_INFINITY;
    int minId = -1;
    
    for (int i = 0; i < partitionSize; i++) {
      double cost = doSingleSwap(swaps);
      if (cost < minCost) {
        minCost = cost; minId = i; 
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
        
        Edge e = graph.findEdge(v_a, v_b);
        double edge_cost = (e != null) ? e.weight : 0;
        // Calculate the gain in cost if these vertices were swapped
        // subtract 2*edge_cost because this edge will still be an external edge
        // after swapping
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
   *  edges and vice versa. **/
  private double getVertexCost(Vertex v) {
    
    double cost = 0;

    boolean v1isInA = A.contains(v);
    
    for (Vertex v2 : graph.getVertices()) {
      
      boolean v2isInA = A.contains(v2);
      Edge edge = graph.findEdge(v, v2);
      
      if (v1isInA != v2isInA) // external
        cost += edge.weight;
      else
        cost -= edge.weight;
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
//package vvgeorgieva;	
//
//import java.util.LinkedList;
//
///**
// * An implentation of the Kernighan-Lin heuristic algorithm for splitting a graph into 
// * two groups where the weights of the edges between groups (cutting cost) is minimised.
// * @author Vanesa Georgieva
// *
// */
//public class KernighanLinAlgorithm {
//  
//  /** Performs KerninghanLin on the given graph **/
//  public static KernighanLinAlgorithm process(Graph g) {
//    return new KernighanLinAlgorithm(g);
//  }
//  
//  final private Graph A, B;
//  final private VertexGroup unswappedA, unswappedB;
//  public Graph getGroupA() { return A; }
//  public Graph getGroupB() { return B; }
//  
//  final private Graph graph;
//  public Graph getGraph() { return graph; }
//  final private int partitionSize;
//  
//  private KernighanLinAlgorithm(Graph g) {
//    this.graph = g;
//    this.partitionSize = g.getVertices().size() / 2;
//    
//    if (g.getVertices().size() != partitionSize * 2) 
//      throw new RuntimeException("Size of vertices must be even");
//    
//    A = new Graph();
//    B = new Graph();
//    // Split vertices into A and B
//    int i = 0;
//    for (Vertex v : g.getVertices()) {
//      (++i > partitionSize ? B : A).addVertex(v, true);
//    }    
//    unswappedA = new VertexGroup(A.getVertices());
//    unswappedB = new VertexGroup(B.getVertices());
//    doAllSwaps();
//  }
//  
//  /** Performs |V|/2 swaps and chooses the one with least cut cost one **/
//  private void doAllSwaps() {
//
//    LinkedList<Edge> swaps = new LinkedList<Edge>();
//    double minCost = Double.POSITIVE_INFINITY;
//    int minId = -1;
//    
//    for (int i = 0; i < partitionSize; i++) {
//      double cost = doSingleSwap(swaps);
//      if (cost < minCost) {
//        minCost = cost; minId = i; 
//      }
//    }
//    // Unwind swaps
//    while (swaps.size()-1 > minId) {
//      Edge pair = swaps.pop();
//      // unswap
//      swapVertices(A, pair.two, B, pair.one);
//    } 
//   }
//  
//  /** Chooses the least cost swap and performs it **/
//  private double doSingleSwap(LinkedList<Edge> swaps) {
//    
//    Edge maxPair = null;
//    double maxGain = Double.NEGATIVE_INFINITY;
//    
//    for (Vertex v_a : unswappedA.vertices) {
//      for (Vertex v_b : unswappedB.vertices) {
//        
//        Edge e = graph.findEdge(v_a, v_b);
//        double edge_cost = (e != null) ? e.weight : 0;
//        // Calculate the gain in cost if these vertices were swapped
//        // subtract 2*edge_cost because this edge will still be an external edge
//        // after swapping
//        double gain = getVertexCost(v_a) + getVertexCost(v_b) - 2 * edge_cost;
//        
//        if (gain > maxGain) {
//          maxPair = new Edge(v_a, v_b);
//          maxGain = gain;
//        }         
//      }
//    }
//    
//    swapVertices(A, maxPair.one, B, maxPair.two);
//    swaps.push(maxPair);
//    
//    unswappedA.vertices.remove(maxPair.one);
//    unswappedB.vertices.remove(maxPair.two);
//    
//    return getCutCost();
//  }
//
//  /** Returns the difference of external cost and internal cost of this vertex.
//   *  When moving a vertex from within group A, all internal edges become external 
//   *  edges and vice versa. **/
//  private double getVertexCost(Vertex v) {
//    
//    double cost = 0;
//
//    boolean v1isInA = A.getVertices().contains(v);
//    
//    for (Vertex v2 : graph.getVertices()) {
//      
//      boolean v2isInA = A.getVertices().contains(v2);
//      Edge edge = graph.findEdge(v, v2);      
//      if (v1isInA != v2isInA) // external
//        cost += edge.weight;
//      else
//        cost -= edge.weight;
//    }
//    return cost;
//  }
//  
//  /** Returns the sum of the costs of all edges between A and B **/
//  public double getCutCost() {
//    double cost = 0;
//
//    for (Edge edge : graph.getEdges()) {     
//      boolean firstInA = A.getVertices().contains(edge.one);
//      boolean secondInA= A.getVertices().contains(edge.two);
//      
//      if (firstInA != secondInA) // external
//        cost += edge.weight;
//    }
//    return cost;
//  }
//  
//  /** Swaps va and vb in groups a and b **/
//  private static void swapVertices(Graph a2, Vertex va, Graph b2, Vertex vb) {
//    if (!a2.getVertices().contains(va) || a2.getVertices().contains(vb) ||
//        !b2.getVertices().contains(vb) || b2.getVertices().contains(va)) throw new RuntimeException("Invalid swap");
//    a2.removeVertex(va.label); a2.addVertex(vb, true);
//    b2.removeVertex(vb.label); b2.addVertex(va, true);
//  }
//}
