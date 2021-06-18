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
  Enemy enemy;
  
  boolean canWalk;
  boolean isSeethrough;
  boolean isVisible;
  boolean hasBeenSeen;
  boolean monsterSpawnFlag;

  /**
   * tile constructor, sets g and h to 0
   * @param x cordinate
   * @param y cordinate
   */
  public Tile(int x, int y) {
    this.x = x;
    this.y = y;
    image = new ImageIcon(this.getClass().getResource("TileFloor.png")).getImage();
    g = 0;
    h = 0;
    canWalk = false;
    enemy = null;
    isSeethrough = true;
    isVisible = false;
  }
  
  public Tile(int x, int y, Enemy enemy) {
    this.x = x;
    this.y = y;
    image = new ImageIcon(this.getClass().getResource("TileFloor.png")).getImage();
    g = 0;
    h = 0;
    canWalk = false;
    this.enemy = enemy;
    isVisible = false;
    isSeethrough = true;
  }
  
  
  public Enemy getEnemy() {return enemy;}
  public void setEnemy(Enemy enemy) {this.enemy = enemy;}
  
  /**
   * returns g+h
   * @return f
   */
  public int getF() {return g + h;}
  
	//public String toString() {
	//	return "(" + x + ", " + y + ")";
	//}
}
