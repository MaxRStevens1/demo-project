import java.util.ArrayList;
import java.util.Stack;

public class Dungeon {
	Player player;
	ArrayList<Map> dungeon = new ArrayList<Map>();
	private int currentDungeonMapIndex; // players current position in dungeon
	
	
	public Dungeon(int x, int y, Player player) {
		this.player = player;
		Boolean hasMapGenerationFailed = false;
		boolean isFirstFloor = true;
		Map map = new Map(x, y, player, isFirstFloor, hasMapGenerationFailed);
		
		//System.out.println("Has MapGenFailed: " + map.hasMapGenFailed);
		int i = 0;
		// loop done in order to sure map generation has successfuly completed
		while(map.hasMapGenFailed) {
			map = new Map(x, y, player, true, hasMapGenerationFailed);
			if(i >= 50) {
				//System.out.println("Error has occured in Map generation: " + i + " attempts failed");
				System.exit(0);
			}
			//System.out.println("Map stats: stairsdown=" + map.getDownStairStack().size() );
			i++;
		}
		dungeon.add(map);
		currentDungeonMapIndex = 0;
	}
	
	/**
	 * moves the player up a map, partners are set in moveplayerdown, and the player should
	 * never be able to move up before moving down
	 * @return
	 */
	public boolean movePlayerUp() {
		UpStairTile oldPlayerTile = (UpStairTile) dungeon.get(currentDungeonMapIndex).getTile(player.x, player.y);
		currentDungeonMapIndex--;
		DownStairTile newPlayerTile = (DownStairTile) oldPlayerTile.partner;
		if (newPlayerTile == null)
			return false;
		player.tile = newPlayerTile;
		oldPlayerTile.player = null;
		newPlayerTile.player = player;
		player.x = newPlayerTile.x;
		player.y = newPlayerTile.y;
		player.currentMapLevel = currentDungeonMapIndex;
		return true;
	}
	
	/**
	 * Moves the player up a map, creates a new map, and moves the player into the partner tile of their stair, set by connectstairs
	 * 
	 * @return true on successful exit
	 */
	public boolean movePlayerDown() {
		Boolean hasMapGenerationFailed = false;
		boolean isFirstFloor = false;
		
		// if there already is a map in the next dungeon slot, move player to next level
		if (currentDungeonMapIndex+1 != dungeon.size()) {
			DownStairTile oldPlayerTile = (DownStairTile) dungeon.get(currentDungeonMapIndex).getTile(player.x, player.y);
			currentDungeonMapIndex++;
			UpStairTile newPlayerTile = (UpStairTile) oldPlayerTile.partner;
			if (newPlayerTile == null)
				return false;
			player.tile = newPlayerTile;
			oldPlayerTile.player = null;
			newPlayerTile.player = player;
			player.x = newPlayerTile.x;
			player.y = newPlayerTile.y;
			player.currentMapLevel = currentDungeonMapIndex;
			return true;
		}
		// else, create a new map
		
		//System.out.println("_________________________________________________________________________________________");
		int x = dungeon.get(currentDungeonMapIndex).x;
		int y = dungeon.get(currentDungeonMapIndex).y;
		
		//System.out.println("player x: " + player.x + " y: " +player.y);
		//System.out.println("player = " + dungeon.get(currentDungeonMapIndex).getTile(player.x, player.y).player);
		//System.out.println("player tile x = x: "+player.tile.x + " y: " + player.tile.y);
		//System.out.println("players name: " + player.name);
		
		// first and second inputs are the size of the map, set by first map, 
		// then its false as it is not the first floor, and then a bool is sent in, and used to check for failure
		Map map = new Map(x, y, player, isFirstFloor, hasMapGenerationFailed);
		
		int i = 0;
		// loops until the map has been properly generated, 
		// ** should work on map generation to reduce load time **
		while(map.hasMapGenFailed) {
			hasMapGenerationFailed = false;
			map = new Map(x, y, player, isFirstFloor, hasMapGenerationFailed);
			if(i >= 50) {
				//System.out.println("Error has occured in Map generation: " + i + " attempts failed");
				System.exit(0);
			}
			i++;
		}
		//System.out.println("player x: " + player.x + " y: " +player.y);
		//System.out.println("player = " + dungeon.get(currentDungeonMapIndex).getTile(player.x, player.y).player);
		//System.out.println("player tile x = x: "+player.tile.x + " y: " + player.tile.y);
		//System.out.println("players name: " + player.name);
		
		// gets the previous players tile position, adds the new map to the dungeon, and iterates the dungeon map index
		DownStairTile oldPlayerTile = (DownStairTile) dungeon.get(currentDungeonMapIndex).getTile(player.x, player.y);
		dungeon.add(map);
		currentDungeonMapIndex++;
		// 
		if(connectStairsBetweenNewFloorAndOld()) {
			// another vomit, but gets the partner of the stairtile in the new map, which is set through connectStairs method
			UpStairTile newPlayerTile = (UpStairTile) map.getTile(oldPlayerTile.partner.x, oldPlayerTile.partner.y);
			newPlayerTile.player = player;
			oldPlayerTile.player = null;
			player.x = newPlayerTile.x;
			player.y = newPlayerTile.y;
			player.currentMapLevel = currentDungeonMapIndex;
			//System.out.println("correct new player x " + newPlayerTile.x + " correct new player y " + newPlayerTile.y);
			//System.out.println("new player x " + player.x + " new player y " + player.y);
		} else {
			//System.out.println("Connection between stairs of new and old floors failed");
			System.exit(0);
			return false;
		}
		return true;
	}
	
	/**
	 * Sets the partners between stairs, connecting them between floors
	 * @return T/F on whether or not both sets of stair tiles have had their partners set
	 */
	public boolean connectStairsBetweenNewFloorAndOld() {
		Map oldMap = dungeon.get(currentDungeonMapIndex - 1);
		Map currentMap = dungeon.get(currentDungeonMapIndex);
		Stack<DownStairTile> oldMapDownStairStack = oldMap.getDownStairStack();
		Stack<UpStairTile> currentMapUpStairStack = currentMap.getUpStairStack();
		//System.out.println("Size of old: " + oldMapDownStairStack.size() + "  size of new: " + currentMapUpStairStack.size());
		while(oldMapDownStairStack.size() > 0 && currentMapUpStairStack.size() > 0) {
			DownStairTile d = oldMapDownStairStack.pop();
			UpStairTile u = currentMapUpStairStack.pop();
			//System.out.println("Old Stair x/y: "+d + "\nnew stair x/y: " + u);
			d.partner = u;
			u.partner = d;
		}
		return oldMapDownStairStack.size() == 0 && currentMapUpStairStack.size() == 0;
	}
	
	public ArrayList<Map> getDungeon() { return dungeon;}
	public Map getMap() {return dungeon.get(currentDungeonMapIndex);}
}
