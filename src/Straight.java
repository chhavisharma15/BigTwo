import java.util.Arrays;

/**
 * The Straight class is a subclass of the Hand class and represents a Straight Hand
 * @author Chhavi Sharma
 *
 */
@SuppressWarnings("serial")
public class Straight extends Hand {

	/** 
	 * Used for assigning the player and the cards
	 * @param player player who is playing
	 * @param cards cards of player
	 */
	public Straight(CardGamePlayer player, CardList cards) {
		super(player, cards);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * a method for checking if this is a valid hand 
	 * @return true or false based on whether hand is valid or not
	 */
	public boolean isValid() {
		int[] newRankCard = new int[5];
		
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
		}
		return true;
	}
	
	/**
	 * a method for returning a string specifying the type of this hand
	 * @return name of Hand
	 */
	public String getType() {
		String s = "Straight";
		return s;
	}

}
