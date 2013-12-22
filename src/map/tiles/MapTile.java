package map.tiles;

import game.characters.GameCharacter;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;

import map.GameEntity;
import map.Map;
import menu.GameMenu;
import Application.Configuration;



public abstract class MapTile {

    // Colors for drawing tiles
	private static final Color TOP_COLOR = new Color(0, 0, 0, 0); // fully transparant!
	private static final Color HIGHLIGHTED_COLOR = new Color(0, 0, 255, 100); // blue but partly transparant
    // Tile Variables
    private double height;
    protected int startHeight;
    protected int endHeight;
    private boolean highlighted;
    protected Color myColor;
    protected TileTexture texture;
    protected Polygon top;
    protected Point fieldLocation;
    protected Point absoluteLocation;
    protected int startTileForTeam;
    
    private GameEntity entity; // the character/object standing on this tile, if any
    
    // link to the parent for drawStart
    protected Map parent;
    
    /**
     * constructor
     * @param startHeight The lower height of the tile (If slanted)
     * @param endHeight The upper hieght of the tile (If slanted)
     * @param parent the map this is a member of. THIS CAN BE NULL!!!!
     */
    public MapTile(int startHeight, int endHeight, Map parent) {
        this.startHeight = startHeight;
        this.endHeight = endHeight;
        this.height = (double)(startHeight + endHeight) / 2;
        this.highlighted = false;
        this.fieldLocation = new Point();
        this.myColor = TOP_COLOR;
        this.entity = null;
        this.texture = null;
        this.parent = parent;
        this.startTileForTeam = 0;
        this.setTexture();
    }

	public MapTile(MapTile tile) {
        this.startHeight = tile.startHeight;
        this.endHeight = tile.endHeight;
        this.height = tile.height;
        this.highlighted = tile.highlighted;
        this.fieldLocation = tile.fieldLocation;
        this.myColor = tile.myColor;
        this.entity = tile.entity;
        this.absoluteLocation = tile.absoluteLocation;
        this.parent = tile.parent;
        this.texture = tile.texture;
        this.startTileForTeam = 0;
    } 
    
    /**
     * for internal 'assertion' use!!! if a mapTile isn't initialised, it means that it isnt a member of any map
     * @return
     */
    protected boolean isInitialised() {
    	return parent != null;
    }
    
    /**
     * if the parent is set durign the construction of this object, then it is automaticly initialised, but if it isnt
     * it will have to be set here, or the maptile will never be drawn!!!
     * @param parent
     */
    public void init(Map parent) {
    	this.parent = parent;
    }
    
    protected abstract void setTexture();
    
    /**
     * gets the parent (map) of this tile
     * @return the parent
     */
    public Map getParent() {
    	return parent;
    }

    /**Get the tiles average height
     * @return the height
     */
    public double getHeight() {
            return height;
    }

    /**Get the tiles lower height (if slanted else return height)
     * @return the startHeight
     */
    public int getStartHeight() {
            return startHeight;
    }

    /**Get the tiles upper height (if slanted else return height)
     * @return the endHeight
     */
    public int getEndHeight() {
            return endHeight;
    }
    
    /**
     * gets the orientation of this tile
     * @return the orientation
     */
    public abstract TileOrientation getOrientation();

    /**Return true if tile is highlighted, else return false
     * @return selected
     */
    public boolean isHighlighted() {
            return highlighted;
    }

    /**Get the polygon that is the top of the tile
     * @return the top
     */
    public Polygon getTop() {
            return top;
    }

    /**Get this tiles location on the field
     * @return the fieldLocation
     */
    public Point getFieldLocation() {
            return fieldLocation;
    }
    
    /**
     * Store the tiles location on the map (set when map is made)
     * @param p The point that represents the (x,y) coordinate of the tile
     */
    public void setFieldLocation(Point p) {
            fieldLocation = p;
    }
    
    /**
     * Gets the absolute location of this tile whitout considering the drawStart
     * @return the absolute location
     */
    public Point getAbsoluteLocation() {
    	return absoluteLocation;
    }
    
    /**
     * Gets the relative location of this tile WITH considering drawStart
     * THIS CAN BE NULL IF THE TILE HASNT BEEN INITIALISED!!!!
     * @return the relative location
     */
    public Point getRelativeLocation() {
    	if (!isInitialised())
    		return null;
    	int x = (int) (parent.getDrawStart().getX() + absoluteLocation.getX());
    	int y = (int) (parent.getDrawStart().getY() + absoluteLocation.getY());
        int finalHeight = (int) (Configuration.getTileHeight() * height);
        return new Point(x, y - finalHeight);
    }
    
    /**
     * Sets the absolute location of the tile (whitout drawStart)
     * @param p the point that represents the absolute (x,y) coordinate of the tile
     */
    public void setAbsoluteLocation(Point p) {
            absoluteLocation = p;
    }
    
    /**
     * A GameEntity is standing on the tile, so we give a reference to it for easy access
     * @param c the entity that is standing on this tile
     */
    public void setEntity(GameEntity c) {
            this.entity = c;
    }
    
    /**
     * Gets the current GameEntity standing on this tile. Be aware that this can be null!!!
     * @return the entity that is currently standing on the tile or null
     */
    public GameEntity getEntity() {
            return this.entity;
    }

    public int getStartTileForTeam() {
		return startTileForTeam;
	}

	public void setStartTileForTeam(int startTileForTeam) {
		this.startTileForTeam = startTileForTeam;
	}

	/**Set the lower height of the tile (also updates average height)
     * @param startHeight the startHeight to set
     */
    public void setStartHeight(int startHeight) {
            this.startHeight = startHeight;
            this.height = (startHeight + endHeight) / 2;
            setTexture();
    }

    /**Set the upper height of the tile (also updates average height)
     * @param endHeight the endHeight to set
     */
    public void setEndHeight(int endHeight) {
            this.endHeight = endHeight;
            this.height = (startHeight + endHeight) / 2;
            setTexture();
    }

    /**Set if the tile is highlighted or not
     * @param highlighted new state of the tiles selection status
     */
    public void setHighlighted(boolean highlighted) {
            this.highlighted = highlighted;
            myColor = highlighted? HIGHLIGHTED_COLOR : TOP_COLOR;
    }
    
    /**
     * Sets the color of this tile to the custom color, for attack radius/walking area
     * @param color the color of the top of the tile
     */
    public void highlightCustomColor(Color color) {
    	myColor = highlighted? HIGHLIGHTED_COLOR : color;
    }
    
    public void clearHighlight() {
    	myColor = highlighted ? HIGHLIGHTED_COLOR : TOP_COLOR;
    }
    
    /**Check to see if this tile was clicked on.
     * This method does not set the selected tile. That is handled in the Map class
     * @param click Location of the mouse click
     */
    public boolean wasClickedOn(Point click) {
            return top.contains(click);
    }
    
    /**
     * checks if a character is allowed to stand on here
     * @return
     */
    public abstract boolean isAccessible();
    
    /**
     * whecks whether there is an entity on this tile
     * @return true if occupied, false if not
     */
    public boolean isOccupied() {
    	return getEntity() != null;
    }
    
    /**
     * this will draw the entity if there is one on the tile.
     * @param g the graphics environment
     * @param transparant true if it has to be drawn transparant, false if opaque
     */
    public void drawEntity(Graphics g, boolean transparant) {
    	if (!isInitialised())
    		return;
        if (entity != null) {
        	int x = (int) (parent.getDrawStart().getX() + absoluteLocation.getX());
        	int y = (int) (parent.getDrawStart().getY() + absoluteLocation.getY());
            int finalHeight = (int) (Configuration.getTileHeight() * height);
            entity.draw(x, y - finalHeight, g, transparant);
        }
    }
        
    public abstract void draw(Graphics g);

    public abstract MapTile clone();
    
    public GameMenu getMenu() {
    	if (getEntity() != null && getEntity() instanceof GameCharacter) {
    		return ((GameCharacter) getEntity()).getMenu();
    	}
    	return null;
    }
    
    public static MapTile loadTileFromString(String s)
    {
    	boolean isStartTile = false;
    	if(s.startsWith("S"))
    	{
    		isStartTile = true;
    		s = s.substring(1);
    	}
    	String[] data = s.split(",");
		int ori = Integer.valueOf(data[0].trim());
		int start = Integer.valueOf(data[1].trim());
		int end = Integer.valueOf(data[2].trim());
		MapTile tile =  TileOrientation.getTileFromOrientation(TileOrientation.values()[ori], start, end, null);
		tile.setStartTileForTeam(isStartTile ? 1 : 0);
		if(data.length > 3)
		{
			tile.setStartTileForTeam(Integer.valueOf(data[3].trim()));
		}
		return tile;
    }
    
    public String mapTileToString()
    {
		StringBuffer s = new StringBuffer();
		s.append(getOrientation().ordinal() + ",");
		s.append(getStartHeight() + ",");
		s.append(getEndHeight() + ",");
		s.append(getStartTileForTeam());
		return s.toString();
    }
    
    public String toString() {
    	return getFieldLocation().toString();
    }
}
