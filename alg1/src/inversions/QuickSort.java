package inversions;

public class QuickSort {
	
	private QuickSort() {
	}
	
	public static int partitionFirst (int Data[], int low, int high){
		int tmp;
		int pivot = Data[low];
		int i = low + 1;
		for (int j = low+1; j <= high; j++){
			if (Data[j] < pivot){
				tmp = Data[j];
				Data[j] = Data[i];
				Data[i] = tmp;
				i++;
			}
		}
		tmp = Data[low];
		Data[low] = Data[i-1];
		Data[i-1] = tmp;
		return i;
	}
	
	public static int partitionLast (int Data[], int low, int high){
		int tmp;
		int pivot = Data[high];
		Data[high] = Data[low];
		Data[low] = pivot;

		int i = low + 1;
		for (int j = low+1; j <= high; j++){
			if (Data[j] < pivot){
				tmp = Data[j];
				Data[j] = Data[i];
				Data[i] = tmp;
				i++;
			}
		}
		tmp = Data[low];
		Data[low] = Data[i-1];
		Data[i-1] = tmp;
		return i;
	}
	
	public static int partitionMedian (int Data[], int low, int high){
		int tmp;
		int pivotPosition;
		int[] tmpArray = new int[3];
		tmpArray[0] = Data[low];
		tmpArray[1] = Data[(low+high)/2];
		tmpArray[2] = Data[high];
		if (tmpArray[0] > tmpArray[1]) {
			if (tmpArray[1] > tmpArray[2]) {
				pivotPosition = (low+high)/2;
			}
			else if ((tmpArray[1] < tmpArray[2]) && (tmpArray[2] < tmpArray[0])) {
				pivotPosition = high;
			}
			else
				pivotPosition = low;
		} else if (tmpArray[1] > tmpArray[2]) {
			if (tmpArray[0] < tmpArray[2]) {
				pivotPosition = high;
			} else
				pivotPosition = low;
		} else
			pivotPosition = (low+high)/2;
		int pivot = Data[pivotPosition];
		Data[pivotPosition] = Data[low];
		Data[low] = pivot;

		int i = low + 1;
		for (int j = low+1; j <= high; j++){
			if (Data[j] < pivot){
				tmp = Data[j];
				Data[j] = Data[i];
				Data[i] = tmp;
				i++;
			}
		}
		tmp = Data[low];
		Data[low] = Data[i-1];
		Data[i-1] = tmp;
		return i;
	}

	
	public static long sort(int Data[], int low, int high, String partitionType) {
		//long comparisons = 0;
		//System.out.println(Data[0] + " " + Data[1] + " " + Data[2] + " " + Data[3]+ " " + Data[4] + " " + Data[5] + " "  + Data[6] + " "  + Data[7] + " " + Data[8] + " " + Data[9]);
		int indx = 0;
		if (partitionType == "first") {
			indx = partitionFirst(Data, low, high);
		} else if (partitionType == "last") {
			indx = partitionLast(Data, low, high);
		} else 
			indx = partitionMedian(Data, low, high);
		long comparisons = high - low;
		//System.out.println(low + " " + high + " " + comparisons + " indx: " + indx);
		//System.out.println(Data[0] + " " + Data[1] + " " + Data[2] + " " + Data[3]+ " " + Data[4] + " " + Data[5] + " "  + Data[6] + " "  + Data[7] + " " + Data[8] + " " + Data[9]);
		//System.out.println("Next");
		if (low < indx - 1){
			//comparisons += 
			comparisons += sort(Data, low, indx - 2, partitionType);
		}
		if (indx < high){
			//comparisons +=  
			comparisons += sort(Data, indx, high, partitionType);
		}
		return comparisons;
	}
}
