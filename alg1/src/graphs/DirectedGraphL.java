package graphs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

public class DirectedGraphL {
	protected final Map<Integer, Map<Integer, Integer>> Edges;
	protected int inf;
	
	public DirectedGraphL (String FileName, String Separator, String Separator1) throws Exception {
		BufferedReader Reader = new BufferedReader(new FileReader(FileName));
		String Row;
		this.Edges = new LinkedHashMap<Integer, Map<Integer, Integer>>();
		this.inf = inf;//
		while ( (Row = Reader.readLine()) != null) {
			String[] RowSplitted = Row.trim().split(Separator);
			int NodeFrom = Integer.parseInt(RowSplitted[0]);
			this.Edges.put(NodeFrom, new LinkedHashMap<Integer, Integer>());
			IntStream.range(1, RowSplitted.length).forEach(i -> {
				String[] EdgeSplitted  = RowSplitted[i].split(Separator1);
				this.Edges.get(NodeFrom).put(Integer.parseInt(EdgeSplitted[0]), Integer.parseInt(EdgeSplitted[1]));
			});
		}
		Reader.close();
	}
	
	public void print() {
		Iterator<Integer> itr = this.Edges.keySet().iterator();
		while (itr.hasNext()) {
			int NodeFrom = itr.next();
			System.out.print("From " + NodeFrom + " to ");
			Iterator<Integer> itr1 = this.Edges.get(NodeFrom).keySet().iterator();
			while (itr1.hasNext()) {
				int NodeTo = itr1.next();
				System.out.print(NodeTo + " costs " + this.Edges.get(NodeFrom).get(NodeTo).toString() + "; ");
			}
			System.out.println(" ");
		}
	}
	
	public int getSize() {
		return this.Edges.keySet().size();
	}
	
	public int[] getNeighbours(int Node) {
		//return this.Edges.get(Node).keySet().toArray(new Integer[this.Edges.get(Node).keySet().size()]);
		return this.Edges.get(Node).keySet().stream().mapToInt(i->i).toArray();
		}
	
	public int[] getEdges() {
		return this.Edges.keySet().stream().mapToInt(i->i).toArray();
		}
	
	public int getDistance (int NodeFrom, int NodeTo) {
		if (this.Edges.containsKey(NodeFrom)){
			if (this.Edges.get(NodeFrom).containsKey(NodeTo))
				return this.Edges.get(NodeFrom).get(NodeTo);
		}
		return Integer.MAX_VALUE;
	}
}
