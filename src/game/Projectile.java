package game;

import game.characters.GameCharacter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;

import particles.Cloud;
import particles.ParticleBehaviour;
import utils.CustomTimer;
import Application.Configuration;

public class Projectile implements Runnable {
	
	private static final int BOUNDS = 1500;
	
	private static final int ARROW_LENGTH = 15;
	private static final Color ARROW_COLOR = new Color(139, 69, 19);
	private static final int ARROW_SPEED = 350;
	
	private static final int FIREBALL_SPEED = 1000;
	private static final int FIREBALL_LIFETIME = 1000;
	private static final int FIREBALL_INNER_RING = 10;
	private static final int FIREBALL_OUTER_RING = 20;
	private static final Color FIREBALL_COLOR_FIRST_INNER = Color.RED;
	private static final Color FIREBALL_COLOR_SECOND_INNER = Color.YELLOW;
	private static final Color FIREBALL_COLOR_FIRST_OUTER = Color.RED;
	private static final Color FIREBALL_COLOR_SECOND_OUTER = null;
	private static final int FIREBALL_RADIUS_PARTICLES = 5;
	
	private static final int ICE_SPEED = 1000;
	private static final int ICE_LIFETIME = 2000;
	private static final int ICE_INNER_RING = 30;
	private static final int ICE_OUTER_RING = 50;
	private static final Color ICE_COLOR_FIRST_INNER = Color.BLUE;
	private static final Color ICE_COLOR_SECOND_INNER = Color.WHITE;
	private static final Color ICE_COLOR_FIRST_OUTER = Color.WHITE;
	private static final Color ICE_COLOR_SECOND_OUTER = null;
	private static final int ICE_RADIUS_PARTICLES = 5;
	private static final int ICE_DISTANCE = 50;
	
	private static final int AMOUNT_SMOKE_PARTICLES = Configuration.useLessResources() ? 2 : 5;
	
	public static final int ARROW = 0;
	public static final int FIREBALL = 1;
	public static final int ICE = 2;
	
	private transient GameCharacter attacker;
	private transient GameCharacter defender;
	private int type;
	private int length;
	
	private CustomTimer timer;
	
	private Cloud particles;
	private double slope; // used for arrow
	private int y; // if the arrow goes straight up, or straight down, this will be set
	private int x; // the x component we'll use to move on the x-ax to draw the arrow
	
	public Projectile(GameCharacter attacker, GameCharacter defender, int type) {
		this.attacker = attacker;
		this.defender = defender;
		this.type = type;
		if (type == ARROW) {
			this.x = ARROW_LENGTH;
			this.y = 0;
			this.length = ARROW_SPEED;
			this.timer = getTimer();
			setArrowValues();
		} else if (type == FIREBALL) {
			this.length = FIREBALL_SPEED;
			ParticleBehaviour behaviour = new ParticleBehaviour(false, true, AMOUNT_SMOKE_PARTICLES, FIREBALL_COLOR_FIRST_INNER, FIREBALL_COLOR_FIRST_OUTER, FIREBALL_RADIUS_PARTICLES);
			behaviour.setSecondInner(FIREBALL_COLOR_SECOND_INNER);
			behaviour.setSecondOuter(FIREBALL_COLOR_SECOND_OUTER);
			particles = new Cloud(BOUNDS, BOUNDS, behaviour);
		} else if (type == ICE) {
			this.length = ICE_SPEED;
			ParticleBehaviour behaviour = new ParticleBehaviour(false, true, AMOUNT_SMOKE_PARTICLES, ICE_COLOR_FIRST_INNER, ICE_COLOR_FIRST_OUTER, ICE_RADIUS_PARTICLES);
			behaviour.setSecondInner(ICE_COLOR_SECOND_INNER);
			behaviour.setSecondOuter(ICE_COLOR_SECOND_OUTER);
			particles = new Cloud(BOUNDS, BOUNDS, behaviour);
		}
	}
	
	private void setArrowValues() {
		try {
			slope = getSlope();
			// jaja, ik weet het... rico normaliseren en pythagoras, ma shush, dees werkt en is minder denkwerk! :p
			while (Point.distance(getX(), getY(), getX() + x, (int)(getY() + (slope * x))) > ARROW_LENGTH) {
				x--;
			}
		} catch (Exception e) {
			if (e.getMessage().equalsIgnoreCase("up"))
				y = ARROW_LENGTH;
			else
				y = -ARROW_LENGTH;
		}
	}
	
	private CustomTimer getTimer() {
		if (timer == null)
			timer = new CustomTimer(length);
		return timer;
	}
	
	public boolean isAlive() {
		if (type == ARROW) {
			return getTimer().isRunning();
		}
		return particles.isAlive();
	}
	
	private double getSlope() throws Exception {
		if ((defender.getTile().getRelativeLocation().x - attacker.getTile().getRelativeLocation().x) == 0) {
			if ((defender.getTile().getRelativeLocation().y - attacker.getTile().getRelativeLocation().y) <= 0)
				throw new Exception("up");
			else
				throw new Exception("down");
		}
		double dY = defender.getTile().getRelativeLocation().getY() - attacker.getTile().getRelativeLocation().getY();
		double dX = defender.getTile().getRelativeLocation().getX() - attacker.getTile().getRelativeLocation().getX();
		return dY / dX;
	}
	
	private int getX() {
		return (int) (defender.getTile().getRelativeLocation().x - (defender.getTile().getRelativeLocation().x - attacker.getTile().getRelativeLocation().x)
				* timer.getRemainingPecentage());
	}
	
	private int getY() {
		return (int) (defender.getTile().getRelativeLocation().y - (defender.getTile().getRelativeLocation().y - attacker.getTile().getRelativeLocation().y)
				* timer.getRemainingPecentage()); 
	}
	
	private int getIceX() {
		return defender.getTile().getRelativeLocation().x;
	}
	
	private int getIceY() {
		return (int) (defender.getTile().getRelativeLocation().y - (defender.getTile().getRelativeLocation().y - (defender.getTile().getRelativeLocation().y - ICE_DISTANCE))
				* timer.getRemainingPecentage()); 
	}
	
	public void draw(Graphics g) {
		if (type == ARROW) {
			Graphics2D g2 = (Graphics2D) g;
			Color prevColor = g2.getColor();
			Stroke prevStroke = g2.getStroke();
			g2.setColor(ARROW_COLOR);
			g2.setStroke(new BasicStroke(2));
			if (y == 0) {
				g.drawLine(getX(), getY(), getX() + x, (int)(getY() + (slope * x)));
			} else {
				g.drawLine(getX(), getY(), getX(), getY() + y);
			}
			g2.setColor(prevColor);
			g2.setStroke(prevStroke);
		} else {
			particles.draw(g);
		}
	}

	@Override
	public void run() {
		while (getTimer().isRunning()) {
			if (type == FIREBALL)
				particles.addParticles(getX(), getY(), FIREBALL_INNER_RING, FIREBALL_OUTER_RING, FIREBALL_LIFETIME);
			else if (type == ICE)
				particles.addParticles(getIceX(), getIceY(), ICE_INNER_RING, ICE_OUTER_RING, ICE_LIFETIME);
			try {
				Thread.sleep(Configuration.useLessResources() ? 50 : 10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
