package graphs;

import java.lang.Math;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class RandomContraction extends UndirectedGraph {
	private final Random random = new Random();

	public RandomContraction(String FileName, String Separator) throws Exception {
		super(FileName, Separator);
	}
	
	public RandomContraction(UndirectedGraph gr){
		super(gr);
	}
	
	public int[] getRandomEdge() {
		int[] RandomEdge = new int[2];
		Iterator<Integer> itr = this.Connections.keySet().iterator();
		//construct list of non-empty nodes to select a NodeA
		int[] NonEmptyNodes = new int[this.getSize()];
		Integer j = 0;
		while (itr.hasNext()) {
			Integer k = itr.next();
			if (!this.Connections.get(k).isEmpty()) {
				NonEmptyNodes[j] = k;
				j++;
			}; 
		}
		//select first node
		Integer NodeA = NonEmptyNodes[random.nextInt(j)];
		RandomEdge[0] = NodeA;
		RandomEdge[1]  = this.Connections.get(NodeA).get(random.nextInt(this.Connections.get(NodeA).size()));
		return RandomEdge;
	}
	
	public Integer MinCut() {
		Integer bestTrial = Integer.MAX_VALUE;
		for (Integer i = 0; i < 1000; i++) {
			//select random node
			RandomContraction tempGraph = new RandomContraction(this.copy());
			int[] RandomEdge = tempGraph.getRandomEdge();
			while (tempGraph.getSize() > 2) {
				int NodeA = RandomEdge[0];
				int NodeB = RandomEdge[1];
				//System.err.println(tempGraph.Connections);
				tempGraph.MergeNodes(NodeA, NodeB);
				RandomEdge = tempGraph.getRandomEdge();
			}
			if (bestTrial > tempGraph.getEdgesSize()) 
				bestTrial = tempGraph.getEdgesSize();
		}
		return bestTrial;
	}
	
	public void MergeNodes(Integer NodeA, Integer NodeB) {
		List<Integer> arcsB = this.getArcs(NodeB);
		//for each nodeC such as arc NodeB > NodeC exists
		arcsB.stream().forEach((NodeC) -> {
			//add an arc NodeC > NodeA and NodeA < NodeC if not exists
			if (!NodeC.equals(NodeA)) {
				//!this.checkArc(NodeA, NodeC) && 
				this.AddEdge(NodeA, NodeC);
				this.AddEdge(NodeC, NodeA);
			}
			//remove nodeB <> nodeC symmetrically
			this.removeNode(NodeC, NodeB);
		});
		this.removeNode(NodeB);
	}
}
