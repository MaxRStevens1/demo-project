import java.util.Stack;

import javax.swing.ImageIcon;

public class Enemy extends Entity {
	Tile tile;
	int health;
	int mana;
	// # of dice to roll
	int damageDiceNumber;
	// # of sides on the dice
	int damageDiceSides;

	boolean isActive;
	boolean doGeneratePath;
	Stack<Tile> currentPath;

	/**
	 * constructor of Enemy
	 * 
	 * @param x x position spawned at
	 * @param y position spawned at
	 */
	public Enemy(int x, int y) {
		super(x, y);
		image = new ImageIcon(this.getClass().getResource("TileEnemy.png")).getImage();
		health = 1;
		damageDiceNumber = 0;
		damageDiceSides = 0;
		name = "Enemy";
		isActive = false;
		doGeneratePath = false;
	}

	/**
	 * constructor of enemy that specificies the tile that the enemy spawns in,
	 * along with previous constructor
	 * 
	 * @param x        pos
	 * @param y        pos
	 * @param roomTile tile enemy spawns in
	 */
	public Enemy(int x, int y, Tile roomTile) {
		this(x, y);
		this.tile = roomTile;
	}

	/**
	 * Enemy takes input number as damage
	 * 
	 * @param damage taken
	 * @return post damage health
	 */
	public int recieveDamager(int damage) {
		health -= damage;
		return health;
	}

	/**
	 * sets new path for enemy
	 * 
	 * @param newPath
	 */
	public void setPath(Stack<Tile> newPath) {
		currentPath = newPath;
	}

}
