package game;

import event.external.AttackRequest;
import event.external.ChangeTurnRequest;
import event.external.CreateGameRequest;
import event.external.GameFinishedRequest;
import event.external.GetGamesRequest;
import event.external.NewCharacterRequest;
import event.external.StartGameRequest;
import event.external.StopAddingCharactersRequest;
import game.characters.GameCharacter;
import game.characters.GameCharacter.CharacterType;
import game.characters.classes.Archer;
import game.characters.classes.Knight;
import game.characters.classes.Mage;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import map.Map;
import map.tiles.MapTile;
import server.ClientHandler;
import server.Server;

public class Game implements Serializable {
	private static final long serialVersionUID = -671164682094704344L;

	//private enum GamePhase {WAITING, STARTING}
	
	private transient ArrayList<ClientHandler> players;
	private int turn;
	private transient Map map;
	private String[] mapString;
	private int maxCharacters;
	private int id;
	private int round = 0;
	private HashMap<Integer, GameCharacter> charList = new HashMap<Integer, GameCharacter>();
	private boolean finished = false;
	
	// Information about the player who's acting now
	private boolean hasMoved, hasAttacked, hasEndedTurn;
	private int characterId = -1;
	
	public Game(Map map, int maxCharacters) {
		this.setMap(map);
		this.setMaxCharacters(maxCharacters);
	}
	
	public Game(CreateGameRequest req) {
		this.setMap(Map.mapFromString(req.getMap()));
		this.setMaxCharacters(req.getMaxCharacters());
	}

	public HashMap<Integer, GameCharacter> getCharList() {
		return charList;
	}

	public void setCharList(HashMap<Integer, GameCharacter> charList) {
		this.charList = charList;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the map
	 */
	public Map getMap() {
		if (map == null && mapString != null)
			map = Map.mapFromString(mapString);
		return map;
	}

	/**
	 * @param map the map to set
	 */
	public void setMap(Map map) {
		this.map = map;
		mapString = map.mapToString();
	}

	/**
	 * @return maxCharacters
	 */
	public int getMaxCharacters() {
		return maxCharacters;
	}

	/**
	 * @param maxCharacters the maxCharacters to set
	 */
	public synchronized void setMaxCharacters(int maxCharacters) {
		this.maxCharacters = maxCharacters;
	}

	/**
	 * @return the players
	 */
	public synchronized ArrayList<ClientHandler> getPlayers() {
		if (players == null)
			players = new ArrayList<ClientHandler>();
		return players;
	}
	
	@Override
	public String toString() {
		return "# " + getId() + " Map: " + getMap() + " - Max Characters: " + getMaxCharacters();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Game) {
			if (this.getId() == ((Game)o).getId())
				return true;
		}
		return false;
	}
	
	public void nextRound() {
		switch (round) {
		case 0:
			if (this.players.size() == 2) {
				for (int i = 0; i < players.size(); i++) {
					players.get(i).setTeam(i+1);
					players.get(i).sendRequest(new StartGameRequest(map, id, i+1));
					players.get(i).sendRequest(new NewCharacterRequest());
				}
				GetGamesRequest req = new GetGamesRequest();
				req.setGames(Server.getGamesWithoutEnoughPlayers());
				players.get(0).sendRequest(req);
			}
			break;
		case 1:
			// Choose player
			turn = new Random().nextInt(2);
			
			// Send new request for the players turn
			hasMoved = false;
			hasAttacked = false;
			hasEndedTurn = false;
			changeTurn();
			break;
		}
	}
	
	public void changeTurn() {
		if ((hasMoved && hasAttacked) || hasEndedTurn) {
			// Beurt over, het is aan de andere speler
			turn = (turn + 1) % 2;
			hasMoved = false;
			hasAttacked = false;
			hasEndedTurn = false;
			characterId = -1;
			players.get(turn).sendRequest(new ChangeTurnRequest(turn + 1, !hasMoved, !hasAttacked, characterId));
		} else {
			players.get(turn).sendRequest(new ChangeTurnRequest(turn + 1, !hasMoved, !hasAttacked, characterId));
		}
	}
	
	public int addCharacter(Point position, CharacterType type, int team) {
		int id = charList.size();
		GameCharacter c;
		switch (type) {
		case KNIGHT:
			c = new Knight(map.getTile(position.x, position.y), team, id);
			break;
		case ARCHER:
			c = new Archer(map.getTile(position.x, position.y), team, id);
			break;
		case MAGE:
			c = new Mage(map.getTile(position.x, position.y), team, id);
			break;
		default:
			c = new Knight(map.getTile(position.x, position.y), team, id);
			break;
		}
		charList.put(id, c);
		
		int count1 = 0;
		int count2 = 0;
		
		for (GameCharacter ch : charList.values()) {
			switch(ch.getTeam()){
				case 1:
					count1++;
					break;
				case 2:
					count2++;
					break;
			}
		}
		
		if (count1 == maxCharacters) {
			players.get(0).sendRequest(new StopAddingCharactersRequest());
		}
		
		if (count2 == maxCharacters) {
			players.get(1).sendRequest(new StopAddingCharactersRequest());
		}
		
		if (count1 == maxCharacters && count2 == maxCharacters) {
			setRound(1);
			nextRound();
		}
		
		return id;
	}
	
	public boolean moveCharacter(Path path, int charId) {
		if (finished || path == null)
			return false;
		GameCharacter character = getCharacter(charId);
		MapTile previous = character.getTile();
		path.setMap(map);
		path.setCharacter(character);
		if (!hasMoved && path.verify() && character.getTeam() == (turn + 1) && (characterId == -1 || characterId == charId)) {
			previous.setEntity(null);
			path.getLast().setEntity(character);
			character.setTile(path.getLast());
			character.removePath();
			characterId = charId;
			hasMoved = true;
			changeTurn();
			return true;
		}
		hasMoved = true;
		changeTurn();
		return false;
	}
	
	public boolean attackCharacter(AttackRequest req) {
		if (finished)
			return false;
		GameCharacter fromChar = getCharacter(req.getFromCharacterId());
		GameCharacter toChar = getCharacter(req.getToCharacterId());
		if (!hasAttacked && fromChar != null && toChar != null && fromChar.findAttackLocations().contains(toChar.getTile())) {
			hasAttacked = true;
			Random r = new Random();
			if (r.nextInt(101) < toChar.getEvade())
				req.setEvaded(true);
			else if (r.nextInt(101) < toChar.getBlock())
				req.setBlocked(true);
			else if (r.nextInt(101) < 5)
				req.setMissed(true);
			else
				req.setDamage((int)((double)fromChar.getAttack() / (double)toChar.getDefence() * (double)fromChar.calculateDamage()));

			toChar.updateHealth(req.getDamage());
			
			// Check if the game is finished
			int countAlive1 = 0, countAlive2 = 0;
			for (GameCharacter ch : charList.values()) {
				if (!ch.isDead()) {
					switch (ch.getTeam()) {
					case 1:
						countAlive1++;
						break;
					case 2:
						countAlive2++;
						break;
					}
				}
			}
			
			if (countAlive1 == 0) {
				// Team 2 wins
				finished = true;
				players.get(0).sendRequest(new GameFinishedRequest(2));
			} else if (countAlive2 == 0) {
				// Team 1 wins
				finished = true;
				players.get(0).sendRequest(new GameFinishedRequest(1));
			}

			characterId = fromChar.getID();
			changeTurn();
			
			return true;
		}
		hasAttacked = true;
		changeTurn();
		return false;
	}
	
	public boolean endTurn() {
		if (finished)
			return false;
		hasEndedTurn = true;
		changeTurn();
		return true;
	}
	
	public GameCharacter getCharacter(int id) {
		return charList.get(id);
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}
}
