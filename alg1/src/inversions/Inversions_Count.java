package inversions;

public class Inversions_Count {
	private Inversions_Count() {}
	
	public static long Count(int[] Data) {
		return mergeSort(Data, 0, Data.length);
	}
		
	public static long mergeSort(int[] Data, int low, int high) {
		if (low == high-1) return 0;
		int mid = (low + high)/2;
		return mergeSort(Data,  low, mid) + mergeSort(Data, mid, high) + merge(Data, low, mid, high);
		
	}
	
	public static long merge(int[] Data, int low, int mid, int high){
		long count = 0;
		int[] buff = new int[Data.length];
		
		for (int i = low, lb = low, hb = mid; i < high; i++) {
			if (hb >= high || lb < mid && Data[lb] <= Data[hb]) {
				buff[i]  = Data[lb++];
			}
			else {
				count = count + (mid - lb);
				buff[i]  = Data[hb++];
			}
		}
		System.arraycopy(buff, low, Data, low, high-low);
		return count;
	
	}
	
}
