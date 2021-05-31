import javax.swing.ImageIcon;

public class DownStairTile extends StairTile {

	public DownStairTile(int x, int y) {
		super(x, y);
		image = new ImageIcon(this.getClass().getResource("TileDownstair.png")).getImage();
	}
}
