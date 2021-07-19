import javax.swing.ImageIcon;

public class PlayerStartTile extends GroundTile {

	/**
	 * the tile the player starts at in map level 0, done to allow for non constant
	 * in game startup
	 * 
	 * @param x
	 * @param y
	 */
	public PlayerStartTile(int x, int y) {
		super(x, y);
		// image = new
		// ImageIcon(this.getClass().getResource("TileFloor.png")).getImage();
	}

}
