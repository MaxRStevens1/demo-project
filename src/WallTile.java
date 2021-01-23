import javax.swing.ImageIcon;

public class WallTile extends Tile
{

  public WallTile(int x, int y)
  {
    super(x, y);
    // TODO Auto-geenerated constructor stub
    image = new ImageIcon(this.getClass().getResource("TileWall.png")).getImage();

  }

}
