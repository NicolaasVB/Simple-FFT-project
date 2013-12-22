package event.external;


import server.Server;
import event.RequestGameEvent;
import game.Game;

public class CreateGameRequest extends RequestGameEvent {
	private static final long serialVersionUID = 6706127924786516456L;
	private String[] map;
	private int maxCharacters;
	
	public CreateGameRequest(String[] map, int maxCharacters) {
		super();
		setType(Type.BROADCAST);
		this.map = map;
		this.maxCharacters = maxCharacters;
	}

	/**
	 * @return the map
	 */
	public String[] getMap() {
		return map;
	}

	/**
	 * @param map the map to set
	 */
	public void setMap(String[] map) {
		this.map = map;
	}

	/**
	 * @return the maxCharacters
	 */
	public int getMaxCharacters() {
		return maxCharacters;
	}

	/**
	 * @param maxCharacters the maxCharacters to set
	 */
	public void setMaxCharacters(int maxCharacters) {
		this.maxCharacters = maxCharacters;
	}

	@Override
	public void process() {
		// Request updated list of games for the lobby
		ServerCommunicator.getInstance().sendObject(new GetGamesRequest());
	}

	@Override
	public boolean verify() {
		Game game = new Game(this);
		game.setId(Server.getNextGameId());
		Server.getGames().add(game);
		return true;
	}

}
