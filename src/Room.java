import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class Room {
	ArrayList<ArrayList<Tile>> room;
	int xLength;
	int yLength;
	// name and number of selected room string
	String roomName;
	String roomNumber;

	/**
	 * room constructor
	 * 
	 * @param fstream    file to make into room
	 * @param roomName   name of the room
	 * @param roomNumber number of the room
	 */
	public Room(InputStream fstream, String roomName, String roomNumber) {
		room = new ArrayList<ArrayList<Tile>>();
		Scanner scan = new Scanner(fstream);
		xLength = 0;
		yLength = 0;

		this.roomName = roomName;
		this.roomNumber = roomNumber;
		constructRoom(scan);

	}

	/**
	 * builds room from text file, x = groundTile W = wallTile D = doorTile p =
	 * player in a groundTile also, if there is a ground tile, 1/8 chance of setting
	 * a MonsterSpawn flag
	 * 
	 * @param scan
	 */
	public void constructRoom(Scanner scan) {
		while (scan.hasNextLine()) {
			String row = scan.nextLine();
			room.add(new ArrayList<Tile>());
			for (int i = 0; i < row.length(); i++) {
				if (row.substring(i, i + 1).equals("x")) {
					GroundTile t = new GroundTile(-1, -1);
					room.get(room.size() - 1).add(t);
					// sets flag to true at 1/8 chance
					t.setMonsterFlag(Math.random() <= GameConstants.MONSTER_SPAWN_CHANCE);
				} else if (row.substring(i, i + 1).equals("W")) {
					room.get(room.size() - 1).add(new WallTile(-1, -1));
				} else if (row.substring(i, i + 1).equals("D")) {
					room.get(room.size() - 1).add(new DoorTile(-1, -1));
				} else if (row.substring(i, i + 1).equals("P")) {
					room.get(room.size() - 1).add(new GroundTile(-1, -1, new Player()));
				} else if (row.substring(i, i + 1).equals("d")) { // d for downstair
					room.get(room.size() - 1).add(new DownStairTile(-1, -1));
				} else if (row.substring(i, i + 1).equals("u")) { // u for upstair
					room.get(room.size() - 1).add(new UpStairTile(-1, -1));
				} else {
					System.out.println("?? symbol not found in room text file");
				}
			}
		}
		yLength = room.size();
		xLength = room.get(0).size();
	}

	// getters and setters
	public Tile getTile(int x, int y) {
		return room.get(y).get(x);
	}

}
