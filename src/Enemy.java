import javax.swing.ImageIcon;

public class Enemy extends Entity{
	Tile tile;
	int health;
	int mana;
	// # of dice to roll
	int damageDiceNumber;
	// # of sides on the dice
	int damageDiceSides;
	public Enemy (int x, int y) {
		super (x, y);
		image = new ImageIcon(this.getClass().getResource("TileEnemy.png")).getImage();
		health = 1;
		damageDiceNumber = 0;
		damageDiceSides = 0;
		name = "Enemy";
	}
	
	public int DamageCalculator () {
		int totalDamage = 0;
		for (int i = 0; i < damageDiceNumber; i++)
			totalDamage += (int) Math.random() * damageDiceSides;
		return totalDamage;
	}
	
	public int recieveDamager (int damage) {
		health -= damage;
		return health;
	}
}
