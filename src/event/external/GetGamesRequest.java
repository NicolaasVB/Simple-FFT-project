package event.external;

import java.util.ArrayList;

import server.Server;
import client.Lobby;
import event.RequestGameEvent;
import game.Game;

public class GetGamesRequest extends RequestGameEvent {
	private static final long serialVersionUID = -3627939000983314518L;
	private ArrayList<Game> games;
	
	public GetGamesRequest() {
		setType(Type.UNICAST);
	}

	public ArrayList<Game> getGames() {
		return games;
	}

	public void setGames(ArrayList<Game> games) {
		this.games = games;
	}
	
	@Override
	public void process() {
		Lobby.getInstance().setGames(games);
	}

	@Override
	public boolean verify() {
		setGames(Server.getGamesWithoutEnoughPlayers());
		return true;
	}

}
