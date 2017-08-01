package ShuffleCard;

import java.util.Random;

public class ShuffleCards {

	/**
	 * 全局洗牌
	 * @param args
	 */
	public static void ShuffleWhole(int[] cards) {
		if(cards == null || cards.length == 0)
			return;
		
		Random random = new Random();		
		
		for(int i=0; i<cards.length; i++) {			
			int index = Math.abs(random.nextInt() % cards.length);
			
			int tmp = cards[i];
			cards[i] = cards[index];
			cards[index] = tmp;
		}
		
		for(int n : cards)
			System.out.print(n + " ");
		System.out.println();
	}
	
	/**
	 * 局部洗牌
	 * @param args
	 */
	public static void ShufflePart(int[] cards) {
		if(cards == null || cards.length == 0)
			return;
		
		Random random = new Random();		
		
		for(int i=0; i<cards.length; i++) {			
			int index = i + Math.abs(random.nextInt() % (cards.length - i));
			
			int tmp = cards[i];
			cards[i] = cards[index];
			cards[index] = tmp;
		}
		
		for(int n : cards)
			System.out.print(n + " ");
		System.out.println();
	}
	
	public static void main(String[] args) {
		int[] cards = new int[54];
		for(int i=0; i<cards.length; i++)
			cards[i] = i+1;
		
		ShuffleWhole(cards);
		ShufflePart(cards);
	}


}
