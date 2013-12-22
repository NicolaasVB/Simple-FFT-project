package map.tiles;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.io.File;

import map.Map;
import utils.ResourceCache;
import Application.Configuration;

public class SouthTile extends MapTile {
	public SouthTile(int startHeight, int endHeight, Map parent) {
		super(startHeight, endHeight, parent);
	}

	public SouthTile(MapTile tile) {
		super(tile);
	}
	
	protected void setTexture() {
		if (endHeight - startHeight == 0) {
			texture =  new TileTexture(ResourceCache.getInstance().getImage("grass_normal", Configuration.Paths.Resources.TILES + "grass" + File.separatorChar + "normal.png"));
		} else {
			if (endHeight - startHeight == 1)
				texture =  new TileTexture(ResourceCache.getInstance().getImage("grass_south_1", Configuration.Paths.Resources.TILES + "grass" + File.separatorChar + "south_1.png"));
			else
				texture = null;
		}
	}

	@Override
	public TileOrientation getOrientation() {
		return TileOrientation.SOUTH;
	}

	@Override
	public boolean isAccessible() {
		return endHeight - startHeight <= 2 && getEntity() == null;
	}

	@Override
	public void draw(Graphics g) {
		if (!isInitialised())
			return;
		int x = (int) (parent.getDrawStart().getX() + absoluteLocation.getX());
		int y = (int) (parent.getDrawStart().getY() + absoluteLocation.getY());

		int horizontal = Configuration.getTileDiagonal();
		int vertical = Configuration.getTileDiagonal() / 2;
		int drawStartHeight = Configuration.getTileHeight() * endHeight;
		int drawEndHeight = Configuration.getTileHeight() * startHeight;
		
		top = new Polygon(new int[] { x, x + horizontal / 2, x,
				x - horizontal / 2 }, new int[] { y - drawEndHeight,
				y - drawEndHeight + vertical / 2,
				y - drawStartHeight + vertical,
				y - drawStartHeight + vertical / 2 }, 4);
		
		Polygon leftSide = new Polygon(new int[] { x, x - horizontal / 2,
				x - horizontal / 2, x }, new int[] {
				y - drawStartHeight + vertical,
				y - drawStartHeight + vertical / 2, y + vertical / 2,
				y + vertical }, 4);
		
		Polygon rightSide = new Polygon(new int[] { x, x + horizontal / 2,
				x + horizontal / 2, x }, new int[] {
				y - drawStartHeight + vertical,
				y - drawEndHeight + vertical / 2, y + vertical / 2,
				y + vertical }, 4);

		Color oldColor = g.getColor();
		g.setColor(myColor); // Draw the top of the tile
		if (texture != null) {
			if (endHeight - startHeight == 0)
				texture.draw(x - horizontal / 2, y - drawEndHeight, g);
			else
				texture.draw(x - horizontal / 2, y - drawStartHeight + vertical / 2, g);
		}
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
		return new SouthTile(startHeight, endHeight, parent);
	}

}
