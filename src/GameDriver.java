import java.util.ArrayList;

public class GameDriver {
	private Map map;
	private Player player;
	//private ArrayList entityTracker;
	private GraphicsDriver graph;
	private Dungeon dungeon;
	private boolean playerIsAlive;
	ArrayList<ArrayList<Point>> pathsToParameter;
	
	boolean controlsLocked; // done for when executing the longer commands, 
	// 						   to prevent the player from moving away from the correct pos

	/**
	 * constructor
	 */
	public GameDriver() {
		player = new Player();
		pathsToParameter = playerVisionCalculation();
		dungeon = new Dungeon(GameConstants.X_MAP_SIZE, GameConstants.Y_MAP_SIZE, player);
		map = dungeon.getMap();
		controlsLocked = false;
		playerIsAlive = true;
		updatePlayerVision();
		//falsePlayer;
	}

	/**
	 * basic map initialization, read map to for more detailed explanation
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
	private boolean isTileValidForPlayerToWalk (int x, int y) {
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
		Tile prospectiveTile = map.getTile(x, y);
		if (prospectiveTile.enemy != null && !player.isFreeCam)
			playerIsAlive = enemyInProspectTile (prospectiveTile.enemy, prospectiveTile);
		boolean isTileValidForPlayerToWalk = isTileValidForPlayerToWalk(x,y);

		if (isTileValidForPlayerToWalk && prospectiveTile.enemy == null) {
			Tile removePlayerTile = map.getTile(player.x, player.y);
			removePlayerTile.player = null;
			// look at player for more info, takes x and y and tile to move player
			player.movePlayer(x, y, prospectiveTile);
			
		}
		updatePlayerVision();
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
		
		Tile prospectiveTile = map.getTile(x, y);
		if (prospectiveTile.enemy != null && !player.isFreeCam)
			playerIsAlive = enemyInProspectTile (prospectiveTile.enemy, prospectiveTile);
		boolean isTileValidForPlayerToWalk = isTileValidForPlayerToWalk(x,y);
		if (isTileValidForPlayerToWalk && prospectiveTile.enemy == null) {
			Tile removePlayerTile = map.getTile(player.x, player.y);
			removePlayerTile.player = null;
			player.movePlayer(x, y, prospectiveTile);
			
		}
		updatePlayerVision();
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
		Tile prospectiveTile = map.getTile(x, y);
		if (prospectiveTile.enemy != null && !player.isFreeCam)
			playerIsAlive = enemyInProspectTile (prospectiveTile.enemy, prospectiveTile);
		boolean isTileValidForPlayerToWalk = isTileValidForPlayerToWalk(x,y);

		if (isTileValidForPlayerToWalk && prospectiveTile.enemy == null) {
			Tile removePlayerTile = map.getTile(player.x, player.y);
			removePlayerTile.player = null;
			player.movePlayer(x, y, prospectiveTile);
		}
		updatePlayerVision();
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
		
		Tile prospectiveTile = map.getTile(x, y);
				
		if (prospectiveTile.enemy != null && !player.isFreeCam) {
			playerIsAlive = enemyInProspectTile (prospectiveTile.enemy, prospectiveTile);
		}
		boolean isTileValidForPlayerToWalk = isTileValidForPlayerToWalk(x,y);
		// case player can move to prospective tile AND there is NO enemy
		if (isTileValidForPlayerToWalk && prospectiveTile.enemy == null) {
			Tile removePlayerTile = map.getTile(player.x, player.y);
			removePlayerTile.player = null;
			player.movePlayer(x, y, prospectiveTile);
		}
		updatePlayerVision();
	}

	/**
	 * 6 7 8
	 * 4 X 6
	 * 1 2 3
	 * 
	 * Waits a turn, not implemented yet
	 */
	private void move5() {
		updatePlayerVision();
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
		Tile prospectiveTile = map.getTile(x, y);
		if (prospectiveTile.enemy != null && !player.isFreeCam)
			playerIsAlive = enemyInProspectTile (prospectiveTile.enemy, prospectiveTile);
		boolean isTileValidForPlayerToWalk = isTileValidForPlayerToWalk(x,y);
		if (isTileValidForPlayerToWalk && prospectiveTile.enemy == null) {
			Tile removePlayerTile = map.getTile(player.x, player.y);
			removePlayerTile.player = null;
			player.movePlayer(x, y, prospectiveTile);
		}
		updatePlayerVision();
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
		Tile prospectiveTile = map.getTile(x, y);
		if (prospectiveTile.enemy != null && !player.isFreeCam)
			playerIsAlive = enemyInProspectTile (prospectiveTile.enemy, prospectiveTile);
		boolean isTileValidForPlayerToWalk = isTileValidForPlayerToWalk(x,y);

		if (isTileValidForPlayerToWalk && prospectiveTile.enemy == null) {
			Tile removePlayerTile = map.getTile(player.x, player.y);
			removePlayerTile.player = null;
			player.movePlayer(x, y, prospectiveTile);
		}
		updatePlayerVision();
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
		Tile prospectiveTile = map.getTile(x, y);
		if (prospectiveTile.enemy != null && !player.isFreeCam)
			playerIsAlive = enemyInProspectTile (prospectiveTile.enemy, prospectiveTile);
		boolean isTileValidForPlayerToWalk = isTileValidForPlayerToWalk(x,y);

		if (isTileValidForPlayerToWalk && prospectiveTile.enemy == null) {
			Tile removePlayerTile = map.getTile(player.x, player.y);
			removePlayerTile.player = null;
			player.movePlayer(x, y, prospectiveTile);
		}
		updatePlayerVision();
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
		Tile prospectiveTile = map.getTile(x, y);
		if (prospectiveTile.enemy != null && !player.isFreeCam)
			playerIsAlive = enemyInProspectTile (prospectiveTile.enemy, prospectiveTile);
		boolean isTileValidForPlayerToWalk = isTileValidForPlayerToWalk(x,y);

		if (isTileValidForPlayerToWalk && prospectiveTile.enemy == null) {
			Tile removePlayerTile = map.getTile(player.x, player.y);
			removePlayerTile.player = null;
			player.movePlayer(x, y, prospectiveTile);
		}
		updatePlayerVision();
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
		updatePlayerVision();
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
		updatePlayerVision();
	}

	/**
	 * 
	 * @return
	 */
	public boolean enemyInProspectTile (Enemy enemy, Tile prospectTile) {
		int enemyDamageRoll = enemy.DamageCalculator();
		int playerDamageRoll = player.getAttackDamage();
		 graph.setNewText(player.name + " just dealt " + playerDamageRoll + " to " + enemy.name);

		 if (enemy.recieveDamager(playerDamageRoll) <= 0) { 
			 graph.setNewText(player.name + " just deleted " + enemy.name);
			 prospectTile.enemy = null;
			 enemy = null;
		 }
		return player.recieveDamager(enemyDamageRoll) > 0;
		
	}
	
	/**
	 * Given the parameter of the players vision, in the 'constant' CIRCUMFRENCE_VIEW_POINTS, 
	 * find a lines formula using point-slope (which is always y/x) and use it to
	 * 'lines' to each point on players parameter, stopping if there is an object that blocks LOS
	 */
	public ArrayList<ArrayList<Point>> playerVisionCalculation () {
		Point[] viewParameter = calculateViewDistance();
        ArrayList<ArrayList<Point>> pathsToParam = new ArrayList<ArrayList<Point>>();

		for (int i = 0; i < viewParameter.length; i++) {
			double lineSlope = viewParameter[i].y;
			// / by 0 exception prevention
			if (viewParameter[i].x != 0)
				lineSlope /= viewParameter[i].x;
			// flags it for x = 0 case
			else 
				lineSlope = -1;
			
			// calls and adds line-path from 0,0 to viewParameter point
			pathsToParam.add(playerVisionCalcPathHelper (viewParameter[i], lineSlope));
		}
		return pathsToParam;
	}
	
	/**
	 * creates a array or 'path' of points from an assumed initial point of (0,0)
	 * this is used to create a set of lines expressed as tiles in game to
	 * roughly approximate a 360 degree field of view from the player character
	 * @param p parameter point to create path from (0,0) to
	 * @param iterValue the x / y value to iterate to
	 * @return array of point in line between p and (0,0)
	 */
	public ArrayList<Point> playerVisionCalcPathHelper (Point p,  double slope) {
		ArrayList<Point> pointsInLine = new ArrayList<Point>();
		// case to iterate using x as the endpoint and y=mx as formula
		if (Math.abs(slope) < 1 || slope == 0) {
			// multiplies signval with y, done in cases where i is negative
			int signVal = 1;
			if (p.x < 0)
				signVal = -1;
			
			for (int i = 0; i <= Math.abs(p.x); i++) {
				double y = i*slope*signVal;
				// case y is a whole #
				if (Math.floor(y) == y)
					pointsInLine.add(new Point(i * signVal,(int) y) ); 
				// case y is NOT a whole #
				else {
					// adds both floor and ceiling of # if not whole #
					pointsInLine.add(new Point(i * signVal, (int) Math.floor(y))); 
					pointsInLine.add(new Point(i * signVal, (int) Math.ceil(y))); 
				}

			}
		}
		// case to iterate using y as endpoint using x = y/m
		else 
		{
			// multiplies signval with y, done in cases where i is negative

			int signVal = 1;
			if (p.y < 0)
				signVal = -1;
			
			for (int i = 0; i <= Math.abs(p.y); i++) {
				double x = i / slope * signVal;
				// case slope is in / 0 case AND it is not actually -1
				if (slope == -1 && p.x != p.y)
					x = 0;
				
				if (Math.floor(x) == x)
					pointsInLine.add(new Point((int) x, i * signVal));
				else 
				{
					pointsInLine.add(new Point((int) Math.floor(x), i * signVal));
					pointsInLine.add(new Point((int) Math.ceil(x),  i * signVal));
				}
			}
		}
		return pointsInLine;
	}
	/**
	 * Calculates the points for the parameter of a PLAYER_VIEW_DISTANCE radius
	 * circle note, does not filter points that are = to each other
	 * 
	 * @return an array composed of the points of the circles parameter
	 */
	public static Point[] calculateViewDistance() {
		Point[] pointList = new Point[GameConstants.PLAYER_VIEW_CIRCUMFRENCE + 1];
		for (int i = 0; i <= GameConstants.PLAYER_VIEW_CIRCUMFRENCE; i++) {
			// gets the (x,y) cord of the vision circle
			// gets the 2pi * i / player_view_circ, which breaks the unit circle in
			// player_view_circumfrence chunks and multiples them by the radius to get
			// that many points on the parameter
			int x = (int) Math.round(GameConstants.PLAYER_VIEW_DISTANCE
					* Math.cos(i * 2 * Math.PI / GameConstants.PLAYER_VIEW_CIRCUMFRENCE));
			int y = (int) Math.round(GameConstants.PLAYER_VIEW_DISTANCE
					* Math.sin(i * 2 * Math.PI / GameConstants.PLAYER_VIEW_CIRCUMFRENCE));
			pointList[i] = new Point(x, y);
		}
		return pointList;
	}
	
	/**
	 * Updates the player FOV for each new update by player
	 */
	public void updatePlayerVision () {
		for (int i = 0; i < pathsToParameter.size(); i++) {
			if (pathsToParameter.get(i) == null)
				continue;
			
			for (int x = 0; x < pathsToParameter.get(i).size(); x++) {
				Point tilePoint = pathsToParameter.get(i).get(x);
				// gets tile at for current vision evaluation
				Tile tile = map.getTile(tilePoint.x + player.x, tilePoint.y + player.y);
				// prelim set visible to make sure player can see things like walls that aren't visible
				tile.isVisible = true;
				// ends this 'line' of sight early if it encouters something that blocks it
				if (!tile.isSeethrough)
					break;
			}
		}
	}
	
}
