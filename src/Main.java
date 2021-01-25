/**
 * 1/23/2021
 * @author Max Stevens
 * V 0.01
 */
public class Main
{
  public static void main(String args[]) {
    GameDriver game = new GameDriver();
    game.intializeMap();
    GraphicsDriver graph = new GraphicsDriver(game.getMap(), game); 
    game.setGraphics(graph);
  }
  
}
