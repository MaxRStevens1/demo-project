import javax.swing.ImageIcon;

public class UpStairTile extends GroundTile {
	StairTile partner; // the stair tile that connects the upstair and the downstair

	public UpStairTile(int x, int y) {
		super(x, y);
		image = new ImageIcon(this.getClass().getResource("TileDownstair.png")).getImage();
	}
}
