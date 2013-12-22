package event.local;


import event.LocalGameEvent;
import game.Path;

public class CharacterMove extends LocalGameEvent {

	private static final long serialVersionUID = 5879605232744177685L;
	private Path path;
	
	public CharacterMove(Path path) {
		this.path = path;
	}
	
	
	@Override
	public void process() {
		new Thread(path).start();
	}

}