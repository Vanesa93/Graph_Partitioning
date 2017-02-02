package vvgeorgieva;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Graph {
	  //Map of adjacency lists for each node
    Map<Integer,LinkedList<Integer>> adj;

    public Graph(int[] nodes) {
    	 adj = new HashMap<Integer, LinkedList<Integer>>();
         for (int i = 0; i <= nodes.length-1; ++i) {
             adj.put(i, new LinkedList<Integer>());
         }
	}

	public void addNeighbor(int v1,int v2) {
         adj.get(v1).add(v2);
    }
	
	public void addEdges(int v1,LinkedList<Integer> linkedList) {
        adj.get(v1).addAll(linkedList);
   }
	

    public List<Integer> getNeighbors(int v) {
        return adj.get(v);
    }
}
