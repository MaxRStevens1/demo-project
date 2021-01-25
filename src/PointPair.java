/**
 * Helper class for Map in order to cut down on rooms being locatied within each
 * other
 * 
 * @author Max
 *
 */

// bugged
public class PointPair {
	int x1, x2, y1, y2;

	/**
	 * PointPairs for a room
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
	
	public boolean liesWithin(PointPair p, int roomSize) {
		// first point to first point
		int FPtoFPX = x1 - p.x1;
		int FPtoFPY = y1 - p.y1;
		// second point to second point
		int SPtoSPX = x2 - p.x2;
		int SPtoSPY = y2 - p.y1;
		
		
		/*
			System.out.println("RoomSize" + roomSize);
			System.out.println(FPtoFPX + " " + FPtoFPY);
			System.out.println(SPtoSPX + " " + SPtoSPY);
			
			
			System.out.println(this.toString());
			System.out.println(p.toString());
		
		System.out.println((FPtoFPX >= 0 && FPtoFPX < roomSize && FPtoFPY >= 0 && FPtoFPY < roomSize) || 
				(SPtoSPX >= 0 && SPtoSPX < roomSize && SPtoSPY >= 0 && SPtoSPY < roomSize));
		*/
		return (FPtoFPX >= 0 && FPtoFPX <= roomSize && FPtoFPY >= 0 && FPtoFPY <= roomSize) || 
				(SPtoSPX >= 0 && SPtoSPX <= roomSize && SPtoSPY >= 0 && SPtoSPY <= roomSize);

	}
	
	public String toString() {
		return "(" + x1 + ", " + y1 + ") (" + x2 + ", " + y2 + ")";
	}
}
