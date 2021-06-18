import javax.swing.ImageIcon;

public class Player extends Entity {
	String name;
	int health;
	int mana;
	int level;
	int experience;
	int currentMapLevel;
	// roll this many d4
	int damageDiceNumber;
	int damageDiceSides;

	// when caps lock is pressed, camera is centered around capX and capY but player
	// is not takening a turn
	int freeX;
	int freeY;
	boolean isFreeCam;

	boolean hasName;
	boolean hasClass;

	Tile tile;

	/**
	 * constructor
	 * @param x player x
	 * @param y player y
	 */
	public Player(int x, int y) {
		super(x, y);
		image = new ImageIcon(this.getClass().getResource("TilePlayer.png")).getImage();
		health = 10;
		mana = 0;
		damageDiceNumber = 1;
		damageDiceSides = 4;
		
		
		freeX = x;
		freeY = y;
		
		
		level = 1;
		experience = 0;
		hasName = false;
		hasClass = false;
		isFreeCam = false;
		tile = null;
		currentMapLevel = 0;
	}

	public Player() {
		this(-1, -1);
	}

	/**
	 * sets name
	 * @param name 
	 */
	public void setName(String name) {
		this.name = name;
		hasName = true;
	}

	/**
	 * gives the player damage
	 * @param damage 
	 * @return new health
	 */
	public int recieveDamager (int damage) {
		health -= damage;
		return health;
	}

	/**
	 * 
	 * @return bool if health > 0
	 */
	public boolean isAlive() {
		return health > 0;
	}
	
	

	/**
	 * moves the player and sets the tile and player tile to correct value if not in freecam mode
	 * @param x new xPos
	 * @param y new yPos
	 * @param t new tile for player
	 */
	public void movePlayer(int x, int y, Tile t) {
		if (!isFreeCam) {
			tile.player = null;
			tile = null;
		}
		setXY(x, y);
		if (!isFreeCam) {
			t.player = this;
			tile = t;
		}

	}
	/**
	 * sets player x and y, to inpuut, unless freecam is activated
	 * @param x new x
	 * @param y new y
	 */
	public void setXY(int x, int y) {
		if (!isFreeCam) {
			this.x = x;
			this.y = y;
		}
		this.freeX = x;
		this.freeY = y;
	}
	
	/**
	 * rolls a total number of damageDiceNumber dice with a damageDiceSides sided die 
	 * @return total value of dmageDiceNumber die rolled with damageDiceSides sides
	 */
	public int getAttackDamage() {
		int totalDamage = 0;
		for (int i = 0; i < damageDiceNumber; i++)
			totalDamage += Math.round(Math.random() * damageDiceSides);
		return totalDamage;
	}
	


}
