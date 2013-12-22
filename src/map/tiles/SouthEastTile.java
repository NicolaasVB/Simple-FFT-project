package map.tiles;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.io.File;

import map.Map;
import utils.ResourceCache;
import Application.Configuration;

public class SouthEastTile extends MapTile {

	public SouthEastTile(int startHeight, int endHeight, Map parent) {
		super(startHeight, endHeight, parent);
	}

	public SouthEastTile(MapTile tile) {
		super(tile);
	}
	
	protected void setTexture() {
		if (endHeight - startHeight == 0) {
			texture =  new TileTexture(ResourceCache.getInstance().getImage("grass_normal", Configuration.Paths.Resources.TILES + "grass" + File.separatorChar + "normal.png"));
		} else {
			if (endHeight - startHeight == 1) {
				texture = new TileTexture(ResourceCache.getInstance().getImage("grass_south_east_1", Configuration.Paths.Resources.TILES + "grass" + File.separatorChar + "south_east_1.png"));
			} else if (endHeight - startHeight == -1) {
				texture = new TileTexture(ResourceCache.getInstance().getImage("grass_south_east_neg1", Configuration.Paths.Resources.TILES + "grass" + File.separatorChar + "south_east_neg1.png"));
			} else if (endHeight - startHeight == -2) {
				texture = new TileTexture(ResourceCache.getInstance().getImage("grass_south_east_neg2", Configuration.Paths.Resources.TILES + "grass" + File.separatorChar + "south_east_neg2.png"));
			} else if (endHeight - startHeight == -3) {
				texture = new TileTexture(ResourceCache.getInstance().getImage("grass_south_east_neg3", Configuration.Paths.Resources.TILES + "grass" + File.separatorChar + "south_east_neg3.png"));
			} else
				texture = null;
		}
	}

	@Override
	public TileOrientation getOrientation() {
		return TileOrientation.SOUTH_EAST;
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

		top = new Polygon(new int[] { x - horizontal / 2, x,
				x + horizontal / 2, x }, new int[] {
				y - drawStartHeight + vertical / 2, y - drawStartHeight,
				y - drawStartHeight + vertical / 2,
				y - drawEndHeight + vertical }, 4);
		Polygon leftSide = new Polygon(new int[] { x - horizontal / 2,
				x - horizontal / 2, x, x }, new int[] { y + vertical / 2,
				y - drawStartHeight + vertical / 2,
				y - drawEndHeight + vertical, y + vertical }, 4);
		Polygon rightSide = new Polygon(new int[] { x, x, x + horizontal / 2,
				x + horizontal / 2 }, new int[] { y + vertical,
				y - drawEndHeight + vertical,
				y - drawStartHeight + vertical / 2, y + vertical / 2 }, 4);

		Color oldColor = g.getColor();
		g.setColor(myColor);
		if (texture != null) {
			if (endHeight - startHeight == 0)
				texture.draw(x - horizontal / 2, y - drawEndHeight, g);
			else
				texture.draw(x - horizontal / 2, y - drawStartHeight, g);
		}
		g.fillPolygon(top); // Draw the top of the tile
		g.setColor(Color.BLACK); // Draw the left side
		g.fillPolygon(leftSide);
		// Draw the right side
		g.fillPolygon(rightSide);
		
		g.setColor(oldColor);

	}

	@Override
	public MapTile clone() {
		return new SouthEastTile(startHeight, endHeight, parent);
	}

}
