package inversions;

import graphs.*;

import java.io.File;
import java.io.PrintStream;
import java.lang.System;
import java.util.List;

public class Main {	
	public static void main(String[] args) throws Exception{
		System.setErr(new PrintStream(new File("log.txt")));
		switch (args.length) {
			case 0: 
				System.out.println("no args");
			case 1:
				if (args[0].equals("inv")) {
					Tsv_Read TsvRead = new Tsv_Read("C:\\Users\\Leonid\\Downloads\\IntegerArray.txt");
					int[] a = TsvRead.ReadFile();
					System.out.println(a.length);
					System.out.println(Inversions_Count.Count(a));
				}
				else if (args[0].equals("qs")) {
					Tsv_Read TsvRead = new Tsv_Read("C:\\Users\\Leonid\\Downloads\\IntegerArray.txt");
					int[] a = TsvRead.ReadFile();
					//System.out.println("Partition = first: " + QuickSort.sort(a, 0, a.length-1, "first"));
					System.out.println("Partition = last: " + QuickSort.sort(a, 0, a.length-1, "last"));
					//System.out.println("Partition = median: " + QuickSort.sort(a, 0, a.length-1, "median"));
				}
				else if (args[0].equals("conc")) {
					RandomContraction rc = new RandomContraction("C:/Users/Leonid/Downloads/kargerMinCut.txt", "(\\s)+");
					//RandomContraction rc = new RandomContraction("C:/Users/Leonid/Downloads/tc1.txt", "(\\s)+");
					System.out.println("Test1");
					System.out.println(rc.MinCut());
					//Integer i = rc.MinCut();
				}
				else if (args[0].equals("SCC")) {
					DirectedGraph gr = new DirectedGraph("C:/Users/Leonid/Downloads/SCC.txt", "(\\s)+");
					//gr.print();
					StrongConnectedComponents SCC = new StrongConnectedComponents(gr);
					//DirectedGraph grR = SCC.reverseGraph(gr);
					//grR.print();
				}
				else if (args[0].equals("dkst")) {
					System.out.println("test2");
					DirectedGraphL gr = new DirectedGraphL("C:/Users/Leonid/Downloads/dijkstraData.txt", "(\\s)+", ",");
					
					//DirectedGraphL gr = new DirectedGraphL("C:/Users/Leonid/Downloads/d1.txt", "(\\s)+", ",");
					//System.out.println(gr.getDistance(10, 20));
					Dijkstra d = new Dijkstra();
					List<int[]> a =d.getDistances(gr, 1);
					for (int[] el : a) {
						for (int el1: el)
							System.out.print(el1 + " ");
						System.out.println(" ");
					}
					//System.out.println(d.getDistances(gr, 1));
					d.FindShortestPaths(gr, 1);
					d.print();
				}
		}
	}
	
}
