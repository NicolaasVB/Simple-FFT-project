package menu;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

import client.GameClient.GameState;

public class StatusWindow {
	
	private static final int TOP = 5;
	private static final int MARGIN = 5;
	private static final int BOTTOM = 35; // heigh = 30
	
	private static final int ARC_SIZE = 5;
	
	private static final Color WINDOW_COLOR = new Color(0, 150, 0, 150);
	
	FontMetrics fm;
	
	public StatusWindow() {
	}
	
	private String getMessage(GameState state) {
		switch (state) {
		case IDLE:
			return "Other player's turn.";
		case TURN: 
			return "Your turn!";
		case ADDING_CHARACTER:
			return "Add your characters. K or 1 for knight, A or 2 for acher, M or 3 for mage. ";
		case BROWSING_MENU:
			return "Select an option. (press esc to cancel)";
		case CHARACTER_MOVING:
			return "Select the tile you want to move your character to.";
		case CHARACTER_ATTACKING:
			return "Select a character you want to attack.";
		default:
			return "";
		}
	}
	
	public void draw(Graphics g, int width, GameState state) {
		String message = getMessage(state);
		if (fm == null)
		      fm = g.getFontMetrics();
		int sw = fm.stringWidth(message);
		int baseline = (TOP + ((BOTTOM + 1 - TOP) / 2) - ((fm.getAscent() + fm.getDescent()) / 2) + fm.getAscent());
		g.setColor(WINDOW_COLOR);
		g.fillRoundRect(MARGIN, TOP, width - (2 * MARGIN), BOTTOM - TOP, ARC_SIZE, ARC_SIZE);
		g.setColor(Color.BLACK);
		g.drawRoundRect(MARGIN, TOP, width - (2 * MARGIN), BOTTOM - TOP, ARC_SIZE, ARC_SIZE);
		int centerH = MARGIN + (width / 2);
		g.drawString(message, centerH - (sw / 2), baseline);
		
	}

}
