package SortDemo;

public class MergeSortDemo {

	public static void main(String[] args) {
		int[] arr = {3,1,5,7,2,4,9,6};  
		MergeSort(arr, 0, arr.length-1);
	}

	public static void MergeSort(int[] arr, int low, int high) {
		if(low < high) {
			int mid = (low + high) / 2;
			MergeSort(arr, low, mid);
			MergeSort(arr, mid+1, high);
			
			Merge(arr, low, mid, high);
		}
		
		for(int i=low; i<high; i++)
			System.out.print(arr[i] + " ");
		System.out.println();
	}

	public static void Merge(int[] arr, int low, int mid, int high) {
		int[] brr = new int[arr.length];
		for(int i=low; i<=high; i++)
			brr[i] = arr[i];
		
		int indexLow = low;
		int indexMid = mid+1;
		int index = low;
		while((indexLow <= mid) && (indexMid <= high)) {
			if(brr[indexLow] <= brr[indexMid])
				arr[index++] = brr[indexLow++];
			else arr[index++] = brr[indexMid++];
		}
		
		while(indexLow <= mid)
			arr[index++] = brr[indexLow++];
		while(indexMid <= high)
			arr[index++] = brr[indexMid++];		
	}

}
