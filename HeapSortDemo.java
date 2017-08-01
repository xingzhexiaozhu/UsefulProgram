package SortDemo;

public class HeapSortDemo {

	public static void main(String[] args) {
		int[] arr = {3,1,5,7,2,4,9,6};  
		HeapSort(arr);		
	}

	public static void HeapSort(int[] arr) {
		BuildHeap(arr, arr.length-1);//建立堆
		
		for(int i=0; i<arr.length; i++)
			System.out.print(arr[i] + " ");
		System.out.println();
		
		for(int i=arr.length-1; i>0; i--) {
			int tmp = arr[i];
			arr[i] = arr[0];
			arr[0] = tmp;
			AdjustDown(arr, 0, i-1);
		}
		
		for(int i=0; i<arr.length; i++)
			System.out.print(arr[i] + " ");
		System.out.println();
	}

	public static void BuildHeap(int[] arr, int n) {
		for(int i=(n-1)/2; i>=0; i--)
			AdjustDown(arr, i, n-1);
	}

	public static void AdjustDown(int[] arr, int k, int n) {
		int tmp = arr[k];
		for(int i=2*k+1; i<=n; i*=2) {
			if((i<n) && (arr[i]<arr[i+1]))
				i++;//比较左右子树，选择子结点大者
			
			if(tmp >= arr[i])
				break;//本身就是大根堆
			else {
				arr[k] = arr[i];
				k = i;
			}
		}
		arr[k] = tmp;
	}

}
