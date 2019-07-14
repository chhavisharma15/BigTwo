import java.util.Arrays;

/**
 * The StraightFlush class is a subclass of the Hand class and represents a StraightFlush Hand
 * @author Chhavi Sharma
 *
 */
@SuppressWarnings("serial")
public class StraightFlush extends Hand{

	/** 
	 * Used for assigning the player and the cards
	 * @param player player who is playing
	 * @param cards cards of player
	 */
	public StraightFlush(CardGamePlayer player, CardList cards) {
		super(player, cards);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * a method for checking if this is a valid hand 
	 * @return true or false based on whether hand is valid or not
	 */	
	public boolean isValid() {
		int[] newRankCard = new int[5];
		int check = 0;
		
		for(int i=0; i<5; i++) {
			if(this.getCard(i).getRank() <= 1) {
				newRankCard[i] = this.getCard(i).getRank() + 13;
			}
			else {
				newRankCard[i] = this.getCard(i).getRank();
			}
		}
		Arrays.sort(newRankCard);
		
		for(int i=0; i<4; i++) {
			if(newRankCard[i] + 1 != newRankCard[i+1]) {
				return false;
			}
			else {
				check = 1;
			}
		}
		
		if(check == 1) {
			for(int j=0; j<4; j++) {
				if(this.getCard(j).getSuit() == this.getCard(j+1).getSuit()) {
					return true;
				}
			}
		}
		return false;
	
	}
	/**
	 * a method for returning a string specifying the type of this hand
	 * @return name of Hand
	 */	
	public String getType() {
		String s = "StraightFlush";
		return s;
	}

}
