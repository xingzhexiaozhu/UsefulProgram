package SortDemo;

public class BubbleSortDemo {

	public static void main(String[] args) {
		int[] arr = {3,1,5,7,2,4,9,6};  
		BubbleSort(arr);
	}

	public static void BubbleSort(int[] arr) {
		if(arr.length == 0 || arr == null)
			return;
		
//		for(int i=0; i<arr.length; i++) {
//			boolean flag = false;
//			for(int j=0; j<arr.length-1-i; j++) {
//				if(arr[j] > arr[j+1]) {
//					int tmp = arr[j];
//					arr[j] = arr[j+1];
//					arr[j+1] = tmp;
//					flag = true;
//				}
//			}
//			
//			if(flag == false)
//				break;
//		}
		for(int i=0; i<arr.length; i++) {
			boolean flag = false;
			for(int j=arr.length-1; j>i; j--) {
				if(arr[j] < arr[j-1]) {
					int tmp = arr[j];
					arr[j] = arr[j-1];
					arr[j-1] = tmp;
					flag = true;
				}
			}
			
			if(flag == false)
				break;
		}
		
		for(int num : arr)
			System.out.print(num + " ");
		System.out.println();
	}

}
