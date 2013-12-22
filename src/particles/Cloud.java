package particles;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import particles.math.GaussianFilter;
import utils.ResourceCache;
import Application.Configuration;


public class Cloud {
	
	private static final int AMOUNT_GAUSSIAN_BLURFILTERS = 5;
	private static final int GAUSSIAN_INTERVALS = 10;
	private static final int GAUSSIAN_INTERVAL_ADDITION = 5;
	
	private int width, height;
	
	private static Random random = new Random();
	
	private List<Particle> list;
	
	
	private ParticleBehaviour behaviour;
	
	@SuppressWarnings("unused")
	private BufferedImage smoke; 
	@SuppressWarnings("unused")
	private BufferedImage smoke2; 
	private BufferedImage[] deformedSmoke;
	private BufferedImage[] deformedSmoke2;
	private BufferedImage mask;
	private GaussianFilter gauss;
	
	public Cloud(int width, int height, ParticleBehaviour behaviour) {
		this.width = width;
		this.height = height;
		this.behaviour = behaviour;
		this.list = new ArrayList<Particle>();
		
		if (behaviour.isSmoke())
			initSmoke();
	}
	
	public static void loadImages() {
		ResourceCache.getInstance().getBufferedImageCopy("smoke", Configuration.Paths.Resources.ROOT + File.separatorChar + "smoke_noise.png");
	}
	
	private void initSmoke() {
		BufferedImage smoke = ResourceCache.getInstance().getBufferedImageCopy("smoke", Configuration.Paths.Resources.ROOT + File.separatorChar + "smoke_noise.png");
		BufferedImage smoke2 = ResourceCache.getInstance().getBufferedImageCopy("smoke", Configuration.Paths.Resources.ROOT + File.separatorChar + "smoke_noise.png");
		setColor(smoke, behaviour.getFirstInner());
		if (behaviour.getSecondInner() != null)
			setColor(smoke, behaviour.getSecondInner());
		setColor(smoke2, behaviour.getFirstOuter());
		if (behaviour.getSecondOuter() != null)
			setColor(smoke2, behaviour.getSecondOuter());
		int w = smoke.getWidth();
		int h = smoke.getHeight();
		mask = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		gauss = new GaussianFilter();
		deformedSmoke = new BufferedImage[AMOUNT_GAUSSIAN_BLURFILTERS];
		deformedSmoke2 = new BufferedImage[AMOUNT_GAUSSIAN_BLURFILTERS];
		for (int i = 0; i < AMOUNT_GAUSSIAN_BLURFILTERS; i++) {
			Graphics2D g2D = mask.createGraphics();
			g2D.setColor(Color.BLACK);
			g2D.fillOval(smoke.getWidth() / 2, smoke.getHeight() / 2, behaviour.getRadius(), behaviour.getRadius());
			gauss.setRadius((GAUSSIAN_INTERVALS * i) + GAUSSIAN_INTERVAL_ADDITION);
			gauss.filter(mask, mask);
			deformedSmoke[i] = ApplyTransparency(smoke, mask);
			deformedSmoke2[i] = ApplyTransparency(smoke2, mask);
			g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f));
			g2D.fillRect(0, 0, w, h);
			g2D.dispose(); 
		}
	}
	
	private static void setColor(BufferedImage image, Color color) {
		for(int i = 0; i < image.getHeight(); i++) {  
			for(int j = 0; j < image.getWidth(); j++) {
				Color c = new Color(image.getRGB(j, i));
				int a = c.getAlpha();
				if (a > 0) {
					int r = (c.getRed() + color.getRed()) / 2;
					int g = (c.getGreen() + color.getGreen()) / 2;
					int b = (c.getBlue() + color.getBlue()) / 2;
					image.setRGB(j, i, getRGB(a, r, g, b));
				}
	        }  
	    }  
	}
	
	private static BufferedImage ApplyTransparency(BufferedImage image, Image mask) {
	    BufferedImage dest = new BufferedImage(
	            image.getWidth(), image.getHeight(),
	            BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2 = dest.createGraphics();
	    g2.drawImage(image, 0, 0, null);
	    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_IN, 1.0F));
	    g2.drawImage(mask, 0, 0, null);
	    g2.dispose();
	    return dest; 
	}
	
	private static int getRGB(int a, int r, int g, int b) {
		return ((a & 0xFF) << 24) |
				((r & 0xFF) << 16) |
				((g & 0xFF) << 8)  |
				((b & 0xFF) << 0);
	}
	
	public void draw(Graphics g) {
		for (Particle p : list) {
			if (!p.isFinished())
				p.draw(g, behaviour.fade());
		}
	}

	public void addParticles(int startX, int startY, int sdInner, int sdOuter, int liftetime) {
		ArrayList<Particle> temp = new ArrayList<Particle>();
		for (Particle p : list) {
    		if (!p.isFinished())
				temp.add(p);
    	}
		list = temp;
		for (int i = 0; i < behaviour.getAmount(); i++) {
			if (behaviour.isSmoke()) {
				list.add(new SmokeParticle(startX, startY, random(0, this.width, startX, sdInner), random(0, this.height, startY, sdInner), random.nextInt(liftetime), deformedSmoke));
				list.add(new SmokeParticle(startX, startY, random(0, this.width, startX, sdOuter), random(0, this.height, startY, sdOuter), random.nextInt(liftetime), deformedSmoke2));
			} else {
				list.add(new Particle(startX, startY, random(0, this.width, startX, sdInner), random(0, this.height, startY, sdInner), random.nextInt(liftetime), behaviour.getFirstInner().getRGB()));
				list.add(new Particle(startX, startY, random(0, this.width, startX, sdOuter), random(0, this.height, startY, sdOuter), random.nextInt(liftetime), behaviour.getFirstOuter().getRGB()));
			}
		}
	}
	
    /**
	 * Returns a normally distributed pseudorandom integer with a provided
	 * standard deviation about a provided mean.
	 *
	 * @param min  The inclusive lower bound.
	 * @param max  The exclusive upper bound.
	 * @param mean The mean (>= min and < max).
	 * @param sd   The standard deviation. A higher value will increase the
	 *             probability of numbers further from the mean being returned.
	 * @return Random integer min <= n < max from the normal distribution
	 *         described by the parameters.
	 */
	private int random(final int min, final int max, final int mean, final int sd) {
		int rand;
		do {
			rand = (int) (random.nextGaussian() * sd + mean);
		} while (rand < min || rand >= max);
		return rand;
	}

	public boolean isAlive() {
		if (list.size() == 0)
			return true;
		for (Particle p : list) {
			if (!p.isFinished())
				return true;
		}
		return false;
	}

}