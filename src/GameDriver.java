import java.util.ArrayList;

public class GameDriver {
	private Map map;
	private Player player;
	private ArrayList entityTracker;
	private GraphicsDriver graph;
	private Dungeon dungeon;
	
	boolean controlsLocked; // done for when executing the longer commands, 
	// 						   to prevent the player from moving away from the correct pos

	/**
	 * constructor
	 */
	public GameDriver() {
		player = new Player();
		dungeon = new Dungeon(GameConstants.X_MAP_SIZE, GameConstants.Y_MAP_SIZE, player);
		map = dungeon.getMap();
		controlsLocked = false;
		//falsePlayer;
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
		case 10: // > than symbol, means move up a floor
			move10();
			break;
		case 11: // < than symbol, means move down a floor
			move11();
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
		if(x >= 0 && x < map.x && y >= 0 && y < map.y && !controlsLocked) {
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
	private void move1() {
		if (controlsLocked)
			return;
		int x = player.x - 1;
		int y = player.y + 1;
		if(player.isFreeCam) {
			x = player.freeX - 1;
			y = player.freeY + 1;
		}
		if (isTileValidForPlayerToWalk(x, y)) {
			Tile removePlayerTile = map.getTile(player.x, player.y);
			removePlayerTile.player = null;
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
	private void move2() {
		if (controlsLocked)
			return;
		int x = player.x;
		int y = player.y + 1;
		if(player.isFreeCam) {
			x = player.freeX;
			y = player.freeY + 1;
		}
		if (isTileValidForPlayerToWalk(x, y)) {
			Tile removePlayerTile = map.getTile(player.x, player.y);
			removePlayerTile.player = null;
			Tile t = map.getTile(x, y);
			player.movePlayer(x, y, t);
		}
	}

	/**
	 * 7 8 9
	 * 4 5 6
	 * 1 2 X
	 */
	private void move3() {
		if (controlsLocked)
			return;
		int x = player.x + 1;
		int y = player.y + 1;
		if(player.isFreeCam) {
			x = player.freeX + 1;
			y = player.freeY + 1;
		}
		if (isTileValidForPlayerToWalk(x, y)) {
			Tile removePlayerTile = map.getTile(player.x, player.y);
			removePlayerTile.player = null;
			Tile t = map.getTile(x, y);
			player.movePlayer(x, y, t);
		}
	}

	/**
	 * 7 8 9
	 * X 5 6
	 * 1 2 3
	 */
	private void move4() {
		if (controlsLocked)
			return;
		int x = player.x - 1;
		int y = player.y;
		if(player.isFreeCam) {
			x = player.freeX - 1;
			y = player.freeY;
		}
		if(isTileValidForPlayerToWalk(x,y)) {
			Tile removePlayerTile = map.getTile(player.x, player.y);
			removePlayerTile.player = null;
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
	private void move5() {
	}
	
	/**
	 * 7 8 9
	 * 4 5 X
	 * 1 2 3
	 */
	private void move6() {
		if (controlsLocked)
			return;
		int x = player.x + 1;
		int y = player.y;
		if(player.isFreeCam) {
			x = player.freeX + 1;
			y = player.freeY;
		}
		if (isTileValidForPlayerToWalk(x, y)) {
			Tile removePlayerTile = map.getTile(player.x, player.y);
			removePlayerTile.player = null;
			Tile t = map.getTile(x, y);
			player.movePlayer(x, y, t);
		}
	}
	
	/**
	 * X 8 9
	 * 4 5 6
	 * 1 2 3
	 */
	private void move7() {
		if (controlsLocked)
			return;
		int x = player.x - 1;
		int y = player.y - 1;
		if(player.isFreeCam) {
			x = player.freeX - 1;
			y = player.freeY - 1;
		}
		if (isTileValidForPlayerToWalk(x, y)) {
			Tile removePlayerTile = map.getTile(player.x, player.y);
			removePlayerTile.player = null;
			Tile t = map.getTile(x, y);
			player.movePlayer(x, y, t);
		}
	}

	/**
	 * 7 X 9
	 * 4 5 6
	 * 1 2 3
	 */
	private void move8() {
		if (controlsLocked)
			return;
		int x = player.x;
		int y = player.y - 1;
		if(player.isFreeCam) {
			x = player.freeX;
			y = player.freeY - 1;
		}
		if (isTileValidForPlayerToWalk(x, y)) {
			Tile removePlayerTile = map.getTile(player.x, player.y);
			removePlayerTile.player = null;
			Tile t = map.getTile(x, y);
			player.movePlayer(x, y, t);
		}
	}

	/**
	 * 7 8 X
	 * 4 5 6
	 * 1 2 3
	 */
	private void move9() {
		if (controlsLocked)
			return;
		int x = player.x + 1;
		int y = player.y - 1;
		if(player.isFreeCam) {
			x = player.freeX + 1;
			y = player.freeY - 1;
		}
		if (isTileValidForPlayerToWalk(x, y)) {
			Tile removePlayerTile = map.getTile(player.x, player.y);
			removePlayerTile.player = null;
			Tile t = map.getTile(x, y);
			player.movePlayer(x, y, t);
		}
	}
	
	/**
	 * Moves the player up a floor
	 */
	private void move10() {
		if (controlsLocked)
			return;
		int x = player.x;
		int y = player.y;
		if (!player.isFreeCam) {
			if (map.getTile(x, y) instanceof UpStairTile) {
				controlsLocked = true;
				dungeon.movePlayerUp();
				graph.setMap(dungeon.getMap());
				map = dungeon.getMap();
				controlsLocked = false;
				
			}
		}

	}

	/**
	 * moves a player down a floor
	 */
	private void move11() {
		if (controlsLocked)
			return;
		int x = player.x;
		int y = player.y;
		if (!player.isFreeCam) {
			if (map.getTile(x, y) instanceof DownStairTile) {
				controlsLocked = true;
				dungeon.movePlayerDown();
				graph.setMap(dungeon.getMap());
				map = dungeon.getMap();
				controlsLocked = false;
			}
		}
	}
	
}
