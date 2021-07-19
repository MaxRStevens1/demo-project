/**
 * 7/18/2021
 * @author Max Stevens
 * V 1.0
 * Hello and Welcome to M Explorer, explore the M's and the randomly generated layout!
 */
public class Main
{
  public static void main(String args[]) {
    GameDriver game = new GameDriver();
    //game.intializeMap();
    GraphicsDriver graph = new GraphicsDriver(game.getMap(), game); 
    game.setGraphics(graph);
  }
  
}
