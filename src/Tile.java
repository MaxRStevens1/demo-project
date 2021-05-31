import java.awt.Image;

import javax.swing.ImageIcon;

public class Tile
{
  int x;
  int y;
  Image image;
  int g,h;
  Tile parent;
  Player player;
  boolean canWalk;
  

  public Tile(int x, int y) {
    this.x = x;
    this.y = y;
    image = new ImageIcon(this.getClass().getResource("TileFloor.png")).getImage();
    g = 0;
    h = 0;
    canWalk = false;
  }
  /**
   * returns g+h
   * @return f
   */
  public int getF() {return g + h;}
  
	//public String toString() {
	//	return "(" + x + ", " + y + ")";
	//}
}
