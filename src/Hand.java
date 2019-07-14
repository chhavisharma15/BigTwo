import java.util.Arrays;

/**
 * The Hand class is a subclass of the CardList class, and is used to model a hand of cards. It
 * has a private instance variable for storing the player who plays this hand. It also has methods
 * for getting the player of this hand, checking if it is a valid hand, getting the type of this hand,
 * getting the top card of this hand, and checking if it beats a specified hand
 * @author Chhavi Sharma
 *
 */
@SuppressWarnings("serial")
public abstract class Hand extends CardList{
	private CardGamePlayer player; //a player who plays this hand
	
	/**
	 * a constructor for building a hand
	 * with the specified player and list of cards
	 * @param player player who is playing
	 * @param cards cards of player
	 */
	public Hand(CardGamePlayer player, CardList cards) {
		this.player = player;
		cards = player.getCardsInHand();
	}
	
	/**
	 * a method for retrieving the player of this hand
	 * @return player - player who is playing
	 */
	public CardGamePlayer getPlayer() {
		return player;
	}
	
	/**
	 * a method for retrieving the top card of this hand
	 * @return top card 
	 */
	public Card getTopCard() {
		this.sort();
		
		if(this.getType() == "Single") {
			return this.getCard(0);
		}
		else if(this.getType() == "Pair") {
			return this.getCard(1);
		}
		else if(this.getType() == "Triple") {
			return this.getCard(2);
		}
		else if(this.getType() == "FullHouse") {
			return this.getCard(2);
		}
		else if(this.getType() == "Quad") {
			return this.getCard(2);
		}
		else {
			int[] rankCard = new int[5];
			
			int i=0;
			while(i!=5) {
				if(this.getCard(i).getRank() <= 1) {
					rankCard[i] = this.getCard(i).getRank() + 13;
				}
				else if(this.getCard(i).getRank() > 1) {
					rankCard[i] = this.getCard(i).getRank();
				}
				i++;
			}
			
			//how to sort array direct
			Arrays.sort(rankCard); 
			
			if(rankCard[4] >= 13) {
				rankCard[4] = rankCard[4] -13; 
			}
			
			int j=0;
			while(j!=0) {
				if(this.getCard(j).getRank() == rankCard[4] ) {
					break;
				}
				j++;
			}
			return this.getCard(j);
			
		}
	}
	
	/**
	 * a method for checking if this hand beats a specified hand
	 * @param hand hand of card
	 * @return true or false based on whether hand beats the hand on table
	 */
	public boolean  beats(Hand hand) {
		
		String[] decks = {"Single", "Pair", "Triple", "Straight", "Flush", "FullHouse", "Quad", "StraightFlush"};
		
		String deck1 = this.getType();
		String deck2 = hand.getType();
		
		int a =0, b=0;
		
		for(int i=0; i<8; i++) {
			//direct equate?
			
			if(deck1.equals(decks[i])) {
				a = i;
			}
			
			if(deck2.equals(decks[i])) {
				b = i;
			}
		}
			
		if(a<b) {
			return false;
		}
		else if(a>b) {
			return true;
		}
		else {
			if(this.getTopCard().compareTo(hand.getTopCard()) == 1) {
				return true;
			}
			else {
				return false;
			}
		}
	}
	
	
	/**
	 * a method for checking if this is a valid hand
	 * @return - not used
	 */
	public abstract boolean isValid();
	/**
	 * a method for returning a string specifying the type of this hand
	 * @return - not used
	 */
	public abstract String getType();
}
