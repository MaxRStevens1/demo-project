import java.util.ArrayList;

public class Dungeon {
	Player player;
	ArrayList<Map> dungeon = new ArrayList<Map>();
	private int currentDungeonMapIndex;
	public Dungeon(int x, int y, Player player) {
		this.player = player;
		dungeon.add(new Map(x, y, player, true));
		currentDungeonMapIndex = 0;
	}

	
	
	public ArrayList<Map> getDungeon() { return dungeon;}
	public Map getMap() {return dungeon.get(currentDungeonMapIndex);}
	public void adjustCurrentMapIndex(int x) {
		currentDungeonMapIndex += x;
	}
}
