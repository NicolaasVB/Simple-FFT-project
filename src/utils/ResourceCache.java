package utils;

import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ResourceCache {
	private static final ResourceCache INSTANCE = new ResourceCache();
	
	// in-memory image collection
	private Hashtable<String, ImageIcon> resources;
	private Hashtable<String, BufferedImage> bufferedImages;
	
	// Path for access from within a jar is a bit different.
	private static boolean RUNNING_FROM_JAR;
	
	/**
	 * private constructor creates instance of ResourceCache
	 */
	private ResourceCache() {
		resources = new Hashtable<String, ImageIcon>();
		bufferedImages = new Hashtable<String, BufferedImage>();
		RUNNING_FROM_JAR = ResourceCache.class.getResource("ResourceCache.class").toString().startsWith("jar:");
	}
	
	/**
	 * we don't use a synchronised method because it is up to 100 times slower...
	 * @return the resource cache singleton
	 */
	public static ResourceCache getInstance() {
		return INSTANCE;
	}
	
	/**
	 * gets the image resource by name, and will try to find it if it isn't in the hashtable
	 * @param name the name of the image to look for
	 * @param path the pathname of the image in case it wasnt already loaded
	 * @return the loaded image or null
	 */
	public ImageIcon getImage(String name, String path) {
		ImageIcon img = null;
		// try to load from memory
		img = (ImageIcon)resources.get(name);
		// if not in memory, we try to load it
		if (img == null) {
			img = RUNNING_FROM_JAR ? new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource(path.replace('\\', '/')))) : new ImageIcon(path);
			// keep track of loaded image
			resources.put(name, img);
		}
		return img;
	}
	
	/**
	 *  returns a deep copy of a bufferedImage
	 * @param name name of the image
	 * @param path where we can find the image
	 * @return a copied version of the image, so any changes wont reflect upon the original copy
	 */
	public BufferedImage getBufferedImageCopy(String name, String path) {
		BufferedImage img = null;
		// try to load from memory
		img = (BufferedImage)bufferedImages.get(name);
		// if not in memory, we try to load it
		if (img == null) {
			try {
				img = RUNNING_FROM_JAR ? ImageIO.read(getClass().getClassLoader().getResource(path.replace('\\', '/'))) : ImageIO.read(new File(path));
				// keep track of loaded image
				bufferedImages.put(name, img);
			} catch (Exception e) {
			}
		}
		return deepCopy(img);
	}
	
	/**
	 * clears the resource cache
	 */
	public void clearResources() {
		resources.clear();
	}
	
	private static BufferedImage deepCopy(BufferedImage bi) {
		if (bi == null)
			return null;
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}
}
