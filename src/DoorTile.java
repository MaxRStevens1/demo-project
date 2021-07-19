import javax.swing.ImageIcon;

public class DoorTile extends GroundTile {
	boolean hasClosed;
	// Doors partner is what is used for pathfinding. Doors daisy chain with
	// partners in order to try and have continous borders.
	DoorTile partner;

	/**
	 * Used as a focus point for connecting rooms
	 * 
	 * @param x in map
	 * @param y in map
	 */
	public DoorTile(int x, int y) {
		super(x, y);
		image = new ImageIcon(this.getClass().getResource("TileDoorOpened.png")).getImage();
		hasClosed = false;
	}

	/**
	 * sets door to be a closed door
	 */
	public void closeDoor() {
		image = new ImageIcon(this.getClass().getResource("TileDoorClosed.png")).getImage();
		hasClosed = true;
	}

}
