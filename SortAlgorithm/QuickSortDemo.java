package SortDemo;

public class QuickSortDemo {

	public static void main(String[] args) {
		int[] arr = {3,1,5,7,2,4,9,6};  
		QuickSort(arr, 0, 7);
		
		for(int num : arr)
			System.out.print(num + " ");
		System.out.println();
	}

	public static void QuickSort(int[] arr, int low, int high) {
		if(low < high) {
			int pos = Partition(arr, low, high);
			QuickSort(arr, low, pos-1);
			QuickSort(arr, pos+1, high);
		}		
	}

	public static int Partition(int[] arr, int low, int high) {
		int posNum = arr[low];
		while(low < high) {
			while(high>low && arr[high]>posNum)
				high--;
			
			arr[low] = arr[high];
			
			while(low<high && arr[low]<posNum)
				low++;
			arr[high] = arr[low];
		}
		
		arr[low] = posNum;
		
		return low;
	}

}
