package vvgeorgieva;

import java.util.HashSet;

public class VertexGroup extends HashSet<Vertex> {  
  /**
	 * 
	 */
	private static final long serialVersionUID = -5366673520751513159L;
	public VertexGroup(HashSet<Vertex> clone) { super(clone); }
	public VertexGroup() { }
}