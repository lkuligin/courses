package inversions;

public class Main {
	
	public static void main(String[] args) throws Exception{
		System.out.println("Test");
		Tsv_Read TsvRead = new Tsv_Read("C:\\Users\\Leonid\\Downloads\\IntegerArray.txt");
		//Tsv_Read TsvRead = new Tsv_Read("C:\\Users\\Leonid\\Downloads\\10.txt");
		//Tsv_Read TsvRead = new Tsv_Read("C:\\Users\\Leonid\\Downloads\\test.txt");
		int[] a = TsvRead.ReadFile();
		System.out.println(a.length);
		int[] test = {4, 3, 2, 1};
		//System.out.println(Inversions_Count.Count(test));
		//System.out.println(Inversions_Count.Count(a));
		//b = QuickSort(a, 0, a.length-1)
		//System.out.println(a[9999]);
		//System.out.println(QuickSort.sort(a, 0, a.length-1));
		//System.out.println("Starting...");
		//System.out.println(a[0] + " " + a[1] + " " + a[2] + " " + a[3] + " " + a[4] + " " + a[5] + " ");
		
		//long b = QuickSort.sort(a, 0, a.length-1);
		//System.out.println("Partition = first: " + QuickSort.sort(a, 0, a.length-1, "first"));
		System.out.println("Partition = last: " + QuickSort.sort(a, 0, a.length-1, "last"));
		//System.out.println("Partition = median: " + QuickSort.sort(a, 0, a.length-1, "median"));
		//System.out.println(a[1]);
	}
	
}
