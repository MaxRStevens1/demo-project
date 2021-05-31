import javax.swing.ImageIcon;

public class UpStairTile extends StairTile {

	public UpStairTile(int x, int y) {
		super(x, y);
		image = new ImageIcon(this.getClass().getResource("TileUpstair.png")).getImage();
	}
}
