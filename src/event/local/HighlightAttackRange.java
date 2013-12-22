package event.local;

import java.awt.Color;
import java.util.Vector;

import map.tiles.MapTile;
import event.LocalGameEvent;
import game.characters.GameCharacter;

public class HighlightAttackRange extends LocalGameEvent {
	
	private static final long serialVersionUID = 957296784072256269L;
	private GameCharacter character;
	
	public HighlightAttackRange(GameCharacter c) {
		this.character = c;
	}

	@Override
	public void process() {
		System.out.println("Highlighting the attackrange of character: " + character);
		Vector<MapTile> locs = character.findAttackLocations();
		Color myColor = new Color(255, 0, 0, 100);
		for (MapTile t : locs) {
			t.highlightCustomColor(myColor);
		}
	}

}
