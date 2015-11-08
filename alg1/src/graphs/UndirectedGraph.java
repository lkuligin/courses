package graphs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class UndirectedGraph {
	private int NodesSize;
	protected final Map<Integer, List<Integer>> Connections;
	
	public UndirectedGraph (int size) {
		this.NodesSize = size;
		this.Connections = new LinkedHashMap<Integer, List<Integer>>();
		for (int i = 0; i < size; i++) 
			this.Connections.put(i, new ArrayList<Integer>());
	}
	
	public UndirectedGraph (String FileName, String Separator) throws Exception {
		BufferedReader Reader = new BufferedReader(new FileReader(FileName));
		int lines = 0;
		while (Reader.readLine() != null) 
			lines++;
		this.NodesSize = lines;
		this.Connections = new LinkedHashMap<Integer, List<Integer>>();
		Reader.close();
		Reader = new BufferedReader(new FileReader(FileName));
		String Row;
		while ( (Row = Reader.readLine()) != null) {
			String[] RowSplitted = Row.trim().split(Separator);
			Integer currNode = Integer.parseInt(RowSplitted[0]);
			this.Connections.put(currNode, new ArrayList<Integer>());
			for (int i = 1; i < RowSplitted.length; i++) {
				this.AddEdge(currNode, Integer.parseInt(RowSplitted[i]));
			}
		}
	}
	
	public UndirectedGraph (UndirectedGraph copy) {
		this.NodesSize = copy.NodesSize;
		this.Connections = new LinkedHashMap<Integer, List<Integer>>();
		Iterator<Integer> itr = copy.Connections.keySet().iterator();
		while (itr.hasNext()) {
			int i = itr.next();
			List<Integer> newconns = new ArrayList<Integer>();
			copy.Connections.get(i).forEach(item -> newconns.add(item));
			this.Connections.put(i, newconns);
		}
	}
	
	public void AddEdge (int NodeFrom, int NodeTo) {
		this.Connections.get(NodeFrom).add(NodeTo);
	}
	
	public UndirectedGraph copy() {
		return new UndirectedGraph(this);
	}
	
	public Integer getSize() {
		return this.NodesSize;
	}
	
	public Integer getEdgesSize() {
		Integer res = 0;
		Iterator<Integer> itr = this.Connections.keySet().iterator();
		while (itr.hasNext()) {
			Integer k = itr.next();
			res += this.Connections.get(k).size(); 
		}
		return res/2;
	}
	
	public List<Integer> getArcs(Integer Node) {
		return this.Connections.get(Node);
	}
	
	public Integer getNextNode(Integer Node){
		Optional<Integer> onextNode = this.getArcs(Node).stream().findFirst();
		Integer nextNode = null;
		if (onextNode.isPresent()){
			nextNode = onextNode.get();
		}
		return nextNode;
	}
	
	public Boolean checkArc(Integer NodeA, Integer NodeB) {
		return this.getArcs(NodeA).stream().anyMatch(u -> u.equals(NodeB));
	}
	
	public void removeNode(Integer NodeA, Integer NodeB) {
		this.Connections.get(NodeA).removeIf(el -> el.equals(NodeB));
	}
	
	public void removeNode(Integer Node) {
		this.Connections.remove(Node);
		this.NodesSize--;
	}
	
}
