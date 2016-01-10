package roadgraph;

import geography.GeographicPoint;

public class MapEdge {
	//from should be considered for deletion later, if it would not have any sense
	private MapNode from;
	private MapNode to;
	private String streetName;
	private Double distance;
	private String roadType;
	
	/**
	 * Create a new MapEdge
	 */
	public MapEdge(MapNode from, MapNode to, String Name, Double distance) {
		this.from = from;
		this.to= to;
		this.streetName = Name;
		this.distance = distance;
	}
	
	MapEdge(String roadName, String roadType,
			MapNode n1, MapNode n2, double length) 
	{
		this.streetName = roadName;
		this.from = n1;
		this.to = n2;
		this.roadType = roadType;
		this.distance= length;
	}
	
	/*
	 * return destination point for the MapEdge
	 */
	public MapNode getDestination() {
		return this.to;
	}
	
	/*
	 * return streetName for the edge
	 */
	public String getStreetName() {
		return this.streetName;
	}
	
	/*
	 * return distance for the edge
	 */
	public Double getDistance() {
		return this.distance;
	}
	
	/*
	 *  given one node in an edge, return the other node
	 */
	MapNode getOtherNode(MapNode node)
	{
		if (node.equals(this.from)) 
			return this.to;
		else if (node.equals(this.to))
			return this.from;
		throw new IllegalArgumentException("Looking for " +
			"a point that is not in the edge");
	}

}
