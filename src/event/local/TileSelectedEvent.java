package event.local;

import map.tiles.MapTile;
import client.GameClient;
import event.GameEventHandler;
import event.LocalGameEvent;
import game.characters.GameCharacter;

/**
 * Event that is called when we select a tile
 * @author Nicolaas
 *
 */
public class TileSelectedEvent extends LocalGameEvent {
	
	private static final long serialVersionUID = -6677063618231010554L;
	private MapTile tile;
	
	public TileSelectedEvent(MapTile tile) {
		this.tile = tile;
	}

	@Override
	public void process() {
		System.out.println("selected a tile. at location (" + tile.getFieldLocation().x + ", " + tile.getFieldLocation().y + ")");
		
		if (tile.getEntity() != null && tile.getEntity() instanceof GameCharacter && !((GameCharacter) tile.getEntity()).isDead() ) {
			
			if (((GameCharacter) tile.getEntity()).getTeam() == GameClient.getInstance().getTeam()) {
				if ((GameClient.getInstance().canAttack() || GameClient.getInstance().canMove()) && (((GameCharacter) tile.getEntity()).getID() == GameClient.getInstance().getCharacterId() || GameClient.getInstance().getCharacterId() == -1))
					GameEventHandler.getInstance().dispatchEvent(new CharacterSelectedEvent((GameCharacter) tile.getEntity()));
			}
		}
	}
}
