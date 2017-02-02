package vvgeorgieva;

import java.util.ArrayList;

public class Node {
    int id;
    ArrayList<Node> adjacent = new ArrayList<Node>();

    public Node(int id){
        this.id = id;
    }
}
