package map;

import game.characters.GameCharacter;

import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;

import map.tiles.MapTile;
import map.tiles.NormalTile;

import Application.Configuration;

import java.util.ArrayList;

import java.util.Scanner;
import java.awt.Point;
import java.awt.Graphics;
import java.io.File;
import java.io.FileWriter;


public class Map {

	// Variables
	private MapTile highlightedTile;
	private ArrayList<ArrayList<MapTile>> field;
	private Point mouseStart, mouseEnd;
	private int offsetX, offsetY;
	private int width, height;
	private String name;

	private Point drawStart;

	// End Variables

	/**
	 * Constructor
	 * 
	 * @param width
	 *            The width of this Map Object
	 * @param height
	 *            The height of this Map Object
	 */
	public Map(int width, int height) {
		this.width = width;
		this.height = height;
		drawStart = Configuration.getDrawStart();
		// initializing the field
		field = new ArrayList<ArrayList<MapTile>>();
		for (int i = 0; i < width; i++) {
			field.add(new ArrayList<MapTile>());
			for (int j = 0; j < height; j++) {
				field.get(i).add(new NormalTile(1, this));
				field.get(i).get(j).setFieldLocation(new Point(i, j));
			}
		}

		setAbsoluteLocationsAndInit();
		highlightedTile = null;
	}

	public Map(int width, int height, ArrayList<ArrayList<MapTile>> field) {
		this.field = field;
		this.width = width;
		this.height = height;
		drawStart = Configuration.getDrawStart();

		setAbsoluteLocationsAndInit();
		highlightedTile = null;
	}

	public Map(int width, int height, ArrayList<ArrayList<MapTile>> newTiles, String name) {
		this(width, height, newTiles);
		this.name = name;
	}

	/**
	 * This will set the absolute locations of all the MapTiles in this map. It
	 * will also initialise all the mapTiles so that they have a reference to
	 * this map.
	 */
	private void setAbsoluteLocationsAndInit() {
		int x = 0;
		int y = 0;
		for (int i = height - 1; i >= 0; i--) {
			for (int j = 0; j < width; j++) {
				field.get(j).get(i).setAbsoluteLocation(new Point(x, y));
				field.get(j).get(i).init(this);
				x += (int) (Configuration.getTileDiagonal() / 2);
				y += (int) (Configuration.getTileDiagonal() / 4);
			}
			x = -(Configuration.getTileDiagonal() / 2) * (height - i);
			y = (Configuration.getTileDiagonal() / 4) * (height - i);
		}
	}

	/**
	 * Get the ArrayList of ArrayLists that represents the field
	 * 
	 * @return
	 */
	public ArrayList<ArrayList<MapTile>> getField() {
		return field;
	}

	/**
	 * Get the width of the field
	 * 
	 * @return The width of this field object
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Get the height of the field
	 * 
	 * @return The height of this field object
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * gets the tile at coords x, y
	 * 
	 * @param x
	 *            the Field x-coordinate of the tile
	 * @param y
	 *            the Field y-coordinate of the tile
	 * @return the tile at (x, y)
	 */
	public MapTile getTile(int x, int y) {
		return field.get(x).get(y);
	}

	/**
	 * Gets the point where the map gest started to be drawn
	 * 
	 * @return drawStart
	 */
	public Point getDrawStart() {
		return drawStart;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void clearHighlights() {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				field.get(i).get(j).clearHighlight();
			}
		}
	}

	/**
	 * Draw the map to the screen
	 * 
	 * @param g
	 *            The Graphics object with which to draw the map
	 */
	public void draw(Graphics g) {
		// we iterate over every row from top to bottom
		// then we draw from left to right. This is to avoid the sides of tiles
		// in the middle to get drawn
		for (int i = height - 1; i >= 0; i--) {
			for (int j = 0; j < width; j++) {
				field.get(j).get(i).draw(g);
			}
		}
	}

	/**
	 * Select the file that is under the mouse click
	 * 
	 * @param x
	 *            X position of the mouse click
	 * @param y
	 *            Y position of the mouse click
	 */
	private void findAndHighlightTile(int x, int y) {
		int xIndex = -1, yIndex = -1;
		for (int i = height - 1; i >= 0; i--) {
			for (int j = 0; j < width; j++) {
				if (field.get(j).get(i).wasClickedOn(new Point(x, y))) {
					xIndex = j;
					yIndex = i;
				}
			}
		}
		if (xIndex > -1 && yIndex > -1)
			this.setHighlightedTile(xIndex, yIndex);
		else
			return;
	}

	/**
	 * Set the tile at a certain position to the new tile
	 * 
	 * @param x
	 *            Tiles x-location on the map
	 * @param y
	 *            Tiles y-location on the map
	 * @param data
	 *            The tile that replaces the old tile
	 */
	public void setTileData(int x, int y, MapTile data) {
		field.get(x).set(y, data);
	}

	/**
	 * Set the selected tile on the Map
	 * 
	 * @param x
	 *            The value 1 -> Configuration.field_width that represents the
	 *            new selected tile
	 * @param y
	 *            The value 1 -> Configuration.field_height that represents the
	 *            new selected tile
	 */
	public void setHighlightedTile(int x, int y) {
		if (x == -1 && y == -1) {
			highlightedTile = null;
			return;
		}
		if (highlightedTile != null) {
			highlightedTile.setHighlighted(false);
		}
		highlightedTile = field.get(x).get(y);
		highlightedTile.setHighlighted(true);
	}

	/**
	 * Method to adjust the size of the Map you are editing. It does NOT delete
	 * all of the data in the ArrayList of ArrayList. However, it will delete
	 * tiles that are outside the new bounds.
	 * 
	 * @param width
	 *            The new width of the field
	 * @param height
	 *            The new height of the field
	 */
	public void setMapSize(int width, int height) {
		this.width = width;
		this.height = height;
		ArrayList<ArrayList<MapTile>> tempField = new ArrayList<ArrayList<MapTile>>();
		for (int i = 0; i < width; i++) {
			tempField.add(new ArrayList<MapTile>());
			for (int j = 0; j < height; j++) {
				try {
					tempField.get(i).add(field.get(i).get(j));
				} catch (Exception e) {
					tempField.get(i).add(new NormalTile(1, null));
					tempField.get(i).get(j).setFieldLocation(new Point(i, j));
				}
			}
		}

		field = tempField;
		setAbsoluteLocationsAndInit();
	}

	/**
	 * Get the tile that is currently selected
	 * 
	 * @return The MapTile which is currently selected
	 */
	public MapTile getHighlightedTile() {
		return highlightedTile;
	}
	
	public String[] mapToString() {
		String[] contents = new String[width * height + 3];
		contents[0] = name;
		contents[1] = "" + width;
		contents[2] = "" + height;
		int index = 3;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				contents[index] = field.get(i).get(j).mapTileToString();
				index++;
			}
		}
		return contents;
	}
	
	public void writeToFile() {
		this.setName((String)JOptionPane.showInputDialog(null, "Choose a name for the map you wish to save:", "Save Map"));

		File directory = new File("Maps");
		if(!directory.exists())
			directory.mkdirs();
		
			File file = new File("Maps" + File.separator + getName() + ".imf");
			System.out.println(file.getAbsolutePath());
			try {
				System.out.println(file.createNewFile());
			} catch (Exception e) {
			}
			try {
				FileWriter writer = new FileWriter(file);
				for(String s : this.mapToString())
					writer.write(s + "\n");
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	public static Map mapFromString(String[] string) {
		try {
			ArrayList<ArrayList<MapTile>> newTiles = new ArrayList<ArrayList<MapTile>>();
			
			String name = string[0];
			int width = Integer.parseInt(string[1]);
			int height = Integer.parseInt(string[2]);
			int index = 3;
			for (int i = 0; i < width; i++) {
				newTiles.add(new ArrayList<MapTile>());
				for (int j = 0; j < height; j++) {
					newTiles.get(i).add(MapTile.loadTileFromString(string[index]));
					newTiles.get(i).get(j).setFieldLocation(new Point(i, j));
					index++;
				}
			}
			return new Map(width, height, newTiles, name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Map loadMap(File map) {
		try {
			Scanner reader = new Scanner(map);
			String name = reader.nextLine();
			int width = Integer.parseInt(reader.nextLine());
			int height = Integer.parseInt(reader.nextLine());
			String[] contents = new String[width * height + 3];
			contents[0] = name;
			contents[1] = "" + width;
			contents[2] = "" + height;
			for (int i = 3; i < width * height + 3; i++) {
				contents[i] = reader.nextLine();
			}
			return mapFromString(contents);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void mousePressed(MouseEvent e) {
		mouseStart = e.getPoint();

		offsetX = e.getX() - drawStart.x;
		offsetY = e.getY() - drawStart.y;

	}

	public void mouseReleased(MouseEvent e) {
		if (mouseStart == null)
			return; // if mouseclick was initiated otuside the panel, this would
					// result in a nullpointer
		mouseEnd = e.getPoint();

		int a = Math.abs((int) (mouseEnd.getX() - mouseStart.getX()));
		int b = Math.abs((int) (mouseEnd.getY() - mouseStart.getY()));

		if (Math.sqrt(a * a + b * b) <= 3) {
			if (getHighlightedTile() != null && getHighlightedTile().getEntity() != null && getHighlightedTile().getEntity() instanceof GameCharacter)
				((GameCharacter) getHighlightedTile().getEntity()).setShowHealth(false);
			this.findAndHighlightTile(e.getX(), e.getY());
			if (getHighlightedTile() != null && getHighlightedTile().getEntity() != null && getHighlightedTile().getEntity() instanceof GameCharacter)
				((GameCharacter) getHighlightedTile().getEntity()).setShowHealth(true);
		}
	}

	public void mouseDragged(MouseEvent e) {
		drawStart = new Point(e.getX() - offsetX, e.getY() - offsetY);
	}
	
	@Override
	public String toString() {
		return getName();
	}
}