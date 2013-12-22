package map.tiles;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.io.File;

import map.Map;
import utils.ResourceCache;
import Application.Configuration;

public class NorthWestTile extends MapTile {

	public NorthWestTile(int startHeight, int endHeight, Map parent) {
		super(startHeight, endHeight, parent);
	}

	public NorthWestTile(MapTile tile) {
		super(tile);
	}
	
	protected void setTexture() {
		if (endHeight - startHeight == 0) {
			texture =  new TileTexture(ResourceCache.getInstance().getImage("grass_normal", Configuration.Paths.Resources.TILES + "grass" + File.separatorChar + "normal.png"));
		} else {
			if (endHeight - startHeight == 1)
				texture = new TileTexture(ResourceCache.getInstance().getImage("grass_north_west_1", Configuration.Paths.Resources.TILES + "grass" + File.separatorChar + "north_west_1.png"));
			if (endHeight - startHeight == 2)
				texture = new TileTexture(ResourceCache.getInstance().getImage("grass_north_west_2", Configuration.Paths.Resources.TILES + "grass" + File.separatorChar + "north_west_2.png"));
			if (endHeight - startHeight == 3)
				texture = new TileTexture(ResourceCache.getInstance().getImage("grass_north_west_3", Configuration.Paths.Resources.TILES + "grass" + File.separatorChar + "north_west_3.png"));
			if (endHeight - startHeight == -1)
				texture = new TileTexture(ResourceCache.getInstance().getImage("grass_north_west_neg1", Configuration.Paths.Resources.TILES + "grass" + File.separatorChar + "north_west_neg1.png"));
		}
	}

	@Override
	public TileOrientation getOrientation() {
		return TileOrientation.NORTH_WEST;
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
		int drawStartHeight = Configuration.getTileHeight() * startHeight;
		int drawEndHeight = Configuration.getTileHeight() * endHeight;

		top = new Polygon(new int[] { x, x + horizontal / 2, x,
				x - horizontal / 2 }, new int[] { y - drawEndHeight,
				y - drawStartHeight + vertical / 2,
				y - drawStartHeight + vertical,
				y - drawStartHeight + vertical / 2 }, 4);
		Polygon leftSide = new Polygon(new int[] { x - horizontal / 2,
				x - horizontal / 2, x, x }, new int[] { y + vertical / 2,
				y - drawStartHeight + vertical / 2,
				y - drawStartHeight + vertical, y + vertical }, 4);
		Polygon rightSide = new Polygon(new int[] { x, x, x + horizontal / 2,
				x + horizontal / 2 }, new int[] { y + vertical,
				y - drawStartHeight + vertical,
				y - drawStartHeight + vertical / 2, y + vertical / 2 }, 4);

		Color oldColor = g.getColor();
		g.setColor(myColor);
		// Draw the top of the tile
		if (texture != null) {
			if (endHeight - startHeight == -1)
				texture.draw(x - horizontal / 2, y - drawStartHeight + vertical / 2, g);
			else
				texture.draw(x - horizontal / 2, y - drawEndHeight, g);
		}
		g.fillPolygon(top);
		g.setColor(Color.BLACK); 
		// Draw the left side
		g.fillPolygon(leftSide);
		// Draw the right side
		g.fillPolygon(rightSide);

		g.setColor(oldColor);
	}

	@Override
	public MapTile clone() {
		return new NorthWestTile(startHeight, endHeight, parent);
	}

}
