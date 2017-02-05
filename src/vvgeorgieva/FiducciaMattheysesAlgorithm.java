package vvgeorgieva;	

import java.util.LinkedList;

/**
 * An implentation of the Kernighan-Lin heuristic algorithm for splitting a graph into 
 * two groups where the weights of the edges between groups (cutting cost) is minimised.
 * @author Vanesa Georgieva
 *
 */
public class FiducciaMattheysesAlgorithm {
  
  /** Performs FiducciaMattheyses on the given graph **/
  public static FiducciaMattheysesAlgorithm process(Graph g) {
    return new FiducciaMattheysesAlgorithm(g);
  }

  
  private VertexGroup A;
private static VertexGroup B;
  public VertexGroup getGroupA() { return A; }
  public VertexGroup getGroupB() { return B; }
  
  final private Graph graph;
  public Graph getGraph() { return graph; }
  final private int partitionSize;
  private VertexGroup groupToSwap;
private VertexGroup groupToAdd;
  
  private FiducciaMattheysesAlgorithm(Graph g) {

	  this.graph = g;
	  this.partitionSize = g.getVertices().size() / 2;
	  
	  A = new VertexGroup();
	  B = new VertexGroup();
	  
	  // Split vertices into A and B
	  int i = 0;
	  
	  for (Vertex v : g.getVertices()) {
		  (++i > partitionSize ? B : A).add(v);
	   }
	  
	  System.out.println(A.size()+" "+B.size());
	  for (int y = 0; y<graph.getVertices().size(); y++){
		  groupToSwap= B.size() > A.size() ? B : A;
		  groupToAdd= B.size() < A.size() ? B : A;
		  System.out.println(groupToSwap);
		  System.out.println(groupToAdd);

		  doAllSwaps(groupToSwap, groupToAdd);
	  }
	  
  }
  
  /** Performs |V|/2 swaps and chooses the one with least cut cost one **/
  private void doAllSwaps(VertexGroup groupToSwap, VertexGroup groupToAdd) {

    LinkedList<Vertex> swaps = new LinkedList<Vertex>();
    
    for (int i = 0; i < groupToSwap.size(); i++) {
       doSingleSwap(swaps, groupToSwap, groupToAdd);
    }
   }
  
  /** Chooses the least cost swap and performs it **/
  private void doSingleSwap(LinkedList<Vertex> swaps, VertexGroup groupToSwap, VertexGroup groupToAdd) {
    
    Vertex vertexToSwap = null;
    double maxGain = Double.NEGATIVE_INFINITY;
    
    for (Vertex v_a : groupToSwap) {
        double gain = getVertexGain(v_a,groupToSwap);
        
        if (gain > maxGain) {
          vertexToSwap = new Vertex(v_a.label);
          maxGain = gain;
        }
    }
    
    swapVertices(groupToSwap, vertexToSwap, groupToAdd);
    swaps.push(vertexToSwap);
  }

  /** Returns the difference of external cost and internal cost of this vertex.
   *  When moving a vertex from within group A, all internal edges become external 
   *  edges and vice versa. 
 * @param groupToSwap2 **/
  private double getVertexGain(Vertex v, VertexGroup groupToSwap) {  
//	  the “moving force“ FS(c) is the number of nets connected 
//	  to c but not connected to any other cells within c’s partition, 
//	  i.e., cut nets that connect only to c, and 
//	  the “retention force“ TE(c) is the number of uncut nets connected to c.

    double movingForce = 0;
    double retentionForce = 0;
    
    for (Vertex v2 : graph.getVertices()) {      
      Edge edge = graph.findEdge(v, v2); 
      boolean v2IsInGroupToSwap = groupToSwap.contains(v2);
      if(edge != null && !v2IsInGroupToSwap) // connected
    	 movingForce++;
      if(edge != null && v2IsInGroupToSwap)
    	  retentionForce++;
      
     
    }
    return movingForce-retentionForce;
    
//    for (Vertex v2 : graph.getVertices()) {      
//      Edge edge = graph.findEdge(v, v2); 
//      if(edge = 0)
//      if (!vIsInGroupToSwap) // external
//        cost += edge.weight;
//      else
//        cost -= edge.weight;
//    }
//    return cost;
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
  
  /** Swaps va and vb in groups a and b 
 * @param b **/
  private static void swapVertices(VertexGroup a, Vertex va, VertexGroup b) {
    if (b.contains(va)) throw new RuntimeException("Invalid swap");
    a.remove(va);
    b.add(va);
  }
}
