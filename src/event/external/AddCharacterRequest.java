package event.external;

import java.awt.Point;

import client.GameClient;
import event.RequestGameEvent;
import game.characters.GameCharacter;
import game.characters.GameCharacter.CharacterType;


public class AddCharacterRequest extends RequestGameEvent {
	private static final long serialVersionUID = -4383233282279021157L;
	private Point position;
	private CharacterType type;
	private int id;
	private int team;
	
	public AddCharacterRequest(Point position, CharacterType type) {
		super();
		setType(Type.MULTICAST_GAME);
		this.position = position;
		this.type = type;
	}

	@Override
	public void process() {
		GameClient.getInstance().addCharacter(position, type, id, team);
	}

	@Override
	public boolean verify() {
		for (GameCharacter character : getClientHandler().getGame().getCharList().values()) {
			if(character.getTile().getFieldLocation().x == position.x && character.getTile().getFieldLocation().y == position.y)
				return false;
		}
		if (getClientHandler().getGame().getMap().getTile(position.x, position.y).getStartTileForTeam() != getClientHandler().getTeam())
			return false;
		id = getClientHandler().getGame().addCharacter(position, type, getClientHandler().getTeam());
		team = getClientHandler().getTeam();
		return true;
	}

}
