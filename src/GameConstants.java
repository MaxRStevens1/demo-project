public class GameConstants {
	public static int RANDOM_X_DISTANCE = 16;
	public static int RANDOM_Y_DISTANCE = 16;

	public static final int GREATER_THAN_NUM = 46;
	public static final int LESS_THAN_NUM = 44;
	// array of types of rooms, and their
	public static String[][] ROOM_NAMES = { { "StandardRoom", "5" }, { "StairRoomDown", "3" }, { "StairRoomUp", "1" } };

	public static int NUM_UPSTAIR_ROOMS = 2;
	public static int NUM_DOWNSTAIR_ROOMS = 2;

	public static int NUMBER_OF_ROOM_PLACEMENT_ATTEMPTS = 6;

	public static double MONSTER_SPAWN_CHANCE = .125;

	public static int X_MAP_SIZE = 100;
	public static int Y_MAP_SIZE = 100;
	public static int PLAYER_VIEW_DISTANCE = 10;
	public static int PLAYER_VIEW_CIRCUMFRENCE = (int) Math.round(2 * Math.PI * PLAYER_VIEW_DISTANCE) + 1;
	public static int ITERATIONS_PER_PATH = 4;

	/**
	 * Calculates the points for the parameter of a PLAYER_VIEW_DISTANCE radius
	 * circle note, does not filter points that are = to each other
	 * 
	 * @return an array composed of the points of the circles parameter
	 */
	public static Point[] calculateViewDistance() {
		Point[] pointList = new Point[GameConstants.PLAYER_VIEW_CIRCUMFRENCE + 1];
		for (int i = 1; i <= GameConstants.PLAYER_VIEW_CIRCUMFRENCE; i++) {
			// gets the (x,y) cord of the vision circle
			// gets the 2pi * i / player_view_circ, which breaks the unit circle in
			// player_view_circumfrence chunks and multiples them by the radius
			int x = (int) Math.round(GameConstants.PLAYER_VIEW_DISTANCE
					* Math.cos(i * 2 * Math.PI / GameConstants.PLAYER_VIEW_CIRCUMFRENCE));
			int y = (int) Math.round(GameConstants.PLAYER_VIEW_DISTANCE
					* Math.sin(i * 2 * Math.PI / GameConstants.PLAYER_VIEW_CIRCUMFRENCE));
			pointList[i] = new Point(x, y);
			System.out.println(pointList[i]);
		}
		return pointList;
	}

}