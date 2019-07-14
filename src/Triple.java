/**
 * The Triple class is a subclass of the Hand class and represents a Triple Hand
 * @author Chhavi Sharma
 *
 */
@SuppressWarnings("serial")
public class Triple extends Hand {

	/** 
	 * Used for assigning the player and the cards
	 * @param player player who is playing
	 * @param cards cards of player
	 */	
	public Triple(CardGamePlayer player, CardList cards) {
		super(player, cards);
		// TODO Auto-generated constructor stub
	}

	/**
	 * a method for checking if this is a valid hand 
	 * @return true or false based on whether hand is valid or not
	 */
	public boolean isValid() {
		int rank1 = this.getCard(0).getRank();
		int rank2 = this.getCard(1).getRank();
		int rank3 = this.getCard(2).getRank();
		
		if(rank1 == rank2 && rank2 == rank3 && rank1 == rank3) {
			return true;
		}
		return false; 
	}
	
	/**
	 * a method for returning a string specifying the type of this hand
	 * @return name of Hand
	 */
	public String getType() {
		String s = "Triple";
		return s;
	}

}
