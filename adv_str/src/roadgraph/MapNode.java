/**
 * A class to represent a node in the map
 */
package roadgraph;

import java.util.HashSet;
import java.util.Set;

import geography.GeographicPoint;

/**
 * Class representing a vertex (or node) in our MapGraph
 */
@SuppressWarnings("rawtypes")
class MapNode implements Comparable
{
	private HashSet<MapEdge> edges;
	private GeographicPoint location;
	//predicted distance to node explored
	private double distance;
	//actual distance to node explored
	private double actualDistance;
	
	MapNode(GeographicPoint loc)
	{
		location = loc;
		edges = new HashSet<MapEdge>();
		distance = 0.0;
		actualDistance = 0.0;
	}
		
	void addEdge(MapEdge edge) {
		edges.add(edge);
	}
	
	/** Return the neighbors of this MapNode */
	Set<MapNode> getNeighbors()
	{
		Set<MapNode> neighbors = new HashSet<MapNode>();
		for (MapEdge edge : edges) {
			neighbors.add(edge.getOtherNode(this));
		}
		return neighbors;
	}
	
	
	/** get the location of a node */
	GeographicPoint getLocation()
	{
		return location;
	}
	
	/** return the edges out of this node */
	Set<MapEdge> getEdges()
	{
		return edges;
	}
	
	/** Returns whether two nodes are equal.
	 * Nodes are considered equal if their locations are the same, 
	 * even if their street list is different.
	 */
	public boolean equals(Object o)
	{
		if (!(o instanceof MapNode) || (o == null)) {
			return false;
		}
		MapNode node = (MapNode)o;
		return node.location.equals(this.location);
	}
	
	/** Because we compare nodes using their location, we also 
	 * may use their location for HashCode.
	 * @return The HashCode for this node, which is the HashCode for the 
	 * underlying point
	 */
	public int HashCode()
	{
		return location.hashCode();
	}
	
	/** ToString to print out a MapNode method
	 *  @return the string representation of a MapNode
	 */
	public String toString()
	{
		String toReturn = "[NODE at location (" + location + ")";
		toReturn += " intersects streets: ";
		for (MapEdge e: edges) {
			toReturn += e.getStreetName() + ", ";
		}
		toReturn += "]";
		return toReturn;
	}

	// For debugging, output roadNames as a String.
	public String roadNamesAsString()
	{
		String toReturn = "(";
		for (MapEdge e: edges) {
			toReturn += e.getStreetName() + ", ";
		}
		toReturn += ")";
		return toReturn;
	}

	//  WEEK 3 SOLUTIONS 
	
	// get node distance (predicted)
	public double getDistance() {
		return this.distance;
	}
	
	// set node distance (predicted)
	public void setDistance(double distance) {
	    this.distance = distance;
	}

	// get node distance (actual)
	public double getActualDistance() {
		return this.actualDistance;
	}
	
	// set node distance (actual)	
	public void setActualDistance(double actualDistance) {
	    this.actualDistance = actualDistance;
	}
	
    // Code to implement Comparable
	public int compareTo(Object o) {
		// convert to map node, may throw exception
		MapNode m = (MapNode)o; 
		return ((Double)this.getDistance()).compareTo((Double) m.getDistance());
	}
}
