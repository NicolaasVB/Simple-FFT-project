package Application;

import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Configuration class. Here are all the constants, and the configuration file
 * gets read.
 * 
 * DONT FORGET TO READ THE CONFIG FILE OR NONE OF THE PROPERTIES WILL BE SET!!!!
 * 
 * @author Nicolaas
 * 
 */
public class Configuration {

	public static class Paths {
		public static class Resources {
			public static final String ROOT = "resources";
			public static final String TILES = ROOT + File.separatorChar + "tiles" + File.separatorChar;
			public static final String SPRITES = ROOT + File.separatorChar + "sprites" + File.separatorChar;
			public static final String MENU = ROOT + File.separatorChar + "menu" + File.separatorChar;
		}

		public static final String ROOT = new File(".").getAbsolutePath();

		public static String getConfigPath() {
			return Paths.ROOT + File.separator + "configuration.conf";
		}
	}

	// map settings
	private static int tileDiagonal;
	private static int tileHeight;
	private static Point drawStart;
	private static int standardFieldWidth;
	private static int standardFieldHeight;

	// gui settings
	private static int fps;

	// server settings
	private static String ipAddress;
	private static int port;
	private static String nickname;
	
	// other settings
	private static int characterMoveTime;
	private static boolean lessResources;

	public static int getTileDiagonal() {
		return tileDiagonal;
	}

	public static int getTileHeight() {
		return tileHeight;
	}

	public static Point getDrawStart() {
		return drawStart;
	}

	public static int getStandardFieldWidth() {
		return standardFieldWidth;
	}

	public static int getStandardFieldHeight() {
		return standardFieldHeight;
	}

	public static int getFPS() {
		return fps;
	}
	
	public static int getCharacterMoveTime() {
		return characterMoveTime;
	}
	
	public static String getIpAddress()
	{
		return ipAddress;
	}
	
	public static void setIpAddress(String ipAddress)
	{
		Configuration.ipAddress = ipAddress;
	}

	public static int getPort()
	{
		return port;
	}
	
	public static void setPort(int port)
	{
		Configuration.port = port;
	} 
	
	public static String getNickname()
	{
		return nickname;
	}
	
	public static void setNickname(String nickname)
	{
		Configuration.nickname = nickname;
	}
	
	public static boolean useLessResources() {
		return lessResources;
	}
	
	public static void setLuseResources(boolean lessResources)
	{
		Configuration.lessResources = lessResources;
	}

	/**
	 * @param fps the fps to set
	 */
	public static void setFps(int fps) {
		Configuration.fps = fps;
	}

	/**
	 * @param characterMoveTime the characterMoveTime to set
	 */
	public static void setCharacterMoveTime(int characterMoveTime) {
		Configuration.characterMoveTime = characterMoveTime;
	}

	private static void useDefaultSettings() {
		tileDiagonal = 65;
		tileHeight = 20;
		drawStart = new Point(300, 100);
		standardFieldWidth = 10;
		standardFieldHeight = 10;
		fps = 50;
		ipAddress = "localhost";
		port = 44444;
		characterMoveTime = 400;
		nickname = "";
		lessResources = false;
	}

	public static void readConfigFile() {
		Properties configFile = new Properties();
		try {
			configFile.load(new FileInputStream(Paths.getConfigPath()));
		} catch (FileNotFoundException e) {
			System.err.println("The configuration.conf file is missing! Using default settings.");
			useDefaultSettings();
			writeConfigFile();
			return;
		} catch (IOException e) {
			System.err.println("An IOException occured. " + e.getMessage());
			System.err.println("Using default settings.");
			useDefaultSettings();
			writeConfigFile();
			return;
		}

		try {
			tileDiagonal = Integer.parseInt(configFile
					.getProperty("tileDiagonal"));
			tileHeight = Integer.parseInt(configFile.getProperty("tileHeight"));
			int x = Integer.parseInt(configFile.getProperty("drawStart_x"));
			int y = Integer.parseInt(configFile.getProperty("drawStart_y"));
			drawStart = new Point(x, y);
			standardFieldWidth = Integer.parseInt(configFile
					.getProperty("standardFieldWidth"));
			standardFieldHeight = Integer.parseInt(configFile
					.getProperty("standardFieldHeight"));
			fps = Integer.parseInt(configFile.getProperty("fps"));
			characterMoveTime = Integer.parseInt(configFile.getProperty("characterMoveTime"));
			ipAddress = configFile.getProperty("ipAddress");
			port = Integer.parseInt(configFile.getProperty("port"));
			nickname = configFile.getProperty("nickname");
			lessResources = configFile.getProperty("less_resources").equalsIgnoreCase("1");
		} catch (Exception e) {
			System.err
					.println("Something went wrong while trying to read the config file. It got opened, but one of the properties couldn't be read.");
			System.err.println(e.getMessage());
			System.err.println("Using default settings.");
			useDefaultSettings();
			writeConfigFile();
		}
	}
	
	public static void writeConfigFile()
	{
		Properties configFile = new Properties();
		configFile.setProperty("tileDiagonal", "" + tileDiagonal);
		configFile.setProperty("tileHeight", "" + tileHeight);
		configFile.setProperty("drawStart_x", "" + (int) drawStart.getX());
		configFile.setProperty("drawStart_y", "" + (int) drawStart.getY());
		configFile.setProperty("standardFieldWidth", "" + standardFieldWidth);
		configFile.setProperty("standardFieldHeight", "" + standardFieldHeight);
		configFile.setProperty("fps", "" + fps);
		configFile.setProperty("characterMoveTime", "" + characterMoveTime);
		configFile.setProperty("ipAddress", ipAddress);
		configFile.setProperty("port", "" + port);
		configFile.setProperty("nickname", nickname);
		configFile.setProperty("less_resources", lessResources ? "1" : "0");
		
		try {
			configFile.store(new FileOutputStream(Paths.getConfigPath()), "");
		} catch (Exception e) {
			System.err.println("Something went wrong when trying to save the configfile");
			e.printStackTrace();
		}
	}

}
