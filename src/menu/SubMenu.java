package menu;

import javax.swing.ImageIcon;

import menu.menuItems.MenuItem;
import utils.ResourceCache;
import Application.Configuration;

public class SubMenu extends GameMenu {
	
	private GameMenu parent;
	
	public SubMenu(GameMenu parent, MenuItem item) {
		super(parent.getCharacter());
		this.parent = parent;
		parent.setSubMenu(this);
		setWidth();
		setHeight(item);
		isSubMenu = true;
	}
	
	private void setWidth() {
		ImageIcon middle = ResourceCache.getInstance().getImage("middle", Configuration.Paths.Resources.MENU + "menu_middle.png");
		xFactor = -middle.getIconWidth() - 30;
	}
	
	private void setHeight(MenuItem item) {
		yFactor = -50; 
		for (MenuItem parentItem : parent.menuItems) {
			if (parentItem == item)
				yFactor += 15;
		}
	}
	
}
