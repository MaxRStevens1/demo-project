import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
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
	public LinkedList<DoorTile> hallWayCreator;

	private ArrayList<PointPair> pointPairs = new ArrayList<PointPair>();

	boolean isFirstFloor;

	/**
	 * Constuctor
	 * 
	 * @param x      maximum x length of map
	 * @param y      maximum y length of map
	 * @param player player to put in map
	 */
	public Map(int x, int y, Player player, boolean isFirstFloor) {
		map = new Tile[x][y];
		doorList = new LinkedList<DoorTile>();
		hallWayCreator = new LinkedList<DoorTile>();
		playerStartingRoomDoorList = new LinkedList<DoorTile>();
		this.x = x;
		this.y = y;
		this.player = player;
		// checks if it is the first level
		this.isFirstFloor = isFirstFloor;

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
	}

	/**
	 * Sets up and creams the starting floor, first it creates the intial room then
	 * calls populate level
	 */
	public void setUpMap() {
		InputStream fstream = this.getClass().getResourceAsStream("MainRoom1.txt");
		Room room = new Room(fstream);
		int rXAdd = x / 2 - room.xLength / 2 - 1;
		int rYAdd = y / 2 - room.xLength / 2 - 1;
		
		pointPairs.add(new PointPair(rXAdd, rYAdd, rXAdd + room.xLength, +rYAdd + room.yLength));
		

		for (int startY = 0; startY < room.yLength; startY++) {
			for (int startX = 0; startX < room.xLength; startX++) {
				Tile tile = room.getTile(startX, startY);
				if (tile instanceof DoorTile) {
					// door list is used in pathfinding
					doorList.add((DoorTile) tile);
					playerStartingRoomDoorList.add((DoorTile) tile);
				}
				if (tile.player != null) {
					tile.player = player;
					player.x = startX + rXAdd;
					player.y = startY + rYAdd;
					player.tile = tile;
				}
				setTile(startX + rXAdd, startY + rYAdd, tile);
			}
		}
		populateLevel();
		// necessary as you are popping and polling
		int hallWayCreatorSize = playerStartingRoomDoorList.size();
		// iterates through the hallwaycreator connecting the first and last element
		// through a* in roomconnecter, and stamping the ground to moveable tile in
		// groundstamp
		for (int i = 0; i < hallWayCreatorSize; i++) {
			DoorTile t = playerStartingRoomDoorList.poll();
			while (t.partner != null) {
				groundStampForPathHelper(roomConnector(t, t.partner));
				t = t.partner;
			}
		}

	}

	/**
	 * decides where to put new rooms in levels, which is decided on a base offset
	 * of RANDOM_X_DISTANCE and an addition random number between 0 and random base
	 * offset
	 */
	public void populateLevel() {
		int i = 0;
		while (doorList.size() != 0 && i < 10) {
			Room room = roomSelector();
			DoorTile door = (DoorTile) doorList.poll();
			int signVal = Math.round((long) Math.random()); // needs to be either 0 or 2
			if (Math.random() >= .5) {
				signVal = 1;
			} else {
				signVal = -1;
			}
			int newDoorX = (int) (door.x + ((Math.random() + 1) * GameConstants.RANDOM_X_DISTANCE) * signVal);
			if (Math.random() >= .5) {
				signVal = 1;
			} else {
				signVal = -1;
			}
			int newDoorY = (int) (door.y + ((Math.random() + 1) * GameConstants.RANDOM_Y_DISTANCE) * signVal);
			// prospective pointVal for door
			PointPair pair = new PointPair(newDoorX, newDoorY, newDoorX + room.xLength, +newDoorY + room.yLength);
			while (!validRoomPlacement(pair, room.xLength)) {
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
				pair = new PointPair(newDoorX, newDoorY, newDoorX + room.xLength, +newDoorY + room.yLength);
			}
			pointPairs.add(pair);

			ArrayList<DoorTile> localDoorList = roomStamper(newDoorX, newDoorY, room, door);
			if (localDoorList != null) {
				for (int x = 0; x < localDoorList.size(); x++) {
					doorList.add(localDoorList.get(x));
				}
			}
			i++;
		}

	}

	public boolean validRoomPlacement(PointPair p, int roomSize) {
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
	 * @param midX       rough midpoint of room
	 * @param midY       rough midpoint
	 * @param room       set of tiles that compose a room
	 * @param parentDoor not to be confused with parent of a tile used in
	 *                   pathfinding, this is done to make sure level stays
	 *                   continous and not disjointed
	 * @return
	 */
	public ArrayList<DoorTile> roomStamper(int midX, int midY, Room room, DoorTile partnertDoor) {
		int rXAdd = midX - room.xLength / 2 - 1;
		int rYAdd = midY - room.xLength / 2 - 1;
		if (rXAdd >= x || rXAdd < 0 || rYAdd >= y || rYAdd < 0) {
			return null;
		}
		ArrayList<DoorTile> localDoorList = new ArrayList<DoorTile>();
		for (int startY = 0; startY < room.yLength; startY++) {
			for (int startX = 0; startX < room.xLength; startX++) {
				Tile roomTile = room.getTile(startX, startY);
				Tile mapTile = getTile(startX + rXAdd, startY + rYAdd);
				if (mapTile != null && !mapTile.canWalk) {
					if (roomTile instanceof DoorTile) {
						partnertDoor.partner = (DoorTile) roomTile;
						localDoorList.add((DoorTile) roomTile);
						hallWayCreator.add((DoorTile) roomTile);
					}
					setTile(startX + rXAdd, startY + rYAdd, roomTile);
				}
			}
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
			closedTiles.add(openTile);
			openTiles.remove(openTileIndex);

			if (openTile == endDoor) {
				return roomPathMakerHelper(endDoor);
			}
			Tile[] adjacentTiles = childrenRoomConnectorHelper(openTile);
			for (int i = 0; i < adjacentTiles.length; i++) {
				Tile adjacentTile = adjacentTiles[i];
			if (adjacentTile != null && !closedTiles.contains(adjacentTile) && !(adjacentTile instanceof DoorTile)
				) {
					// if (!adjacentTile.canWalk || adjacentTile == endDoor) {
					int newCostToAdjacentFVal = movementDistance(adjacentTile, startDoor)
							+ movementDistance(endDoor, adjacentTile);
					if (newCostToAdjacentFVal <= currentOpenFVal && !openTiles.contains(adjacentTile)) {
						adjacentTile.parent = openTile;
						openTiles.add(adjacentTile);
					}
					// }
				} else if(adjacentTile == endDoor) {
					adjacentTile.parent = openTile;
					openTiles.add(adjacentTile);
					return roomPathMakerHelper(endDoor);
				}

			}
		}
		/*
		 * for(int i = 0; i < openTiles.size(); i++) { System.out.println("X "
		 * +openTiles.get(i).x +" Y " + openTiles.get(i).y); }
		 */
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
				if (!t.canWalk) {
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
		// mapXYVis();
		// System.out.println("X: " + t.x + " Y:" + t.y);

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
				System.out.print("X: " + map[x][i].x + " Y: " + map[x][i].y + ", ");
				if (map[x][i].x != x || map[x][i].y != i) {
					System.exit(0);
				}
			}
			System.out.println();
		}
	}

	// helps a* calculate movement
	private int movementDistance(Tile current, Tile end) {
		return Math.abs(current.x - end.x) + Math.abs(current.y - end.y);
	}

	/**
	 * selects room randomly from aviable rooms using string concat for file name
	 * 
	 * @return the room seelcted
	 */
	public Room roomSelector() {
		int roomSelectedIndex = (int) (Math.random() * GameConstants.ROOM_NAMES.length);
		String roomName = GameConstants.ROOM_NAMES[roomSelectedIndex][0];
		String roomNumber = GameConstants.ROOM_NAMES[roomSelectedIndex][1];

		// System.out.println(new File("rooms/" + roomName + roomNumber +
		// ".txt").getAbsolutePath());
		InputStream fstream = this.getClass().getResourceAsStream(roomName + roomNumber + ".txt");
		return new Room(fstream);
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
}
