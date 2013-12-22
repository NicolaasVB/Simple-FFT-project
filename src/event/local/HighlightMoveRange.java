package event.local;

import java.awt.Color;
import java.util.Vector;

import map.tiles.MapTile;
import event.LocalGameEvent;
import game.characters.GameCharacter;

/**
 * local event to highlight the moveRange
 * @author Nicolaas
 *
 */
public class HighlightMoveRange extends LocalGameEvent {
	
	private static final long serialVersionUID = 2611738938273675556L;
	private GameCharacter character;
	
	public HighlightMoveRange(GameCharacter c) {
		this.character = c;
	}

	@Override
	public void process() {
		System.out.println("Highlighting the moverange of character: " + character);
		Vector<MapTile> locs = character.findMoveLocations();
		Color myColor = new Color(255, 0, 0, 100);
		for (MapTile t : locs) {
			t.highlightCustomColor(myColor);
		}
	}

}
