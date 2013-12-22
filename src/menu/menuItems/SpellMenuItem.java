package menu.menuItems;

import client.GameClient;
import menu.GameMenu;
import menu.SubMenu;

public class SpellMenuItem extends MenuItem {
	
	public SpellMenuItem(GameMenu parent) {
		super("Spell", parent);		
	}

	/**
	 * executes the action that comes with this menuItem
	 * @return true if the action closes the menu, false if it should keep the menu open
	 */
	public boolean execute() {
		if (!GameClient.getInstance().canAttack())
			return false;
		SubMenu sub = new SubMenu(parent, this);
		sub.addMenuItem(new FireBallMenuItem(sub));
		sub.addMenuItem(new IceMenuItem(sub));
		return false;
	}
}
