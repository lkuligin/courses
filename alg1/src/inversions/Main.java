package inversions;

import graphs.RandomContraction;
import graphs.UndirectedGraph;

import java.io.File;
import java.io.PrintStream;
import java.lang.System;

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
		}
	}
	
}
