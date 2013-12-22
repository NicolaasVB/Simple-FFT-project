package map.tiles;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.io.File;

import map.Map;
import utils.ResourceCache;
import Application.Configuration;

public class WestTile extends MapTile {

	public WestTile(int startHeight, int endHeight, Map parent) {
		super(startHeight, endHeight, parent);
	}

	public WestTile(MapTile tile) {
		super(tile);
	}
	
	protected void setTexture() {
		if (endHeight - startHeight == 0) {
			texture =  new TileTexture(ResourceCache.getInstance().getImage("grass_normal", Configuration.Paths.Resources.TILES + "grass" + File.separatorChar + "normal.png"));
		} else {
			if (endHeight - startHeight == 1)
				texture =  new TileTexture(ResourceCache.getInstance().getImage("grass_west_1", Configuration.Paths.Resources.TILES + "grass" + File.separatorChar + "west_1.png"));
			if (endHeight - startHeight == 2)
				texture =  new TileTexture(ResourceCache.getInstance().getImage("grass_west_2", Configuration.Paths.Resources.TILES + "grass" + File.separatorChar + "west_2.png"));
			if (endHeight - startHeight == 3)
				texture =  new TileTexture(ResourceCache.getInstance().getImage("grass_west_3", Configuration.Paths.Resources.TILES + "grass" + File.separatorChar + "west_3.png"));
		}
		
	}

	@Override
	public TileOrientation getOrientation() {
		return TileOrientation.WEST;
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
		int drawStartHeight =  Configuration.getTileHeight() * endHeight;
		int drawEndHeight = Configuration.getTileHeight() * startHeight;
		
		top = new Polygon(new int[] { x, x + horizontal / 2, x,
				x - horizontal / 2 }, new int[] { y - drawStartHeight,
				y - drawEndHeight + vertical / 2, y - drawEndHeight + vertical,
				y - drawStartHeight + vertical / 2 }, 4);
		
		Polygon leftSide = new Polygon(new int[] { x, x - horizontal / 2,
				x - horizontal / 2, x }, new int[] {
				y - drawEndHeight + vertical,
				y - drawStartHeight + vertical / 2, y + vertical / 2,
				y + vertical }, 4);
		
		Polygon rightSide = new Polygon(new int[] { x, x + horizontal / 2,
				x + horizontal / 2, x }, new int[] {
				y - drawEndHeight + vertical, y - drawEndHeight + vertical / 2,
				y + vertical / 2, y + vertical }, 4);

		Color oldColor = g.getColor();
		g.setColor(myColor); // Draw the top of the tile
		if (texture != null)
			texture.draw(x - horizontal / 2, y - drawStartHeight, g);
		g.fillPolygon(top);
		g.setColor(Color.BLACK); // Draw the right side
		g.fillPolygon(rightSide);
		// Draw the left side
		g.fillPolygon(leftSide);
		
		g.setColor(oldColor);
		drawEntity(g, false);
	}

	@Override
	public MapTile clone() {
		return new WestTile(startHeight, endHeight, parent);
	}
}
