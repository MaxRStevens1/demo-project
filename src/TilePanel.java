import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class TilePanel extends JPanel {
	public static int BASE_MAP_OFFSET_X = 0;
	public static int BASE_MAP_OFFSET_y = 0;
	public static int TILE_SIZE = 32;
	private Map map;

	public TilePanel(Map map) {
		this.map = map;
	}

	public void setImage(int x, int y, Image image) {
		map.getTile(x, y).image = image;
		repaint();
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
		repaint();
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		int pX = map.player.x;
		int pY = map.player.y;
		if (map.player.isFreeCam) {
			pX = map.player.freeX;
			pY = map.player.freeY;
		}
		// what it does is uses the players pos - constants to have the player always in
		// the center of the displayed screen
		for (int y = pY - GameConstants.PLAYER_VIEW_DISTANCE; y < pY + GameConstants.PLAYER_VIEW_DISTANCE; y++) {
			for (int x = pX - GameConstants.PLAYER_VIEW_DISTANCE; x < pX + GameConstants.PLAYER_VIEW_DISTANCE; x++) {
				Tile t = map.getTile(x, y);
				if (t != null && t.isVisible) {
					// long and complicated drawing, but what it ends up doing is drawing a 16x16
					// grid centered around the player character
					g.drawImage(t.image, (x - pX + GameConstants.PLAYER_VIEW_DISTANCE) * TILE_SIZE + BASE_MAP_OFFSET_X,
							(y - pY + GameConstants.PLAYER_VIEW_DISTANCE) * TILE_SIZE + BASE_MAP_OFFSET_X, TILE_SIZE,
							TILE_SIZE, this);
					// draws player at position if the tiles x / y matches the player
					if (t.x == map.player.x && t.y == map.player.y) {
						g.drawImage(map.player.image,
								(x - pX + GameConstants.PLAYER_VIEW_DISTANCE) * TILE_SIZE + BASE_MAP_OFFSET_X,
								(y - pY + GameConstants.PLAYER_VIEW_DISTANCE) * TILE_SIZE + BASE_MAP_OFFSET_X,
								TILE_SIZE, TILE_SIZE, this);
					}
					// draws Enemies if enemy in tile is not null
					if (t.enemy != null) {
						g.drawImage(t.enemy.image, (x - pX + GameConstants.PLAYER_VIEW_DISTANCE) * TILE_SIZE + BASE_MAP_OFFSET_X,
								(y - pY + GameConstants.PLAYER_VIEW_DISTANCE) * TILE_SIZE + BASE_MAP_OFFSET_X, TILE_SIZE,
								TILE_SIZE, this);
					}
					
				}
			}
		}

		repaint();
	}

}
