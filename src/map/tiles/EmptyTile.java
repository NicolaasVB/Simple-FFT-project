package map.tiles;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.io.File;

import map.Map;
import utils.ResourceCache;
import Application.Configuration;

public class EmptyTile extends MapTile {
	
	public EmptyTile(int startHeight, int endHeight, Map parent) {
		super(startHeight, endHeight, parent);
		top = new Polygon(); // so we dont get a nullpointer
	}

	public EmptyTile(MapTile tile) {
		super(tile);
	}
	
	protected void setTexture() {
		texture = new TileTexture(ResourceCache.getInstance().getImage("water", Configuration.Paths.Resources.TILES + File.separatorChar + "water.png"));
	}

	@Override
	public TileOrientation getOrientation() {
		return TileOrientation.EMPTY;
	}

	@Override
	public boolean isAccessible() {
		return false;
	}

	@Override
	public void draw(Graphics g) {
		if (!isInitialised())
			return;
		int x = (int) (parent.getDrawStart().getX() + absoluteLocation.getX());
		int y = (int) (parent.getDrawStart().getY() + absoluteLocation.getY());
		
		int horizontal = Configuration.getTileDiagonal();
		int vertical = Configuration.getTileDiagonal() / 2;
		
		top = new Polygon(new int[] { x, x + horizontal / 2, x, x - horizontal / 2 }, new int[] { y, y + vertical / 2, y + vertical, y + vertical / 2 }, 4);
		
		Color oldColor = g.getColor();
		g.setColor(myColor); 
		// Draw the top of the tile
		if (texture != null)
			texture.draw(x - horizontal / 2, y, g);
		g.fillPolygon(top);
		
		g.setColor(oldColor);
	}

	@Override
	public MapTile clone() {
		return new EmptyTile(startHeight, endHeight, parent);
	}

}
