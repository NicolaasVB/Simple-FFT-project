package game.characters.classes;

import game.Projectile;
import game.characters.ChangeSprite;
import game.characters.GameCharacter;
import game.characters.HealthBar;

import java.io.File;
import java.util.Random;

import Application.CharacterConfiguration;

import map.tiles.MapTile;
import menu.GameMenu;
import menu.menuItems.AttackMenuItem;
import menu.menuItems.EndMenuItem;
import menu.menuItems.MoveMenuItem;
import sprite.Sprite;
import utils.ResourceCache;
import client.GameClient;

public class Archer extends GameCharacter {
	
	private static final int ATTACK_ANIMATION_DELAY = 800;
	
	private static final String TYPE = "archer";

	/**
	 * constructor. sucks that super() has to come first :/
	 * @param pos tile this archer is on
	 * @param team which team this character belongs to
	 */
	public Archer(MapTile pos, int team, int id) {
		super(new Sprite(ResourceCache.getInstance().getImage(team == 1 ? TYPE + "_" + TEAM_ONE + "_" + SPRITE_WALKING + "_west" : TYPE + "_" + TEAM_TWO + "_" + SPRITE_WALKING + "_west", (team == 1 ? getResourceLocation(TYPE) + TEAM_ONE + File.separatorChar : getResourceLocation(TYPE) + TEAM_TWO + File.separatorChar) + SPRITE_WALKING + "_west.gif")), pos, team, id);
		sprite.mirror(true);
		setStats();
		healthBar = new HealthBar(maxHealth);
	}
	
	protected void setStats() {
		moveRange = CharacterConfiguration.getInstance().getArcherMoveRange();
		attackRange = CharacterConfiguration.getInstance().getArcherAttackRange();
		maxHealth = CharacterConfiguration.getInstance().getArcherMaxHealth();
		health = maxHealth;
		attack = CharacterConfiguration.getInstance().getArcherAttack();
		defence = CharacterConfiguration.getInstance().getArcherDefense();
		evade = CharacterConfiguration.getInstance().getArcherEvade();
		block = CharacterConfiguration.getInstance().getArcherBlock();
	}
	
	@Override
	public String getCharType() {
		return TYPE;
	}
	
	public static void loadImages() {
		String green = getResourceLocation(TYPE) + TEAM_TWO + File.separatorChar;
		String red = getResourceLocation(TYPE) + TEAM_ONE + File.separatorChar;

		ResourceCache.getInstance().getImage(TYPE + "_" + TEAM_TWO + "_" + SPRITE_ATTACKING + "_west", green + SPRITE_ATTACKING + "_west.gif");
		ResourceCache.getInstance().getImage(TYPE + "_" + TEAM_TWO + "_" + SPRITE_ATTACKING + "_south", green + SPRITE_ATTACKING + "_south.gif");
		ResourceCache.getInstance().getImage(TYPE + "_" + TEAM_TWO + "_" + SPRITE_DEAD + "_south", green + SPRITE_DEAD + "_south.gif");
		ResourceCache.getInstance().getImage(TYPE + "_" + TEAM_TWO + "_" + SPRITE_DEAD + "_west", green + SPRITE_DEAD + "_west.gif");
		ResourceCache.getInstance().getImage(TYPE + "_" + TEAM_TWO + "_" + SPRITE_HIT + "_south", green + SPRITE_HIT + "_south.gif");
		ResourceCache.getInstance().getImage(TYPE + "_" + TEAM_TWO + "_" + SPRITE_HIT + "_west", green + SPRITE_HIT + "_west.gif");
		ResourceCache.getInstance().getImage(TYPE + "_" + TEAM_TWO + "_" + SPRITE_IDLE + "_south", green + SPRITE_IDLE + "_south.gif");
		ResourceCache.getInstance().getImage(TYPE + "_" + TEAM_TWO + "_" + SPRITE_IDLE + "_west", green + SPRITE_IDLE + "_west.gif");
		ResourceCache.getInstance().getImage(TYPE + "_" + TEAM_TWO + "_" + SPRITE_WALKING + "_south", green + SPRITE_WALKING + "_south.gif");
		ResourceCache.getInstance().getImage(TYPE + "_" + TEAM_TWO + "_" + SPRITE_WALKING + "_west", green + SPRITE_WALKING + "_west.gif");
		

		ResourceCache.getInstance().getImage(TYPE + "_" + TEAM_ONE + "_" + SPRITE_ATTACKING + "_west", red + SPRITE_ATTACKING + "_west.gif");
		ResourceCache.getInstance().getImage(TYPE + "_" + TEAM_ONE + "_" + SPRITE_ATTACKING + "_south", red + SPRITE_ATTACKING + "_south.gif");
		ResourceCache.getInstance().getImage(TYPE + "_" + TEAM_ONE + "_" + SPRITE_DEAD + "_south", red + SPRITE_DEAD + "_south.gif");
		ResourceCache.getInstance().getImage(TYPE + "_" + TEAM_ONE + "_" + SPRITE_DEAD + "_west", red + SPRITE_DEAD + "_west.gif");
		ResourceCache.getInstance().getImage(TYPE + "_" + TEAM_ONE + "_" + SPRITE_HIT + "_south", red + SPRITE_HIT + "_south.gif");
		ResourceCache.getInstance().getImage(TYPE + "_" + TEAM_ONE + "_" + SPRITE_HIT + "_west", red + SPRITE_HIT + "_west.gif");
		ResourceCache.getInstance().getImage(TYPE + "_" + TEAM_ONE + "_" + SPRITE_IDLE + "_south", red + SPRITE_IDLE + "_south.gif");
		ResourceCache.getInstance().getImage(TYPE + "_" + TEAM_ONE + "_" + SPRITE_IDLE + "_west", red + SPRITE_IDLE + "_west.gif");
		ResourceCache.getInstance().getImage(TYPE + "_" + TEAM_ONE + "_" + SPRITE_WALKING + "_south", red + SPRITE_WALKING + "_south.gif");
		ResourceCache.getInstance().getImage(TYPE + "_" + TEAM_ONE + "_" + SPRITE_WALKING + "_west", red + SPRITE_WALKING + "_west.gif");
	}
	
	public void attack(GameCharacter attackableCharacter, int damage, boolean entity, int type) {		
		if (entity && attackableCharacter != this && !attackableCharacter.isDead()) {
			setDirection(getTile(), attackableCharacter.getTile());
			new Thread(new ChangeSprite(this, ATTACK_ANIMATION_DELAY, SPRITE_ATTACKING)).start();
			GameClient.getInstance().addProjectile(this, attackableCharacter, Projectile.ARROW);
			
			attackableCharacter.updateHealth(damage);
		}
	}
	
	@Override
	public String toString() {
		return "Archer at location (" + getLocation().x + ", " + getLocation().y + ") playing for team " + getTeam();
	}

	@Override
	public GameMenu getMenu() {
		GameMenu menu = new GameMenu(this);
		menu.addMenuItem(new MoveMenuItem(menu));
		menu.addMenuItem(new AttackMenuItem(menu));
		menu.addMenuItem(new EndMenuItem(menu));
		return menu;
	}

	@Override
	public int calculateDamage() {
		return 8 + new Random().nextInt(5);
	}
}
