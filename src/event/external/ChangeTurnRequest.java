package event.external;

import client.GameClient;
import client.GameClient.GameState;
import event.RequestGameEvent;

public class ChangeTurnRequest extends RequestGameEvent {
	private static final long serialVersionUID = -2248855692344858048L;
	private int team;
	private boolean canMove, canAttack;
	private int characterId;
	
	public ChangeTurnRequest(int team, boolean canMove, boolean canAttack, int characterId) {
		super();
		setType(Type.MULTICAST_GAME);
		this.team = team;
		this.canMove = canMove;
		this.canAttack = canAttack;
		this.characterId = characterId;
	}

	public boolean isCanMove() {
		return canMove;
	}

	public boolean isCanAttack() {
		return canAttack;
	}

	public int getCharacterId() {
		return characterId;
	}

	@Override
	public void process() {
		if (GameClient.getInstance().getTeam() == team) {
			GameClient.getInstance().setCharacterId(characterId);
			GameClient.getInstance().setGameState(GameState.TURN);
			GameClient.getInstance().canAttack(this.canAttack);
			GameClient.getInstance().canMove(this.canMove);
		} else {
			GameClient.getInstance().setGameState(GameState.IDLE);
		}
	}

	@Override
	public boolean verify() {
		return false;
	}

}
