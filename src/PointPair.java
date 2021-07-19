/**
 * Helper class for Map in order to cut down on rooms being locatied within each
 * other
 * 
 * @author Max
 *
 */

public class PointPair {
	int x1, x2, y1, y2;

	/**
	 * PointPairs for a room, similar to a point but 2 sets of cordinate points
	 * 
	 * @param x1 x at room 0 with offset of map X
	 * @param y1 y at room with offset of mapy
	 * @param x2 x at room.size with offset of map x
	 * @param y2 y at room.size with offset of map y
	 */
	public PointPair(int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	public boolean equals(PointPair p) {
		return (p.x1 == x1 && p.y1 == y1 && p.x2 == x2 && p.y2 == y2);
	}

	/**
	 * 
	 * pretty much just AABB style collision checking.
	 * 
	 * @param p        other point
	 * @param roomSize size of the rooms
	 * @return returns true if this PointPair lies within PointPair P
	 */
	public boolean liesWithin(PointPair p, int roomSize) {
		return this.x1 <= p.x2 + 1 && this.x2 >= p.x1 - 1 && this.y1 <= p.y2 + 1 && this.y2 >= p.y1 - 1;
	}

	public String toString() {
		return "(" + x1 + ", " + y1 + ") (" + x2 + ", " + y2 + ")";
	}
}
