import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;



/**
 * The BigTwoTable class implements the CardGameTable interface. It is used to build a GUI
 * for the Big Two card game and handle all user actions
 * @author Chhavi Sharma
 *
 */
public class BigTwoTable implements CardGameTable {
	
	private CardGame game; //a card game associates with this table
	private boolean[] selected; //a boolean array indicating which cards are selected
	private int activePlayer; //an integer specifying the index of the active player
	private JFrame frame; //the main window of the application
	private JPanel bigTwoPanel; //a panel for showing the cards of each player and cards on table
	private JButton playButton; //a "Play" button for the active player to play selected cards
	private JButton passButton; //a "Pass" button for the active player to pass his/her turn to the next player
	private JTextArea msgArea; //a text area for showing the current game status as well as end of game
	private Image[][] cardImages; //a 2D array storing images for the faces of the card
	private Image cardBackImage; //an image for the backs of the cards
	private Image[] avatars; //an array storing images for the avatars
	private JMenuBar menuBar; //menu bar which contains the menu
	private JMenuItem quit; //a menu item that quits the game
	private JMenu menu; //menu to be put into the menu bar
	private JTextArea textArea;
	private JMenu menu1;
	private JMenuItem connect;
	private JTextField message;
	private JMenuItem clearMsg;
	private JButton sendMessage;
	
	private ArrayList<CardGamePlayer> playerList; //list of players
	private ArrayList<Hand> handsOnTable; //hand on the table
	
	/**
	 * whether turn is passed or not
	 */
	public static boolean clickPass= false; 
	/**
	 * whether player plays or not
	 */
	public static boolean clickPlay = false;
	
	/**
	 * a constructor for creating a BigTwoTable. The parameter game is a reference to a card game associates with this table.
	 * @param game: card game associated with the table
	 */
	public BigTwoTable (BigTwoClient game) {
		this.game = game;
		
		handsOnTable = game.getHandsOnTable();
		playerList = game.getPlayerList();
		
		bigTwoPanel = new BigTwoPanel();
		JPanel mainPanel = new JPanel();
		
		menuBar = new JMenuBar();
		
		frame = new JFrame("Welcome to Big Two Game");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setJMenuBar(menuBar);
		frame.setSize(1280, 720);
		
		
		menu = new JMenu("Game");
		menu1 = new JMenu("Message");
		
		selected = new boolean[13];
		for(int i=0; i<13; i++) {
			selected[i] = false;
		}
		
		JPanel lFrame = new JPanel();
		JPanel rFrame = new JPanel();
		JPanel buttonPanel = new JPanel();
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		
		connect = new JMenuItem("Connect");
		connect.addActionListener(new  ConnectButtonListener());
		quit = new JMenuItem("Quit");
		quit.addActionListener(new  QuitMenuItemListener());
		menu.add(connect);
		menu.add(quit);
		menuBar.add(menu);
		
		clearMsg = new JMenuItem("Clear Messages");
		clearMsg.addActionListener(new ClearMessageButtonListener());
		menu1.add(clearMsg);
		menuBar.add(menu1);

		playButton = new JButton("PLAY");
		playButton.addActionListener(new PlayButtonListener());
		passButton = new JButton("PASS");
		passButton.addActionListener(new PassButtonListener());
		
		message = new JTextField(50);
		message.addKeyListener(new EnterMessageListener());
		msgArea = new JTextArea();
		msgArea.setEditable( false);
		
		sendMessage = new JButton("Send");
		sendMessage.addActionListener(new SendButtonListener());

		mainPanel.setLayout(new GridLayout(1,2));
		lFrame.setLayout(new BorderLayout());
		bigTwoPanel.setLayout(new GridBagLayout());
		
		buttonPanel.add(playButton);
		buttonPanel.add(passButton);
		
		JPanel chat = new JPanel();
		chat.add(message);
		chat.add(sendMessage);
		
		JPanel down = new JPanel();
		down.setLayout(new GridLayout(1,2) );
		down.add(buttonPanel);
		
		
		down.add(chat);
		frame.add(down, BorderLayout.SOUTH);
		lFrame.add(bigTwoPanel);

		char[] suits = {'d','c','h','s'};
		char[] ranks = {'a','2','3','4','5','6','7','8', '9','t','j','q','k'};
		
		cardImages = new Image[4][13];
		
		for(int i=0; i<13; i++) {
			for(int j=0; j<4; j++) {
				cardImages[j][i] = new ImageIcon("Images/"+ranks[i]+suits[j]+".gif").getImage();
			}
		}
		
		cardBackImage = new ImageIcon("Images/b.gif").getImage();
		
		avatars = new Image[4];
		avatars[0] = new ImageIcon("Images/batman2.png").getImage();
		avatars[1] = new ImageIcon("Images/flash.png").getImage();
		avatars[2] = new ImageIcon("Images/superman.png").getImage();
		avatars[3] = new ImageIcon("Images/greenlantern.png").getImage();

		
		JScrollPane scroller = new JScrollPane(msgArea);
		rFrame.add(scroller);
		
		JScrollPane scroller1 = new JScrollPane(textArea);
		rFrame.add(scroller1);
		
		rFrame.setLayout(new BoxLayout(rFrame, BoxLayout.Y_AXIS));
		
		mainPanel.setBackground(new Color(0,153,0));
		
		mainPanel.add(lFrame);
		mainPanel.add(rFrame);
		frame.add(mainPanel);
		frame.setVisible(true);
	
	}
	
	
	/**
	 * a method for setting the index of the active player (i.e., the current player)
	 * @param activePlayer - player that is active
	 */
	public void setActivePlayer(int activePlayer) {
		if(activePlayer >= playerList.size() || activePlayer < 0 ) {
			this.activePlayer = -1;
		}
		else {
			this.activePlayer = activePlayer; 
		}
	}
	
	/**
	 * a method for getting an array of indices of the cards selected
	 **/
	public int[] getSelected() {
		int counter = 0;
		
		
		for(int i=0; i<selected.length; i++) {
			if(selected[i] == true) {
				counter++;
			}
		}
		
		int[] cardIdx = new int[counter];
		if(counter!=0) {
			cardIdx = new int[counter];
			counter=0;
			
			int n=0; 
			while(n<selected.length) {
				if(selected[n]==true) {
					cardIdx[counter] = n;
					counter++;
				}
				n++;
			}
		}
		
		return cardIdx;
		
	}
	
	/**
	 * a method for resetting the list of selected cards
	 **/
	public void resetSelected() {
		int a =0;
		
		while(a<13) {
			selected[a]= false;
			a++;
		}
		
	}
	
	public void printChat(String str) {
		textArea.append(str+"\n");
	}
	
	/**
	 * a method for repainting the GUI
	 **/
	public void repaint() {
		frame.repaint();
	}
	
	/**
	 * a method for printing the specified string to the message area of the GUI
	 **/
	public void printMsg(String msg) {
		msgArea.append(msg);
		frame.repaint();
	}
	
	/**
	 * a method for clearing the message area of the GUI
	 **/
	public void clearMsgArea() {
		msgArea.setText(null);
	}
	
	/**
	 * a method for resetting the GUI
	 **/
	public void reset() {
		resetSelected();
		clearMsgArea();
		enable();
	}
	
	/**
	 * a method for enabling user interactions with the GUI
	 **/
	public void enable() {
		playButton.setEnabled(true);
		passButton.setEnabled(true);
		bigTwoPanel.setEnabled(true);
		
	}
	
	/**
	 * a method for disabling user interactions with the GUI
	 **/
	public void disable() {
		playButton.setEnabled(false);
		passButton.setEnabled(false);
		bigTwoPanel.setEnabled(false);
		
	}
	
	/**
	 * an inner class that extends the JPanel class and implements the
	 * MouseListener interface. Overrides the paintComponent() method inherited from the
	 * JPanel class to draw the card game table. Implements the mouseClicked() method from
	 * the MouseListener interface to handle mouse click events
	 * @author Chhavi Sharma
	 *
	 */
	class BigTwoPanel extends JPanel implements MouseListener {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		
		
		/**
		 * a constructor for setting up the Big Two panel
		 */
		public BigTwoPanel() {
			this.addMouseListener(this);
		}

		/**
		 *  Overrides the paintComponent() method inherited from the
		 *  JPanel class to draw the card game table
		 *  @param graphics - Graphics for the table
		 **/
		public void paintComponent(Graphics graphics) {

			graphics.drawLine(0, 130, 1000, 130);
			graphics.drawLine(0, 260, 1000, 260);
			graphics.drawLine(0, 390, 1000, 390);
			graphics.drawLine(0, 520, 1000, 520);
			
			
			if(activePlayer == 0 && BigTwoClient.endGame == false && bigTwoPanel.isEnabled()==true) {
				graphics.setColor(Color.BLUE);
				
			}
			graphics.setFont(new Font("TimesNewRoman", Font.BOLD, 14));
			graphics.drawString(game.getPlayerList().get(0).getName(), 9, 20);
			graphics.setColor(Color.BLACK);
			
			if(activePlayer == 1 && BigTwoClient.endGame == false) {
				graphics.setColor(Color.BLUE);
				
			}
			graphics.setFont(new Font("TimesNewRoman", Font.BOLD, 14));
			graphics.drawString(game.getPlayerList().get(1).getName(), 9, 147);
			graphics.setColor(Color.BLACK);
			
			if(activePlayer == 2 && BigTwoClient.endGame == false) {
				graphics.setColor(Color.BLUE);
				
			}
			graphics.setFont(new Font("TimesNewRoman", Font.BOLD, 14));
			graphics.drawString(game.getPlayerList().get(2).getName(), 9, 280);
			graphics.setColor(Color.BLACK);
			
			if(activePlayer == 3 && BigTwoClient.endGame == false) {
				graphics.setColor(Color.BLUE);
				
			}
			graphics.setFont(new Font("TimesNewRoman", Font.BOLD, 14));
			graphics.drawString(game.getPlayerList().get(3).getName(), 9, 410);
			graphics.setColor(Color.BLACK);
			
			
			for(int i=0; i<4; i++) {
				if (i==0) {
					graphics.drawImage(avatars[i], 0, 30, this);
				}
				else if(i==1) {
					graphics.drawImage(avatars[i], 0, 157, this);
				}
				else if(i==2) {
					graphics.drawImage(avatars[i], 0, 290, this);
				}
				else if (i==3) {
					graphics.drawImage(avatars[i], 0, 420, this);
				}
			}
			
			if(((BigTwoClient) game).getPlayerID() == 0 ) {
				for(int b=0;b<playerList.get(0).getNumOfCards();b++)
					{ 
						int rank = game.getPlayerList().get(0).getCardsInHand().getCard(b).getRank();
						int suit = game.getPlayerList().get(0).getCardsInHand().getCard(b).getSuit();
						
						
						if(selected[b] == true) {
							//System.out.println("check");
							graphics.drawImage(cardImages[suit][rank], (150+b*20), (15+130*0-15), this);
							
						}
						else if(selected[b]==false){
							graphics.drawImage(cardImages[suit][rank], (150+b*20), (15+130*0), this);
		
						}
					}	
				
				for (int i = 0; i < game.getPlayerList().get(1).getCardsInHand().size(); i++) {
		    		graphics.drawImage(cardBackImage, (150+i*20), (15+130*1), this);
		    	}
				
				for (int i = 0; i < game.getPlayerList().get(2).getCardsInHand().size(); i++) {
		    		graphics.drawImage(cardBackImage, (150+i*20), (15+130*2), this);
		    	}
				
				for (int i = 0; i < game.getPlayerList().get(3).getCardsInHand().size(); i++) {
		    		graphics.drawImage(cardBackImage, (150+i*20), (15+130*3), this);
		    	}
			
			}
			
			
			
			if(((BigTwoClient) game).getPlayerID() == 1 ) {
				for(int b=0;b<playerList.get(1).getNumOfCards();b++)
					{ 
						int rank = game.getPlayerList().get(1).getCardsInHand().getCard(b).getRank();
						int suit = game.getPlayerList().get(1).getCardsInHand().getCard(b).getSuit();
						
						
						if(selected[b] == true) {
							//System.out.println("check");
							graphics.drawImage(cardImages[suit][rank], (150+b*20), (15+130*1-15), this);
							
						}
						else if(selected[b]==false){
							graphics.drawImage(cardImages[suit][rank], (150+b*20), (15+130*1), this);
		
						}
					}	
				
				for (int i = 0; i < game.getPlayerList().get(0).getCardsInHand().size(); i++) {
		    		graphics.drawImage(cardBackImage, (150+i*20), (15+130*0), this);
		    	}
				
				for (int i = 0; i < game.getPlayerList().get(2).getCardsInHand().size(); i++) {
		    		graphics.drawImage(cardBackImage, (150+i*20), (15+130*2), this);
		    	}
				
				for (int i = 0; i < game.getPlayerList().get(3).getCardsInHand().size(); i++) {
		    		graphics.drawImage(cardBackImage, (150+i*20), (15+130*3), this);
		    	}
			
			}
			

			
			if(((BigTwoClient) game).getPlayerID() == 2 ) {
				for(int b=0;b<playerList.get(2).getNumOfCards();b++)
					{ 
						int rank = game.getPlayerList().get(2).getCardsInHand().getCard(b).getRank();
						int suit = game.getPlayerList().get(2).getCardsInHand().getCard(b).getSuit();
						
						
						if(selected[b] == true) {
							//System.out.println("check");
							graphics.drawImage(cardImages[suit][rank], (150+b*20), (15+130*2-15), this);
							
						}
						else if(selected[b]==false){
							graphics.drawImage(cardImages[suit][rank], (150+b*20), (15+130*2), this);
		
						}
					}	
				
				for (int i = 0; i < game.getPlayerList().get(1).getCardsInHand().size(); i++) {
		    		graphics.drawImage(cardBackImage, (150+i*20), (15+130*1), this);
		    	}
				
				for (int i = 0; i < game.getPlayerList().get(0).getCardsInHand().size(); i++) {
		    		graphics.drawImage(cardBackImage, (150+i*20), (15+130*0), this);
		    	}
				
				for (int i = 0; i < game.getPlayerList().get(3).getCardsInHand().size(); i++) {
		    		graphics.drawImage(cardBackImage, (150+i*20), (15+130*3), this);
		    	}
			}
			
			
			if(((BigTwoClient) game).getPlayerID() == 3) {
				for(int b=0;b<playerList.get(3).getNumOfCards();b++)
					{ 
						int rank = game.getPlayerList().get(3).getCardsInHand().getCard(b).getRank();
						int suit = game.getPlayerList().get(3).getCardsInHand().getCard(b).getSuit();
						
						
						if(selected[b] == true) {
							graphics.drawImage(cardImages[suit][rank], (150+b*20), (15+130*3-15), this);
							
						}
						else if(selected[b]==false){
							graphics.drawImage(cardImages[suit][rank], (150+b*20), (15+130*3), this);
		
						}
					}	
				
				for (int i = 0; i < game.getPlayerList().get(1).getCardsInHand().size(); i++) {
		    		graphics.drawImage(cardBackImage, (150+i*20), (15+130*1), this);
		    	}
				
				for (int i = 0; i < game.getPlayerList().get(2).getCardsInHand().size(); i++) {
		    		graphics.drawImage(cardBackImage, (150+i*20), (15+130*2), this);
		    	}
				
				for (int i = 0; i < game.getPlayerList().get(0).getCardsInHand().size(); i++) {
		    		graphics.drawImage(cardBackImage, (150+i*20), (15+130*0), this);
		    	}
			
			}
			
			handsOnTable = game.getHandsOnTable();
			if(handsOnTable.size()!=0) {
				
				String text = ("Played by " +handsOnTable.get(handsOnTable.size()-1).getPlayer().getName()+": ") ;
				graphics.setFont(new Font("TimesNewRoman", Font.BOLD, 14));
				graphics.drawString(text, 9, 550);
				
				int d=0;
				while(d<handsOnTable.get(handsOnTable.size()-1).size() ) {
					int rank = handsOnTable.get(handsOnTable.size()-1).getCard(d).getRank();
					int suit = handsOnTable.get(handsOnTable.size()-1).getCard(d).getSuit();
					
					graphics.drawImage(cardImages[suit][rank], (150+d*20), (40+125*4), this);
					d++;
				}
			}
		}
		
		
		
		/**
		 * handles mouse clicks
		 * @param mouseEvent - handles MouseEvent
		 **/
		@Override
		public void mouseClicked(MouseEvent mouseEvent) {
			// TODO Auto-generated method stub
			int mx, my;
			
			mx = mouseEvent.getX();
			my = mouseEvent.getY();
			
			for(int i = game.getPlayerList().get(activePlayer).getNumOfCards()-1; i>=0; i-- ) {
				if(((BigTwoClient)game).getCurrentIdx() == ((BigTwoClient)game).getPlayerID() && (my>=15+130*activePlayer-15 && my<=15+130*activePlayer+94-15) && (mx>=150+i*20 && mx<=150+i*20+70) && selected[i]==true) {
					selected[i] =false;
					break;
				}
				else if(((BigTwoClient)game).getCurrentIdx() == ((BigTwoClient)game).getPlayerID() && (my>=15+130*activePlayer && my<=15+130*activePlayer+94) && (mx>=150+i*20 && mx<=150+i*20+70) && selected[i] == false) {
					selected[i] =true;
					break;
				}
				
			}
			
			
			frame.repaint();
			}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	/**
	 * an inner class that implements the ActionListener
	 * interface. Implements the actionPerformed() method from the ActionListener interface
	 * to handle button-click events for the Play button. 
	 * @author Chhavi Sharma
	 *
	 */
	class PlayButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent actionEvent) {
			
			clickPlay = true;
			
			int[] cardIdx = getSelected();
			
			if(cardIdx.length!=0) {
			game.makeMove(activePlayer,cardIdx);
				
			resetSelected();
				
			frame.repaint();
			}
			else {
				printMsg("Not allowed" + "\n");
			}
		}
		
	}
		
	/**
	 * an inner class that implements the ActionListener
	 * interface. Implements the actionPerformed() method from the ActionListener interface
	 * to handle button-click events for the Pass button. 
	 * @author Chhavi Sharma
	 *
	 */
	class PassButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent actionEvent) {
			
			
			clickPass=true;
		
			game.makeMove(activePlayer,null);
				
			resetSelected();
				
			frame.repaint();		
		}
	}
		
	/**
	 * an inner class that implements the ActionListener
	 * interface. Implements the actionPerformed() method from the ActionListener interface
	 * to handle button-click events for the Restart button. 
	 * @author Chhavi Sharma
	 *
	 */
	class ConnectButtonListener implements ActionListener {
		/**
		 *
		 * Method to perform Restart Button logic on the click of the Restart button.
		 *
		 */
		public void actionPerformed(ActionEvent e) {
			if(((BigTwoClient)game).getPlayerID()>=0 && ((BigTwoClient)game).getPlayerID()<=3) {
				printMsg("You are already connected to the server!");
			} else {
				((BigTwoClient)game).makeConnection();
			}


		}
	}
		
	/**
	 * an inner class that implements the ActionListener
	 * interface. Implements the actionPerformed() method from the ActionListener interface
	 * to handle menu-item-click events for the Quit menu item. 
	 * @author Chhavi Sharma
	 *
	 */
	class QuitMenuItemListener implements ActionListener {
			
		public void actionPerformed(ActionEvent actionEvent) {			
			System.exit(0);
		}
		
	}
	
	class ClearMessageButtonListener implements ActionListener {
		/*
		 * {@inheritDoc}
		 */
		public void actionPerformed(ActionEvent e) {
			cleartextArea();
		}
	}

	class SendButtonListener implements ActionListener {
		/*
		 * {@inheritDoc}
		 */
		public void actionPerformed(ActionEvent e) {
			if(message.getText()!="" && message.getText()!=null) {
				((BigTwoClient)game).sendMessage(new CardGameMessage(CardGameMessage.MSG, -1, message.getText()));
				message.setText("");
			}
		}
	}

	class EnterMessageListener implements KeyListener {

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				if(message.getText()!="" && message.getText()!=null) {
					((BigTwoClient)game).sendMessage(new CardGameMessage(CardGameMessage.MSG, -1, message.getText()));
					message.setText("");
				}
			}
		}

	}

	/**
	 * clearing the text area of the GUI
	 */
	public void cleartextArea() {
		this.textArea.setText("");
	}
		
	
}
