package menu.menuItems;

import menu.GameMenu;
import client.GameClient;
import client.GameClient.GameState;
import event.GameEventHandler;
import event.local.HighlightMoveRange;

public class MoveMenuItem extends MenuItem {
	
	public MoveMenuItem(GameMenu parent) {
		super("Move", parent);		
	}
	
	/**
	 * executes the action that comes with this menuItem
	 * @return true if the action closes the menu, false if it should keep the menu open
	 */
	public boolean execute() {
		if (!GameClient.getInstance().canMove())
			return false;
		GameEventHandler.getInstance().dispatchEvent(new HighlightMoveRange(parent.getCharacter()));
		parent.getCharacter().initPath();
		GameClient.getInstance().setGameState(GameState.CHARACTER_MOVING);
		System.out.println(GameState.CHARACTER_MOVING);
		return true;
	}
}
