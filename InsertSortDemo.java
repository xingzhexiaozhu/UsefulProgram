package SortDemo;

public class InsertSortDemo {

	public static void main(String[] args) {
		int[] arr = {3,1,5,7,2,4,9,6};  
		InsertSort(arr);
	}

	public static void InsertSort(int[] arr) {
		if(arr == null || arr.length == 0)
			return;
		
		for(int i=1; i<arr.length; i++) {
			if(arr[i] < arr[i-1]) {
				int tmp = arr[i];
				int index = i-1;
				while(index>=0 && arr[index]>tmp) {
					arr[index+1] = arr[index];
					index--;
				}
				arr[index+1] = tmp;
			}
		}
		
		for(int num : arr)
			System.out.print(num + " ");
		System.out.println();
	}

}
