/**
 * The Flush class is a subclass of the Hand class and represents a Flush Hand
 * @author Chhavi Sharma
 *
 */
@SuppressWarnings("serial")
public class Flush extends Hand{

	/** 
	 * Used for assigning the player and the cards
	 * @param player player who is playing
	 * @param cards cards of player
	 */
	public Flush(CardGamePlayer player, CardList cards) {
		super(player, cards);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * a method for checking if this is a valid hand 
	 * @return true or false based on whether hand is valid or not
	 */
	public boolean isValid() {
		int suit1 = this.getCard(0).getSuit();
		int suit2 = this.getCard(1).getSuit();
		int suit3 = this.getCard(2).getSuit();
		int suit4 = this.getCard(3).getSuit();
		int suit5 = this.getCard(4).getSuit();
		
		if(suit1 == suit2 && suit2 == suit3 && suit3 == suit4 && suit4 == suit5 && suit5 == suit1 && suit1 == suit3 && suit2 == suit4 && suit3 == suit5 && suit4 == suit1 && suit5 == suit2) {
			return true;
		}
		return false;
	}
	
	/**
	 * a method for returning a string specifying the type of this hand
	 * @return name of Hand
	 */
	public String getType() {
		String s = "Flush";
		return s;
	}

}
