package graphs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import graphs.DirectedGraph;

public class StrongConnectedComponents {
	private HashSet<Integer> ExploredNodes;
	private Integer t;
	private Integer s;
	private int[] f;
	private int[] Leaders;
	private int[] newLabels;
	//private Map<Integer, List<Integer>> SCCs; //leader + list of vertices
	private Stack<int[]> St;
	private Stack<Integer> St1;
	
	public StrongConnectedComponents(DirectedGraph Gr) {
		this.t= 0;
		this.s = null;
		this.ExploredNodes = new HashSet<Integer>();
		this.f = new int[Gr.getSize()];
		this.Leaders = new int[Gr.getSize()];
		this.newLabels= new int[Gr.getSize()];
		//this.SCCs =  new LinkedHashMap<Integer, List<Integer>>();
		this.St = new Stack<int[]>();
		this.St1 = new Stack<Integer>();
		DirectedGraph revGr = this.reverseGraph(Gr);
		System.out.println("Reversing done");
		System.out.println(Gr.getSize());
		//revGr.print();
		DFS_loop1(revGr);
		System.out.println("DFS1 loop is done");
		//print1();
		for (int i = 0; i < f.length; i++) {
			this.newLabels[this.f[i]-1] = i+1; 
		}
		DFS_loop2(Gr);
		//print2();
		getTopSCCs(5);
		//print();
	}
	
	public void getTopSCCs(int n) {
		Map<Integer, List<Integer>> SCCs =  new LinkedHashMap<Integer, List<Integer>>();
		for (int i=0; i < this.Leaders.length; i++) {
			if (!(SCCs.containsKey(this.Leaders[i]))) {
				SCCs.put(this.Leaders[i], new ArrayList<Integer>());
			}
			SCCs.get(this.Leaders[i]).add((i+1));
		}
		Map<Integer, List<Integer>> lSCCs = new LinkedHashMap<Integer, List<Integer>>(); //length of SCC + list of leaders
		Iterator<Integer> itr = SCCs.keySet().iterator();
		List<Integer> len = new ArrayList<Integer>();
		while (itr.hasNext()) {
			int l = itr.next();
			int s = SCCs.get(l).size();
			if (!(lSCCs.containsKey(s))) {
				lSCCs.put(s, new ArrayList<Integer>());
				len.add(s);
			}
			//System.out.println(s);
			lSCCs.get(s).add(l);
		}
		len.sort(null);
		//System.out.println(len.toString());
		int res = 0;
		int i = 1;
		while ((res <= n) && (i <= len.size())) {
			int l = len.get(len.size()-i);
			lSCCs.get(l).forEach(item -> {
				System.out.println("Leader = " + item + "; size = " + SCCs.get(item).size());
			});
			res += lSCCs.get(l).size();
			i++;
		}
	}
	
	//for debugging purposes only
	private void print1() {
		System.out.println("Printing f");
		for (int i=0; i < this.f.length; i++) {
			System.out.println("Node " + (i+1) + " : " + this.f[i]);
		}
	}
	
	private void print2() {
		System.out.println("Printing SCCs");
		for (int i=0; i < this.Leaders.length; i++) {
			System.out.println("Node " + (i+1) + " : " + this.Leaders[i]);
		}
	}
	
	
	public void DFS_loop1(DirectedGraph Gr) {
		for (int i = Gr.getSize(); i>0; i--) {
			if (!(this.ExploredNodes.contains(i))) {
				//System.out.println(i);
				DFS1i(Gr, i);
			}
		}
	}
	
	public void DFS_loop2(DirectedGraph Gr) {
		this.ExploredNodes.clear();
		for (int i = this.newLabels.length; i>0; i--) {
			//System.out.println("new label: " + i + " : " + this.newLabels[i-1]);
			if (!(this.ExploredNodes.contains(i))) {
				this.s = this.newLabels[i-1];
				//System.out.println("leader: " + this.s);
				DFS2i(Gr, i);
			}
		}
	}
	
	public void DFS2(DirectedGraph Gr, int j) {
		if (!(this.ExploredNodes.contains(j))) 
			this.ExploredNodes.add(j);
		List<Integer> edg = Gr.getEdges(this.newLabels[j-1]);
		//System.out.println(j + " " + this.s);
		//System.out.println(edg.toString());
		this.Leaders[(this.newLabels[j-1])-1] = this.s;
		edg.forEach(item -> {
			if (!(this.ExploredNodes.contains(this.f[item-1]))) {
				DFS2(Gr, this.f[item-1]);				
			}	
		});
	}
	
	public void DFS1(DirectedGraph Gr, int j) {
		if (!(this.ExploredNodes.contains(j))) 
			this.ExploredNodes.add(j);
		List<Integer> edg = Gr.getEdges(j);
		if (!(edg.isEmpty())){
			edg.forEach(item -> {
				if (!(this.ExploredNodes.contains(item))) {
					DFS1(Gr, item);
					}
				});
		}
		this.t++;
		this.f[j-1]=t;
	}
	
	//iterative DFS http://www.csl.mtu.edu/cs2321/www/newLectures/26_Depth_First_Search.html
	public void DFS1i(DirectedGraph Gr, int j) {
		if (!(this.ExploredNodes.contains(j))) { 
			this.St.push(new int[]{0,j});
			//System.err.println("staring: " + j);
			while (!(this.St.empty())) {
				int[] a = this.St.pop();
				//System.out.println(this.St.toArray().toString());
				//System.err.println("pop from stack: " + a[0] + " & " + a[1]);
				//System.err.println("explored" + this.ExploredNodes.size());
				if (this.ExploredNodes.contains(a[1]) && a[0] == 0)
					continue;
				if (a[0] == 1) {
					this.t++;
					this.f[a[1]-1]=t;
					if (t % 20000 == 0)
						System.out.println("t: " + t);
					continue;
				}
				this.ExploredNodes.add(a[1]);
				this.St.push(new int[]{1,a[1]});
				List<Integer> edg1 = Gr.getEdges(a[1]);
				if (!(edg1.isEmpty())){
					edg1.forEach(u -> {
						if (!(this.ExploredNodes.contains(u))) {
							//System.err.println("push to stack: " + u);
							St.push(new int[]{0,u});
						}
					});					
				}
			}
		}
	}
	
	public void DFS2i(DirectedGraph Gr, int j) {
		if (!(this.ExploredNodes.contains(j))) { 
			this.St1.push(j);
			while (!(this.St1.empty())) {
				int a = this.St1.pop();
				this.ExploredNodes.add(a);
				List<Integer> edg = Gr.getEdges(this.newLabels[a-1]);
				this.Leaders[(this.newLabels[a-1])-1] = this.s;
				edg.forEach(item -> {
					if (!(this.ExploredNodes.contains(this.f[item-1]))) {
						St1.push(this.f[item-1]);				
					}	
				});
			}
		}
	}


	public DirectedGraph reverseGraph(DirectedGraph copy) {
		int NodesSize = copy.getSize();
		LinkedHashMap<Integer, List<Integer>> Edges = new LinkedHashMap<Integer, List<Integer>>();
		Iterator<Integer> itr = copy.Edges.keySet().iterator();
		while (itr.hasNext()) {
			int NodeFrom = itr.next();
			copy.Edges.get(NodeFrom).forEach(NodeTo-> {
				if (Edges.containsKey(NodeTo)) {
					Edges.get(NodeTo).add(NodeFrom);
				}
				else {
					List<Integer> l = new ArrayList<Integer>();
					l.add(NodeFrom);
					Edges.put(NodeTo, l);
				}
			});
		}
		return new DirectedGraph(NodesSize, Edges);
	}
	
}
