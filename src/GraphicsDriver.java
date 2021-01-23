import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class GraphicsDriver implements KeyListener {
	JFrame frame;
	TilePanel tilePane;
	JLabel nameLabel;
	JLabel healthLabel;
	JLabel manaLabel;
	JLabel levelLabel;

	JTextArea outputText;
	JTextField inputText;

	Map map;
	Player player;
	GameDriver game;

	/**
	 * big old intializes monster, makes it so the various labels, panels, and
	 * frames work nicely once intialization is done, runs gameGraphicSetUp
	 * 
	 * @param map
	 * @param game
	 */
	public GraphicsDriver(Map map, GameDriver game) {
		this.map = map;
		player = map.player;
		this.game = game;
		tilePane = new TilePanel(map);
		frame = new JFrame();
		outputText = new JTextArea();
		inputText = new JTextField();

		nameLabel = new JLabel();
		healthLabel = new JLabel();
		manaLabel = new JLabel();
		levelLabel = new JLabel();

		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(960, 998);
		frame.setContentPane(tilePane);
		frame.setLayout(null);

		tilePane.setVisible(true);
		tilePane.setBounds(0, 0, 1000, 1000);
		tilePane.setFocusable(true);
		tilePane.addKeyListener(this);
		tilePane.setBackground(Color.BLACK);
		gameGraphicSetUp();
	}

	/**
	 * Makes player chose name before starting game, and further intializes various
	 * displays Has a key Listener for the in panel textbox
	 */
	public void gameGraphicSetUp() {
		JScrollPane scrollText = new JScrollPane(outputText);

		tilePane.add(scrollText);
		// tilePane.add(outputText); new comment
		tilePane.add(inputText);

		outputText.setText(outputText.getText()
				+ "Welcome to a graphical, pathfinding, and gui demo,"
				+ "\nMove around using a computer numpad or arrowkeys"
				+ "\nIf your key presses are not registering, try clicking on the panel and pressing shifttab"
				+ "\nMovement is 8 directional, but there should be no issue if you are using arrow keys"
				+ "\nIf you want to pan around the map without moving the charecter, press caps locks"
				+ "\nType h into the textbox below and press enter to see this message again."
				+ "\nType c if you want to clear this textbox");

		outputText.setText(outputText.getText() + "\n" + "Enter your name to start the game!\nWhat is your name?");
		outputText.setFocusable(false);
		scrollText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollText.setSize(600, 200);
		scrollText.setLocation(0, 735);
		scrollText.setFocusable(false);

		inputText.setSize(600, 25);
		inputText.setLocation(0, 935);
		inputText.setBackground(Color.LIGHT_GRAY);
		inputText.setSelectedTextColor(Color.WHITE);

		inputText.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_ENTER) {
					outputText.setText(outputText.getText() + "\n" + inputText.getText());
					if (!player.hasName) {
						outputText
								.setText(outputText.getText() + "\nYou chose your name to be: " + inputText.getText());
						player.setName(inputText.getText());
						player.hasName = true;
						nameLabel.setText(inputText.getText());
						map.getTile(player.x, player.y).player = player;
						updatePlayerText();
					} else if (!player.hasClass) {

					}
					if (inputText.getText().equals("h")) {
						outputText.setText(outputText.getText() + "\n"
								+ "Welcome to a graphical, pathfinding, and gui demo,"
								+ "\nMove around using a computer numpad or arrowkeys"
								+ "\nIf your key presses are not registering, try clicking on the panel and pressing shifttab"
								+ "\nMovement is 8 directional, but there should be no issue if you are using arrow keys"
								+ "\nIf you want to pan around the map without moving the charecter, press caps locks"
								+ "\nType h into the textbox below and press enter to see this message again."
								+ "\nType c if you want to clear this textbox");
					} else if (inputText.getText().equals("c")) {
						outputText.setText("");
					}
					inputText.setText("");
				}
			}
		});
		setUpPlayerInfo();
	}

	/**
	 * sudo refresh / intialization for player info
	 */
	public void setUpPlayerInfo() {
		tilePane.add(nameLabel);
		tilePane.add(healthLabel);
		tilePane.add(manaLabel);
		tilePane.add(levelLabel);

		nameLabel.setLocation(600, 100);
		nameLabel.setFont(new Font("Serif", Font.PLAIN, 18));
		nameLabel.setSize(300, 100);
		nameLabel.setVisible(true);
		nameLabel.setFocusable(false);
		nameLabel.setForeground(Color.white);

		healthLabel.setLocation(600, 120);
		healthLabel.setFont(new Font("Serif", Font.PLAIN, 18));
		healthLabel.setSize(200, 100);
		healthLabel.setVisible(true);
		healthLabel.setFocusable(false);
		healthLabel.setForeground(Color.white);

		manaLabel.setLocation(650, 120);
		manaLabel.setFont(new Font("Serif", Font.PLAIN, 18));
		manaLabel.setSize(200, 100);
		manaLabel.setVisible(true);
		manaLabel.setFocusable(false);
		manaLabel.setForeground(Color.white);

		levelLabel.setLocation(600, 140);
		levelLabel.setFont(new Font("Serif", Font.PLAIN, 18));
		levelLabel.setSize(400, 100);
		levelLabel.setVisible(true);
		levelLabel.setFocusable(false);
		levelLabel.setForeground(Color.white);
	}

	/**
	 * updating info about player available to player, update whenever on var has
	 * changed
	 */
	public void updatePlayerText() {
		healthLabel.setText("HP: " + player.health);
		manaLabel.setText("MP: " + player.mana);
		levelLabel.setText("Level: " + player.level + " EXP: " + player.experience);

	}

	public void setMap(Map map) {
		tilePane.setMap(map);
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * gets input for keys when focused on panel, and pushes them, and a
	 * arbitariarly chosen number to gameDriver to handel what happens with keypress
	 */
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		/*
		 * Movement based on numpad
		 */
		switch (key) {
		case KeyEvent.VK_CAPS_LOCK:
			game.moveSelector(0);
			break;
		case KeyEvent.VK_NUMPAD1:
			game.moveSelector(1);
			break;
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_NUMPAD2:
			game.moveSelector(2);
			break;
		case KeyEvent.VK_NUMPAD3:
			game.moveSelector(3);
			break;
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_NUMPAD4:
			game.moveSelector(4);
			break;
		case KeyEvent.VK_NUMPAD5:
			game.moveSelector(5);
			break;
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_NUMPAD6:
			game.moveSelector(6);
			break;
		case KeyEvent.VK_NUMPAD7:
			game.moveSelector(7);
			break;
		case KeyEvent.VK_UP:
		case KeyEvent.VK_NUMPAD8:
			game.moveSelector(8);
			break;
		case KeyEvent.VK_NUMPAD9:
			game.moveSelector(9);
			break;
		}

	}
}
