import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JOptionPane;

/**
 * a constructor for creating a Big Two client
 * @author Chhavi Sharma
 *
 */
public class BigTwoClient implements CardGame, NetworkGame {
	
	private int numOfPlayers; //an integer specifying the number of players
	private Deck deck; //a deck of cards
	private ArrayList<CardGamePlayer> playerList; //a list of players
	private ArrayList<Hand> handsOnTable; //a list of hands played on the table
	private int playerID; //an integer specifying the playerID of the local player
	private String playerName; //a string specifying the name of the local player
	private String serverIP; //a string specifying the IP address of the game server
	private int serverPort; //an integer specifying the TCP port of the game server
	private Socket sock; //a socket connection to the game server
	private ObjectOutputStream oos; //an ObjectOutputStream for the sending messages to the server
	private int currentIdx; //an integer specifying the index of the player for the current turn
	private BigTwoTable table; //a Big Two table which builds the GUI for the game and handles all user actions
	private Card threeOfDiam= new Card(0,2); //three of diamonds is the card the first player starts with
	
	/**
	 * whether the chance is passed or not
	 */
	public static int pass=0; 
	/**
	 * whether or not three passes have been made
	 */
	public static boolean threePass=false; 
	/**
	 * whether to continue game or not
	 */
	public static boolean game =true; 
	/**
	 * whether the current player is the first player or not
	 */
	public static boolean firstPlayer = true; 
	/**
	 * whether the move is legal or not
	 */
	public static boolean legal = true; 
	/**
	 * whether pass button is clicked or not
	 */
	public boolean clickPass; 
	/**
	 * whether play button is clicked or not
	 */
	public boolean clickPlay; 
	/**
	 * whether game has ended or not
	 */
	public static boolean endGame = false;
	
	/**
	 * names of players
	 */
	public static String[] playerNames = new String[4];
	
	/**
	 * a constructor for creating a Big Two client
	 */
	public BigTwoClient() {
		handsOnTable= new ArrayList<Hand>();
		playerList = new ArrayList<CardGamePlayer>();
		
		
		for(int i=0; i<4; i++) {
			playerList.add(new CardGamePlayer());
		}
		
		table = new BigTwoTable(this);
		table.disable();
		
		
		this.playerName = "";
		
		makeConnection();
		table.repaint();
	}
	
	/**
	 * a method for creating an instance of BigTwoClient
	 * @param args - not used
	 */
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		BigTwoClient bigTwoClient = new BigTwoClient();
	}

	
	
	/**
	 * a method for getting the playerID
	 */
	@Override
	public int getPlayerID() {
		// TODO Auto-generated method stub
		return this.playerID;
	}
	/**
	 * a method for setting the playerID (i.e., index) of
	 * the local player. This method should be called from the parseMessage() method when a
	 * message of the type PLAYER_LIST is received from the game server
	 */
	@Override
	public void setPlayerID(int playerID) {
		// TODO Auto-generated method stub
		this.playerID = playerID;
		
	}
	/**
	 * a method for getting the name of the player
	 */
	@Override
	public String getPlayerName() {
		// TODO Auto-generated method stub
		return playerName;
	}
	/**
	 * a method for setting the name of the local player
	 */
	@Override
	public void setPlayerName(String playerName) {
		// TODO Auto-generated method stub
		this.playerName = playerName;
		
	}
	/**
	 * a method for getting the IP address of the game server
	 */
	@Override
	public String getServerIP() {
		// TODO Auto-generated method stub
		return serverIP;
	}
	/*
	 * a method for setting the IP address of the game server
	 */
	@Override
	public void setServerIP(String serverIP) {
		// TODO Auto-generated method stub
		this.serverIP = serverIP;
		
	}
	/*
	 * a method for getting the TCP port of the game server
	 */
	@Override
	public int getServerPort() {
		// TODO Auto-generated method stub
		return serverPort;
	}
	/**
	 * a method for setting the TCP port of the game server
	 */
	@Override
	public void setServerPort(int serverPort) {
		// TODO Auto-generated method stub
		this.serverPort = serverPort;
		
	}
	/**
	 * a method for making a socket connection with the game server
	 */
	@Override
	public void makeConnection() {
		// TODO Auto-generated method stub
		this.setPlayerName (JOptionPane.showInputDialog("Name: ", "Default Name"));
		if(playerName == null) {
			playerName = "Default";
		}
		
		setServerPort(2396);
		setServerIP("127.0.0.1");
		
		try {
			sock = new Socket(this.serverIP, this.serverPort);
			oos = new ObjectOutputStream(sock.getOutputStream());
			
			Thread sH = new Thread(new ServerHandler());
			
			sH.start();
			
			sendMessage(new CardGameMessage (CardGameMessage.JOIN, -1, this.getPlayerName()));
			sendMessage(new CardGameMessage (CardGameMessage.READY, -1, null));
			
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
		table.repaint();
		
	}
	/**
	 * a method for parsing the messages received from the game server. This method should be called from the thread 
	 * responsible for receiving messages from the game server. Based on the message type,
	 * different actions will be carried out.
	 */
	@Override
	public void parseMessage(GameMessage message) {
		// TODO Auto-generated method stub
		
		int msgType = message.getType();
		
		if(msgType == 0) {
			if(message.getData() != null) {
				for(int i=0; i<4; i++) {
					String data = ((String[])message.getData())[i];
					if(data != null) {
						this.playerList.get(i).setName(data);
					}
				}
			}
			
			this.playerID = message.getPlayerID();
			
			table.repaint();
		}
		
		else if ( msgType == 1) {
			playerList.get(message.getPlayerID()).setName((String)message.getData());
			
			table.repaint();
			
			table.printMsg("Player "  + playerList.get(message.getPlayerID()).getName() + " has joined the game" + "\n");
		}

		else if(msgType == 2) {
			table.printMsg( "Server is full, try again later!!" +"\n");
			
			playerID = -1;
			
			table.repaint();
		}

		else if (msgType == 3) {
			
			
			table.printMsg( playerList.get(message.getPlayerID()).getName() + " has left the game" + "\n");
			playerList.get(message.getPlayerID()).setName("");
			
			if(endOfGame() != true) {
				table.disable();
				
				sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
				
				for(int i=0; i<4; i++) {
					playerList.get(i).removeAllCards();
				}
			}
			
			table.repaint();
		}

		else if (msgType == 4) {
			handsOnTable = new ArrayList <Hand>();
			
			table.printMsg(playerList.get(message.getPlayerID()).getName() + " is ready!" + "\n");
			table.repaint();
		}

		else if(msgType == 5) {
			this.deck = new BigTwoDeck();
			this.deck = (BigTwoDeck) message.getData();
			start(this.deck);
			
			table.printMsg("Big Two game has started!!" + "\n");
			table.printMsg("Player " + playerList.get(currentIdx).getName() + ", it is your turn \n");
			table.enable();
			table.repaint();
		}
		
		else if (msgType == 6) {
			checkMove(message.getPlayerID(), (int[])message.getData());
			table.repaint();
		}

		else if (msgType == 7) {
			table.printChat((String)message.getData());
		}

		else {
			table.printMsg("Unknown message recieved"+ message.getType() + "\n");
			table.repaint();
		}
		
	}
	/**
	 * a method for sending the specified message to the game server. This method should be called whenever the client wants to
	 * communicate with the game server or other clients. 
	 */
	@Override
	public void sendMessage(GameMessage message) {
		// TODO Auto-generated method stub
		try {
			oos.writeObject(message);
			
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
	/**
	 * a method for getting the playerID (i.e., index) of the local player
	 */
	@Override
	public int getNumOfPlayers() {
		// TODO Auto-generated method stub
		return numOfPlayers;
	}
	/**
	 * a method for getting the deck of cards being used.
	 */
	@Override
	public Deck getDeck() {
		// TODO Auto-generated method stub
		return deck;
	}
	/**
	 * a method for getting the list of players
	 */
	@Override
	public ArrayList<CardGamePlayer> getPlayerList() {
		// TODO Auto-generated method stub
		return playerList;
	}
	/**
	 * a method for getting the list of hands played on the table
	 */
	@Override
	public ArrayList<Hand> getHandsOnTable() {
		// TODO Auto-generated method stub
		return handsOnTable;
	}
	/**
	 * a method for getting the index of the player for the current turn
	 */
	@Override
	public int getCurrentIdx() {
		// TODO Auto-generated method stub
		return currentIdx;
	}
	/**
	 * a method for starting/restarting the game with a given
	 * shuffled deck of cards
	 */
	@Override
	public void start(Deck deck) {
		// TODO Auto-generated method stub
		handsOnTable.clear();
		
		int random =0;
		for(int i = 0; i < 4; i++) {
			playerList.get(i).removeAllCards();
			
			for(int j = 0; j < 13; j++) {
				playerList.get(i).addCard(deck.getCard(random+i+j));
			
			}
			
			random += 12;
		}
		
		for(int i=0; i<4; i++) {
			playerList.get(i).sortCardsInHand();
		}

		
		
		for(int i=0; i<4; i++) {
			if(playerList.get(i).getCardsInHand().contains(threeOfDiam)) {
				currentIdx = i;
				break;
			}
		}
		
		table.setActivePlayer(currentIdx);
		table.repaint();
		

		
	}
	/**
	 * a method for making a move by a
	 * player with the specified playerID using the cards specified by the list of indices. This
	 * method should be called from the BigTwoTable when the local player presses either the
	 * Play or Pass button
	 */
	@Override
	public void makeMove(int playerID, int[] cardIdx) {
		// TODO Auto-generated method stub
		
		CardGameMessage message = new CardGameMessage(CardGameMessage.MOVE, playerID, cardIdx);
		sendMessage(message);
		
	}
	
	/**
	 * a method for checking a move
	 * made by a player. This method should be called from the parseMessage() method from
	 * the NetworkGame interface when a message of the type MOVE is received from the game
	 * server
	 */
	@Override
	public void checkMove(int playerID, int[] cardIdx) {
		// TODO Auto-generated method stub
		
		CardList cardList = new CardList();
		CardList hand = new CardList();
		CardGamePlayer player = new CardGamePlayer();
		Hand valid = null;
		
		
		player = playerList.get(currentIdx);
		table.repaint();

		cardList = player.getCardsInHand();
		hand = player.play(cardIdx);
		
		if(currentIdx == playerID) {
			
			
			if(firstPlayer ==true && hand==null) {
				legal = false;
			}
			else {
				if (hand==null ) {
//					if(BigTwoTable.clickPlay == true && BigTwoTable.clickPass == false) {
//						table.printMsg("Not a legal move!! Please select a hand to play. Or pass." + "\n");
//						BigTwoTable.clickPlay = false;
//						
//					}
						pass++;
						table.printMsg("{Pass}" + "\n");
						if(pass == 3) {
							threePass = true;
							pass=0;
							legal = true;
						}
						
						if(currentIdx == 3) {
							currentIdx = 0;
						}
						else {
							currentIdx++;
						}
						table.setActivePlayer(currentIdx);
						
						if(threePass == true) {
							firstPlayer = true; 
						}
						else {
							firstPlayer = false;
						}
						threePass=false;
						
						BigTwoTable.clickPass = false;

					}

				else {
					valid = composeHand(player, hand);
					
					if(firstPlayer==true && valid!=null) {
						if(valid.contains(threeOfDiam) && game==true) {
							legal = false;
							game = false;
						}
						if(game==false) {
							pass = 0;
							table.printMsg("{"+valid.getType()+"}" + valid.toString()+"\n");
							
							handsOnTable.add(valid);
							
							if(currentIdx==3) {
								currentIdx = 0;
							}
							else {
								currentIdx++;
							}
							table.setActivePlayer(currentIdx);
							
							for(int i=0; i<valid.size(); i++) {
								cardList.removeCard(valid.getCard(i));
							}
							legal = true;
							if(threePass==true) {
								firstPlayer = true;
							}
							else {
								firstPlayer=false;
								
							}
							threePass=false;
							
						}
						
					}

					
					else if(handsOnTable.size()!=0 && valid!=null) {
						
						if(handsOnTable.get(handsOnTable.size()-1).size() == valid.size() && 
								valid.beats(handsOnTable.get(handsOnTable.size() - 1))) {
							table.printMsg("{" + valid.getType() + "}" +valid.toString() + "\n");
							
							pass = 0;
							handsOnTable.add(valid);
							
							if(currentIdx == 3) {
								currentIdx = 0;
							}
							else {
								currentIdx++;
							}
							
							table.setActivePlayer(currentIdx);
							
							for(int i =0; i<valid.size(); i++) {
								cardList.removeCard(valid.getCard(i));
							}
							legal = true;
							
							if(threePass == true) {
								firstPlayer = true;
							}
							else {
								firstPlayer = false;
							}
							threePass = false;
							}
						else {
							legal = false;
						}
					}
					
					else{
						legal = false;
						}
				}
			}
			if(game) {
				legal = false;
			}

			
			if(legal == false) {
				if(hand==null) {
					table.printMsg("Not a legal move!!" + "\n");
				}
				else {
					String send = hand.toString() + "	<=== Not a legal move\n";
					table.printMsg(send);
				}
				legal = true;
			}
			
			if(endOfGame() == true) {
				table.disable();
				
				
				
				String finalGame = "";
				for(CardGamePlayer cgp : playerList) {
					finalGame += cgp.getName()+" ";
					
					if(cgp.getNumOfCards()==0) {
						finalGame += " wins the game." + "\n";
					}
					else {
						finalGame += " has "+ cgp.getNumOfCards()+" cards in hand." + "\n";
					}
	
				}
				
				int result = JOptionPane.showConfirmDialog(null, finalGame, "Game ends!!", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
				
				if(result == JOptionPane.OK_OPTION){
					try {
						oos.writeObject(new CardGameMessage(CardGameMessage.READY, -1, null));
					}
					catch(Exception ex){
						ex.printStackTrace();}
				}
				
				for(int i=0; i<4; i++) {
					playerList.get(i).removeAllCards();
				}
				
				handsOnTable.clear();

				

				
				
			}
			else {
				table.printMsg("Player " + playerList.get(currentIdx).getName() + ", it is your turn \n");
			}
		}
		table.resetSelected();
		table.repaint();

		
	}
	
	/**
	 * to check if it is the end of the game or not
	 */
	@Override
	public boolean endOfGame() {
		// TODO Auto-generated method stub
		int last;
		if (currentIdx == 0) {
			last = 3;
		}
		else {
			last = currentIdx - 1;
		}
		
		if (playerList.get(last).getCardsInHand().isEmpty() == true) {
			endGame = true;	
			return true;
		}
		return false;
	} 
	
	
	/**
	 * an inner class that implements the Runnable interface. You
	 * should implement the run() method from the Runnable interface and create a thread
	 * with an instance of this class as its job in the makeConnection() method from the
	 * NetworkGame interface for receiving messages from the game server. Upon receiving a
	 * message, the parseMessage() method from the NetworkGame interface should be
	 * called to parse the messages accordingly
	 * @author Chhavi Sharma
	 *
	 */
	public class ServerHandler implements Runnable {
		private ObjectInputStream input;
		public ServerHandler() {
			try {
				input = new ObjectInputStream(sock.getInputStream());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				while(true) {
					CardGameMessage msg = (CardGameMessage)input.readObject();
					parseMessage(msg);
				}
			} 
			catch (Exception ex) {
				ex.printStackTrace();
			}

		}

	}
	
	/**
	 * a method for
	 * returning a valid hand from the specified list of cards of the player
	 * @param player - player who is playing the game
	 * @param cards - cards of the deck
	 * @return hand or null based on whether hand is valid or not
	 */
	public Hand composeHand(CardGamePlayer player, CardList cards) {

		
		if(cards.size() == 1) {
			Single single = new Single(player, cards);
			
			for(int i=0; i<cards.size(); i++) {
				single.addCard(cards.getCard(i));
			}
			
			if(single.isValid() == true) {
				return single; 
			}
			return null; 
		}
		
		else if(cards.size() == 2) {
			Pair pair = new Pair(player, cards);
			
			for(int i=0; i<cards.size(); i++) {
				pair.addCard(cards.getCard(i));
			}
			
			if(pair.isValid() == true) {
				return pair; 
			}
			return null; 
		}
		
		else if(cards.size() == 3) {
			Triple triple = new Triple(player, cards);
			
			for(int i=0; i<cards.size(); i++) {
				triple.addCard(cards.getCard(i));
			}
			
			if(triple.isValid() == true) {
				return triple; 
			}
			return null; 
		}
		
		else if(cards.size() == 5) {
			
			Straight straight = new Straight(player, cards);
			for(int i=0; i<cards.size(); i++) {
				straight.addCard(cards.getCard(i));
			}
			
			Flush flush = new Flush(player, cards);
			for(int i=0; i<cards.size(); i++) {
				flush.addCard(cards.getCard(i));
			}
			
			FullHouse fullhouse = new FullHouse(player, cards);
			for(int i=0; i<cards.size(); i++) {
				fullhouse.addCard(cards.getCard(i));
			}

			Quad quad = new Quad(player, cards);
			for(int i=0; i<cards.size(); i++) {
				quad.addCard(cards.getCard(i));
			}
			
			StraightFlush straightflush = new StraightFlush(player, cards);
			for(int i=0; i<cards.size(); i++) {
				straightflush.addCard(cards.getCard(i));
			}
			
			
			if(straight.isValid() == true) {
				return straight; 
			}
			else if(flush.isValid() == true) {
				return flush;
			}
			else if(fullhouse.isValid() == true) {
				return fullhouse;
			}
			else if(quad.isValid() == true) {
				return quad;
			}
			else if(straightflush.isValid() == true) {
				return straightflush;
			}
			return null;
		}
		return null;
		
	}

	
}
