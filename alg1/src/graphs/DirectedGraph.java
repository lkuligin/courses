package graphs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DirectedGraph {
	private int NodesSize;
	protected final Map<Integer, List<Integer>> Edges;
	
	public DirectedGraph(String FileName, String Separator) throws Exception{
		BufferedReader Reader = new BufferedReader(new FileReader(FileName));
		String Row;
		int NodesSize = 0;
		this.Edges = new LinkedHashMap<Integer, List<Integer>>();
		while ( (Row = Reader.readLine()) != null) {
			String[] RowSplitted = Row.trim().split(Separator);
			int NodeFrom = Integer.parseInt(RowSplitted[0]);
			int NodeTo = Integer.parseInt(RowSplitted[1]);
			if (this.Edges.containsKey(NodeFrom)) {
				this.Edges.get(NodeFrom).add(NodeTo);
			}
			else {
				List<Integer> l = new ArrayList<Integer>();
				l.add(NodeTo);
				this.Edges.put(NodeFrom, l);
			}
			if (NodesSize < NodeFrom)
				NodesSize = NodeFrom;
			if (NodesSize < NodeTo)
				NodesSize = NodeTo;
		}
		this.NodesSize = NodesSize;
		Reader.close();
	}
	
	public DirectedGraph(int Nodes,  Map<Integer, List<Integer>>  Edges) {
		this.NodesSize = Nodes;
		this.Edges = new LinkedHashMap<Integer, List<Integer>>();
		Iterator<Integer> itr = Edges.keySet().iterator();
		while (itr.hasNext()) {
			int i = itr.next();
			List<Integer> newconns = new ArrayList<Integer>();
			Edges.get(i).forEach(item -> newconns.add(item));
			this.Edges.put(i, newconns);
		}
	}
	
	public Integer getSize() {
		return this.NodesSize;
	}
	
	public List<Integer> getEdges(Integer Node) {
		if (this.Edges.containsKey(Node))
			return this.Edges.get(Node);
		return new ArrayList<Integer>();
	}
	
	public void print() {
		Iterator<Integer> itr = this.Edges.keySet().iterator();
		while (itr.hasNext()) {
			int NodeFrom = itr.next();
			System.out.println("From " + NodeFrom + " to " + this.Edges.get(NodeFrom).toString());
		}
		
	}
}
