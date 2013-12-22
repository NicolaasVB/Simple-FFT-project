package event.external;



import client.GameClient;
import event.GameEventHandler;
import event.RequestGameEvent;
import event.local.CharacterAttack;
import game.characters.GameCharacter;

public class AttackRequest extends RequestGameEvent {
	
	private static final long serialVersionUID = 4661749222666576589L;
	private int fromCharacterId;
	private int toCharacterId;
	private int type;
	private int damage = 0;
	private boolean evaded = false;
	private boolean blocked = false;
	private boolean missed = false;
	
	public AttackRequest(int fromCharacterId, int toCharacterId, int type) {
		setType(Type.MULTICAST_GAME);
		this.fromCharacterId = fromCharacterId;
		this.toCharacterId = toCharacterId;
		this.type = type;
	}
	
	public int getFromCharacterId() {
		return fromCharacterId;
	}

	public void setFromCharacterId(int fromCharacterId) {
		this.fromCharacterId = fromCharacterId;
	}

	public int getToCharacterId() {
		return toCharacterId;
	}

	public void setToCharacterId(int toCharacterId) {
		this.toCharacterId = toCharacterId;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public boolean isEvaded() {
		return evaded;
	}

	public void setEvaded(boolean evaded) {
		this.evaded = evaded;
	}

	public boolean isBlocked() {
		return blocked;
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}

	public boolean isMissed() {
		return missed;
	}

	public void setMissed(boolean missed) {
		this.missed = missed;
	}

	@Override
	public void process() {
	//	GameClient.getInstance().getCharacter(toCharacterId).showHealth = true;
		GameCharacter fromChar = GameClient.getInstance().getCharacter(fromCharacterId);
		GameCharacter toChar = GameClient.getInstance().getCharacter(toCharacterId);
		toChar.setDamageProperties(missed, evaded, blocked);
		GameEventHandler.getInstance().dispatchEvent(new CharacterAttack(fromChar, toChar, damage, type));
	}

	@Override
	public boolean verify() {
		return getClientHandler().getGame().attackCharacter(this);
	}
	
	

}
