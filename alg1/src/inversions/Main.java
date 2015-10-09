package inversions;

import java.util.Arrays;

public class Main {
	
	public static void main(String[] args) throws Exception{
		System.out.println("Test");
		Tsv_Read TsvRead = new Tsv_Read("C:\\Users\\kuligin\\Downloads\\IntegerArray.txt");
		int[] a = TsvRead.ReadFile();
		System.out.println(a.length);
		int[] test = {4, 3, 2, 1};
		System.out.println(Inversions_Count.Count(test));
		System.out.println(Inversions_Count.Count(a));
	}
	
}
