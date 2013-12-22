package game;

import map.GameEntity;
import map.tiles.MapTile;
import sprite.Sprite;

public abstract class GameObject extends GameEntity {

	public GameObject(Sprite s, MapTile pos) {
		super(s, pos);
	}

}
