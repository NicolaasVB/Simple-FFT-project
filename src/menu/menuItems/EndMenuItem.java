package menu.menuItems;

import menu.GameMenu;
import client.GameClient;
import client.GameClient.GameState;
import event.GameEventHandler;
import event.external.EndTurnRequest;

public class EndMenuItem extends MenuItem {
	
	public EndMenuItem(GameMenu parent) {
		super("End", parent);		
	}
	
	/**
	 * executes the action that comes with this menuItem
	 * @return true if the action closes the menu, false if it should keep the menu open
	 */	
	public boolean execute() {
		GameClient.getInstance().getSelectedCharacter().setSelected(false);
		GameEventHandler.getInstance().dispatchEvent(new EndTurnRequest());
		GameClient.getInstance().setGameState(GameState.IDLE);
		return true;	
	}
}
