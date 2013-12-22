package map.tiles;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;

public class TileTexture {
	private ImageIcon image;
	
	private boolean mirror;
	
	public TileTexture(ImageIcon img) {
		this.image = img;
		this.mirror = false;
	}
	
	public void mirror(boolean mirror) {
		this.mirror = mirror;
	}
	
	public void setImage(ImageIcon img) {
		this.image = img;
	}
	
	public void draw(int x, int y, final Graphics g) {
		
		Graphics2D g2 = (Graphics2D) g;
		
		if (mirror) {
			// We mirror the x-ax, so a point that is on (100,100) will now appear at (-100,100)
			g2.scale(-1, 1);
			// Draw the image
			g2.drawImage(image.getImage(), -x - image.getIconWidth(), y, null);
			// We mirror again so the coordinates are back to normal : the inverse matrix, so 1 / -1 = -1
			g2.scale(-1, 1);
		} else {
			g2.drawImage(image.getImage(), x, y, null);
		}

	}
}
