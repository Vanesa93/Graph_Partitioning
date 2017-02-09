package graph;

import java.util.ArrayList;

public class Vertex {

    public ArrayList<Edge> neighborhood;
    public String label;
  
   /**
     *
     * @param label The unique label associated with this Vertex
     */

    public Vertex(String label){
       this.label = label;
       this.neighborhood = new ArrayList<Edge>();
    }
   
    /**
     * @param edge The edge to add
     *  add edge to the neighborhood
     */

    public void addNeighbor(Edge edge){
       if(this.neighborhood.contains(edge)){
            return;
        }      
        this.neighborhood.add(edge);
    }
    
    /**
     *
     * @param search for neighbor edge
     * @return if the other is in the neighborhood true
     */

    public boolean containsNeighbor(Edge other){
        return this.neighborhood.contains(other);
    }
    
    /**
     *
     * @param index The index of the Edge to retrieve
     * @return get specified edge by index
     */    
    
    public Edge getNeighbor(int index){
    	return this.neighborhood.get(index);
    }
    
    /**
     *
     * @param index The index of the edge to remove from this.neighborhood
     * @return Edge The removed Edge
     */

    Edge removeNeighbor(int index){
       return this.neighborhood.remove(index);
    }
    
    /**
     *
     * @param e The Edge to remove from this.neighborhood
     */
    
    public void removeNeighbor(Edge e){
        this.neighborhood.remove(e);
   }
    
    /**
     *
     * @return int The number of neighbors of this Vertex
     */

  public int getNeighborCount(){
        return this.neighborhood.size();
    }
  
  /**
    *
    * @return String The label of this Vertex
    */

    public String getLabel(){
        return this.label;

    }
    
    /**
     *
     * @return represents the vertex as string
     */

    public String toString(){
        return "Vertex " + label;
    }

     

    /**
     *
     * @return The hash code of this Vertex's label
     */

   public int hashCode(){
        return this.label.hashCode();
    }

     

   /**
     *
     * @param other The object to compare
     * @return compare two vertex by label
  */

    public boolean equals(Object other){
        if(!(other instanceof Vertex)){
            return false;
     }     
        Vertex v = (Vertex)other;
        return this.label.equals(v.label);
    }

     

    /**
     *
     * @return ArrayList<Edge> A copy of this.neighborhood. 
     */

    public ArrayList<Edge> getNeighbors(){
        return new ArrayList<Edge>(this.neighborhood);
    }

     

}
