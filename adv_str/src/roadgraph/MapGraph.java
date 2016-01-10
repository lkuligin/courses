/**
 * @author UCSD MOOC development team and YOU
 * 
 * A class which represents a graph of geographic locations
 * Nodes in the graph are intersections between 
 *
 */
package roadgraph;


import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.function.Consumer;

import geography.GeographicPoint;
import util.GraphLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * @author UCSD MOOC development team and YOU
 * 
 * A class which represents a graph of geographic locations
 * Nodes in the graph are intersections between 
 *
 */
public class MapGraph {
	private HashMap<GeographicPoint,MapNode> pointNodeMap;
	private HashSet<MapEdge> edges;
	
	/** 
	 * Create a new empty MapGraph 
	 */
	public MapGraph()
	{
		pointNodeMap = new HashMap<GeographicPoint,MapNode>();
		edges = new HashSet<MapEdge>();
	}
	
	/**
	 * Get the number of vertices (road intersections) in the graph
	 * @return The number of vertices in the graph.
	 */
	public int getNumVertices()
	{
		return this.pointNodeMap.values().size();
	}
	
	/**
	 * Return the intersections, which are the vertices in this graph.
	 * @return The vertices in this graph as GeographicPoints
	 */
	public Set<GeographicPoint> getVertices()
	{
		return this.pointNodeMap.keySet();
	}
	
	/**
	 * Get the number of road segments in the graph
	 * @return The number of edges in the graph.
	 */
	public Integer getNumEdges()
	{
		//we have a directed graph -> for each vertice we sum up the amount of outgoing edges 
		//return getVertices().stream().mapToInt(x -> vertices.get(x).size()).sum();
		return this.edges.size();
	}

	/** Add a node corresponding to an intersection at a Geographic Point
	 * If the location is already in the graph or null, this method does 
	 * not change the graph.
	 * @param location  The location of the intersection
	 * @return true if a node was added, false if it was not (the node
	 * was already in the graph, or the parameter is null).
	 */
	public void addVertex(GeographicPoint location)
	{
		MapNode n = pointNodeMap.get(location);
		if (n == null) {
			n = new MapNode(location);
			pointNodeMap.put(location, n);
		}
		else {
			System.out.println("Warning: Node at location " + location +
					" already exists in the graph.");
		}
	}
	
	/**
	 * Adds a directed edge to the graph from pt1 to pt2.  
	 * Precondition: Both GeographicPoints have already been added to the graph
	 * @param from The starting point of the edge
	 * @param to The ending point of the edge
	 * @param roadName The name of the road
	 * @param roadType The type of the road
	 * @param length The length of the road, in km
	 * @throws IllegalArgumentException If the points have not already been
	 *   added as nodes to the graph, if any of the arguments is null,
	 *   or if the length is less than 0.
	 */
	public void addEdge(GeographicPoint pt1, GeographicPoint pt2, String roadName,
			String roadType, double length) throws IllegalArgumentException {
		MapNode n1 = this.pointNodeMap.get(pt1);
		MapNode n2 = this.pointNodeMap.get(pt2);

		// check nodes are valid
		if (n1 == null)
			throw new NullPointerException("addEdge: pt1:"+pt1+"is not in graph");
		if (n2 == null)
			throw new NullPointerException("addEdge: pt2:"+pt2+"is not in graph");

		addEdge(n1, n2, roadName, roadType, length);
	}
	
	/**
	 * Given a point, return if there is a corresponding MapNode 
	 */ 
	public boolean isNode(GeographicPoint point)
	{
		return pointNodeMap.containsKey(point);
	}

	/**
	 *  Add an edge when you already know the nodes involved in the edge
	 * @param n1
	 * @param n2
	 * @param roadName
	 * @param roadType
	 * @param length
	 */
	private void addEdge(MapNode n1, MapNode n2, String roadName,
			String roadType,  double length)
	{
		MapEdge edge = new MapEdge(roadName, roadType, n1, n2, length);
		edges.add(edge);
		n1.addEdge(edge);
	}
	

	/** Find the path from start to goal using breadth first search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest (unweighted)
	 *   path from start to goal (including both start and goal).
	 */
	public List<GeographicPoint> bfs(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
        Consumer<GeographicPoint> temp = (x) -> {};
        return bfs(start, goal, temp);
	}
	
	/* reconstruct path after bfs
	 * @param start the starting location
	 * @param goal the endpoint location
	 * @param parent hashmap (the result of bfs search)s 
	 * @return a list of path
	 */
	
	private List<GeographicPoint> reconstructPath(HashMap<MapNode,MapNode> parentMap,
					MapNode start, MapNode goal)
	{
		LinkedList<GeographicPoint> path = new LinkedList<GeographicPoint>();
		MapNode current = goal;

		while (!current.equals(start)) {
			path.addFirst(current.getLocation());
			current = parentMap.get(current);
		}

		// add start
		path.addFirst(start.getLocation());
		return path;
	}
	
	// get a set of neighbor nodes from a mapnode
	private Set<MapNode> getNeighbors(MapNode node) {
		return node.getNeighbors();
	}
		

	/** Find the path from start to goal using Breadth First Search
	 *
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest path from
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> bfs(GeographicPoint start, GeographicPoint goal,
									Consumer<GeographicPoint> nodeSearched)
	{
		// Setup - check validity of inputs
		if (start == null || goal == null)
			throw new NullPointerException("Cannot find route from or to null node");
		MapNode startNode = pointNodeMap.get(start);
		MapNode endNode = pointNodeMap.get(goal);
		if (startNode == null) {
			System.err.println("Start node " + start + " does not exist");
			return null;
		}
		if (endNode == null) {
			System.err.println("End node " + goal + " does not exist");
			return null;
		}

		// setup to begin BFS
		HashMap<MapNode,MapNode> parentMap = new HashMap<MapNode,MapNode>();
		Queue<MapNode> toExplore = new LinkedList<MapNode>();
		HashSet<MapNode> visited = new HashSet<MapNode>();
		toExplore.add(startNode);
		MapNode next = null;

		while (!toExplore.isEmpty()) {
			next = toExplore.remove();
			
			 // hook for visualization
			nodeSearched.accept(next.getLocation());
			
			if (next.equals(endNode)) break;
			Set<MapNode> neighbors = getNeighbors(next);
			for (MapNode neighbor : neighbors) {
				if (!visited.contains(neighbor)) {
					visited.add(neighbor);
					parentMap.put(neighbor, next);
					toExplore.add(neighbor);
				}
			}
		}
		if (!next.equals(endNode)) {
			System.out.println("No path found from " +start+ " to " + goal);
			return null;
		}
		
		// Reconstruct the parent path
		List<GeographicPoint> path =
				reconstructPath(parentMap, startNode, endNode);

		return path;
	}	

	/** Find the path from start to goal using Dijkstra's algorithm
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> dijkstra(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
		// You do not need to change this method.
        Consumer<GeographicPoint> temp = (x) -> {};
        return dijkstra(start, goal, temp);
	}
	
	/**return distance to MapNode*/
	public Double getDistanceFromTo(MapNode From, MapNode To) {
		for (MapEdge edge: From.getEdges()) {
			if (edge.getOtherNode(From).equals(To)) {
				return edge.getDistance();
			}
		}		
		return null;
	}
	
	/** Find the path from start to goal using Dijkstra's algorithm
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> dijkstra(GeographicPoint start, 
										  GeographicPoint goal, Consumer<GeographicPoint> nodeSearched)
	{
		// Setup - check validity of inputs
		if (start == null || goal == null)
			throw new NullPointerException("Cannot find route from or to null node");
		MapNode startNode = pointNodeMap.get(start);
		MapNode endNode = pointNodeMap.get(goal);
		if (startNode == null) {
			System.err.println("Start node " + start + " does not exist");
			return null;
		}
		if (endNode == null) {
			System.err.println("End node " + goal + " does not exist");
			return null;
		}

		// setup to begin dijkstra
		HashMap<MapNode,MapNode> parentMap = new HashMap<MapNode,MapNode>();
		PriorityQueue<MapNode> toExplore = new PriorityQueue<MapNode>();
		HashSet<MapNode> visited = new HashSet<MapNode>();
		startNode.setDistance(0);
		toExplore.add(startNode);
		for (GeographicPoint pt : getVertices()) {
			this.pointNodeMap.get(pt).setDistance(Double.POSITIVE_INFINITY);
		}
		this.pointNodeMap.get(start).setDistance(0);
			
		MapNode next = null;
		int count = 0;

		while (!toExplore.isEmpty()) {
			next = toExplore.poll();
			count += 1;
			
			 // hook for visualization
			nodeSearched.accept(next.getLocation());
			
			if (!(visited.contains(next))) {
				visited.add(next);
				if (next.equals(endNode)) break;
				Set<MapNode> neighbors = getNeighbors(next);
				for (MapNode neighbor : neighbors) {
					if (!visited.contains(neighbor)) {
						double distance = next.getDistance() + getDistanceFromTo(next, neighbor); 
						if (distance < neighbor.getDistance()) {
							if (parentMap.containsKey(neighbor)) {
								parentMap.remove(neighbor);
							}
							parentMap.put(neighbor, next);
							neighbor.setDistance(distance);
							toExplore.add(neighbor);
						}
					}
				}
				
			}
		}
		
		System.out.println("Visited: " + count);
		
		if (!next.equals(endNode)) {
			System.out.println("No path found from " +start+ " to " + goal);
			return null;
		}
		
		// Reconstruct the parent path
		List<GeographicPoint> path =
				reconstructPath(parentMap, startNode, endNode);

		return path;
	}

	/** Find the path from start to goal using A-Star search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> aStarSearch(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
        Consumer<GeographicPoint> temp = (x) -> {};
        return aStarSearch(start, goal, temp);
	}
	
	/** Find the path from start to goal using A-Star search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> aStarSearch(GeographicPoint start, 
											 GeographicPoint goal, Consumer<GeographicPoint> nodeSearched)
	{
		// Setup - check validity of inputs
		if (start == null || goal == null)
			throw new NullPointerException("Cannot find route from or to null node");
		MapNode startNode = pointNodeMap.get(start);
		MapNode endNode = pointNodeMap.get(goal);
		if (startNode == null) {
			System.err.println("Start node " + start + " does not exist");
			return null;
		}
		if (endNode == null) {
			System.err.println("End node " + goal + " does not exist");
			return null;
		}

		// setup to begin A*
		HashMap<MapNode,MapNode> parentMap = new HashMap<MapNode,MapNode>();
		PriorityQueue<MapNode> toExplore = new PriorityQueue<MapNode>();
		HashSet<MapNode> visited = new HashSet<MapNode>();
		startNode.setDistance(0);
		toExplore.add(startNode);
		MapNode next = null;
		for (GeographicPoint pt : getVertices()) {
			this.pointNodeMap.get(pt).setDistance(Double.POSITIVE_INFINITY);
			this.pointNodeMap.get(pt).setActualDistance(Double.POSITIVE_INFINITY);
		}
		this.pointNodeMap.get(start).setDistance(start.distance(goal));
		this.pointNodeMap.get(start).setActualDistance(0);
		int count = 0;

		while (!toExplore.isEmpty()) {
			next = toExplore.poll();
			count += 1;
			
			 // hook for visualization
			nodeSearched.accept(next.getLocation());
			
			if (!(visited.contains(next))) {
				visited.add(next);
				if (next.equals(endNode)) break;
				Set<MapNode> neighbors = getNeighbors(next);
				for (MapNode neighbor : neighbors) {
					if (!visited.contains(neighbor)) {
						double geoDistance = neighbor.getLocation().distance(goal);
						double actualDistance = next.getActualDistance() + getDistanceFromTo(next, neighbor);
						double distance = actualDistance + geoDistance;
						//distance += geoDistance;
						if (actualDistance < neighbor.getActualDistance()) {
							if (parentMap.containsKey(neighbor)) {
								parentMap.remove(neighbor);
							}
							neighbor.setDistance(distance);
							neighbor.setActualDistance(actualDistance);
							parentMap.put(neighbor, next);
							toExplore.add(neighbor);
						}
					}
				}
				
			}
		}
		
		System.out.println("Visited: " + count);
		
		if (!next.equals(endNode)) {
			System.out.println("No path found from " +start+ " to " + goal);
			return null;
		}
		
		// Reconstruct the parent path
		List<GeographicPoint> path =
				reconstructPath(parentMap, startNode, endNode);

		return path;
	}

	
	
	public static void main(String[] args)
	{
		System.out.print("Making a new map...");
		MapGraph theMap = new MapGraph();
		System.out.print("DONE. \nLoading the map...");
		GraphLoader.loadRoadMap("data/testdata/simpletest.map", theMap);
		System.out.println("DONE.");
		//System.out.println("Num nodes: " + theMap.getNumVertices());
		//System.out.println("Num edges: " + theMap.getNumEdges());
		//System.out.println(theMap.pointNodeMap.toString());
		//List<GeographicPoint> route = theMap.bfs(new GeographicPoint(1.0,1.0), 
		//		 new GeographicPoint(8.0,-1.0));
		//System.out.println(route);
		List<GeographicPoint> route1 = theMap.dijkstra(new GeographicPoint(1.0,1.0), 
				 new GeographicPoint(8.0,-1.0));
		System.out.println(route1);
		List<GeographicPoint> route2 = theMap.aStarSearch(new GeographicPoint(1.0,1.0), 
				 new GeographicPoint(8.0,-1.0));
		System.out.println(route2);
		
		GraphLoader.loadRoadMap("data/maps/utc.map", theMap);
		System.out.println("DONE2.");
		GeographicPoint start = new GeographicPoint(32.8648772, -117.2254046);
		GeographicPoint end = new GeographicPoint(32.8660691, -117.217393);
		List<GeographicPoint> routeD = theMap.dijkstra(start, end);
		List<GeographicPoint> routeA = theMap.aStarSearch(start, end);
		// You can use this method for testing.  
		
		/* Use this code in Week 3 End of Week Quiz
		MapGraph theMap = new MapGraph();
		System.out.print("DONE. \nLoading the map...");
		GraphLoader.loadRoadMap("data/maps/utc.map", theMap);
		System.out.println("DONE.");

		GeographicPoint start = new GeographicPoint(32.868629, -117.215393);
		GeographicPoint end = new GeographicPoint(32.868629, -117.215393);
		
		List<GeographicPoint> route = theMap.dijkstra(start,end);
		List<GeographicPoint> route2 = theMap.aStarSearch(start,end);

		*/
		
	}
	
}
