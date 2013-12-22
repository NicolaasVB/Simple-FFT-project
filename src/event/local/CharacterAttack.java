package event.local;


import event.LocalGameEvent;
import game.characters.GameCharacter;

public class CharacterAttack extends LocalGameEvent {

	private static final long serialVersionUID = -7592543058584000148L;
	private GameCharacter fromChar;
	private GameCharacter toChar;
	private int damage;
	private int type;
	
	public CharacterAttack(GameCharacter fromChar, GameCharacter toChar, int damage, int type) {
		this.fromChar = fromChar;
		this.toChar = toChar;
		this.damage = damage;
		this.type = type;
	}
	
	
	@Override
	public void process() {
		fromChar.attack(toChar, damage, true, type);
		fromChar.setSelected(false);
	}

}