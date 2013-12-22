package Application;

import builder.MapEditor;
import game.characters.classes.Archer;
import game.characters.classes.Knight;
import game.characters.classes.Mage;
import menu.GameMenu;
import particles.Cloud;
import server.Server;
import client.ConfigurationWindow;
import client.LoginWindow;

public class Boot {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length > 0 && args[0].equals("server")) {
			ServerConfiguration.readConfigFile();
			CharacterConfiguration.getInstance().readConfigFile();
			Server.main(args);
		} else if(args.length > 0 && args[0].equals("builder")) {
			Configuration.readConfigFile();
			MapEditor.main(args);
		} else if(args.length > 0 && args[0].equals("configuration")) {
			Configuration.readConfigFile();
			ConfigurationWindow.getInstance().setVisible(true);
		} else {
			Configuration.readConfigFile();
			loadImages();
			LoginWindow.getInstance().setVisible(true);
		}
	}
	
	private static void loadImages() {
		Archer.loadImages();
		Knight.loadImages();
		Mage.loadImages();
		Cloud.loadImages();
		GameMenu.loadImages();
	}

}
