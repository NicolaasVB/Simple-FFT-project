package game.characters;

import game.Path;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.Vector;

import map.GameEntity;
import map.Map;
import map.tiles.MapTile;
import menu.GameMenu;
import sprite.Sprite;
import utils.CustomTimer;
import utils.ResourceCache;
import Application.Configuration;
import client.GameClient;

/**
 * 
 * @author Nicolaas
 * 
 */
public abstract class GameCharacter extends GameEntity {
	
	protected enum Direction { NORTH, EAST, SOUTH, WEST, INVALID; }
	public enum CharacterType {ARCHER, KNIGHT, MAGE }
	protected static final String SPRITE_WALKING = "walking";
	protected static final String SPRITE_ATTACKING = "attacking";
	protected static final String SPRITE_HIT = "hit";
	protected static final String SPRITE_DEAD = "dead";
	protected static final String SPRITE_IDLE = "idle";
	
	protected static final String TEAM_ONE = "red";
	protected static final String TEAM_TWO = "green";
	
	private static final Font FONT = new Font("Rockwell Extra Bold", Font.PLAIN, 12);
	private static final int DAMAGE_LENGTH_SHOWING = 1500;
	private static final String MISS_MESSAGE = "miss";
	private static final String EVADE_MESSAGE = "evade";
	private static final String BLOCK_MESSAGE = "block";
	
	private static final int HIT_ANIMATION_DELAY = 900;
	
	private Direction lookingDirection;
	
	protected int health;
	
	private CustomTimer moveTimer;
	private CustomTimer damageTimer;
	private int damage;

	private Vector<MapTile> moveLocations;
	
	private Vector<MapTile> attackLocations;

	private MapTile previousTile; // this is for characters moving inbetween
									// tiles

	protected int moveRange;
	
	protected int attackRange;
	
	protected int attack = 100;
	protected int defence = 100;
	protected int evade = 10;
	protected int block = 10;
	
	private boolean selected;
	
	private int team; // 1 and 2
	private boolean swap;

	
	private Path path;
	
	private int id;
	
	private boolean isDead;
	
	private boolean damageMissed;
	private boolean damageEvaded;
	private boolean damageBlocked;
	protected HealthBar healthBar;
	protected int maxHealth;
	
	
	private boolean showHealth;

	public GameCharacter(Sprite s, MapTile pos, int team, int id) {
		super(s, pos);
		this.team = team;
		moveTimer = new CustomTimer(Configuration.getCharacterMoveTime());
		damageTimer = new CustomTimer(DAMAGE_LENGTH_SHOWING);
		lookingDirection = Direction.NORTH;
		selected = false;
		this.id = id;
		swap = false;
		isDead = false;
		damage = -1;
		damageMissed = false;
		damageEvaded = false;
		damageBlocked = false;
		showHealth = false;
	}
	
	public void setShowHealth(boolean showHealth)
	{
		this.showHealth = showHealth;
	}
	
	public void setDamageProperties(boolean miss, boolean evade, boolean block) {
		damageMissed = miss;
		damageEvaded = evade;
		damageBlocked = block;
	}
	
	public abstract int calculateDamage();
	
	/**
	 * gets the team this character is playing for
	 * @return 1 or 2
	 */
	public int getTeam() {
		return team;
	}
	
	public int getID() {
		return id;
	}
	
	public Path getPath() {
		return path;
	}
	
	protected abstract void setStats();
	
	public abstract String getCharType();
	
	/**
	 * @return the attack
	 */
	public int getAttack() {
		return attack;
	}

	/**
	 * @param attack the attack to set
	 */
	public void setAttack(int attack) {
		this.attack = attack;
	}

	/**
	 * @return the defence
	 */
	public int getDefence() {
		return defence;
	}

	/**
	 * @param defence the defence to set
	 */
	public void setDefence(int defence) {
		this.defence = defence;
	}

	/**
	 * @return the evade
	 */
	public int getEvade() {
		return evade;
	}

	/**
	 * @param evade the evade to set
	 */
	public void setEvade(int evade) {
		this.evade = evade;
	}

	/**
	 * @return the block
	 */
	public int getBlock() {
		return block;
	}

	/**
	 * @param block the block to set
	 */
	public void setBlock(int block) {
		this.block = block;
	}

	public void initPath() {
		path = new Path(new ArrayList<MapTile>(), this, GameClient.getInstance().getMap());
		path.addTile(getTile());
	}
	
	public void removePath() {
		path = null;
		moveLocations = null;
		attackLocations = null;
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public Direction getLookingDirection() {
		return lookingDirection;
	}
	
	public abstract GameMenu getMenu();
	
	/**
	 * Gives the direction a tile is in compared to the original tile
	 * @param orig the tile we're comparing with
	 * @param dest the tile destination tile
	 * @return the direction or invalid
	 */
	protected void setDirection(MapTile orig, MapTile dest) {
		if (orig.getFieldLocation().distance(dest.getFieldLocation()) != 1.0)
			lookingDirection = Direction.INVALID;
		int xOrig = orig.getFieldLocation().x;
		int yOrig = orig.getFieldLocation().y;
		int xDest = dest.getFieldLocation().x;
		int yDest = dest.getFieldLocation().y;
		if ((yDest - yOrig) >= (xOrig - xDest) && (yDest - yOrig) >= (xDest - xOrig))
			lookingDirection = Direction.NORTH;
		else if ((yOrig - yDest) >= (xOrig - xDest) && (yOrig - yDest) >= (xDest - xOrig))
			lookingDirection = Direction.SOUTH;
		else if ((xDest - xOrig) >= (yOrig - yDest) && (xDest - xOrig) >= (yDest - yOrig))
			lookingDirection = Direction.EAST;
		else 
			lookingDirection = Direction.WEST;
	}
	
	protected static String getResourceLocation(String charType) {
		return Configuration.Paths.Resources.SPRITES + "characters" + File.separatorChar + charType + File.separatorChar;
	} 
	
	protected void setSprite(String spriteType) {
		String path = getTeam() == 1 ? getResourceLocation(getCharType()) + "red" + File.separatorChar : getResourceLocation(getCharType()) + "green" + File.separatorChar;
		String imagePreFix = getTeam() == 1 ? getCharType() + "_red_" : getCharType() + "_green_";
		switch (getLookingDirection()) {
		case NORTH:
			sprite.setImage(ResourceCache.getInstance().getImage(imagePreFix + spriteType + "_west", path + spriteType + "_west.gif"));
			sprite.mirror(true);
			break;
		case EAST:
			sprite.setImage(ResourceCache.getInstance().getImage(imagePreFix + spriteType + "_south", path + spriteType + "_south.gif"));
			sprite.mirror(true);
			break;
		case SOUTH:
			sprite.setImage(ResourceCache.getInstance().getImage(imagePreFix + spriteType + "_south", path + spriteType + "_south.gif"));
			sprite.mirror(false);
			break;
		case WEST:
			sprite.setImage(ResourceCache.getInstance().getImage(imagePreFix + spriteType + "_west", path + spriteType + "_west.gif"));
			sprite.mirror(false);
			break;
		default:
			break;
		}
	}

	/**
	 * Method to gather the tiles that this unit can move to, calls recursive
	 * method addLocations(Map map, int x, int y, int depth) to do so.
	 * 
	 * @param map
	 *            The Map object from which to obtain tile info
	 */
	public Vector<MapTile> findMoveLocations() {
		if (moveLocations != null)
			return moveLocations;
		moveLocations = new Vector<MapTile>();
		addLocations(tile.getParent(), tile.getFieldLocation(), 0, true);
		return moveLocations;
	}
	
	public Vector<MapTile> findAttackLocations() {
		if (attackLocations != null)
			return attackLocations;
		attackLocations = new Vector<MapTile>();
		addLocations(tile.getParent(), tile.getFieldLocation(), 0, false);
		return attackLocations;		
	}
	
	public boolean isDead() {
		return isDead;
	}

	public void move(MapTile t) {
		if (t.getFieldLocation().distance(tile.getFieldLocation()) != 1.0)
			return;
		setDirection(tile, t);
		setSprite(SPRITE_WALKING);
		if (swap) {
			swap = false;
			previousTile.setEntity(null);
			tile.setEntity(this);
		}
		moveTimer = new CustomTimer(Configuration.getCharacterMoveTime());
		if (lookingDirection == Direction.NORTH || lookingDirection == Direction.WEST) {
			swap = true;
			previousTile = tile;
			tile = t;
		} else { 
			tile.setEntity(null);
			previousTile = tile;
			tile = t;
			tile.setEntity(this);
		}
		System.out.println("Moving " + toString());
	}

	public abstract void attack(GameCharacter attackableCharacter, int damage, boolean entity, int type);
	
	public void updateHealth(int damage) {
		damageTimer.reset();
		this.damage = damage;
		health -= damage;
		if (health <= 0) {
			isDead = true;
			setSprite(SPRITE_DEAD);
			showHealth = false;
		} else {
			new Thread(new ChangeSprite(this, HIT_ANIMATION_DELAY, SPRITE_HIT)).start();
		}
	}
	
	/**
	 *  Method to add MapTiles from the map to the moveLocations or attackLocations Vector
	 * 
	 * @param map Map object from which to obtain tile info
	 * @param location Value of the List of Lists to check
	 * @param depth The distance out to check from the units' location
	 * @param move true if it should be added to moveLocations, false to attack
	 */
	private void addLocations(Map map, Point location, int depth, boolean move) {
		
		int range = 0;
		int x = location.x;
		int y = location.y;
		
		if (move) {
			range = moveRange;
			if (depth >= 0)
				moveLocations.add(map.getField().get(x).get(y));
		} else {
			range = attackRange;
			if (depth >= 0)
				attackLocations.add(map.getField().get(x).get(y));
		}
		
		try { // Move up
			if ((!move || map.getField().get(x).get(y - 1).isAccessible())
					&& depth < range) {
				addLocations(map, new Point(x, y - 1), depth + 1, move);
			}
		} catch (Exception e) {/* Exceptions can be ignored for now. */
		}

		try { // Move right
			if ((!move || map.getField().get(x + 1).get(y).isAccessible())
					&& depth < range) {
				addLocations(map, new Point(x + 1, y), depth + 1, move);
			}
		} catch (Exception e) {/* Exceptions can be ignored for now. */
		}

		try { // Move down
			if ((!move || map.getField().get(x).get(y + 1).isAccessible())
					&& depth < range) {
				addLocations(map, new Point(x, y + 1), depth + 1, move);
			}
		} catch (Exception e) {/* Exceptions can be ignored for now. */
		}

		try { // Move left
			if ((!move || map.getField().get(x - 1).get(y).isAccessible())
					&& depth < range) {
				addLocations(map, new Point(x - 1, y), depth + 1, move);
			}
		} catch (Exception e) {/* Exceptions can be ignored for now. */
		}
	}
	

	/**
	 * override because we need to temper with the x and y coordinates
	 */
	@Override
	public void draw(int x, int y, Graphics g, boolean transparant) {
		if (moveTimer != null) {
			if (swap && !moveTimer.isRunning()) {
				swap = false;
				previousTile.setEntity(null);
				tile.setEntity(this);
				tile.drawEntity(g, transparant);
				return;
			}
			if (previousTile != null && moveTimer.isRunning()) {
				int endX, endY;
				if (!swap) {
					endX = (int) (x - (x - previousTile.getRelativeLocation().x)
							* moveTimer.getRemainingPecentage());
					endY = (int) (y - (y - previousTile.getRelativeLocation().y)
							* moveTimer.getRemainingPecentage());
				} else {
					endX = (int) (tile.getRelativeLocation().x - (tile.getRelativeLocation().x - previousTile.getRelativeLocation().x)
							* moveTimer.getRemainingPecentage());
					endY = (int) (tile.getRelativeLocation().y - (tile.getRelativeLocation().y - previousTile.getRelativeLocation().y)
							* moveTimer.getRemainingPecentage());
				}
				super.draw(endX, endY, g, transparant);
				if ((showHealth) && (!isDead))
					healthBar.draw(endX, endY, health, g);
			} else {
				super.draw(x, y, g, transparant);
				if ((showHealth) && (!isDead))
					healthBar.draw(x, y, health, g);
			}
		}
		if (damageTimer != null && damageTimer.isRunning() && damage >= 0) {
			Font previousFont = g.getFont();
			Color previousColor = g.getColor();
			g.setFont(FONT);
			g.setColor(Color.WHITE);
			String message;
			if (damageMissed)
				message = MISS_MESSAGE;
			else if (damageEvaded)
				message = EVADE_MESSAGE;
			else if (damageBlocked)
				message = BLOCK_MESSAGE;
			else if (damage == 0)
				message = MISS_MESSAGE;
			else
				message = Integer.toString(damage);
			g.drawString(message, x,  y + (int)(damageTimer.getRemainingPecentage() * 20));
			g.setFont(previousFont); // gotta set it back!
			g.setColor(previousColor); 
		}
	}

	public void flushSprite() {
		sprite.flush();
	}
	
}
