import javax.swing.ImageIcon;

public class DownStairTile extends GroundTile {
	StairTile partner; // the stair tile that connects the upstair and the downstair

	public DownStairTile(int x, int y) {
		super(x, y);
		image = new ImageIcon(this.getClass().getResource("TileDownstair.png")).getImage();
	}
}
