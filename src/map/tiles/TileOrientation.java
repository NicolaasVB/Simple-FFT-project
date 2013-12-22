package map.tiles;

import map.Map;

public enum TileOrientation {
	EMPTY, NORMAL, NORTH, EAST, SOUTH, WEST, NORTH_WEST, NORTH_EAST, SOUTH_EAST, SOUTH_WEST;

	public static MapTile getTileFromOrientation(TileOrientation ori, int start, int end, Map parent) {
		switch (ori) {
		case EMPTY:
			return new EmptyTile(start, end, parent);
		case NORMAL:
			return new NormalTile(end, parent);
		case NORTH:
			return new NorthTile(start, end, parent);
		case EAST:
			return new EastTile(start, end, parent);
		case SOUTH:
			return new SouthTile(start, end, parent);
		case WEST:
			return new WestTile(start, end, parent);
		case NORTH_WEST:
			return new NorthWestTile(start, end, parent);
		case NORTH_EAST:
			return new NorthEastTile(start, end, parent);
		case SOUTH_EAST:
			return new SouthEastTile(start, end, parent);
		case SOUTH_WEST:
			return new SouthWestTile(start, end, parent);
		default:
			return null;
		}
	}

}