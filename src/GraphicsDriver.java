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
	JLabel enemiesCrunchedLabel;
	JLabel floorLevelLabel;

	JTextArea outputText;
	JTextField inputText;

	Map map;
	Player player;
	GameDriver game;

	/**
	 * big old initializes monster, makes it so the various labels, panels, and
	 * frames work nicely once initialization is done, runs gameGraphicSetUp
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
		enemiesCrunchedLabel = new JLabel();
		floorLevelLabel = new JLabel();

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
		tilePane.add(inputText);

		outputText.setText(outputText.getText() + "Welcome to M Explorer, a procedurally generated demo game"
				+ "\nMove around using a computer numpad or arrowkeys"
				+ "\nIf your key presses are not registering, try clicking on the panel and pressing tab"
				+ "\nMovement is done using num pad or arrow keys"
				+ "\nIf you want to pan around the map without moving the charecter, press caps locks to enter free cam"
				+ "\nThe tiles that are is not the standard wall, floor, or door is a stair tile!"
				+ "\nThey can be used to ascend or descend the dungeon using >, <, or PageUp or PageDown"
				+ "\nYour vision is in a circle with a radius of 8 tiles, if a tile is black that means you haven't seen it yet!"
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
								+ "Welcome to M Explorer, a procedurally generated demo game"
								+ "\nMove around using a computer numpad or arrowkeys"
								+ "\nIf your key presses are not registering, try clicking on the panel and pressing tab"
								+ "\nMovement is done using num pad or arrow keys"
								+ "\nIf you want to pan around the map without moving the charecter, press caps locks to enter free cam"
								+ "\nThe tiles that are is not the standard wall, floor, or door is a stair tile!"
								+ "\nThey can be used to ascend or descend the dungeon using >, <, or PageUp or PageDown"
								+ "\nYour vision is in a circle with a radius of 8 tiles, if a tile is black that means you haven't seen it yet!"
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

	public void setNewText(String input) {
		outputText.setText(outputText.getText() + "\n" + input);
	}

	/**
	 * sudo refresh / intialization for player info
	 */
	public void setUpPlayerInfo() {
		tilePane.add(nameLabel);
		tilePane.add(enemiesCrunchedLabel);
		tilePane.add(floorLevelLabel);

		nameLabel.setLocation(650, 100);
		nameLabel.setFont(new Font("Serif", Font.PLAIN, 18));
		nameLabel.setSize(300, 100);
		nameLabel.setVisible(true);
		nameLabel.setFocusable(false);
		nameLabel.setForeground(Color.white);

		enemiesCrunchedLabel.setLocation(640, 120);
		enemiesCrunchedLabel.setFont(new Font("Serif", Font.PLAIN, 18));
		enemiesCrunchedLabel.setSize(200, 100);
		enemiesCrunchedLabel.setVisible(true);
		enemiesCrunchedLabel.setFocusable(false);
		enemiesCrunchedLabel.setForeground(Color.white);

		floorLevelLabel.setLocation(650, 140);
		floorLevelLabel.setFont(new Font("Serif", Font.PLAIN, 18));
		floorLevelLabel.setSize(400, 100);
		floorLevelLabel.setVisible(true);
		floorLevelLabel.setFocusable(false);
		floorLevelLabel.setForeground(Color.white);
	}

	/**
	 * updating info about player available to player, update whenever on var has
	 * changed
	 */
	public void updatePlayerText() {
		floorLevelLabel.setText("Layers in: " + player.currentMapLevel);
		enemiesCrunchedLabel.setText("Enemies Crunched: " + player.enemiesCrunched);
	}

	/**
	 * Sets map in game driver to new map
	 * 
	 * @param map new map
	 */
	public void setMap(Map map) {
		tilePane.setMap(map);
		this.map = map;
		floorLevelLabel.setText("Layers in: " + player.currentMapLevel);
	}

	/**
	 * done to implement interface, unused
	 */
	public void keyTyped(KeyEvent e) {
	}

	/**
	 * gets input for keys when focused on panel, and pushes them, and a Arbitrarily
	 * chosen number to GameDriver to handle what happens with key press
	 */
	public void keyPressed(KeyEvent e) {
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
		case KeyEvent.VK_DOWN: // doesn't break so it just runs its sister command
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
		// case KeyEvent.VK_PERIOD:
		case KeyEvent.VK_PAGE_UP:
		case GameConstants.GREATER_THAN_NUM: // is the > symbol
			game.moveSelector(10); // number is arbitrary
			updatePlayerText();
			break;
		// case KeyEvent.VK_COMMA:
		case KeyEvent.VK_PAGE_DOWN:
		case GameConstants.LESS_THAN_NUM: // is the < symbol
			game.moveSelector(11);
			updatePlayerText();
			break;
		}

	}

	/**
	 * done to implement interface, unused
	 */
	public void keyReleased(KeyEvent e) {
	}

}
