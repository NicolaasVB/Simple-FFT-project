package particles;

import java.awt.Color;
import java.awt.Graphics;

class Particle {
	private int startX, startY, endX, endY;
	
	private long beginTime, lifetime;
	
	protected int RGBColor;
	
	/**
	 * constructor for a Particle. Notice we don't use a CustomTimer. We do this so we have less overhead.
	 * @param startX the origin on the X ax
	 * @param startY the origin on the Y ax
	 * @param endX the destination on the X ax
	 * @param endY the destination on the Y ax
	 * @param lifetime the time it takes for the particle to fade
	 * @param RGB the color
	 */
	public Particle(int startX, int startY, int endX, int endY, int lifetime, int RGB) {
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		this.beginTime = System.currentTimeMillis();
		this.lifetime = lifetime;
		this.RGBColor = RGB;
	}
	
	public int getX() {
		double elapsedPercentage = (double)(System.currentTimeMillis() - beginTime) / this.lifetime;
		return (int) (elapsedPercentage * (this.endX - this.startX) + this.startX);
	}
	
	public int getY() {
		double elapsedPercentage = (double)(System.currentTimeMillis() - beginTime) / this.lifetime;
		return (int) (elapsedPercentage * (this.endY - this.startY) + this.startY);	
	} 
	
/*	public int getColor(boolean fade) {
		if (!fade)
			return this.RGBColor;
		float elapsedPercentage = (float)(System.currentTimeMillis() - beginTime) / this.lifetime;
		int alpha = (int) ((1 - elapsedPercentage) * 255);
		return (0x00FFFFFF | (alpha << 24)) & this.RGBColor;
	} */
	
	private Color getColor(boolean fade) {
		if (!fade)
			return new Color(RGBColor);
		float elapsedPercentage = (float)(System.currentTimeMillis() - beginTime) / lifetime;
		int alpha = (int) ((1 - elapsedPercentage) * 255);
		return new Color((0x00FFFFFF | (alpha << 24)) & RGBColor);
	}
	
	public boolean isFinished() {
		return System.currentTimeMillis() - this.beginTime >= this.lifetime;
	}
	
	public int getBlurCount() {
		float elapsedPercentage = (float)(System.currentTimeMillis() - beginTime) / this.lifetime;
		if (elapsedPercentage < 0.2)
			return 0;
		if (elapsedPercentage < 0.4)
			return 1;
		if (elapsedPercentage < 0.4)
			return 2;
		if (elapsedPercentage < 0.4)
			return 3;
		return 4;
	}
	
	public void draw(Graphics g, boolean fade) {
		g.setColor(getColor(fade));
		g.drawLine(getX(), getY(), getX(), getY());
	}
	
}
