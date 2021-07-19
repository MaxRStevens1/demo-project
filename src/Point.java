
public class Point {
	int x, y;

	/**
	 * Similar to (x,y) done to represent either a point in the map or a point
	 * relative to another
	 * 
	 * @param x
	 * @param y
	 */
	public Point(int x, int y) {
		this.x = x;
		this.y = y;

	}

	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	/**
	 * checks if one point is equal to another
	 * 
	 * @param p comparison point
	 * @return returns true if x = p.x and y = p.y
	 */
	public boolean equals(Point p) {
		return x == p.x && y == p.y;
	}
}
