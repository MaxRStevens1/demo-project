import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class Room {
	ArrayList<ArrayList<Tile>> room;
	int x;
	int y;

	/**
	 * room constructor
	 * @param fstream file to make into room
	 */
	public Room(InputStream fstream) {
		room = new ArrayList<ArrayList<Tile>>();
		Scanner scan;
		x = 0;
		y = 0;
		scan = new Scanner(fstream);
		constructRoom(scan);
	}

	/**
	 * builds room from text file,
	 * x = groundTile
	 * W = wallTile
	 * D = doorTile
	 * p = player in a groundTile
	 * @param scan
	 */
	public void constructRoom(Scanner scan) {
		while (scan.hasNextLine()) {
			String row = scan.nextLine();
			room.add(new ArrayList<Tile>());
			for (int i = 0; i < row.length(); i++) {
				if (row.substring(i, i + 1).equals("x")) {
					room.get(room.size() - 1).add(new GroundTile(-1, -1));
				} else if (row.substring(i, i + 1).equals("W")) {
					room.get(room.size() - 1).add(new WallTile(-1, -1));
				} else if (row.substring(i, i + 1).equals("D")) {
					room.get(room.size() - 1).add(new DoorTile(-1, -1));
				} else if (row.substring(i, i + 1).equals("P")) {
					room.get(room.size() - 1).add(new GroundTile(-1, -1, new Player()));
				} else {
					System.out.println("?? symbol not found in room text file");
				}
			}
		}
		y = room.size();
		x = room.get(0).size();
	}

	public Tile getTile(int x, int y) {
		return room.get(y).get(x);
	}

}
