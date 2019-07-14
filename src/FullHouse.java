import java.util.Arrays;


/**
 * The FullHouse class is a subclass of the Hand class and represents a FullHouse Hand
 * @author Chhavi Sharma
 *
 */
@SuppressWarnings("serial")
public class FullHouse extends Hand{

	
	/** 
	 * Used for assigning the player and the cards
	 * @param player player who is playing
	 * @param cards cards of player
	 */
	public FullHouse(CardGamePlayer player, CardList cards) {
		super(player, cards);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * a method for checking if this is a valid hand 
	 * @return true or false based on whether hand is valid or not
	 */
	public boolean isValid() {
		int[] newRank = new int[5];
		
		for(int i=0; i<5; i++) {
			newRank[i] = this.getCard(i).getRank();
		}
		
		Arrays.sort(newRank);
		
		if(newRank[0] == newRank[1]) {
			if(newRank[1] == newRank[2]) {
				if(newRank[3] == newRank[4]) {
					return true;
				}
			}
			else if(newRank[2] == newRank[3] && newRank[3] == newRank[4]) {
				return true;
			}
			return false;
		}
		return false;
	}
	
	/**
	 * a method for returning a string specifying the type of this hand
	 * @return name of Hand
	 */
	public String getType() {
		String s = "FullHouse";
		return s;
	}

}
