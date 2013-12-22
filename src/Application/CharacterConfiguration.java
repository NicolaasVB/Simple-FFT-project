package Application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;


public class CharacterConfiguration implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8429503211421862354L;

	public static class Paths {
		public static final String ROOT = new File(".").getAbsolutePath();

		public static String getConfigPath() {
			return Paths.ROOT + File.separator + "character.conf";
		}
	}
	
	private static CharacterConfiguration INSTANCE;
	
	private CharacterConfiguration(){}
	
	public static CharacterConfiguration getInstance()
	{
		if(INSTANCE == null)
			INSTANCE = new CharacterConfiguration();
		return INSTANCE;
	}

	private int mageMoveRange;
	private int mageAttackRange;
	private int mageMaxHealth;
	private int mageAttack;
	private int mageDefense;
	private int mageEvade;
	private int mageBlock;
	
	private int archerMoveRange;
	private int archerAttackRange;
	private int archerMaxHealth;
	private int archerAttack;
	private int archerDefense;
	private int archerEvade;
	private int archerBlock;

	private int knightMoveRange;
	private int knightAttackRange;
	private int knightMaxHealth;
	private int knightAttack;
	private int knightDefense;
	private int knightEvade;
	private int knightBlock;

	/**
	 * @return the mageMoveRange
	 */
	public int getMageMoveRange() {
		return mageMoveRange;
	}

	/**
	 * @param mageMoveRange the mageMoveRange to set
	 */
	public void setMageMoveRange(int mageMoveRange) {
		this.mageMoveRange = mageMoveRange;
	}

	/**
	 * @return the mageAttackRange
	 */
	public int getMageAttackRange() {
		return mageAttackRange;
	}

	/**
	 * @param mageAttackRange the mageAttackRange to set
	 */
	public void setMageAttackRange(int mageAttackRange) {
		this.mageAttackRange = mageAttackRange;
	}

	/**
	 * @return the mageMaxHealth
	 */
	public int getMageMaxHealth() {
		return mageMaxHealth;
	}

	/**
	 * @param mageMaxHealth the mageMaxHealth to set
	 */
	public void setMageMaxHealth(int mageMaxHealth) {
		this.mageMaxHealth = mageMaxHealth;
	}

	/**
	 * @return the mageAttack
	 */
	public int getMageAttack() {
		return mageAttack;
	}

	/**
	 * @param mageAttack the mageAttack to set
	 */
	public void setMageAttack(int mageAttack) {
		this.mageAttack = mageAttack;
	}

	/**
	 * @return the mageDefense
	 */
	public int getMageDefense() {
		return mageDefense;
	}

	/**
	 * @param mageDefense the mageDefense to set
	 */
	public void setMageDefense(int mageDefense) {
		this.mageDefense = mageDefense;
	}

	/**
	 * @return the mageEvade
	 */
	public int getMageEvade() {
		return mageEvade;
	}

	/**
	 * @param mageEvade the mageEvade to set
	 */
	public void setMageEvade(int mageEvade) {
		this.mageEvade = mageEvade;
	}

	/**
	 * @return the mageBlock
	 */
	public int getMageBlock() {
		return mageBlock;
	}

	/**
	 * @param mageBlock the mageBlock to set
	 */
	public void setMageBlock(int mageBlock) {
		this.mageBlock = mageBlock;
	}

	/**
	 * @return the archerMoveRange
	 */
	public int getArcherMoveRange() {
		return archerMoveRange;
	}

	/**
	 * @param archerMoveRange the archerMoveRange to set
	 */
	public void setArcherMoveRange(int archerMoveRange) {
		this.archerMoveRange = archerMoveRange;
	}

	/**
	 * @return the archerAttackRange
	 */
	public int getArcherAttackRange() {
		return archerAttackRange;
	}

	/**
	 * @param archerAttackRange the archerAttackRange to set
	 */
	public void setArcherAttackRange(int archerAttackRange) {
		this.archerAttackRange = archerAttackRange;
	}

	/**
	 * @return the archerMaxHealth
	 */
	public int getArcherMaxHealth() {
		return archerMaxHealth;
	}

	/**
	 * @param archerMaxHealth the archerMaxHealth to set
	 */
	public void setArcherMaxHealth(int archerMaxHealth) {
		this.archerMaxHealth = archerMaxHealth;
	}

	/**
	 * @return the archerAttack
	 */
	public int getArcherAttack() {
		return archerAttack;
	}

	/**
	 * @param archerAttack the archerAttack to set
	 */
	public void setArcherAttack(int archerAttack) {
		this.archerAttack = archerAttack;
	}

	/**
	 * @return the archerDefense
	 */
	public int getArcherDefense() {
		return archerDefense;
	}

	/**
	 * @param archerDefense the archerDefense to set
	 */
	public void setArcherDefense(int archerDefense) {
		this.archerDefense = archerDefense;
	}

	/**
	 * @return the archerEvade
	 */
	public int getArcherEvade() {
		return archerEvade;
	}

	/**
	 * @param archerEvade the archerEvade to set
	 */
	public void setArcherEvade(int archerEvade) {
		this.archerEvade = archerEvade;
	}

	/**
	 * @return the archerBlock
	 */
	public int getArcherBlock() {
		return archerBlock;
	}

	/**
	 * @param archerBlock the archerBlock to set
	 */
	public void setArcherBlock(int archerBlock) {
		this.archerBlock = archerBlock;
	}

	/**
	 * @return the knightMoveRange
	 */
	public int getKnightMoveRange() {
		return knightMoveRange;
	}

	/**
	 * @param knightMoveRange the knightMoveRange to set
	 */
	public void setKnightMoveRange(int knightMoveRange) {
		this.knightMoveRange = knightMoveRange;
	}

	/**
	 * @return the knightAttackRange
	 */
	public int getKnightAttackRange() {
		return knightAttackRange;
	}

	/**
	 * @param knightAttackRange the knightAttackRange to set
	 */
	public void setKnightAttackRange(int knightAttackRange) {
		this.knightAttackRange = knightAttackRange;
	}

	/**
	 * @return the knightMaxHealth
	 */
	public int getKnightMaxHealth() {
		return knightMaxHealth;
	}

	/**
	 * @param knightMaxHealth the knightMaxHealth to set
	 */
	public void setKnightMaxHealth(int knightMaxHealth) {
		this.knightMaxHealth = knightMaxHealth;
	}

	/**
	 * @return the knightAttack
	 */
	public int getKnightAttack() {
		return knightAttack;
	}

	/**
	 * @param knightAttack the knightAttack to set
	 */
	public void setKnightAttack(int knightAttack) {
		this.knightAttack = knightAttack;
	}

	/**
	 * @return the knightDefense
	 */
	public int getKnightDefense() {
		return knightDefense;
	}

	/**
	 * @param knightDefense the knightDefense to set
	 */
	public void setKnightDefense(int knightDefense) {
		this.knightDefense = knightDefense;
	}

	/**
	 * @return the knightEvade
	 */
	public int getKnightEvade() {
		return knightEvade;
	}

	/**
	 * @param knightEvade the knightEvade to set
	 */
	public void setKnightEvade(int knightEvade) {
		this.knightEvade = knightEvade;
	}

	/**
	 * @return the knightBlock
	 */
	public int getKnightBlock() {
		return knightBlock;
	}

	/**
	 * @param knightBlock the knightBlock to set
	 */
	public void setKnightBlock(int knightBlock) {
		this.knightBlock = knightBlock;
	}
	
	public void applySettings(CharacterConfiguration conf)
	{
		INSTANCE = conf;
	}

	private void useDefaultSettings()
	{
		 mageMoveRange = 4;
		 mageAttackRange = 3;
		 mageMaxHealth = 40;
		 mageAttack = 40;
		 mageDefense = 25;
		 mageEvade = 15;
		 mageBlock = 0;
		
		 archerMoveRange = 4;
		 archerAttackRange = 4;
		 archerMaxHealth = 35;
		 archerAttack = 40;
		 archerDefense = 25;
		 archerEvade = 15;
		 archerBlock = 0;

		 knightMoveRange = 5;
		 knightAttackRange = 1;
		 knightMaxHealth = 50;
		 knightAttack = 30;
		 knightDefense = 40;
		 knightEvade = 2;
		 knightBlock = 10;
	}
	
	public void readConfigFile() {
		Properties configFile = new Properties();
		try {
			configFile.load(new FileInputStream(Paths.getConfigPath()));
		} catch (FileNotFoundException e) {
			System.err.println("The character.conf file is missing! Using default settings.");
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
			 mageMoveRange = Integer.parseInt(configFile.getProperty("mageMoveRange"));
			 mageAttackRange = Integer.parseInt(configFile.getProperty("mageAttackRange"));
			 mageMaxHealth = Integer.parseInt(configFile.getProperty("mageMaxHealth"));
			 mageAttack = Integer.parseInt(configFile.getProperty("mageAttack"));
			 mageDefense = Integer.parseInt(configFile.getProperty("mageDefense"));
			 mageEvade = Integer.parseInt(configFile.getProperty("mageEvade"));
			 mageBlock = Integer.parseInt(configFile.getProperty("mageBlock"));
			
			 archerMoveRange = Integer.parseInt(configFile.getProperty("archerMoveRange"));
			 archerAttackRange = Integer.parseInt(configFile.getProperty("archerAttackRange"));
			 archerMaxHealth = Integer.parseInt(configFile.getProperty("archerMaxHealth"));
			 archerAttack = Integer.parseInt(configFile.getProperty("archerAttack"));
			 archerDefense = Integer.parseInt(configFile.getProperty("archerDefense"));
			 archerEvade = Integer.parseInt(configFile.getProperty("archerEvade"));
			 archerBlock = Integer.parseInt(configFile.getProperty("archerBlock"));

			 knightMoveRange = Integer.parseInt(configFile.getProperty("knightMoveRange"));
			 knightAttackRange = Integer.parseInt(configFile.getProperty("knightAttackRange"));
			 knightMaxHealth = Integer.parseInt(configFile.getProperty("knightMaxHealth"));
			 knightAttack = Integer.parseInt(configFile.getProperty("knightMaxHealth"));
			 knightDefense = Integer.parseInt(configFile.getProperty("knightDefense"));
			 knightEvade = Integer.parseInt(configFile.getProperty("knightEvade"));
			 knightBlock = Integer.parseInt(configFile.getProperty("knightBlock"));
		} catch (Exception e) {
			System.err
					.println("Something went wrong while trying to read the config file. It got opened, but one of the properties couldn't be read.");
			System.err.println(e.getMessage());
			System.err.println("Using default settings.");
			useDefaultSettings();
			writeConfigFile();
		}
	}
	
	public void writeConfigFile()
	{
		Properties configFile = new Properties();
		configFile.setProperty("mageMoveRange", "" + mageMoveRange);
		configFile.setProperty("mageAttackRange", "" + mageAttackRange);
		configFile.setProperty("mageMaxHealth", "" + mageMaxHealth);
		configFile.setProperty("mageAttack", "" + mageAttack);
		configFile.setProperty("mageDefense", "" + mageDefense);
		configFile.setProperty("mageEvade", "" + mageEvade);
		configFile.setProperty("mageBlock", "" + mageBlock);

		configFile.setProperty("archerMoveRange", "" + archerMoveRange);
		configFile.setProperty("archerAttackRange", "" + archerAttackRange);
		configFile.setProperty("archerMaxHealth", "" + archerMaxHealth);
		configFile.setProperty("archerAttack", "" + archerAttack);
		configFile.setProperty("archerDefense", "" + archerDefense);
		configFile.setProperty("archerEvade", "" + archerEvade);
		configFile.setProperty("archerBlock", "" + archerBlock);

		configFile.setProperty("knightMoveRange", "" + knightMoveRange);
		configFile.setProperty("knightAttackRange", "" + knightAttackRange);
		configFile.setProperty("knightMaxHealth", "" + knightMaxHealth);
		configFile.setProperty("knightAttack", "" + knightAttack);
		configFile.setProperty("knightDefense", "" + knightDefense);
		configFile.setProperty("knightEvade", "" + knightEvade);
		configFile.setProperty("knightBlock", "" + knightBlock);
		
		try {
			configFile.store(new FileOutputStream(Paths.getConfigPath()), "");
		} catch (Exception e) {
			System.err.println("Something went wrong when trying to save the configfile");
			e.printStackTrace();
		}
	}
}
