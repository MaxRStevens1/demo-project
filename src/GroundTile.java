import javax.swing.ImageIcon;

public class GroundTile extends Tile {
	/**
	 * Constructor for groundtile
	 * @param x tiles current x,y
	 * @param y tiles current x,y
	 */
	
	
	public GroundTile(int x, int y) {
		super(x, y);
		image = new ImageIcon(this.getClass().getResource("TileFloor.png")).getImage();
		player = null;
		canWalk = true;
		monsterSpawnFlag = false;
	}
	// same as above but with player added
	public GroundTile(int x, int y, Player player) {
		super(x, y);
		image = new ImageIcon(this.getClass().getResource("TileFloor.png")).getImage();
		this.player = player;
		canWalk = true;
	}

	
	public boolean getMonsterFlag () {return monsterSpawnFlag;}
	public void setMonsterFlag (boolean mFlag) {monsterSpawnFlag = mFlag;}

	
}
