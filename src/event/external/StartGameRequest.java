package event.external;

import map.Map;
import client.ChatWindow;
import client.GameClient;
import client.Lobby;
import client.WaitForOtherPlayerWindow;
import event.RequestGameEvent;

public class StartGameRequest extends RequestGameEvent {
	private static final long serialVersionUID = 7500598056078698553L;
	private int gameId;
	private int team;
	private String[] map;
	
	public StartGameRequest(Map map, int gameId, int team) {
		super();
		setType(Type.UNICAST);
		this.map = map.mapToString();
		this.gameId = gameId;
		this.setTeam(team);
	}
	
	public int getGameId() {
		return gameId;
	}
	
	public void setGameId(int gameId) {
		this.gameId = gameId;
	}
	
	public Map getMap() {
		return Map.mapFromString(map);
	}
	
	public void setMap(Map map) {
		this.map = map.mapToString();
	}
	
	public int getTeam() {
		return team;
	}

	public void setTeam(int team) {
		this.team = team;
	}

	@Override
	public void process() {
		// Lobby afsluiten
		Lobby.getInstance().setVisible(false);
		// Wait window afsluiten
		WaitForOtherPlayerWindow.getInstance().setGameId(gameId);
		WaitForOtherPlayerWindow.getInstance().setVisible(false);
		// Gameclient opstarten
		GameClient.getInstance().setVisible(true);
		// Gameclient instellen (map en id)
		GameClient.getInstance().setMap(getMap());
		GameClient.getInstance().setGameId(gameId);
		GameClient.getInstance().setTeam(team);
		// Chat window opstarten
		ChatWindow.getInstance().setVisible(true);
	}

	@Override
	public boolean verify() {
		// Altijd false, request kan enkel gestuurd worden van de server
		return false;
	}

}
