package map.tiles;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.io.File;

import map.Map;
import utils.ResourceCache;
import Application.Configuration;

public class NormalTile extends MapTile {

	public NormalTile(int endHeight, Map parent) {
		super(endHeight, endHeight, parent);
	}

	public NormalTile(MapTile tile) {
		super(tile);
	}
	
	@Override
	protected void setTexture() {
		texture = new TileTexture(ResourceCache.getInstance().getImage("grass_normal", Configuration.Paths.Resources.TILES + "grass" + File.separatorChar + "normal.png"));
	}

	@Override
	public TileOrientation getOrientation() {
		return TileOrientation.NORMAL;
	}

	@Override
	public boolean isAccessible() {
		return getEntity() == null;
	}
	
	@Override
	public void draw(Graphics g) {
		if (!isInitialised())
			return;
		int x = (int) (parent.getDrawStart().getX() + absoluteLocation.getX());
		int y = (int) (parent.getDrawStart().getY() + absoluteLocation.getY());

		int finalHeight = (int) (Configuration.getTileHeight() * endHeight);
		int horizontal = Configuration.getTileDiagonal();
		int vertical = Configuration.getTileDiagonal() / 2;
		
		top = new Polygon(new int[] { x, x + horizontal / 2, x,
				x - horizontal / 2 }, new int[] { y - finalHeight,
				y + vertical / 2 - finalHeight, y + vertical - finalHeight,
				y + vertical / 2 - finalHeight }, 4);
		
		Polygon leftSide = new Polygon(new int[] { x, x - horizontal / 2,
				x - horizontal / 2, x }, new int[] {
				y - finalHeight + vertical, y - finalHeight + vertical / 2,
				y + vertical / 2, y + vertical }, 4);
		
		Polygon rightSide = new Polygon(new int[] { x, x + horizontal / 2,
				x + horizontal / 2, x }, new int[] {
				y - finalHeight + vertical, y - finalHeight + vertical / 2,
				y + vertical / 2, y + vertical }, 4);

		Color oldColor = g.getColor();
		g.setColor(myColor); 
		// Draw the top of the tile
		if (texture != null)
			texture.draw(x - horizontal / 2, y - finalHeight, g);
		g.fillPolygon(top);
		
		g.setColor(Color.BLACK); 
		// Draw the right side
		g.fillPolygon(rightSide);
		// Draw the left side
		g.fillPolygon(leftSide);
		
		g.setColor(oldColor);
		drawEntity(g, false);
	}

	@Override
	public MapTile clone() {
		return new NormalTile(endHeight, parent);
	}

}
