package game.characters;



public class ChangeSprite implements Runnable {
	private int timer;
	private GameCharacter character;
	private String spriteType;

	
	public ChangeSprite(GameCharacter character, int timer, String spriteType) {
		this.timer = timer;		
		this.character = character;
		this.spriteType = spriteType;
	}
	
	@Override
	public void run() {
		character.setSprite(spriteType);
		
		try {
			Thread.sleep(timer);
			
		} catch (InterruptedException e) {
			e.printStackTrace();		
		}
		character.flushSprite();
		character.setSprite(GameCharacter.SPRITE_WALKING);
		character.setShowHealth(false);
	}
}
