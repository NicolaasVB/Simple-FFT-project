package event.local;

import client.GameClient;
import client.GameClient.GameState;
import event.LocalGameEvent;
import game.characters.GameCharacter;

/**
 * this event should only ever be called from inside another Event, hence i put
 * no modifier, so it's visible for classes in the same package, but not for
 * classes outside this package
 * 
 * @author Nicolaas
 * 
 */
class CharacterSelectedEvent extends LocalGameEvent {
	
	private static final long serialVersionUID = 2827972328910761792L;
	private GameCharacter character;
	
	public CharacterSelectedEvent(GameCharacter c) {
		this.character = c;
	}

	@Override
	public void process() {
		System.out.println("Selected character: " + character);
		character.setSelected(true);
		character.getMenu().setActive(true);
		character.setShowHealth(true);
		GameClient.getInstance().setGameState(GameState.BROWSING_MENU);
	}

}
