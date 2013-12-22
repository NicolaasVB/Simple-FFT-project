package event.local;

import java.awt.Color;
import java.util.ArrayList;

import map.tiles.MapTile;
import client.GameClient;
import event.LocalGameEvent;

public class HighlightStartTiles extends LocalGameEvent {

	private static final long serialVersionUID = 6921285214856635262L;

	@Override
	public void process() {
		System.out.println("Highlighting the start tiles");
		Color myColor = new Color(255, 0, 0, 100);
		for (ArrayList<MapTile> row : GameClient.getInstance().getMap().getField()) {
			for (MapTile t : row) {
				if (t.getStartTileForTeam() == GameClient.getInstance().getTeam())
					t.highlightCustomColor(myColor);
			}
		}
	}

}
