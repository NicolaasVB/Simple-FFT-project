package particles;

import java.awt.Color;

public class ParticleBehaviour {
	private static final int DEFAULT_RADIUS = 5;
	private static final int DEFAULT_SMOKE_AMOUNT = 5;
	private static final int DEFAULT_DOT_AMOUNT = 20;
	
	private boolean fade;
	private boolean smoke;
	private int amount;
	private Color firstInner, secondInner, firstOuter, secondOuter;
	private int radius;
	
	/**
	 * constructor for particleBehaviour. 
	 * @param fade whether the particles have to fade out or not
	 * @param smoke whether you want smoke particles or dot particles
	 * @param amount the amount of particles
	 * @param inner the radius of the inner core
	 * @param outer the radius of the outer core
	 * @param smokeRadius the radius of the smoke particles for the gaussian filter
	 */
	public ParticleBehaviour(boolean fade, boolean smoke, int amount, Color inner, Color outer, int smokeRadius) {
		this.fade = fade;
		this.smoke = smoke;
		this.amount = amount;
		this.firstInner = inner;
		this.firstOuter = outer;
		this.radius = smokeRadius;
	}
	
	public ParticleBehaviour(Color inner, Color outer) {
		this(true, false, DEFAULT_DOT_AMOUNT, inner, outer, DEFAULT_RADIUS);
	}

	public ParticleBehaviour(int amount, Color inner, Color outer) {
		this(true, false, amount, inner, outer, DEFAULT_RADIUS);
	}
	
	public ParticleBehaviour(boolean smoke, Color inner, Color outer) {
			this(true, smoke, DEFAULT_SMOKE_AMOUNT, inner, outer, DEFAULT_RADIUS);
	}
	
	public ParticleBehaviour(boolean smoke, int amount, Color inner, Color outer) {
		this(true, smoke, amount, inner, outer, DEFAULT_RADIUS);
	}
	
	public void setSecondInner(Color second) {
		this.secondInner = second;
	}
	
	public void setSecondOuter(Color second) {
		this.secondOuter = second;
	}
	
	public boolean fade() {
		return fade;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public boolean isSmoke() {
		return smoke;
	}
	
	public Color getFirstInner() {
		return firstInner;
	}
	
	public Color getFirstOuter() {
		return firstOuter;
	}
	
	public Color getSecondInner() {
		return secondInner;
	}
	
	public Color getSecondOuter() {
		return secondOuter;
	}
	
	public int getRadius() {
		return radius;
	}

}
