package map;



import java.awt.Graphics;
import java.awt.Point;

import map.tiles.MapTile;
import sprite.Sprite;

/**
 * @author Nicolaas
 * 
 */

public abstract class GameEntity {
	protected Sprite sprite;

	protected MapTile tile;

	/**
	 * Constructor for GameEntity
	 * 
	 * @param s
	 *            The default sprite that comes with this entity
	 * @param pos
	 *            Important!!! This is a reference to the tile that the entity
	 *            is situated on!!!
	 */
	public GameEntity(Sprite s, MapTile pos) {
		this.sprite = s;
		this.tile = pos;
		pos.setEntity(this);
	}

	/**
	 * This will draw the entity on the correct tile
	 * 
	 * @param x
	 *            X-coord of the north west corner of the tile this entity is
	 *            situated on.
	 * @param y
	 *            Y-coord of the north west corner of the tile this entity is
	 *            situated on.
	 * @param g
	 *            The Graphics context of the panel we're drawing on.
	 */
	public void draw(int x, int y, Graphics g, boolean transparant) {
		sprite.draw(x, y, g, transparant);
	}

	/**
	 * gets the tile this entity is situated on
	 * 
	 * @return the tile
	 */
	public MapTile getTile() {
		return tile;
	}
	
	public void setTile(MapTile tile) {
		this.tile = tile;
	}

	/**
	 * gets the field location of this entity
	 * 
	 * @return the field location
	 */
	public Point getLocation() {
		return tile.getFieldLocation();
	}
	
}
