package menu.menuItems;

import menu.GameMenu;
import client.GameClient;
import client.GameClient.GameState;
import event.GameEventHandler;
import event.local.HighlightAttackRange;

public class FireBallMenuItem extends MenuItem {
	
	public FireBallMenuItem(GameMenu parent) {
		super("Fireball", parent);		
	}
	
	/**
	 * executes the action that comes with this menuItem
	 * @return true if the action closes the menu, false if it should keep the menu open
	 */
	public boolean execute() {
		GameEventHandler.getInstance().dispatchEvent(new HighlightAttackRange(parent.getCharacter()));		
		GameClient.getInstance().setAttackType(1);
		GameClient.getInstance().setGameState(GameState.CHARACTER_ATTACKING);
		System.out.println(GameState.CHARACTER_ATTACKING);
		return true;	
	}
}
