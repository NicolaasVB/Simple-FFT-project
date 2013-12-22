package menu.menuItems;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;

import menu.GameMenu;
import utils.ResourceCache;
import Application.Configuration;

public abstract class MenuItem {

	protected static final Font FONT = new Font("Verdana", Font.BOLD, 15);
	
	private static final int VERTICAL_MARGIN = 12;
	private static final int HORIZONTAL_MARGIN = 30;
	private static final int HORIZONTAL_SCALED_MARGIN = 5;

	protected GameMenu parent;

	protected String name;

	protected boolean selected;

	public MenuItem(String name, GameMenu parent) {
		this.name = name;
		this.selected = false;
		this.parent = parent;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isSelected() {
		return selected;
	}

	/**
	 * executes the action that comes with this menuItem
	 * @return true if the action closes the menu, false if it should keep the menu open
	 */
	public abstract boolean execute();

	public void draw(int x, int y, Graphics g, boolean sub) {
		g.drawString(name, x, y + VERTICAL_MARGIN);
		if (isSelected()) {
			ImageIcon pointer = ResourceCache.getInstance().getImage("pointer", Configuration.Paths.Resources.MENU + "menu_pointer.gif");
			if (sub) {
				// we do this to get the width of the image
				ImageIcon middle = ResourceCache.getInstance().getImage("middle", Configuration.Paths.Resources.MENU + "menu_middle.png");
				Graphics2D g2 = (Graphics2D) g;
				g2.scale(-1, 1);
				g.drawImage(pointer.getImage(), - (x + middle.getIconWidth()) - HORIZONTAL_MARGIN + HORIZONTAL_SCALED_MARGIN, y, null);
				g2.scale(-1, 1);
			} else {
				g.drawImage(pointer.getImage(), x - HORIZONTAL_MARGIN, y , null);
			}
		}
	}

}
