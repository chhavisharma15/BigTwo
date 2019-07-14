/**
 * The Pair class is a subclass of the Hand class and represents a Pair Hand
 * @author Chhavi Sharma
 */

@SuppressWarnings("serial")
public class Pair extends Hand{

	/** 
	 * Used for assigning the player and the cards
	 * @param player player who is playing
	 * @param cards cards of player
	 */
	public Pair(CardGamePlayer player, CardList cards) {
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
		
		if(rank1 == rank2) {
			return true;
		}
		return false;
	}
	
	/**
	 * a method for returning a string specifying the type of this hand
	 * @return name of Hand
	 */
	public String getType() {
		String s = "Pair";
		return s;
	}

}
