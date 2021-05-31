import javax.swing.ImageIcon;

public class StairTile extends GroundTile {
	StairTile partner;

	public StairTile(int x, int y) {
		super(x, y);
		image = new ImageIcon(this.getClass().getResource("TileFloor.png")).getImage();
	}
}
