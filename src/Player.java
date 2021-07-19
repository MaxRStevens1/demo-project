import javax.swing.ImageIcon;
import java.util.ArrayList;

public class Player extends Entity {
	String name;
	int currentMapLevel;
	int enemiesCrunched;

	// roll this many d4
	int damageDiceNumber;
	int damageDiceSides;
	int speed;

	// when caps lock is pressed, camera is centered around capX and capY but player
	// is not takening a turn
	int freeX;
	int freeY;
	boolean isFreeCam;

	boolean hasName;
	boolean hasClass;

	Tile tile;
	ArrayList<ArrayList<Tile>> endTilesForEnemy;

	/**
	 * constructor
	 * 
	 * @param x player x
	 * @param y player y
	 */
	public Player(int x, int y) {
		super(x, y);
		image = new ImageIcon(this.getClass().getResource("TilePlayer.png")).getImage();
		damageDiceNumber = 1;
		damageDiceSides = 4;
		speed = 100;

		freeX = x;
		freeY = y;

		hasName = false;
		hasClass = false;
		isFreeCam = false;
		tile = null;
		currentMapLevel = 0;
		endTilesForEnemy = new ArrayList<ArrayList<Tile>>();
	}

	public Player() {
		this(-1, -1);
	}

	/**
	 * sets name
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
		hasName = true;
	}

	public void increaseEnemiesCrunched() {
		enemiesCrunched++;
	}

	/**
	 * moves the player and sets the tile and player tile to correct value if not in
	 * freecam mode
	 * 
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
	 * 
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
	 * rolls a total number of damageDiceNumber dice with a damageDiceSides sided
	 * die
	 * 
	 * @return total value of dmageDiceNumber die rolled with damageDiceSides sides
	 */
	public int getAttackDamage() {
		int totalDamage = 0;
		for (int i = 0; i < damageDiceNumber; i++)
			totalDamage += Math.round(Math.random() * damageDiceSides);
		return totalDamage;
	}

	/**
	 * Gets the a random neighboring cordinate point, used for determining end step
	 * of enemy pathfinding
	 * 
	 * @return x/y cord
	 */
	public Point getNeighborPoint() {
		Point retPoint = null;
		int randomPointSelection = (int) Math.round(Math.random() * 7 + 1);
		switch (randomPointSelection) {
		case 1:
			retPoint = new Point(x - 1, y - 1);
			break;
		case 2:
			retPoint = new Point(x, y - 1);
			break;
		case 3:
			retPoint = new Point(x + 1, y - 1);
			break;
		case 4:
			retPoint = new Point(x - 1, y);
			break;
		case 5:
			retPoint = new Point(x + 1, y);
			break;
		case 6:
			retPoint = new Point(x - 1, y + 1);
			break;
		case 7:
			retPoint = new Point(x, y + 1);
			break;
		case 8:
			retPoint = new Point(x + 1, y + 1);
			break;
		}
		return retPoint;
	}

}
