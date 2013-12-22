package client;

import event.GameEventHandler;
import event.external.AddCharacterRequest;
import event.external.AttackRequest;
import event.external.CharacterMoveRequest;
import event.local.HighlightAttackRange;
import event.local.HighlightMoveRange;
import event.local.HighlightStartTiles;
import event.local.TileSelectedEvent;
import game.Projectile;
import game.characters.GameCharacter;
import game.characters.GameCharacter.CharacterType;
import game.characters.classes.Archer;
import game.characters.classes.Knight;
import game.characters.classes.Mage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import map.Map;
import menu.GameMenu;
import menu.StatusWindow;
import Application.Configuration;


/**
 * singleton of our client
 * @author Nicolaas
 *
 */
public class GameClient extends JFrame implements MouseListener,
		MouseMotionListener {
	
	private static final long serialVersionUID = 5581564614764107409L;
	private static final Dimension clientSize = new Dimension(700, 500);
	private static GameClient INSTANCE = new GameClient();
	private static final Color BACKGROUND_COLOR = new Color(173, 216, 230);
	private static final Font LOADING_FONT = new Font("Rockwell Extra Bold", Font.PLAIN, 12);
	
	public enum GameState { IDLE, BROWSING_MENU, CHARACTER_MOVING, CHARACTER_ATTACKING, ADDING_CHARACTER, TURN }
	
	private GameState state;

	private static final int MILLISECONDS = 1000;


	private Map map;
	private GameMenu activeMenu;
	private StatusWindow status;

	private static HashMap<Integer, GameCharacter> charList = new HashMap<Integer, GameCharacter>();
	private static ArrayList<Projectile> projectiles = new ArrayList<Projectile>();

	private Timer t;
	private JPanel gamePanel;
	private JMenuBar menu;
	private JMenu OptionsMenu;
	private JMenuItem optionsMenuItem;
	private int gameId = -1;
	private int team = -1;
	private int characterId = -1;
	private boolean canMove, canAttack;
	private int attackType;
	
	private boolean loading = true;
	

	private GameClient() {
		super("GameClient team tactics");
		new Thread(GameEventHandler.getInstance()).start();
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (final Exception ignored) {
		} // ignored
		map = new Map(Configuration.getStandardFieldWidth(),
				Configuration.getStandardFieldHeight());
		initComponents();
		setTimerTask();

		// Add the mouse listeners to the Panel
		gamePanel.addMouseListener(this);
		gamePanel.addMouseMotionListener(this);
		state = GameState.IDLE;
		canMove = true;
		canAttack = true;
		
	}
	
	public static GameClient getInstance() {
		return INSTANCE;
	}	

	private void initComponents() {
		status = new StatusWindow();
		gamePanel = new JPanel(true) {
			private static final long serialVersionUID = 3434172749729596206L;

			@Override
			public void paint(Graphics g) {
				if (loading) {
					Font prevFont = g.getFont();
					g.setFont(LOADING_FONT);
					g.drawString("Loading", 20, 30);
					g.setFont(prevFont);
					loading = false;
				}
				g.setColor(BACKGROUND_COLOR);
				g.fillRect(0, 0, getWidth(), getHeight());
				map.draw(g);
				try {
					for (GameCharacter c : charList.values()) {
						c.getTile().drawEntity(g, true);
					}
				} catch (ConcurrentModificationException e) {
					// ignored. This means that while we were looping over our charlist, we added or removed a character.
				}
				try {
					for (Projectile p : projectiles) {
						if (p.isAlive())
							p.draw(g);
						else
							projectiles.remove(p);
					}
				} catch (ConcurrentModificationException e) {
					// ignored. This means that while we were looping over our projectiles, we added or removed a projectile.
				}
				if (activeMenu != null && activeMenu.isActive())
					activeMenu.draw(g);
				status.draw(g, getWidth(), state);
				if (!ConfigurationWindow.getInstance().isVisible())
					this.requestFocusInWindow();
			}
		};
		
		menu = new JMenuBar();
		OptionsMenu = new JMenu("Options");
		optionsMenuItem = new JMenuItem("Option");
		optionsMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				openOptionsMenu();
			}
		});
		
		OptionsMenu.add(optionsMenuItem);
		menu.add(OptionsMenu);
		setJMenuBar(menu);

		gamePanel.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(map.getHighlightedTile() == null) {
					System.out.println(map.getHighlightedTile());
					return;
				}
				
				int x = map.getHighlightedTile().getFieldLocation().x;
				int y = map.getHighlightedTile().getFieldLocation().y;
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					switch (state) {
					case IDLE:
					case TURN:
						if (x == 0) {
							return;
						} else {
							if (map.getHighlightedTile().getEntity() != null && map.getHighlightedTile().getEntity() instanceof GameCharacter)
								((GameCharacter) map.getHighlightedTile().getEntity()).setShowHealth(false);
							map.setHighlightedTile(x - 1, y);
							if (map.getHighlightedTile().getEntity() != null && map.getHighlightedTile().getEntity() instanceof GameCharacter)
								((GameCharacter) map.getHighlightedTile().getEntity()).setShowHealth(true);
						}
						break;
					case BROWSING_MENU:
						break;
					case CHARACTER_MOVING:
						if (x == 0) {
							return;
						} else {
							if (getSelectedCharacter().findMoveLocations().contains(map.getTile(x - 1, y))) {
								map.setHighlightedTile(x - 1, y);
								getSelectedCharacter().getPath().addTile(map.getHighlightedTile());
								HighlightMoveRange();
							}
						}
						break;
					case CHARACTER_ATTACKING:
						if (x == 0) {
							return;
						} else {
							if (getSelectedCharacter().findAttackLocations().contains(map.getTile(x - 1, y))) {
								if (map.getHighlightedTile().getEntity() != null && map.getHighlightedTile().getEntity() instanceof GameCharacter && map.getHighlightedTile().getEntity() != getSelectedCharacter())
									((GameCharacter) map.getHighlightedTile().getEntity()).setShowHealth(false);
								map.setHighlightedTile(x - 1, y);
								if (map.getHighlightedTile().getEntity() != null && map.getHighlightedTile().getEntity() instanceof GameCharacter)
									((GameCharacter) map.getHighlightedTile().getEntity()).setShowHealth(true);
								HighlightAttackRange();
							}
						}
						break;
					case ADDING_CHARACTER:
						if (x != 0)
							map.setHighlightedTile(x - 1, y);
						HighlightStartTiles();
						break;
					default:
						System.err.println("Error while handling left KeyEvent.");
						break;
					}
				} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					switch (state) {
					case IDLE:
					case TURN:
						if (x == map.getWidth() - 1) {
							return;
						} else {
							if (map.getHighlightedTile().getEntity() != null && map.getHighlightedTile().getEntity() instanceof GameCharacter)
								((GameCharacter) map.getHighlightedTile().getEntity()).setShowHealth(false);
							map.setHighlightedTile(x + 1, y);
							if (map.getHighlightedTile().getEntity() != null && map.getHighlightedTile().getEntity() instanceof GameCharacter)
								((GameCharacter) map.getHighlightedTile().getEntity()).setShowHealth(true);
						}
						break;
					case BROWSING_MENU:
						break;
					case CHARACTER_MOVING:
						if (x == map.getWidth() - 1) {
							return;
						} else {
							if (getSelectedCharacter().findMoveLocations().contains(map.getTile(x + 1, y))) {
								map.setHighlightedTile(x + 1, y);
								getSelectedCharacter().getPath().addTile(map.getHighlightedTile());
								HighlightMoveRange();
							}
						}
						break;
					case CHARACTER_ATTACKING:
						if (x == map.getWidth() - 1) {
							return;
						} else {
							if (getSelectedCharacter().findAttackLocations().contains(map.getTile(x + 1, y))) {
								if (map.getHighlightedTile().getEntity() != null && map.getHighlightedTile().getEntity() instanceof GameCharacter && map.getHighlightedTile().getEntity() != getSelectedCharacter())
									((GameCharacter) map.getHighlightedTile().getEntity()).setShowHealth(false);
								map.setHighlightedTile(x + 1, y);
								if (map.getHighlightedTile().getEntity() != null && map.getHighlightedTile().getEntity() instanceof GameCharacter)
									((GameCharacter) map.getHighlightedTile().getEntity()).setShowHealth(true);
								HighlightAttackRange();
							}
						}
						break;
					case ADDING_CHARACTER:
						if (x != map.getWidth() - 1)
							map.setHighlightedTile(x + 1, y);
						HighlightStartTiles();
						break;
					default:
						System.err.println("Error while handling right KeyEvent.");
						break;
					}
				} else if (e.getKeyCode() == KeyEvent.VK_UP) {
					switch (state) {
					case IDLE:
					case TURN:
						if (y == map.getHeight() - 1) {
							return;
						} else {
							if (map.getHighlightedTile().getEntity() != null && map.getHighlightedTile().getEntity() instanceof GameCharacter)
								((GameCharacter) map.getHighlightedTile().getEntity()).setShowHealth(false);
							map.setHighlightedTile(x, y + 1);
							if (map.getHighlightedTile().getEntity() != null && map.getHighlightedTile().getEntity() instanceof GameCharacter)
								((GameCharacter) map.getHighlightedTile().getEntity()).setShowHealth(true);
						}
						break;
					case BROWSING_MENU:
						activeMenu.selectPrevious();
						break;
					case CHARACTER_MOVING:
						if (y == map.getHeight() - 1) {
							return;
						} else {
							if (getSelectedCharacter().findMoveLocations().contains(map.getTile(x, y + 1))) {
								map.setHighlightedTile(x, y + 1);
								getSelectedCharacter().getPath().addTile(map.getHighlightedTile());
								HighlightMoveRange();
							}
						}
						break;
					case CHARACTER_ATTACKING:
						if (y == map.getHeight() - 1) {
							return;
						} else {
							if (getSelectedCharacter().findAttackLocations().contains(map.getTile(x, y + 1))) {
								if (map.getHighlightedTile().getEntity() != null && map.getHighlightedTile().getEntity() instanceof GameCharacter && map.getHighlightedTile().getEntity() != getSelectedCharacter())
									((GameCharacter) map.getHighlightedTile().getEntity()).setShowHealth(false);
								map.setHighlightedTile(x, y + 1);
								if (map.getHighlightedTile().getEntity() != null && map.getHighlightedTile().getEntity() instanceof GameCharacter)
									((GameCharacter) map.getHighlightedTile().getEntity()).setShowHealth(true);
								HighlightAttackRange();
							}
						}
						break;
					case ADDING_CHARACTER:
						if (y != map.getHeight() - 1)
							map.setHighlightedTile(x, y + 1);
						HighlightStartTiles();
						break;
					default:
						System.err.println("Error while handling up KeyEvent.");
						break;
					}
				} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					switch (state) {
					case IDLE:
					case TURN:
						if (y == 0) {
							return;
						} else {
							if (map.getHighlightedTile().getEntity() != null && map.getHighlightedTile().getEntity() instanceof GameCharacter)
								((GameCharacter) map.getHighlightedTile().getEntity()).setShowHealth(false);
							map.setHighlightedTile(x, y - 1);
							if (map.getHighlightedTile().getEntity() != null && map.getHighlightedTile().getEntity() instanceof GameCharacter)
								((GameCharacter) map.getHighlightedTile().getEntity()).setShowHealth(true);
						}
						break;
					case BROWSING_MENU:
						activeMenu.selectNext();
						break;
					case CHARACTER_MOVING:
						if (y == 0) {
							return;
						} else {
							if (getSelectedCharacter().findMoveLocations().contains(map.getTile(x, y - 1))) {
								map.setHighlightedTile(x, y - 1);
								getSelectedCharacter().getPath().addTile(map.getHighlightedTile());
								HighlightMoveRange();
							}
						}
						break;
					case CHARACTER_ATTACKING:
						if (y == 0) {
							return;
						} else {
							if (getSelectedCharacter().findAttackLocations().contains(map.getTile(x, y - 1))) {
								if (map.getHighlightedTile().getEntity() != null && map.getHighlightedTile().getEntity() instanceof GameCharacter && map.getHighlightedTile().getEntity() != getSelectedCharacter())
									((GameCharacter) map.getHighlightedTile().getEntity()).setShowHealth(false);
								map.setHighlightedTile(x, y - 1);
								if (map.getHighlightedTile().getEntity() != null && map.getHighlightedTile().getEntity() instanceof GameCharacter)
									((GameCharacter) map.getHighlightedTile().getEntity()).setShowHealth(true);
								HighlightAttackRange();
							}
						}
						break;
					case ADDING_CHARACTER:
						if(y != 0)
							map.setHighlightedTile(x, y - 1);
						HighlightStartTiles();
						break;
					default:
						System.err.println("Error while handling down KeyEvent.");
						break;
					}
				} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					switch (state) {
					case IDLE:
						break;
					case TURN:
						GameEventHandler.getInstance().dispatchEvent(new TileSelectedEvent(map.getHighlightedTile()));
						if (map.getHighlightedTile().getEntity() != null && map.getHighlightedTile().getEntity() instanceof GameCharacter) {
							
							if (!((GameCharacter) map.getHighlightedTile().getEntity()).isDead() && ((GameCharacter) map.getHighlightedTile().getEntity()).getTeam() == team) {
								
								if (characterId == -1 || ((GameCharacter) map.getHighlightedTile().getEntity()).getID() == characterId) {
									
									activeMenu = map.getHighlightedTile().getMenu();
									if(activeMenu != null)
										activeMenu.setActive(true);	
								}
							}
						}
						break;
					case BROWSING_MENU:
						if (getTeam() == getSelectedCharacter().getTeam()) {
							if (activeMenu.executeSelected())
								activeMenu = null;
						}
						break;
					case CHARACTER_MOVING:
						GameEventHandler.getInstance().dispatchEvent(new CharacterMoveRequest(getSelectedCharacter().getID(), getSelectedCharacter().getPath()));
						map.clearHighlights();
						getSelectedCharacter().setSelected(false);
						break;
					case CHARACTER_ATTACKING:
						if (getSelectedCharacter() != null && map.getHighlightedTile() != null) {
							if (map.getHighlightedTile().getEntity() != null && map.getHighlightedTile().getEntity() instanceof GameCharacter && getSelectedCharacter().getID() != ((GameCharacter) map.getHighlightedTile().getEntity()).getID())
								GameEventHandler.getInstance().dispatchEvent(new AttackRequest(getSelectedCharacter().getID(), ((GameCharacter) map.getHighlightedTile().getEntity()).getID(), attackType));
							else 
								setGameState(GameState.TURN);
						} else {
							setGameState(GameState.TURN);
						}
						map.clearHighlights();
						break;
					case ADDING_CHARACTER:
						if(map.getHighlightedTile().getStartTileForTeam() == team)
							GameEventHandler.getInstance().dispatchEvent(new AddCharacterRequest(map.getHighlightedTile().getFieldLocation(), CharacterType.KNIGHT));
						break;
					default:
						System.err.println("Error while handling enter KeyEvent.");
						break;
					}
				} else if (e.getKeyCode() == KeyEvent.VK_K || e.getKeyCode() == KeyEvent.VK_1 || e.getKeyCode() == KeyEvent.VK_NUMPAD1  ) {
						switch (state) {
						case IDLE:					
							break;						
						case TURN:						
							break;					
						case BROWSING_MENU:
							break;
						case CHARACTER_MOVING:	
							break;																								
						case CHARACTER_ATTACKING:		
							break;																
						case ADDING_CHARACTER:
							if(map.getHighlightedTile().getStartTileForTeam() == team)
								GameEventHandler.getInstance().dispatchEvent(new AddCharacterRequest(map.getHighlightedTile().getFieldLocation(), CharacterType.KNIGHT));
							break;
						}
				} else if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_2 || e.getKeyCode() == KeyEvent.VK_NUMPAD2  ) {
				
					switch (state) {
					case IDLE:					
						break;						
					case TURN:						
						break;					
					case BROWSING_MENU:
						break;
					case CHARACTER_MOVING:	
						break;																								
					case CHARACTER_ATTACKING:		
						break;															
					case ADDING_CHARACTER:
						if(map.getHighlightedTile().getStartTileForTeam() == team)
							GameEventHandler.getInstance().dispatchEvent(new AddCharacterRequest(map.getHighlightedTile().getFieldLocation(), CharacterType.ARCHER));
						break;
					}
				} else if (e.getKeyCode() == KeyEvent.VK_M || e.getKeyCode() == KeyEvent.VK_3 || e.getKeyCode() == KeyEvent.VK_NUMPAD3  ) {
					switch (state) {
					case IDLE:					
						break;					
					case TURN:						
						break;					
					case BROWSING_MENU:
						break;
					case CHARACTER_MOVING:	
						break;																							
					case CHARACTER_ATTACKING:		
						break;														
					case ADDING_CHARACTER:
						if(map.getHighlightedTile().getStartTileForTeam() == team)
							GameEventHandler.getInstance().dispatchEvent(new AddCharacterRequest(map.getHighlightedTile().getFieldLocation(), CharacterType.MAGE));
						break;
					}
					
				} else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					switch (state) {
					case IDLE:
						return;						
					case TURN:
						return;					
					case BROWSING_MENU:
						if(activeMenu.isActive()) {
							if (activeMenu.hasSubMenu()) {
								activeMenu.setSubMenu(null);
							} else {
								activeMenu.setActive(false);
								getSelectedCharacter().setSelected(false);
								setGameState(GameState.TURN);
							}
						} else {
							setGameState(GameState.TURN);
						}
						break;
					case CHARACTER_MOVING:
						getSelectedCharacter().setSelected(false);
						setGameState(GameState.TURN);
						map.clearHighlights();
						return;																								
					case CHARACTER_ATTACKING:
						getSelectedCharacter().setSelected(false);
						setGameState(GameState.TURN);
						map.clearHighlights();
						return;						
					default:
						System.err.println("Error while handling backspace KeyEvent.");
						break;
					}
				}
			}

		});

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		this.add(gamePanel);
		this.setPreferredSize(clientSize);
		this.pack();
	}

	private void openOptionsMenu() {
		ConfigurationWindow.getInstance().setVisible(true);
	}

	private void setTimerTask() {
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				gamePanel.repaint();
			}
		};
		// we set a new timer, and we set it as a daemon, so that it's in fact a
		// backgroundtask
		t = new Timer(true);
		// we shedule our task starting right now (0) and it runs every 1000/fps
		// milliseconds
		t.schedule(task, 0, MILLISECONDS / Configuration.getFPS());
	}
	
	public void setGameState(GameState state) {
		System.out.println(state);
		this.state = state;
	}
	
	public void setMap(Map map) {
		this.map = map;
		this.remove(gamePanel);
		initComponents();
		initComponents();
		setTimerTask();
		charList.clear();

		// Add the mouse listeners to the Panel
		gamePanel.addMouseListener(this);
		gamePanel.addMouseMotionListener(this);
		state = GameState.IDLE;
	}
	
	public Map getMap() {
		return map;
	}	
	
	public void setGameId(int gameId) {
		this.gameId = gameId;
	}
	
	public int getGameId() {
		return gameId;
	}
	
	public int getTeam() {
		return team;
	}

	public void setTeam(int team) {
		this.team = team;
	}
	
	public boolean canMove() {
		return canMove;
	}
	
	public boolean canAttack() {
		return canAttack;
	}
	
	public void canMove(boolean canMove) {
		this.canMove = canMove;
	}

	public void canAttack(boolean canAttack) {
		this.canAttack = canAttack;
	}
	
	public GameCharacter getCharacter(int id) {
		return charList.get(id);
	}
	
	public GameCharacter getSelectedCharacter() {
		for (GameCharacter c : charList.values()) {
			if (c.isSelected())
				return c;
		}
		return null;
	}
	
	public int getCharacterId() {
		return characterId;
	}

	public void setCharacterId(int characterId) {
		this.characterId = characterId;
	}
	
	public void addCharacter(Point position, CharacterType type, int id, int team) {		
		switch (type) {
		case KNIGHT:
			GameCharacter c = new Knight(map.getTile(position.x, position.y), team, id);
			charList.put(id, c);
			break;
		case ARCHER:
			GameCharacter d = new Archer(map.getTile(position.x, position.y), team, id);			
			charList.put(id, d);
			break;
		case MAGE:
			GameCharacter e = new Mage(map.getTile(position.x, position.y), team, id);			
			charList.put(id, e);
			break;
			default:
				System.err.println("Error finding type");
			break;
		}
	}
			
	public void setAttackType(int type) {
		attackType = type;
	}
	
	public void addProjectile(GameCharacter att, GameCharacter def, int type) {
		System.out.println("adding projectile");
		Projectile p = new Projectile(att, def, type);
		projectiles.add(p);
		new Thread(p).start();

	}
	
	private void HighlightMoveRange() {
		GameEventHandler.getInstance().dispatchEvent(new HighlightMoveRange(getSelectedCharacter()));
	}
	
	private void HighlightAttackRange()	{
		GameEventHandler.getInstance().dispatchEvent(new HighlightAttackRange(getSelectedCharacter()));
	}
	
	private void HighlightStartTiles()
	{
		GameEventHandler.getInstance().dispatchEvent(new HighlightStartTiles());
	}

	public void mousePressed(MouseEvent e) {
		map.mousePressed(e);
	}

	public void mouseReleased(MouseEvent e) {
		switch(state) {
			case ADDING_CHARACTER:
				map.mouseReleased(e);
				HighlightStartTiles();
				break;
			case IDLE:
				map.mouseReleased(e);
				break;
			case TURN:
				map.mouseReleased(e);
				break;
		}
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseDragged(MouseEvent e) {
		map.mouseDragged(e);
	}

	public void mouseMoved(MouseEvent e) {
	}

}
