package menu;

import game.characters.GameCharacter;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import menu.menuItems.MenuItem;
import utils.ResourceCache;
import Application.Configuration;

public class GameMenu {
	
	private static final int DEFAULT_X_FACTOR = -300;
	private static final int DEFAULT_Y_FACTOR = -50;

	protected static final Color YELLOW = new Color(255, 215, 0);

	private boolean active;
	protected ArrayList<MenuItem> menuItems;
	private int selectedItemIndex;
	
	private GameCharacter referringCharacter;
	
	private SubMenu subMenu;
	
	protected int yFactor;
	protected int xFactor;
	protected boolean isSubMenu;
	

	public GameMenu(GameCharacter c) {
		this.referringCharacter = c;
		this.active = false;
		this.selectedItemIndex = 0;
		this.menuItems = new ArrayList<MenuItem>();
		this.subMenu = null;
		xFactor = DEFAULT_X_FACTOR;
		yFactor = DEFAULT_Y_FACTOR;
		isSubMenu = false;
	}
	
	public void setSubMenu(SubMenu sub) {
		this.subMenu = sub;
	}
	
	public static void loadImages() {
		ResourceCache.getInstance().getImage("pointer", Configuration.Paths.Resources.MENU + "menu_pointer.gif");
		ResourceCache.getInstance().getImage("top", Configuration.Paths.Resources.MENU + "menu_top.png");
		ResourceCache.getInstance().getImage("middle", Configuration.Paths.Resources.MENU + "menu_middle.png");
	}

	public boolean isActive() {
		return active;
	}
	
	public boolean hasSubMenu() {
		return subMenu != null;
	}
	
	public GameCharacter getCharacter() {
		return referringCharacter;
	}

	public void draw(Graphics g) {
		
		Graphics2D g2 = (Graphics2D) g;
		g.setColor(YELLOW);
		int x = g.getClipBounds().width + xFactor;
		int y = (g.getClipBounds().height / 4) + yFactor;
		ImageIcon top = ResourceCache.getInstance().getImage("top", Configuration.Paths.Resources.MENU + "menu_top.png");
		ImageIcon middle = ResourceCache.getInstance().getImage("middle", Configuration.Paths.Resources.MENU + "menu_middle.png");
		
		g.drawImage(top.getImage(), x , y , null);
		
		y += 3;
		for (MenuItem item : menuItems) 
		{
			g.drawImage(middle.getImage(), x , y , null);
			item.draw(x+5, y , g, isSubMenu);
			y+=15;
		}
		
		g2.scale(1, -1);
		g2.drawImage(top.getImage(), x , -y - top.getIconHeight() , null);
		g2.scale(1, -1); // scale back
		
		if (subMenu != null)
			subMenu.draw(g);
	}

	public void setActive(boolean isActive) {
		active = isActive;
		
	}

	public void selectNext() {
		if (subMenu != null) {
			subMenu.selectNext();
		} else {
			if (selectedItemIndex == menuItems.size() - 1)
				return;
			menuItems.get(selectedItemIndex).setSelected(false);
			selectedItemIndex++;
			menuItems.get(selectedItemIndex).setSelected(true);
		}
	}

	public void selectPrevious() {
		if (subMenu != null) {
			subMenu.selectPrevious();
		} else {
			if (selectedItemIndex == 0)
				return;
			menuItems.get(selectedItemIndex).setSelected(false);
			selectedItemIndex--;
			menuItems.get(selectedItemIndex).setSelected(true);
		}
	}

	public void addMenuItem(MenuItem item) {
		menuItems.add(item);
		menuItems.get(selectedItemIndex).setSelected(true);
		
	}

	public void setMenuItems(ArrayList<MenuItem> items) {
		menuItems = items;
		menuItems.get(selectedItemIndex).setSelected(true);
	}

	/**
	 * executes the selected menuItem
	 * @return true if the action ends the menu, false if the menu should stay open
	 */
	public boolean executeSelected() {
		if (subMenu != null)
			return subMenu.executeSelected();
		if (selectedItemIndex < 0 || selectedItemIndex >= menuItems.size())
			return false;
		return menuItems.get(selectedItemIndex).execute();
	}

}
