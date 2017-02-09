package graph;

public class Edge implements Comparable<Edge> {

	public Vertex one, two;
    public int weight;   

    /**
     * @param one The first vertex in the Edge
     * @param two The second vertex in the Edge
     */

    public Edge(Vertex one, Vertex two){
        this(one, two, 1);
    } 

    /**
     *
     * @param one The first vertex in the Edge
     * @param two The second vertex of the Edge
     * @param weight The weight of this Edge
     */

    public Edge(Vertex one, Vertex two, int weight){
        this.one = (one.getLabel().compareTo(two.getLabel()) <= 0) ? one : two;
        this.two = (this.one == one) ? two : one;
        this.weight = weight;
    }

    /**
     *
     * @param current
     * @return The neighbor of current vertex along this Edge
     */

    public Vertex getNeighbor(Vertex current){
        if(!(current.equals(one) || current.equals(two))){
            return null;
        }
        return (current.equals(one)) ? two : one;
    }


    /**
     *
     * @return first vertex from this edge
     */

    public Vertex getOne(){
        return this.one;
   }
    /**
     *
     * @return second vertex in this edge
     */

    public Vertex getTwo(){
        return this.two;
    }
    
    /**
     *
     * @return weight of this Edge
     */

    public int getWeight(){
        return this.weight;
    }  
  
    /**
     *
     * @param set new weight of this Edge
     */

    public void setWeight(int weight){
       this.weight = weight;
    }
    
    /**
     *
     * @param other edge for comparing
     * @return int this.weight - other.weight

     */

    public int compareTo(Edge other){
        return this.weight - other.weight;
    }

    /**
     *
     * @return this edge represent as a string
     */

    public String toString(){
        return "({" + one + ", " + two + "}, " + weight + ")";
   }   

    /**
     *
     * @return hash code for this Edge - integer
     */

    public int hashCode(){
        return (one.getLabel() + two.getLabel()).hashCode();
    }
    
    /**
     * compares two edges
     * @param other edge for comparing
     * @return search for edge with same vertices
    */
    
    public boolean equals(Object other){
       if(!(other instanceof Edge)){    
           return false;
           }
       Edge e = (Edge)other;
       return e.one.equals(this.one) && e.two.equals(this.two);
    }  
}

