package sprite;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;

import Application.Configuration;

public class Sprite {

	private static final int OFFSET = 4;
	// composites
	private static final AlphaComposite drawHiddenComposite = AlphaComposite
			.getInstance(AlphaComposite.SRC_OVER, 0.2f);
	private static final AlphaComposite drawNormalComposite = AlphaComposite
			.getInstance(AlphaComposite.SRC_OVER, 1.0f);

	private ImageIcon image;
	
	private boolean mirror;

	public Sprite(ImageIcon img) {
		this.image = img;
		this.mirror = false;
	}
	
	public void mirror(boolean mirror) {
		this.mirror = mirror;
	}
	
	public void setImage(ImageIcon img) {
		this.image = img;
	}

	/**
	 * Draws a sprite on our panel
	 * 
	 * @param x
	 *            x coordinate of north west corner of the tile
	 * @param y
	 *            y coordinate of north west corner of the tile
	 * @param g
	 *            the graphix context we're drawing on
	 */
	public void draw(int x, int y, final Graphics g, boolean transparant) {
		// We center the image
		// Considering the x coordinate is already at the middle of our tile, we
		// don't have to do much about his
		int drawX = x - image.getIconWidth() / 2;
		// We first make sure our base point is the exact middle point of the
		// tile
		// Then we add a couple pixels, so it looks more natural.
		int drawY = y + (Configuration.getTileDiagonal() / 4) + OFFSET - image.getIconHeight();
		
		Graphics2D g2 = (Graphics2D) g;
		
		if (transparant)
			g2.setComposite(drawHiddenComposite);
		
		if (mirror) {
			// We mirror the x-ax, so a point that is on (100,100) will now appear at (-100,100)
			g2.scale(-1, 1);
			// Draw the image
			g2.drawImage(image.getImage(), -drawX - image.getIconWidth(), drawY, null);
			// We mirror again so the coordinates are back to normal : the inverse matrix, so 1 / -1 = -1
			g2.scale(-1, 1);
		} else {
			g2.drawImage(image.getImage(), drawX, drawY, null);
		}

		g2.setComposite(drawNormalComposite);
	}

	public void flush() {
		image.getImage().flush();
	}
}
