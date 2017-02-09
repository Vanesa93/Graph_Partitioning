package vvgeorgieva;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FiducciaMattheysesProcessFile {
	private static BufferedReader br;
	 public static void runKerninghanLin(){

	    Graph graph = new Graph();
	        //initialize some vertices and add them to the graph
	      try {
				 br = new BufferedReader(new FileReader("D:\\workspace\\Graph_Clustering\\src\\vvgeorgieva\\graphE.txt"));
				 String line = br.readLine();
				 if (line != null) {
					 String[] verticesNames = line.split(" ");
					 Vertex[] vertices = new Vertex[verticesNames.length];
					 for(int i = 0; i < vertices.length; i++){						 
						 vertices[i] = new Vertex("" + i);
			             graph.addVertex(vertices[i], true);
			         }
					 
					 int currentVertex = 0;
					 while(line != null) {
						 String[] tokens = line.split(" ");
						 for(int i = 0; i < tokens.length; i++) {
							 int currentToken = Integer.parseInt(tokens[i]);							 
							 if(currentToken == 1){
								    graph.addEdge(vertices[currentVertex], vertices[i]);
								 }
						 } 
						 currentVertex++;
						
						 line = br.readLine();
					 }
					 System.out.println(graph.getEdges());
					 FiducciaMattheysesAlgorithm k = FiducciaMattheysesAlgorithm.process(graph);					
					 System.out.println("Cluster 1");
					 System.out.println(k.getGroupA());
					 System.out.println("Cluster 2");
					 System.out.println(k.getGroupB());
					 
			 }
					 br.close();

	        } catch (FileNotFoundException ex) {
	    		
	   		 ex.printStackTrace();
	   		
	   		 } catch (IOException ex) {
	   		
	   		 ex.printStackTrace();
	   		
	   		 } catch (Exception e) {
	   		
	   		 System.out.println("Error: " + e);
	   		
	   		 }
	    }
}
