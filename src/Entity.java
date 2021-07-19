import java.awt.Image;

public class Entity {
	int x;
	int y;
	Image image;
	String name;

	/**
	 * basic, interactable constructor
	 * 
	 * @param x cordinate on map
	 * @param y cordinate on map
	 */
	public Entity(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * generic constructor
	 */
	public Entity() {
		x = -1;
		y = -1;
	}

}
