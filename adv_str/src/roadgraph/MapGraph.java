/**
 * @author UCSD MOOC development team and YOU
 * 
 * A class which reprsents a graph of geographic locations
 * Nodes in the graph are intersections between 
 *
 */
package roadgraph;


import java.util.List;
import java.util.Map;
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
	private Map<GeographicPoint, ArrayList<MapEdge>> vertices;	
	
	/** 
	 * Create a new empty MapGraph 
	 */
	public MapGraph()
	{
		this.vertices = new HashMap<GeographicPoint, ArrayList<MapEdge>>();
	}
	
	/**
	 * Get the number of vertices (road intersections) in the graph
	 * @return The number of vertices in the graph.
	 */
	public int getNumVertices()
	{
		return this.vertices.size();
	}
	
	/**
	 * Return the intersections, which are the vertices in this graph.
	 * @return The vertices in this graph as GeographicPoints
	 */
	public Set<GeographicPoint> getVertices()
	{
		return this.vertices.keySet();
	}
	
	/**
	 * Get the number of road segments in the graph
	 * @return The number of edges in the graph.
	 */
	public Integer getNumEdges()
	{
		//we have a directed graph -> for each vertice we sum up the amount of outgoing edges 
		return getVertices().stream().mapToInt(x -> vertices.get(x).size()).sum();
	}

	/** Add a node corresponding to an intersection at a Geographic Point
	 * If the location is already in the graph or null, this method does 
	 * not change the graph.
	 * @param location  The location of the intersection
	 * @return true if a node was added, false if it was not (the node
	 * was already in the graph, or the parameter is null).
	 */
	public boolean addVertex(GeographicPoint location)
	{
		//check whether the vertex exists and add it with no outgoing edges
		if ((location != null) && !(getVertices().contains(location))) {
			this.vertices.put(location, new ArrayList<MapEdge>());
			return true;
		}
		return false;
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
	public void addEdge(GeographicPoint from, GeographicPoint to, String roadName,
			String roadType, double length) throws IllegalArgumentException {
		//check whether FROM and TO edges exist, roadType is not used anywhere
		if (!(this.vertices.containsKey(from))) {
			addVertex(from);
		}
		if (!(this.vertices.containsKey(to))) {
			addVertex(to);
		}
		this.vertices.get(from).add(new MapEdge(from, to, roadName, length));
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
	
	/* BFS implementation
	 * @param start the starting location
	 * @param goal the endpoint location
	 * @param hashmap node - parent node 
	 * @return whether the search was successfull or not
	 */
	private boolean bfsSearch(GeographicPoint start, GeographicPoint goal, 
			Map<GeographicPoint, GeographicPoint> parent) {
		Set<GeographicPoint> visited = new HashSet<GeographicPoint>();
		Queue<GeographicPoint> queue = new LinkedList<GeographicPoint>();
		queue.add(start);
		visited.add(start);
		while (!(queue.isEmpty())) {
			GeographicPoint curr = queue.poll();
			if (curr.equals(goal)) {
				return true;
			}
			for (MapEdge neigh : this.vertices.get(curr)){
				if (!(visited.contains(neigh.getDestination()))) {
					visited.add(neigh.getDestination());
					parent.put(neigh.getDestination(), curr);
					queue.add(neigh.getDestination());
				}
			}			
		}
		return false;
	}
	
	/* reconstruct path after bfs
	 * @param start the starting location
	 * @param goal the endpoint location
	 * @param parent hashmap (the result of bfs search)s 
	 * @return a list of path
	 */
	
	private LinkedList<GeographicPoint> reconstructPath(GeographicPoint start, GeographicPoint goal,
			Map<GeographicPoint, GeographicPoint> parent) {
		LinkedList<GeographicPoint> path = new LinkedList<GeographicPoint>();
		GeographicPoint c = goal;
		while (c != start) {
			path.addFirst(c);
			c = parent.get(c);
		}
		path.addFirst(start);
		return path;
	}

	/** Find the path from start to goal using breadth first search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest (unweighted)
	 *   path from start to goal (including both start and goal).
	 */

	public List<GeographicPoint> bfs(GeographicPoint start, 
			 					     GeographicPoint goal, Consumer<GeographicPoint> nodeSearched)
	{
		Map<GeographicPoint, GeographicPoint> parent = new HashMap<GeographicPoint, GeographicPoint>();
		if (bfsSearch(start, goal, parent)) {
			return reconstructPath(start, goal, parent);
		}
		return null;
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
		// TODO: Implement this method in WEEK 3

		// Hook for visualization.  See writeup.
		//nodeSearched.accept(next.getLocation());
		
		return null;
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
		// TODO: Implement this method in WEEK 3
		
		// Hook for visualization.  See writeup.
		//nodeSearched.accept(next.getLocation());
		
		return null;
	}

	
	
	public static void main(String[] args)
	{
		System.out.print("Making a new map...");
		MapGraph theMap = new MapGraph();
		System.out.print("DONE. \nLoading the map...");
		GraphLoader.loadRoadMap("data/testdata/simpletest.map", theMap);
		System.out.println("DONE.");
		System.out.println(theMap.vertices.toString());
		GeographicPoint start = new GeographicPoint(1.0, 1.0);
		GeographicPoint end = new GeographicPoint(4.0, -1.0);
		System.out.println("trying bfs");
		System.out.println(theMap.bfs(start, end).toString());
		System.out.println("Done");
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
