package roadgraph;

import geography.GeographicPoint;

public class MapEdge {
	//from should be considered for deletion later, if it would not have any sense
	private GeographicPoint from;
	private GeographicPoint to;
	private String streetName;
	private Double distance;
	
	/*
	 * Create a new MapEdge
	 */
	public MapEdge(GeographicPoint from, GeographicPoint to, String Name, Double distance) {
		this.from = from;
		this.to= to;
		this.streetName = Name;
		this.distance = distance;
	}
	
	/*
	 * return destination point for the MapEdge
	 */
	public GeographicPoint getDestination() {
		return this.to;
	}
	
	/*
	 * return streetName for the edge
	 */
	public String getStreetName() {
		return this.streetName;
	}

}
