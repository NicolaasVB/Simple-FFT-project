package game;

import game.characters.GameCharacter;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;

import map.Map;
import map.tiles.MapTile;
import utils.CustomTimer;
import Application.Configuration;

/**
 * This is a path a character can traverse.
 * @author Nicolaas
 *
 */
public class Path implements Runnable, Serializable {
	
	private static final long serialVersionUID = -1646077521238191751L;
	private ArrayList<Point> tiles;
	private transient CustomTimer timer;
	private transient Map map;
	private int selected;
	private transient GameCharacter character;
	
	public Path(ArrayList<MapTile> tiles, GameCharacter character, Map map) {
		this.tiles = new ArrayList<Point>();
		for (MapTile tile : tiles) {
			this.tiles.add(tile.getFieldLocation());
		}
		this.selected = 0;
		this.setCharacter(character);
		this.map = map;
	}
	
	private CustomTimer getTimer() {
		if (timer == null)
			timer = new CustomTimer(Configuration.getCharacterMoveTime());
		return timer;
	}
	
	public void addTile(MapTile tile) {
		Point testPoint = tile.getFieldLocation();
		
		if (tiles.contains(testPoint)) {
			for (int i = tiles.size() - 1; i > tiles.indexOf(testPoint); i--) {
				tiles.remove(i);
			}
		} else {
			tiles.add(tile.getFieldLocation());
		}
	}
	
	
	public ArrayList<MapTile> getTiles() {
		ArrayList<MapTile> tiles = new ArrayList<MapTile>();
		for (Point t : this.tiles) {
			tiles.add(getTile(t));
		}
		return tiles;
	}
	
	public MapTile getTile(Point tile) {
		return getMap().getTile(tile.x, tile.y);
	}
	
	private MapTile getNext() {
		return ++selected < tiles.size() ? getTile(tiles.get(selected)) : null;
	}
	
	protected MapTile getLast() {
		return getTile(tiles.get(tiles.size() - 1));
	}
	
	public boolean isFinished() {
		return selected == tiles.size() - 1;
	}
	
	public boolean verify() {
		for (MapTile tile : getTiles()) {
			if (!getCharacter().findMoveLocations().contains(tile) && !tile.equals(character.getTile()))
				return false;
		}
		return true;
	}

	@Override
	public void run() {
		while (!isFinished()) {
			getTimer().reset();
			MapTile tile = getNext();
			System.out.println(tile);
			getCharacter().move(tile);
			while (getTimer().isRunning()) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		getCharacter().removePath();
		
	}
	
	public String toString() {
		return tiles.toString();
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public GameCharacter getCharacter() {
		return character;
	}

	public void setCharacter(GameCharacter character) {
		this.character = character;
	}

}
