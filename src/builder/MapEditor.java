package builder;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import map.Map;
import map.tiles.MapTile;
import map.tiles.NormalTile;
import map.tiles.TileOrientation;
import Application.Configuration;

public class MapEditor extends JFrame implements MouseListener,
		MouseMotionListener {
	private static final long serialVersionUID = -67106496277214320L;

	private static final String[] oriNames = { "Empty", "Normal", "North",
			"East", "South", "West", "North West", "North East",
			"South East", "South West" };

	private Map map;
	private MapTile coppiedTile = null;
	private JPopupMenu popup;

	private MapTile selectedTile = null;

	private Timer t;
	private DefaultListModel listModel;
	private DefaultListModel listModel1;
	

	private int count = 0;
	private int counter = 0;
	
	// gui variables
	private JMenuItem changeSettingsItem;
	private JMenuItem clearMapItem;
	private JMenuItem copyTileItem;
	private JTextField dimField;
	private JTextField endField;
	private JMenuItem exitItem;
	private JLabel jLabel1;
	private JLabel jLabel2;
	private JLabel jLabel3;
	private JLabel jLabel4;
	private JLabel jLabel5;
	private JLabel jLabel6;
	private JLabel jLabel8;
	private JMenu jMenu1;
	private JMenu jMenu2;
	private JMenuBar jMenuBar1;
	private JPanel jPanel1;
	private JPanel jPanel2;
	private JSeparator jSeparator1;
	private JMenuItem loadMapItem;
	private JMenuItem newMapItem;
	private JComboBox orientationBox;
	private JMenuItem pasteTileItem;
	private JMenuItem resetTileItem;
	private JMenuItem saveMapItem;
	private JTextField selectField;
	private JButton selectTile;
	private JButton setDim;
	private JButton setProperties;
	private JTextField startField;
	private JList list;
	private JButton btnRemove;

	/** Creates new form MapEditor */
	public MapEditor() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (final Exception ignored) {
		}
		map = new Map(10, 10);
		initComponents();
		setTimerTask();
		jPanel1.addMouseListener(this);
		jPanel1.addMouseMotionListener(this);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 */
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {

		jPanel1 = new JPanel() {
			private static final long serialVersionUID = 5265160414824539303L;

			public void paint(Graphics g) {
				super.paint(g);
				if (map.getHighlightedTile() == null || selectedTile == null || !map.getHighlightedTile().equals(selectedTile)) {
					this.requestFocusInWindow();
					showTileSettings();
					selectedTile = map.getHighlightedTile();
				}
				map.draw(g);
				g.drawString(counter + " fps", 50, 50);
				count++;
			}
		};
		jPanel2 = new JPanel();
		jLabel1 = new JLabel();
		dimField = new JTextField();
		selectField = new JTextField();
		jLabel2 = new JLabel();
		startField = new JTextField();
		endField = new JTextField();
		orientationBox = new JComboBox();
		jLabel3 = new JLabel();
		jLabel4 = new JLabel();
		jLabel5 = new JLabel();
		setDim = new JButton();
		selectTile = new JButton();
		setProperties = new JButton();
		jLabel6 = new JLabel();
		jLabel8 = new JLabel();
		jMenuBar1 = new JMenuBar();
		jMenu1 = new JMenu();
		newMapItem = new JMenuItem();
		saveMapItem = new JMenuItem();
		loadMapItem = new JMenuItem();
		exitItem = new JMenuItem();
		jMenu2 = new JMenu();
		clearMapItem = new JMenuItem();
		copyTileItem = new JMenuItem();
		pasteTileItem = new JMenuItem();
		resetTileItem = new JMenuItem();
		jSeparator1 = new JSeparator();
		changeSettingsItem = new JMenuItem();
		popup = new JPopupMenu();
		listModel = new DefaultListModel();
		listModel1 = new DefaultListModel();

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		jPanel1.setBorder(BorderFactory.createTitledBorder("Editor"));

		GroupLayout gl_jPanel1 = new GroupLayout(jPanel1);
		jPanel1.setLayout(gl_jPanel1);
		gl_jPanel1.setHorizontalGroup(gl_jPanel1.createParallelGroup(
				GroupLayout.Alignment.LEADING).addGap(0, 518, Short.MAX_VALUE));
		gl_jPanel1.setVerticalGroup(gl_jPanel1.createParallelGroup(
				GroupLayout.Alignment.LEADING).addGap(0, 456, Short.MAX_VALUE));

		jPanel2.setBorder(BorderFactory.createTitledBorder("Properties"));

		jLabel1.setText("Dimensions");

		dimField.setText("width_# x height_#");
		dimField.setToolTipText("Enter the dimensions of the new Map:\n<width> x <height>");

		selectField.setText("tile_x , tile_y");
		selectField
				.setToolTipText("Enter the coordinates of the tile to select:\n<x_coordinate>,<y_coordinate>");

		jLabel2.setText("Selected Tile");

		startField.setText("startHeight");

		endField.setText("startHeight");

		orientationBox.setModel(new DefaultComboBoxModel(oriNames));

		jLabel3.setText("Start Height");

		jLabel4.setText("End Height");

		jLabel5.setText("Orientation");

		setDim.setText("Set");
		setDim.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				setDimActionPerformed();
			}
		});

		selectTile.setText("Select");
		selectTile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				selectTileActionPerformed();
			}
		});

		setProperties.setText("Set");
		setProperties.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				setPropertiesActionPerformed();
			}
		});

		jLabel6.setText("Tile Properties:");

		jLabel8.setText("Start Tiles Team 1:");
		
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (map.getHighlightedTile() != null && map.getHighlightedTile().getStartTileForTeam() == 0) {
					map.getHighlightedTile().setStartTileForTeam(1);
					listModel.addElement((map.getHighlightedTile().getFieldLocation().x + 1) + ", " + (map.getHighlightedTile().getFieldLocation().y + 1));
				}
			}
		});
		
		btnRemove = new JButton("Remove");
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (list.getSelectedIndex() != -1) {
					// Tile zoeken
					String[] input = ((String) list.getSelectedValue()).split(",");
					int x = Integer.parseInt(input[0].trim()) - 1;
					int y = Integer.parseInt(input[1].trim()) - 1;
					map.getTile(x, y).setStartTileForTeam(0);
					
					listModel.remove(list.getSelectedIndex());
				}
			}
		});
		
		JScrollPane scrollPane = new JScrollPane();
		
		JLabel lblNewLabel = new JLabel("Start Tiles Team 2:");
		
		JScrollPane scrollPane_1 = new JScrollPane();
		
		JButton btnAdd_1 = new JButton("Add");
		btnAdd_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (map.getHighlightedTile() != null && map.getHighlightedTile().getStartTileForTeam() == 0) {
					map.getHighlightedTile().setStartTileForTeam(2);
					listModel1.addElement((map.getHighlightedTile().getFieldLocation().x + 1) + ", " + (map.getHighlightedTile().getFieldLocation().y + 1));
				}
			}
		});

		final JList list_1 = new JList(listModel1);
		JButton btnRemove_1 = new JButton("Remove");
		btnRemove_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (list_1.getSelectedIndex() != -1) {
					// Tile zoeken
					String[] input = ((String) list_1.getSelectedValue()).split(",");
					int x = Integer.parseInt(input[0].trim()) - 1;
					int y = Integer.parseInt(input[1].trim()) - 1;
					map.getTile(x, y).setStartTileForTeam(0);
					
					listModel1.remove(list_1.getSelectedIndex());
				}
			}
		});

		GroupLayout gl_jPanel2 = new GroupLayout(jPanel2);
		gl_jPanel2.setHorizontalGroup(
			gl_jPanel2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_jPanel2.createSequentialGroup()
					.addComponent(setProperties)
					.addContainerGap(212, Short.MAX_VALUE))
				.addGroup(gl_jPanel2.createSequentialGroup()
					.addGap(10)
					.addGroup(gl_jPanel2.createParallelGroup(Alignment.LEADING)
						.addComponent(orientationBox, 0, 146, Short.MAX_VALUE)
						.addComponent(startField, GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
						.addComponent(endField, GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_jPanel2.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_jPanel2.createSequentialGroup()
							.addGroup(gl_jPanel2.createParallelGroup(Alignment.LEADING)
								.addComponent(jLabel4)
								.addComponent(jLabel5))
							.addGap(4))
						.addComponent(jLabel3))
					.addContainerGap(56, Short.MAX_VALUE))
				.addGroup(gl_jPanel2.createSequentialGroup()
					.addGap(10)
					.addComponent(jLabel6)
					.addContainerGap(189, Short.MAX_VALUE))
				.addGroup(gl_jPanel2.createSequentialGroup()
					.addContainerGap()
					.addComponent(jLabel8)
					.addContainerGap(170, Short.MAX_VALUE))
				.addGroup(gl_jPanel2.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnAdd)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnRemove)
					.addGap(113))
				.addGroup(gl_jPanel2.createSequentialGroup()
					.addComponent(selectTile)
					.addContainerGap())
				.addGroup(gl_jPanel2.createSequentialGroup()
					.addComponent(setDim)
					.addContainerGap())
				.addGroup(gl_jPanel2.createSequentialGroup()
					.addGap(10)
					.addGroup(gl_jPanel2.createParallelGroup(Alignment.TRAILING)
						.addComponent(dimField, GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
						.addComponent(selectField, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_jPanel2.createParallelGroup(Alignment.LEADING)
						.addComponent(jLabel2)
						.addComponent(jLabel1))
					.addContainerGap(53, Short.MAX_VALUE))
				.addGroup(gl_jPanel2.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_jPanel2.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane_1, Alignment.TRAILING, 0, 0, Short.MAX_VALUE)
						.addGroup(gl_jPanel2.createParallelGroup(Alignment.TRAILING, false)
							.addComponent(lblNewLabel, Alignment.LEADING)
							.addComponent(scrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)))
					.addContainerGap(58, Short.MAX_VALUE))
				.addGroup(gl_jPanel2.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnAdd_1)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnRemove_1)
					.addContainerGap(113, Short.MAX_VALUE))
		);
		gl_jPanel2.setVerticalGroup(
			gl_jPanel2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_jPanel2.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_jPanel2.createParallelGroup(Alignment.BASELINE)
						.addComponent(dimField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(jLabel1))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(setDim)
					.addGap(18)
					.addGroup(gl_jPanel2.createParallelGroup(Alignment.BASELINE)
						.addComponent(selectField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(jLabel2))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(selectTile)
					.addGap(12)
					.addComponent(jLabel6)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_jPanel2.createParallelGroup(Alignment.BASELINE)
						.addComponent(startField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(jLabel3))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_jPanel2.createParallelGroup(Alignment.BASELINE)
						.addComponent(endField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(jLabel4))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_jPanel2.createParallelGroup(Alignment.BASELINE)
						.addComponent(orientationBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(jLabel5))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(setProperties)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(jLabel8)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_jPanel2.createParallelGroup(Alignment.LEADING)
						.addComponent(btnAdd)
						.addComponent(btnRemove))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNewLabel)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
					.addGroup(gl_jPanel2.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnAdd_1)
						.addComponent(btnRemove_1)))
		);
		gl_jPanel2.linkSize(SwingConstants.HORIZONTAL, new Component[] {setDim, selectTile, setProperties});
		
		scrollPane_1.setViewportView(list_1);
		list = new JList(listModel);
		scrollPane.setViewportView(list);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jPanel2.setLayout(gl_jPanel2);

		jMenu1.setText("File");

		newMapItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				InputEvent.CTRL_MASK));
		newMapItem.setText("New Map");
		newMapItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				newMapItemActionPerformed();
			}
		});
		jMenu1.add(newMapItem);

		saveMapItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				InputEvent.CTRL_MASK));
		saveMapItem.setText("Save Map");
		saveMapItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				saveMapItemActionPerformed();
			}
		});
		jMenu1.add(saveMapItem);

		loadMapItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				InputEvent.CTRL_MASK));
		loadMapItem.setText("Load Map");
		loadMapItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				loadMapItemActionPerformed();
			}
		});
		jMenu1.add(loadMapItem);

		exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4,
				InputEvent.ALT_MASK));
		exitItem.setText("Exit");
		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				exitItemActionPerformed();
			}
		});
		jMenu1.add(exitItem);

		jMenuBar1.add(jMenu1);

		jMenu2.setText("Edit");

		clearMapItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_BACK_SPACE, InputEvent.ALT_MASK
						| InputEvent.CTRL_MASK));
		clearMapItem.setText("Clear Map");
		clearMapItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				clearMapItemActionPerformed();
			}
		});
		jMenu2.add(clearMapItem);

		copyTileItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
				InputEvent.CTRL_MASK));
		copyTileItem.setText("Copy Tile");
		copyTileItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				copyTileItemActionPerformed();
			}
		});
		jMenu2.add(copyTileItem);

		pasteTileItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,
				InputEvent.CTRL_MASK));
		pasteTileItem.setText("Paste Tile");
		pasteTileItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				pasteTileItemActionPerformed();
			}
		});
		jMenu2.add(pasteTileItem);

		resetTileItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
				InputEvent.CTRL_MASK));
		resetTileItem.setText("Reset Tile");
		resetTileItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				resetTileItemActionPerformed();
			}
		});
		jMenu2.add(resetTileItem);
		jMenu2.add(jSeparator1);

		changeSettingsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
				InputEvent.CTRL_MASK));
		changeSettingsItem.setText("Change Settings");
		changeSettingsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				changeSettingsItemActionPerformed();
			}
		});
		jMenu2.add(changeSettingsItem);

		jMenuBar1.add(jMenu2);

		setJMenuBar(jMenuBar1);

		GroupLayout layout = new GroupLayout(getContentPane());
		layout.setHorizontalGroup(
			layout.createParallelGroup(Alignment.TRAILING)
				.addGroup(layout.createSequentialGroup()
					.addContainerGap()
					.addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(jPanel2, GroupLayout.PREFERRED_SIZE, 263, GroupLayout.PREFERRED_SIZE)
					.addGap(48))
		);
		layout.setVerticalGroup(
			layout.createParallelGroup(Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
					.addContainerGap()
					.addGroup(layout.createParallelGroup(Alignment.TRAILING)
						.addComponent(jPanel2, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 533, Short.MAX_VALUE)
						.addComponent(jPanel1, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 664, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		getContentPane().setLayout(layout);

		JMenuItem dummy = new JMenuItem("Copy Tile");
		dummy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
				InputEvent.CTRL_MASK));
		dummy.addActionListener(copyTileItem.getActionListeners()[0]);
		popup.add(dummy);
		dummy = new JMenuItem("Paste Tile");
		dummy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,
				InputEvent.CTRL_MASK));
		dummy.addActionListener(pasteTileItem.getActionListeners()[0]);
		popup.add(dummy);
		dummy = new JMenuItem("Reset Tile");
		dummy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
				InputEvent.CTRL_MASK));
		dummy.addActionListener(resetTileItem.getActionListeners()[0]);
		popup.add(dummy);
		popup.add(new JSeparator());
		dummy = new JMenuItem("Clear Map");
		dummy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE,
				InputEvent.ALT_MASK | InputEvent.CTRL_MASK));
		dummy.addActionListener(clearMapItem.getActionListeners()[0]);
		popup.add(dummy);

		// Add listener to components that can bring up popup menus.
		MouseListener popupListener = new PopupListener();
		jPanel1.addMouseListener(popupListener);

		jPanel1.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int x = map.getHighlightedTile().getFieldLocation().x;
				int y = map.getHighlightedTile().getFieldLocation().y;
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					if (x - 1 == -1)
						return;
					else
						map.setHighlightedTile(x - 1, y);
				}
				if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					if (x + 1 == map.getWidth())
						return;
					else
						map.setHighlightedTile(x + 1, y);
				}
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					if (y + 1 == map.getHeight())
						return;
					else
						map.setHighlightedTile(x, y + 1);
				}
				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					if (y - 1 == -1)
						return;
					else
						map.setHighlightedTile(x, y - 1);
				}
			}
		});

		pack();
	}// </editor-fold>

	/**
	 * sets a daemon task to repaint the panel at the set fps
	 */
	private void setTimerTask() {
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				jPanel1.repaint();
			}
		};
		TimerTask resetCounter = new TimerTask() {
			@Override
			public void run() {
				counter = count;
				count = 0;
			}
		};
		// we set a new timer, and we set it as a daemon, so that it's in fact a
		// backgroundtask
		t = new Timer(true);
		// we shedule our task starting right now (0) and it runs every 1000/fps
		// milliseconds
		t.schedule(task, 0, 1000 / Configuration.getFPS());
		t.schedule(resetCounter, 0, 1000);
	}

	/**
	 * Fills in the settings of the selected tile.
	 */
	private void showTileSettings() {
		MapTile tile = map.getHighlightedTile();

		if (tile == null)
			return;

		int sHeight = tile.getStartHeight();
		int eHeight = tile.getEndHeight();
		TileOrientation ori = tile.getOrientation();
		Point loc = tile.getFieldLocation();

		dimField.setText(map.getWidth() + "x" + map.getHeight());
		startField.setText(Integer.toString(sHeight));
		endField.setText(Integer.toString(eHeight));
		orientationBox.setSelectedIndex(ori.ordinal());
		selectField.setText((loc.x + 1) + "," + (loc.y + 1));
	}

	/**
	 * Sets the dimension of the map
	 */
	private void setDimActionPerformed() {
		try {
			String[] input = dimField.getText().split("x");
			int width = Integer.parseInt(input[0]);
			int height = Integer.parseInt(input[1]);
			map.setMapSize(width, height);
			return;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "That isn't a valid Dimension.\n"
									+ "Please enter the Dimension in the following way:\n"
									+ "width x height", "Error!", JOptionPane.ERROR_MESSAGE);
			return;
		}
	}

	/**
	 * Selects the tile at given location
	 */
	private void selectTileActionPerformed() {
		try {
			String[] input = selectField.getText().split(",");
			int x = Integer.parseInt(input[0]) - 1;
			int y = Integer.parseInt(input[1]) - 1;
			map.setHighlightedTile(x, y);
			return;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "That isn't a valid tile.\n"
					+ "Please select a tile in the following way:\n"
					+ "xValue,yValue where:\n"
					+ "field_width>x>1 and field_height>y>1.", "Error!",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
	}

	/**
	 * Sets the properties of a tile with the given settings.
	 */
	private void setPropertiesActionPerformed() {
		try {
			int sHeight = Integer.parseInt(startField.getText());
			int eHeight = Integer.parseInt(endField.getText());
			TileOrientation ori = TileOrientation.values()[orientationBox.getSelectedIndex()];
			if (sHeight > eHeight && !(ori == TileOrientation.NORTH_EAST || ori == TileOrientation.NORTH_WEST || ori == TileOrientation.SOUTH_EAST || ori == TileOrientation.SOUTH_WEST)) {
				JOptionPane.showMessageDialog(null, "That isn't a valid height.\n"
						+ "Only cornerTiles can have a startHeight higher than endHeight.",
						"Error!", JOptionPane.ERROR_MESSAGE);
			}
			if (Math.abs(sHeight - eHeight) > 3) {
				JOptionPane.showMessageDialog(null, "That isn't a valid height.\n"
						+ "The max difference in height is 3",
						"Error!", JOptionPane.ERROR_MESSAGE);
			}
			if (ori == map.getHighlightedTile().getOrientation()) {
				map.getHighlightedTile().setStartHeight(sHeight);
				map.getHighlightedTile().setEndHeight(eHeight);
			} else {
				int x = map.getHighlightedTile().getFieldLocation().x;
				int y = map.getHighlightedTile().getFieldLocation().y;
				Point absoluteLocation = map.getHighlightedTile().getAbsoluteLocation();
				map.setTileData(x, y, TileOrientation.getTileFromOrientation(ori, sHeight, eHeight, map));
				map.getField().get(x).get(y).setFieldLocation(new Point(x, y));
				map.getField().get(x).get(y).setAbsoluteLocation(absoluteLocation);
				map.setHighlightedTile(x, y);
			}
			return;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "That isn't a valid height.\n"
					+ "Please enter a positive numerical value in the start\n"
					+ "and end height fields (including decimal numbers)",
					"Error!", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			return;
		}
	}

	/**
	 * This is a placeholder menuItem for anything you might want to implement.
	 */
	private void changeSettingsItemActionPerformed() {
		// TODO add your handling code here:
	}

	/**
	 * Resets the given tile to a normal tile with height = 1
	 */
	private void resetTileItemActionPerformed() {
		int x = map.getHighlightedTile().getFieldLocation().x;
		int y = map.getHighlightedTile().getFieldLocation().y;
		Point absoluteLocation = map.getHighlightedTile().getAbsoluteLocation();
		map.setTileData(x, y, new NormalTile(1, map));
		map.getField().get(x).get(y).setFieldLocation(new Point(x, y));
		map.getField().get(x).get(y).setAbsoluteLocation(absoluteLocation);
		map.setHighlightedTile(x, y);
	}

	/**
	 * Pastes a copied tile to the selected tile.
	 */
	private void pasteTileItemActionPerformed() {
		int x = map.getHighlightedTile().getFieldLocation().x;
		int y = map.getHighlightedTile().getFieldLocation().y;
		Point absolute = map.getHighlightedTile().getAbsoluteLocation();
		if (coppiedTile == null)
			return;
		map.setTileData(x, y, coppiedTile.clone());
		map.getField().get(x).get(y).setFieldLocation(new Point(x, y));
		map.getField().get(x).get(y).setAbsoluteLocation(absolute);
		map.setHighlightedTile(x, y);
	}

	/**
	 * copies the selected tile.
	 */
	private void copyTileItemActionPerformed() {
		coppiedTile = map.getHighlightedTile();
	}

	/**
	 * Creates a new map with all normal tiles at the same height with the same dimensions.
	 */
	private void clearMapItemActionPerformed() {
		jPanel1.removeMouseListener(this);
		jPanel1.removeMouseMotionListener(this);
		map = new Map(map.getWidth(), map.getHeight());
		jPanel1.addMouseListener(this);
		jPanel1.addMouseMotionListener(this);
	}

	/**
	 * Creates a new map with all normal tiles at the same height with given dimensions
	 */
	private void newMapItemActionPerformed() {
		int w = 0, h = 0;
		String width = JOptionPane.showInputDialog(
				"Enter the new Map's width.", "Width");
		String height = JOptionPane.showInputDialog(
				"Enter the new Map's height.", "Height");
		try {
			w = Integer.parseInt(width);
			h = Integer.parseInt(height);
			jPanel1.removeMouseListener(this);
			jPanel1.removeMouseMotionListener(this);
			map = new Map(w, h);
			jPanel1.addMouseListener(this);
			jPanel1.addMouseMotionListener(this);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Invalid Dimension.");
			newMapItemActionPerformed();
		}
	}

	/**
	 * Saves the current map.
	 */
	private void saveMapItemActionPerformed() {
		map.writeToFile();
	}

	/**
	 * Loads a map from a .imf file
	 */
	private void loadMapItemActionPerformed() {
		final JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new FileNameExtensionFilter("IMF file", "imf"));
		int choice = chooser.showOpenDialog(null);
		if (choice == JFileChooser.APPROVE_OPTION) {
			jPanel1.removeMouseListener(this);
			jPanel1.removeMouseMotionListener(this);
			this.map = Map.loadMap(chooser.getSelectedFile());
			jPanel1.addMouseListener(this);
			jPanel1.addMouseMotionListener(this);
			listModel.clear();
			for (ArrayList<MapTile> lineOfTiles : map.getField()) {
				for (MapTile tile : lineOfTiles) {
					if (tile.getStartTileForTeam() == 1) {
						listModel.addElement((tile.getFieldLocation().x + 1) + ", " + (tile.getFieldLocation().y + 1));
					} else if(tile.getStartTileForTeam() == 2) {
						listModel1.addElement((tile.getFieldLocation().x + 1) + ", " + (tile.getFieldLocation().y + 1));
					}
				}
			}
		} 
	}

	/**
	 * exits
	 */
	private void exitItemActionPerformed() {
		System.exit(0);
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				Configuration.readConfigFile();
				new MapEditor().setVisible(true);
			}
		});
	}

	class PopupListener extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			maybeShowPopup(e);
		}

		public void mouseReleased(MouseEvent e) {
			maybeShowPopup(e);
		}

		private void maybeShowPopup(MouseEvent e) {
			if (e.isPopupTrigger()) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}

	public void mousePressed(MouseEvent e) {
		map.mousePressed(e);
	}

	public void mouseReleased(MouseEvent e) {
		map.mouseReleased(e);
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
