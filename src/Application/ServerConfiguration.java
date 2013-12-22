package Application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ServerConfiguration {

	public static class Paths {
		public static final String ROOT = new File(".").getAbsolutePath();

		public static String getConfigPath() {
			return Paths.ROOT + File.separator + "server.conf";
		}
	}
	
	private static int port;

	public static int getPort() {
		return port;
	}

	public static void setPort(int port) {
		ServerConfiguration.port = port;
	}
	
	private static void useDefaultSettings()
	{
		setPort(44444);
	}
	
	public static void readConfigFile() {
		Properties configFile = new Properties();
		try {
			configFile.load(new FileInputStream(Paths.getConfigPath()));
		} catch (FileNotFoundException e) {
			System.err.println("The server.conf file is missing! Using default settings.");
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
			setPort(Integer.parseInt(configFile.getProperty("port")));
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
		configFile.setProperty("port", "" + port);
		
		try {
			configFile.store(new FileOutputStream(Paths.getConfigPath()), "");
		} catch (Exception e) {
			System.err.println("Something went wrong when trying to save the configfile");
			e.printStackTrace();
		}
	}
}
