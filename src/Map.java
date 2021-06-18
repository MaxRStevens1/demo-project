import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Map {
	private Tile[][] map;
	int x;
	int y;
	Player player;
	private Queue<DoorTile> playerStartingRoomDoorList; // poor solution to the doorlist 'problem'. I should only need
														// the original 4 doorTiles in player room but am not going to
														// refactor that code
	private Queue<DoorTile> doorList;
	private LinkedList<DoorTile> hallWayCreator;
	
	private Stack<UpStairTile> upStairStack;
	private Stack<DownStairTile> downStairStack;
	
	private ArrayList<PointPair> pointPairs;
	
	//private arrayList<UpStairTile> upStairList;
	
	// bool to see if map needs to be reset and regen'd
	boolean hasMapGenFailed;
	boolean isFirstFloor;
	boolean hasFinishedGen;

	/**
	 * Constuctor
	 * 
	 * @param x      maximum x length of map
	 * @param y      maximum y length of map
	 * @param player player to put in map
	 */
	public Map(int x, int y, Player player, boolean isFirstFloor, Boolean hasMapGenFailed) {
		map = new Tile[x][y];
		doorList = new LinkedList<DoorTile>();
		hallWayCreator = new LinkedList<DoorTile>();
		playerStartingRoomDoorList = new LinkedList<DoorTile>();
		pointPairs = new ArrayList<PointPair>();
		downStairStack = new Stack<DownStairTile>();
		hasFinishedGen = false;
		if(!isFirstFloor) {
			upStairStack = new Stack<UpStairTile>();
		}
		
		this.x = x;
		this.y = y;
		this.player = player;
		// checks if it is the first level
		this.isFirstFloor = isFirstFloor;
		
		//hasMapGenFailed = true;

		IntializaeMap();
		setUpMap();
		
	}

	/**
	 * Intializes map, construct base tiles for a pre populated map
	 */
	private void IntializaeMap() {
		for (int sy = 0; sy < y; sy++) {
			for (int sx = 0; sx < x; sx++) {
				setTile(sx, sy, new BuildTile(sx, sy));
			}
		}
	}

	/**
	 * replaces current map with new map
	 */
	public void setMap(Tile[][] newMap) {
		map = newMap;
		y = map.length;
		x = map[0].length;
	}

	/**
	 * sets current tile at map position x y to tile t Also copies player over, and
	 * sets player to new x y if there is a player at the input tile
	 * @param x cord
	 * @param y cord
	 * @param t new Tile to set at map x/y cord 
	 */
	public void setTile(int x, int y, Tile t) {
		map[x][y] = t;
		t.x = x;
		t.y = y;
		if (t.player != null) {
			player.x = x;
			player.y = y;
			player.tile = t;
		}
		if (t.enemy != null)
		{
			t.enemy.x = x;
			t.enemy.y = y;
			t.enemy.tile = t;
		}
	}

	/**
	 * Sets up and creates the starting floor, first it creates the intial room then
	 * calls populate level, and handles the main loop for connecting doors to each
	 * other
	 */
	public void setUpMap() {
			InputStream fstream; 
			Room room;
			if(isFirstFloor) {
				fstream = this.getClass().getResourceAsStream("MainRoom1.txt");
				room = new Room(fstream, "MainRoom", "1");
			} else {
				fstream = this.getClass().getResourceAsStream("StandardRoom1.txt");
				room = new Room(fstream, "StandardRoom1", "1");
			}
			int rXAdd = x / 2 - room.xLength / 2 - 1; // places intial room in roughly the center of the map
			int rYAdd = y / 2 - room.xLength / 2 - 1;
	
			pointPairs.add(new PointPair(rXAdd, rYAdd, rXAdd + room.xLength, +rYAdd + room.yLength));
	
			for (int startY = 0; startY < room.yLength; startY++) {
				for (int startX = 0; startX < room.xLength; startX++) {
					Tile tile = room.getTile(startX, startY);
					if (tile instanceof DoorTile) {
						// door list is used in pathfinding
						doorList.add((DoorTile) tile);
						//doorListDeBugger.add((DoorTile) tile);
						playerStartingRoomDoorList.add((DoorTile) tile);
					}
					if (tile.player != null && player == null) {
						tile.player = player;
						player.x = startX + rXAdd;
						player.y = startY + rYAdd;
						player.tile = tile;
					}
					setTile(startX + rXAdd, startY + rYAdd, tile);
				}
			}
		
		
		// populatelevel has failed to generate the correct number of up / down stairs and now the map will be remade
		int i = populateLevel();
		//System.out.println("isPopulate Level Good: " + i);
		// done to prevent an infinite loop on first floor generation
		if(i != 0 || (!isFirstFloor && upStairStack.size() != GameConstants.NUM_UPSTAIR_ROOMS) 
				|| downStairStack.size() != GameConstants.NUM_DOWNSTAIR_ROOMS) {
			hasMapGenFailed = true;
			return;
		} else {
		// loops through the hallwaycreator connecting the first and last element
		// through a* in roomconnecter, and stamping the ground to moveable tile in
		// groundstamp
			while(playerStartingRoomDoorList.size() > 0) {
				DoorTile t = playerStartingRoomDoorList.poll();
				while (t.partner != null) {
					groundStampForPathHelper(roomConnector(t, t.partner));
					t = t.partner;
				}
			} 
		}

	}

	/**
	 * decides where to put new rooms in levels, which is decided on a base offset
	 * of RANDOM_X_DISTANCE and an addition random number between 0 and random base
	 * offset 
	 * 
	 * @return returns the added value of numUpstairs and numDownStairs, with an error condition if the added values are NOT 0
	 */
	public int populateLevel() {
		int i = 0; // ITERATIONS of while loop below, done to prevent an infinite loop
		// number of up and down stairs to place in the level
		int numUpstairsToPlace = GameConstants.NUM_UPSTAIR_ROOMS;
		// done as the first floor has no up to move too
		if (isFirstFloor) {
			numUpstairsToPlace = 0;
		}
		int numDownstairsToPlace = GameConstants.NUM_DOWNSTAIR_ROOMS;
		
		while (doorList.size() != 0) {
			Room room = roomSelector();
			// if room is an stairroomup, and stairroomdown, and it as not not first floor
			if (room.roomName.equals("StairRoomUp") || room.roomName.equals("StairRoomDown")) {
				if(numUpstairsToPlace == 0 && room.roomName.contains("StairRoomUp")) {
					while(room.roomName.equals("StairRoomUp") || room.roomName.equals("StairRoomDown")) {
						room = roomSelector();
					}
					//downstair sections
				} else if(numDownstairsToPlace == 0 && room.roomName.contains("StairRoomDown")) {
					while(room.roomName.equals("StairRoomDown") || room.roomName.equals("StairRoomUp") ) {
						room = roomSelector();
					}
				}
				
				if(room.roomName.contains("StairRoomUp")) {
					numUpstairsToPlace--;
					//System.out.println("STAIRROOMSUP: " + numUpstairsToPlace);
				}
				else if(room.roomName.contains("StairRoomDown")) {
					numDownstairsToPlace--;
					//System.out.println("STAIRROOMSDOWN: " + numDownstairsToPlace);
				}
			}
			
			DoorTile door = (DoorTile) doorList.poll();
			int signVal = 0; // set to either -1 or 1
			if (Math.random() >= .5) {
				signVal = 1;
			} else {
				signVal = -1;
			}
			// bit strange math, but just takes random, multiples it by a constant, and multiplies it by 1 or -1
			// randomly decided sign is to not have rooms gradually going right / up.
			int newDoorX = (int) (door.x + ((Math.random() + 1) * GameConstants.RANDOM_X_DISTANCE) * signVal);
			
			
			if (Math.random() >= .5) {
				signVal = 1;
			} else {
				signVal = -1;
			}
			int newDoorY = (int) (door.y + ((Math.random() + 1) * GameConstants.RANDOM_Y_DISTANCE) * signVal); 
			
			
			// prospective pointVal for door
			PointPair pair = new PointPair(newDoorX, newDoorY, newDoorX + room.xLength, newDoorY + room.yLength);
			int roomPlacementIters = 0;
			while (!validRoomPlacement(pair, room.xLength)
					&& roomPlacementIters < GameConstants.NUMBER_OF_ROOM_PLACEMENT_ATTEMPTS) {
				if (Math.random() >= .5) {
					signVal = 1;
				} else {
					signVal = -1;
				}
				newDoorX = (int) (door.x + ((Math.random() + 1) * GameConstants.RANDOM_X_DISTANCE) * signVal);
				if (Math.random() >= .5) {
					signVal = 1;
				} else {
					signVal = -1;
				}
				newDoorY = (int) (door.y + ((Math.random() + 1) * GameConstants.RANDOM_Y_DISTANCE) * signVal);
				pair = new PointPair(newDoorX, newDoorY, newDoorX + room.xLength, newDoorY + room.yLength);
				roomPlacementIters++;
			}
			
			// ending check, see if valid placement failed
			if (roomPlacementIters >= GameConstants.NUMBER_OF_ROOM_PLACEMENT_ATTEMPTS) {
				pair = null;
				// check done to prevent map generation failing to generate proper number of rooms, 
				// but returning a successful result
				if(room.roomName.contains("StairRoomUp")) {
					//System.out.println("Stair end attempt: Up");
					numUpstairsToPlace++;
				} else if (room.roomName.contains("StairRoomUp")) {
					//System.out.println("IsStaironEndAttempt: Down");
					numUpstairsToPlace++;
				}
			}
			// if pair is valid, create a room centered around pointpair
			if (pair != null) {
				pointPairs.add(pair);
				// creates a new list of doors from roomStamper, which creates and "stamps" a
				// new room on the map
				if (i < 50 || (room.roomName.contains("StairRoom"))) {
					ArrayList<DoorTile> localDoorList = roomStamper(newDoorX, newDoorY, room, door);
					if (localDoorList != null) {
							doorList.add(door.partner);
							//doorListDeBugger.add(door.partner);
					}
				// set of error conditions, if numUpstairs/downstairs are not 0, it means it will fail at floor connection in dungeon
				// and map creation will be redone
				} else {
					if (numUpstairsToPlace != 0 || numDownstairsToPlace != 0)
						return -1;
					return 0;
				}
			} else {
				if (numUpstairsToPlace != 0 || numDownstairsToPlace != 0)
					return -1;
				return 0;
			}
			i++;
		}
		if (numUpstairsToPlace != 0 || numDownstairsToPlace != 0)
			return -1;
		return 0;
	}

	/**
	 * Helper method for checking if a point lies within another room
	 * 
	 * @param p        potetial new rooms point to be checked against all others
	 * @param roomSize size of point p's room
	 * @return t if point p is not within another room f if it is
	 */
	public boolean validRoomPlacement(PointPair p, int roomSize) {
		if (p.x2 >= x - 1 || p.y2 >= y - 1 
				|| p.x1 <= 1 || p.y1 <= 1) {
			return false;
		}
		for (int i = 0; i < pointPairs.size(); i++) {
			if (p.liesWithin(pointPairs.get(i), roomSize)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * starts creating a room in the map, on midX - room.xLength / 2 - 1, and
	 * overwrites the tile at that position to the corresponding tile in the room
	 * 
	 * @param midX       rough midpoint x cord of room 
	 * @param midY       rough midpoint y cord
	 * @param room       set of tiles that compose a room
	 * @param parentDoor not to be confused with parent of a tile used in
	 *                   pathfinding, this is done to make sure level stays
	 *                   continous and not disjointed
	 * @return
	 */
	public ArrayList<DoorTile> roomStamper(int midX, int midY, Room room, DoorTile partnerDoor) {
		int rXAdd = midX ;//- room.xLength / 2 - 1;
		int rYAdd = midY ;//- room.xLength / 2 - 1;
		if (rXAdd >= x || rXAdd < 0 || rYAdd >= y || rYAdd < 0) {
			return null;
		}
		
		ArrayList<DoorTile> localDoorList = new ArrayList<DoorTile>();
		for (int startY = 0; startY < room.yLength; startY++) {
			for (int startX = 0; startX < room.xLength; startX++) {
				Tile roomTile = room.getTile(startX, startY);
				Tile mapTile = getTile(startX + rXAdd, startY + rYAdd);
				if (mapTile != null && !mapTile.canWalk) {
					setTile(startX + rXAdd, startY + rYAdd, roomTile);
					// creates monster if flag is set
					if (roomTile.monsterSpawnFlag)
						roomTile.enemy = new Enemy (roomTile.x, roomTile.y);
					
					if (roomTile instanceof DoorTile) {
						partnerDoor.partner = (DoorTile) roomTile;
						localDoorList.add((DoorTile) roomTile);
						hallWayCreator.add((DoorTile) roomTile);
					} else if (roomTile instanceof UpStairTile) {
						upStairStack.add((UpStairTile) roomTile);
						//System.out.println("UpStairPlaced: " + upStairStack.size());
					} else if (roomTile instanceof DownStairTile) {
						downStairStack.add((DownStairTile) roomTile);
						//System.out.println("DownStairPlaced: " + downStairStack.size());

					}
				}
			}
		}
		// gets a partially random door from the local door list.
		if (localDoorList.size() > 0) {
			partnerDoor.partner = localDoorList.get(midX % localDoorList.size());
		}
		return localDoorList;
	}

	/**
	 * Simplish a* pathfinding. creates a chain of parents in tiles from the
	 * starting point, and ending point Uses a basic calculation of g and h visa vi
	 * this calculation Math.abs(current.x - end.x) + Math.abs(current.y - end.y);
	 * in movementDistance
	 * 
	 * 
	 * @param startDoor start point for pathfinding
	 * @param endDoor   endpoint for pathfinding
	 */
	public Stack<Tile> roomConnector(Tile startDoor, Tile endDoor) {
		ArrayList<Tile> openTiles = new ArrayList<Tile>();
		ArrayList<Tile> closedTiles = new ArrayList<Tile>();
		openTiles.add(startDoor);
		while (openTiles.size() > 0) {
			Tile openTile = openTiles.get(0);
			int openTileIndex = 0;
			int currentOpenFVal = movementDistance(openTile, startDoor) + movementDistance(endDoor, openTile);
			for (int i = 0; i < openTiles.size(); i++) {
				// g = point slope calc for startDoor with current tile, y2-y1 / x2-x1
				// h is the same
				// this is the current openTile value
				int tempOpenFVal = movementDistance(openTiles.get(i), startDoor)
						+ movementDistance(endDoor, openTiles.get(i));
				// current lowest ftile value
				if (tempOpenFVal < currentOpenFVal) {
					openTile = openTiles.get(i);
					openTileIndex = i;
				}
			}
			// adds current tile to the closed list for comparisons, 
			// and removes it from the open list
			closedTiles.add(openTile);
			openTiles.remove(openTileIndex);
			
			// exit condition
			if (openTile == endDoor) {
				return roomPathMakerHelper(endDoor);
			}
			
			// gets a single tile rook movement of tiles around itself
			Tile[] adjacentTiles = childrenRoomConnectorHelper(openTile);
			for (int i = 0; i < adjacentTiles.length; i++) {
				Tile adjacentTile = adjacentTiles[i];
				if (adjacentTile != null && !closedTiles.contains(adjacentTile)
						&& (adjacentTile instanceof BuildTile || adjacentTile instanceof GroundTile)) {
					int newCostToAdjacentFVal = movementDistance(adjacentTile, startDoor)
							+ movementDistance(endDoor, adjacentTile);
					if (newCostToAdjacentFVal <= currentOpenFVal && !openTiles.contains(adjacentTile)) {
						adjacentTile.parent = openTile;
						openTiles.add(adjacentTile);
					}
					// exit condition
				} else if (adjacentTile == endDoor) {
					adjacentTile.parent = openTile;
					openTiles.add(adjacentTile);
					return roomPathMakerHelper(endDoor); // creates an stack of tiles to easily parse through
				}

			}
		}
		// failure condition
		return null;

	}

	/**
	 * Creates a path of tiles from the initial tiles parent, till there is no
	 * parent.
	 * 
	 * @param t starting point for making path
	 * @return
	 */
	private Stack<Tile> roomPathMakerHelper(Tile t) {
		Stack<Tile> path = new Stack<Tile>();
		Tile currentTile = t;
		// for nulling parent reference at currentTile
		Tile tempTile = t;
		while (currentTile.parent != null) {
			path.add(currentTile.parent);
			currentTile = currentTile.parent;
			tempTile.parent = null;
			tempTile = currentTile;
		}
		return path;
	}

	/**
	 * stamps unpassable tiles across inputed path to ground
	 * 
	 * @param path a stack of tiles leading from startpoint to endpoint
	 */
	private void groundStampForPathHelper(Stack<Tile> path) {
		if (path != null) {
			while (path.size() > 0) {
				Tile t = path.pop();
				if (t instanceof BuildTile) {
					setTile(t.x, t.y, new GroundTile(t.x, t.y, t.player));
				}
			}
		}
	}

	/**
	 * OOB error prevention by making sure array accessing stay in bounds Note that
	 * only N S E W are considered neighbors, not "all" 8 directions. It makes for
	 * cleaner paths
	 * 
	 * @param t tile to get neighbors
	 * @return filled array
	 */
	private Tile[] childrenRoomConnectorHelper(Tile t) {
		Tile[] children = new Tile[4];

		if (t.x + 1 != x) {
			children[0] = map[t.x + 1][t.y];
		}
		if (t.y + 1 != y) {
			children[1] = map[t.x][t.y + 1];
		}
		if (t.x != 0) {
			children[2] = map[t.x - 1][t.y];
		}
		if (t.y != 0) {
			children[3] = map[t.x][t.y - 1];
		}
		return children;
	}

	/**
	 * testing method that prints out x/y cords of all tiles
	 */
	private void mapXYVis() {
		for (int i = 0; i < map.length; i++) {
			for (int x = 0; x < map[i].length; x++) {
				//System.out.print("X: " + map[x][i].x + " Y: " + map[x][i].y + ", ");
				if (map[x][i].x != x || map[x][i].y != i) {
					System.exit(0);
				}
			}
			//System.out.println();
		}
	}

	// helps a* calculate movement
	private int movementDistance(Tile current, Tile end) {
		return Math.abs(current.x - end.x) + Math.abs(current.y - end.y);
	}

	/**
	 * selects room randomly from available rooms using string concat for file name
	 * 
	 * @return the room selected
	 */
	public Room roomSelector() {
		int roomSelectedIndex = (int) (Math.random() * GameConstants.ROOM_NAMES.length);
		String roomName = GameConstants.ROOM_NAMES[roomSelectedIndex][0]; // the first position of the room_name array is always the name
		String roomNumber = GameConstants.ROOM_NAMES[roomSelectedIndex][1]; // the second position is always the #
		// randomizes the number from 1 to itself
		int nummberRandomized =  (int) Math.ceil(Math.random() * Integer.parseInt(roomNumber));
		InputStream fstream = this.getClass().getResourceAsStream(roomName + nummberRandomized + ".txt");
		return new Room(fstream, roomName, roomNumber);
	}
	
	
	/**
	 * gets tile at pos x y
	 * 
	 * @param x pos
	 * @param y pos
	 * @return tile at x y pos
	 */
	public Tile getTile(int x, int y) {
		if (x + 1 >= this.x || y + 1 >= this.y || x <= -1 || y <= -1) {
			return null;
		}
		return map[x][y];
	}

	/**
	 * gets x aka x length of map
	 * 
	 * @return x
	 */
	public int getX() {
		return x;
	}

	/**
	 * gets y pos aka y length of map
	 * 
	 * @return y
	 */
	public int getY() {
		return y;
	}

	/**
	 * sets player of amp
	 * 
	 * @param player the new player
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}
	/**
	 * 
	 * @return upstairstack
	 */
	public Stack<UpStairTile> getUpStairStack() {
		return upStairStack;
	}
	/**
	 * 
	 * @return downstairstack
	 */
	public Stack<DownStairTile> getDownStairStack() {
		return downStairStack;
	}
}
