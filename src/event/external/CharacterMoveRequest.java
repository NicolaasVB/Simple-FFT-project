package event.external;


import client.GameClient;
import event.GameEventHandler;
import event.RequestGameEvent;
import event.local.CharacterMove;
import game.Path;


public class CharacterMoveRequest extends RequestGameEvent {
	
	private static final long serialVersionUID = -7387076026719278554L;
	private int id;
	private Path path;
	
	public CharacterMoveRequest(int id, Path path) {
		super();
		setType(Type.MULTICAST_GAME);
		this.id = id;
		this.path = path;
	}
	
	public int getId() {
		return id;
	}
	
	public Path getPath() {
		return path;
	}
	
	@Override
	public void process() {
		path.setCharacter(GameClient.getInstance().getCharacter(id));
		path.setMap(GameClient.getInstance().getMap());
		GameEventHandler.getInstance().dispatchEvent(new CharacterMove(path));
	}

	@Override
	public boolean verify() {
		return getClientHandler().getGame().moveCharacter(path, id);
	}

}
