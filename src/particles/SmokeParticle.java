package particles;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class SmokeParticle extends Particle {

	private BufferedImage[] deformedSmoke;
	
	public SmokeParticle(int startX, int startY, int endX, int endY, int lifetime, BufferedImage[] deformedSmoke) {
		super(startX, startY, endX, endY, lifetime, 0);
		this.deformedSmoke = deformedSmoke;
	}
	
	@Override
	public void draw(Graphics g, boolean fade) {
		g.drawImage(deformedSmoke[getBlurCount()], getX() - (deformedSmoke[getBlurCount()].getWidth() / 2) , getY() - (deformedSmoke[getBlurCount()].getHeight() / 2), null);
	}

}
