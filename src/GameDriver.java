import java.util.ArrayList;

public class GameDriver {
	private Map map;
	private Player player;
	private ArrayList entityTracker;
	private GraphicsDriver graph;
	private Dungeon dungeon;

	/**
	 * constructor
	 */
	public GameDriver() {
		player = new Player();
		dungeon = new Dungeon(GameConstants.X_MAP_SIZE, GameConstants.Y_MAP_SIZE, player);
		map = dungeon.getMap();
	}

	/**
	 * basic map intialization, read map to for more detailed explanation
	 */
	public void intializeMap() {
	}

	/**
	 * getter
	 * @return map
	 */
	public Map getMap() {
		return map;
	}
	
	/**
	 * sets graphics
	 * @param graph
	 */
	public void setGraphics(GraphicsDriver graph) {
		this.graph = graph;
	}
	/**
	 * refreshes the screen. Probably not needed
	 */
	public void refresh() {
		graph.tilePane.repaint();
	}

	/**
	 * info fed in from keyboard inputs from graphicsDriver 
	 * 
	 * @param moveNum a number signfying keyboard input given from graphics driver
	 */
	public void moveSelector(int moveNum) {
		switch (moveNum) {
		case 0: // case for caps lock
			player.isFreeCam = !player.isFreeCam;
			player.freeX = player.x;
			player.freeY = player.y;
			break;
		case 1:
			move1();
			break;
		case 2:
			move2();
			break;
		case 3:
			move3();
			break;
		case 4:
			move4();
			break;
		case 5:
			move5();
			break;
		case 6:
			move6();
			break;
		case 7:
			move7();
			break;
		case 8:
			move8();
			break;
		case 9:
			move9();
			break;
		}
	}
	/**
	 * helper for movement, to make sure player doesn't move OOB
	 * @param x player x
	 * @param y player y
	 * @return boolean if player can walk to the tile, and its not oob
	 */
	private boolean isTileValidForPlayerToWalk(int x, int y) {
		if(x >= 0 && x < map.x && y >= 0 && y < map.y) {
			return map.getTile(x, y).canWalk || player.isFreeCam;
		} 
		return false;
	}
	
	
	/**
	 * Based off numpad inputs, all moveX function the same only difference is in x and y values
	 * 7 8 9
	 * 4 5 6
	 * X 2 3
	 */
	public void move1() {
		int x = player.x - 1;
		int y = player.y + 1;
		if(player.isFreeCam) {
			x = player.freeX - 1;
			y = player.freeY + 1;
		}
		if (isTileValidForPlayerToWalk(x, y)) {
			Tile t = map.getTile(x, y);
			// look at player for more info, takes x and y and tile to move player
			player.movePlayer(x, y, t);
		}
	}
	/**
	 * 7 8 9
	 * 4 5 6
	 * 1 X 3
	 */
	public void move2() {
		
		int x = player.x;
		int y = player.y + 1;
		if(player.isFreeCam) {
			x = player.freeX;
			y = player.freeY + 1;
		}
		if (isTileValidForPlayerToWalk(x, y)) {
			Tile t = map.getTile(x, y);
			player.movePlayer(x, y, t);
		}
	}

	/**
	 * 7 8 9
	 * 4 5 6
	 * 1 2 X
	 */
	public void move3() {
		int x = player.x + 1;
		int y = player.y + 1;
		if(player.isFreeCam) {
			x = player.freeX + 1;
			y = player.freeY + 1;
		}
		if (isTileValidForPlayerToWalk(x, y)) {
			Tile t = map.getTile(x, y);
			player.movePlayer(x, y, t);
		}
	}

	/**
	 * 7 8 9
	 * X 5 6
	 * 1 2 3
	 */
	public void move4() {
		int x = player.x - 1;
		int y = player.y;
		if(player.isFreeCam) {
			x = player.freeX - 1;
			y = player.freeY;
		}
		if(isTileValidForPlayerToWalk(x,y)) {
			Tile t = map.getTile(x, y);
			player.movePlayer(x, y, t);
		}
	}

	/**
	 * 6 7 8
	 * 4 X 6
	 * 1 2 3
	 * 
	 * Waits a turn, not implemented yet
	 */
	public void move5() {
	}
	
	/**
	 * 7 8 9
	 * 4 5 X
	 * 1 2 3
	 */
	public void move6() {
		int x = player.x + 1;
		int y = player.y;
		if(player.isFreeCam) {
			x = player.freeX + 1;
			y = player.freeY;
		}
		if (isTileValidForPlayerToWalk(x, y)) {
			Tile t = map.getTile(x, y);
			player.movePlayer(x, y, t);
		}
	}
	
	/**
	 * X 8 9
	 * 4 5 6
	 * 1 2 3
	 */
	public void move7() {
		int x = player.x - 1;
		int y = player.y - 1;
		if(player.isFreeCam) {
			x = player.freeX - 1;
			y = player.freeY - 1;
		}
		if (isTileValidForPlayerToWalk(x, y)) {
			Tile t = map.getTile(x, y);
			player.movePlayer(x, y, t);
		}
	}

	/**
	 * 7 X 9
	 * 4 5 6
	 * 1 2 3
	 */
	public void move8() {
		int x = player.x;
		int y = player.y - 1;
		if(player.isFreeCam) {
			x = player.freeX;
			y = player.freeY - 1;
		}
		if (isTileValidForPlayerToWalk(x, y)) {
			Tile t = map.getTile(x, y);
			player.movePlayer(x, y, t);
		}
	}

	/**
	 * 7 8 X
	 * 4 5 6
	 * 1 2 3
	 */
	public void move9() {
		int x = player.x + 1;
		int y = player.y - 1;
		if(player.isFreeCam) {
			x = player.freeX + 1;
			y = player.freeY - 1;
		}
		if (isTileValidForPlayerToWalk(x, y)) {
			Tile t = map.getTile(x, y);
			player.movePlayer(x, y, t);
		}
	}
}
