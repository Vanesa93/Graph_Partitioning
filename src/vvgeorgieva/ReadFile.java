package vvgeorgieva;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class ReadFile {

	private static BufferedReader br;

	public static void main(String[] args) throws IOException {
		 try {
			 br = new BufferedReader(new FileReader("D:\\workspace\\Graph_Clustering\\src\\vvgeorgieva\\graph.txt"));
			 String line = br.readLine();	
			
			 if (line != null) {
			 //read nodes		
				 String[] nodeNames = line.split(" ");
				 int[] nodes = new int[nodeNames.length];
				 for (int i = 0; i < nodes.length; ++i) {
					 nodes[i] = Integer.parseInt(nodeNames[i]);
				 }
				//create graph
				 Graph g = new Graph(nodes);				 
				 //read edges
				 int y = 0;
					 while(line != null) {
						 String[] tokens = line.split(" ");
						 LinkedList<Integer> linkedList =  new LinkedList<Integer>();
						 for (int i = 0; i < nodes.length; ++i) {
							 int v2 = Integer.parseInt(tokens[i]);
							 linkedList.add(v2);	
						 } 
						 line = br.readLine();
						 g.addEdges(y, linkedList);
						 y++;
					 }
				System.out.println(g.adj);
				
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
