package algorithms;	

import java.util.HashMap;
import java.util.LinkedList;

import graph.Edge;
import graph.Graph;
import graph.Vertex;

/**
 * An implentation of the Fiduccia Mattheyses Algorithm
 * @author Vanesa Georgieva
 *
 */

public class FiducciaMattheysesAlgorithm {
  
  /** Performs FiducciaMattheyses on the given graph **/
  public static FiducciaMattheysesAlgorithm process(Graph g) {
    return new FiducciaMattheysesAlgorithm(g);
  }

  
  private static VertexGroup A;
  private static VertexGroup B;
  public VertexGroup getGroupA() { return A; }
  public VertexGroup getGroupB() { return B; }
  
  final private Graph graph;
  public Graph getGraph() { return graph; }
  final private int partitionSize;
  private VertexGroup unSwappedA;
  private VertexGroup unSwappedB;
  // vertex group with all unlocked vertices
  private static  VertexGroup unLockedVertices;
  
  private FiducciaMattheysesAlgorithm(Graph g) {
	  
	    this.graph = g;
	    this.partitionSize = g.getVertices().size() / 2;
	    A = new VertexGroup();
	    B = new VertexGroup();
	    unLockedVertices = new VertexGroup();
	    // Split vertices into A and B
	    int i = 0;
	    // if 2 parts are equal 
	    if((g.getVertices().size() % 2) == 0){
		    for (Vertex v : g.getVertices()) {
		      (++i >= partitionSize ? B : A).add(v);
		      unLockedVertices.add(v);
		    }
		// if 2 parts are not equal
		} else {
		    for (Vertex v : g.getVertices()) {
		  	  (++i > partitionSize ? B : A).add(v);
		  	  unLockedVertices.add(v);
		  	}
	    	
	    }
	    unSwappedA = new VertexGroup(A);
	    unSwappedB = new VertexGroup(B);
	    doAllSwaps(unSwappedA, unSwappedB);
  }
  
  
  /** Performs |V|/2 swaps and chooses the one with least cut cost one 
	 * @param unSwappedA 
	 * @param unSwappedB
	 *
	 **/
  private void doAllSwaps(VertexGroup unSwappedA, VertexGroup unSwappedB) {

    LinkedList<Edge> linkedListEdges = new LinkedList<Edge>();
    double minCost = Double.POSITIVE_INFINITY;
    int minId = -1;
    for (int i = 0; i < partitionSize; i++) {
      double cost = doSingleSwap(linkedListEdges);
      if (cost < minCost) {
        minCost = cost; 
        minId = i; 
      }
    }
    
    // Unwind swaps
    while (linkedListEdges.size() > minId) {   	
      Edge pair = linkedListEdges.pop();
      // swaps vertices from the max pair with highest gain
	      double costA = getVertexCost(pair.one);
	      double costB = getVertexCost(pair.two);  
	      if(costA<costB && unSwappedA.contains(pair.one)){    
	    	  if(A.contains(pair.one)){
	    		  swapVertices(A, pair.one, B);
	    	  }else swapVertices(B, pair.one, A);
	    	  unSwappedA.remove(pair.one);
	      }
	      else if( unLockedVertices.contains(pair.two)) {
	      	  if(A.contains(pair.two)){
	      		swapVertices(A, pair.two, B);
	      	  }else swapVertices(B, pair.two, A);
	      	  unSwappedA.remove(pair.two);
	      }
    }
 }
  
  /** Chooses the least cost swap and performs it depending on the vertex gain**/
  private double doSingleSwap(LinkedList<Edge> linkedListEdges) {

    Edge maxPair = null;
    double maxCost = Double.NEGATIVE_INFINITY;    
    for (Vertex v_a : unSwappedA) {
      for (Vertex v_b : unSwappedB) {
        double costA = getVertexCost(v_a);
        if (costA > maxCost && unLockedVertices.contains(v_a) || unLockedVertices.contains(v_b)) {
          maxPair = new Edge(v_a, v_b);
          maxCost = costA;         
        }
      }
    }
    double costA = getVertexCost(maxPair.one);
    double costB = getVertexCost(maxPair.two);
    if(costA<costB && unLockedVertices.contains(maxPair.one)){
    	if(A.contains(maxPair.one)){
    		swapVertices(A, maxPair.one, B);
    	}else swapVertices(B, maxPair.one, A);
    	unSwappedA.remove(maxPair.one);
    }
    else if(unLockedVertices.contains(maxPair.two)){
    	if(A.contains(maxPair.two)){
    		swapVertices(A, maxPair.two, B);
    	}else swapVertices(B, maxPair.two, A);
    	unSwappedA.remove(maxPair.two);
    }
    
    linkedListEdges.push(maxPair);
    return getCutCost();
  }

  /** Returns gain for the current vertex.
   *  When moving a vertex from within group A, all internal edges become external 
   *  edges and vice versa. **/
  private double getVertexCost(Vertex v) {
//	  the “moving force“ FS(c) is the number of nets connected 
////  to c but not connected to any other cells within c’s partition, 
////  i.e., cut nets that connect only to c, and 
////  the “retention force“ TE(c) is the number of uncut nets connected to c.
    double cost = 0;

    boolean v1isInA = A.contains(v);
    for (Vertex v2 : graph.getVertices()) {      
      boolean v2isInA = A.contains(v2);
      Edge edge = graph.findConnected(v, v2);
      double edge_cost = (edge != null) ? edge.weight : 0;
      if (v1isInA != v2isInA) // external
        cost += edge_cost;  // moving force
      else
        cost -= edge_cost; // retention force
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
  
  /** linkedListEdges swaps v in group x **/
  private static void swapVertices(VertexGroup a, Vertex va, VertexGroup b) {
    if (b.contains(va)) throw new RuntimeException("Invalid swap");
       a.remove(va); 
       b.add(va); 
       // locked vertex
       unLockedVertices.remove(va);
    }
}