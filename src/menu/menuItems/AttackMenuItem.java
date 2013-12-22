package menu.menuItems;

import menu.GameMenu;
import client.GameClient;
import client.GameClient.GameState;
import event.GameEventHandler;
import event.local.HighlightAttackRange;

public class AttackMenuItem extends MenuItem {
	
	public AttackMenuItem(GameMenu parent) {
		super("Attack", parent);		
	}
	
	/**
	 * executes the action that comes with this menuItem
	 * @return true if the action closes the menu, false if it should keep the menu open
	 */
	public boolean execute() {
		if (!GameClient.getInstance().canAttack())
			return false;
		GameEventHandler.getInstance().dispatchEvent(new HighlightAttackRange(parent.getCharacter()));		
		GameClient.getInstance().setAttackType(0);
		GameClient.getInstance().setGameState(GameState.CHARACTER_ATTACKING);
		System.out.println(GameState.CHARACTER_ATTACKING);
		return true;
	}
}
