package graphs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.sun.javafx.geom.transform.Identity;

public class Dijkstra {
	private List<Integer> ProcessedNodes;
	private Map<Integer, Integer> Distances;

	public Dijkstra () throws Exception {
		this.ProcessedNodes = new ArrayList<Integer>();
		this.Distances = new LinkedHashMap<Integer, Integer>();
	}
	
	public void FindNextNode (DirectedGraphL Gr) {
		int BestDistance = Integer.MAX_VALUE;
		Integer NextNode = null;
		//System.out.println(this.ProcessedNodes.size());
		for (int i : this.ProcessedNodes) {
			int[] p = Arrays.stream(Gr.getNeighbours(i))
					.filter(el -> !(this.ProcessedNodes.contains(el)))
					.toArray();
			for (int j : p) {
				int c = Gr.getDistance(i, j) + this.Distances.get(i);
				if (c < BestDistance) {
					NextNode = j;
					BestDistance = c;
				}	
			}
		}
		System.out.println(NextNode + " / " + BestDistance);
		this.ProcessedNodes.add(NextNode);
		this.Distances.put(NextNode, BestDistance);
	}
	
	public void FindShortestPaths (DirectedGraphL Gr, int StartNode) {
		this.ProcessedNodes.clear();
		this.ProcessedNodes.add(StartNode);
		this.Distances.clear();
		this.Distances.put(StartNode, 0);
		//IntStream sequence = Arrays.stream(Gr.getEdges())
		//		.filter(i -> !(this.ProcessedNodes.contains(i)));
				//.flatMap(i -> Arrays.stream(Gr.getNeighbours(i)));
		//sequence.forEach(i -> System.out.println(i));
		//		.map(w1 -> Gr.getDistance(1, w1))
		//		.max();
		//System.out.println("res: " + y.getAsInt());
		//this.ProcessedNodes.stream().forEach(v -> {
		//int i = NotVisited.max((w1, w2) -> Integer.compare(Gr.getDistance(1, w1), Gr.getDistance(2, w2))).get();
		//	);
		//});
		while (this.ProcessedNodes.size() < Gr.getSize()) {
			this.FindNextNode(Gr);
		}
			
	
		//System.out.println(NotVisited.length);
		//System.out.println(Gr.getEdges().length);
	}

	public List<int[]> getDistances(DirectedGraphL Gr, int Node) {
		return Arrays.stream(Gr.getNeighbours(Node)).mapToObj(i -> new int[] {Node, i}).collect(Collectors.toList());
	}
	
	public void print() {
		Set<Integer> res = new HashSet<Integer>();
		int[] b = new int[]{7,37,59,82,99,115,133,165,188,197};
		Arrays.stream(b).forEach(item -> res.add(item));
		System.out.println("");
		Arrays.stream(this.Distances.keySet().toArray(new Integer[this.Distances.keySet().size()]))
		.filter(item -> res.contains(item))
		.forEach(item -> {
			System.out.println(item + " : " + this.Distances.get(item));
			//System.out.print(this.Distances.get(item)+",");
		});
		Arrays.stream(b).forEach(item -> {
			System.out.print(this.Distances.get(item)+",");
		});
	}

}
