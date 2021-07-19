import javax.swing.ImageIcon;

public class WallTile extends Tile {
	/**
	 * wall tile
	 * 
	 * @param x
	 * @param y
	 */
	public WallTile(int x, int y) {
		super(x, y);
		// TODO Auto-geenerated constructor stub
		image = new ImageIcon(this.getClass().getResource("TileWall.png")).getImage();
		isSeethrough = false;
	}

}
