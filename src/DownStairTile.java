import javax.swing.ImageIcon;

public class DownStairTile extends StairTile {

	/**
	 * Tile for player to descend downward
	 * 
	 * @param x in map
	 * @param y in map
	 */
	public DownStairTile(int x, int y) {
		super(x, y);
		image = new ImageIcon(this.getClass().getResource("TileDownstair.png")).getImage();
	}
}
