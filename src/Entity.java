import java.awt.Image;

public class Entity
{
  int x;
  int y;
  Image image;
  String name;
  
  public Entity(int x, int y) {
    this.x = x;
    this.y = y;
  }
  public Entity() {
    x = -1; 
    y = -1;
  }
  
  
}
