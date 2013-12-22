package game.characters;

import java.awt.Color;
import java.awt.Graphics;

public class HealthBar {
	private int maxHealth;
	
	private static final Color GRAY = new Color(139, 136, 120);
	private static final Color GREEN = new Color(50, 205, 50);
	private static final Color RED = new Color(178, 34, 34);
	private static final int BOXHEIGHT = 8;
	private static final int BARHEIGHT = 6;
	private static final int MARGIN = 1;
	private static final int HEIGHT_BAR = 30;
	private static final int CENTER = 20;
	
	public HealthBar(int maxHealth) {
		this.maxHealth = maxHealth;
	}
	
	public void draw (int x, int y, int health, Graphics g) {
		y -= HEIGHT_BAR;
		x -= CENTER;
		g.setColor(GRAY);
		g.fillRect(x, y, maxHealth + 2 * MARGIN, BOXHEIGHT);
		x += MARGIN;
		y += MARGIN;
		g.setColor(RED);
		g.fillRect(x, y, maxHealth, BARHEIGHT);
		g.setColor(GREEN);
		g.fillRect(x, y, health, BARHEIGHT);	
	}

}
