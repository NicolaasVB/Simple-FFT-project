package client;

import event.external.CreateGameRequest;
import event.external.GetGamesRequest;
import event.external.GetMapsRequest;
import event.external.JoinGameRequest;
import event.external.ServerCommunicator;
import game.Game;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import map.Map;

import org.eclipse.wb.swing.FocusTraversalOnArray;

public class Lobby extends JFrame {

	private static final long serialVersionUID = 7596499194933315751L;
	private JPanel contentPane;
	private DefaultListModel listModel;
	private static Lobby INSTANCE;
	private final JComboBox comboBox;
	private final JButton btnCreate;
	private final JButton btnJoinGame;
	private final JSpinner spinner;
	private JScrollPane scrollPane;
	private final JList list;
	
	
	public static Lobby getInstance() {
		if (INSTANCE == null)
			INSTANCE = new Lobby();
		return INSTANCE;
	}
	
	public void setMaps(Hashtable<String, Map> maps) {
		if (comboBox != null) {
			comboBox.removeAllItems();
			int index = 0;
			for (Map map : maps.values()) {
				comboBox.insertItemAt(map, index);
				index++;
			}
		}
	}
	
	public synchronized void setGames(final ArrayList<Game> games) {
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				if (listModel != null) {
					listModel.clear();
					int index = 0;
					for (Game game : games) {
						listModel.add(index, game);
						index++;
					}
				}
			}
			
		});
	}
	
	/**
	 * Create the frame.
	 */
	private Lobby() {
		setResizable(false);
		setTitle("Team Tactics Lobby");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 508, 301);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblGames = new JLabel("Games:");
		lblGames.setBounds(6, 6, 61, 16);
		contentPane.add(lblGames);
		
		btnJoinGame = new JButton("Join");
		btnJoinGame.setBounds(267, 244, 69, 29);
		contentPane.add(btnJoinGame);
		
		listModel = new DefaultListModel();
		
		JLabel lblNewGame = new JLabel("Create game:");
		lblNewGame.setBounds(348, 26, 118, 16);
		contentPane.add(lblNewGame);
		
		JLabel lblMap_1 = new JLabel("Map:");
		lblMap_1.setBounds(358, 55, 61, 16);
		contentPane.add(lblMap_1);
		
		comboBox = new JComboBox();
		comboBox.setBounds(368, 75, 134, 27);
		contentPane.add(comboBox);
		
		JLabel lblCharacters_1 = new JLabel("# Characters:");
		lblCharacters_1.setBounds(356, 101, 95, 16);
		contentPane.add(lblCharacters_1);
		
		spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(5, 1, 15, 1));
		spinner.setBounds(378, 117, 47, 28);
		contentPane.add(spinner);
		
		btnCreate = new JButton("Create");
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (comboBox.getSelectedIndex() != -1) {
					CreateGameRequest req = new CreateGameRequest(((Map)comboBox.getSelectedItem()).mapToString(), Integer.parseInt("" + spinner.getValue()));
					ServerCommunicator.getInstance().sendObject(req);
				} else {
					JOptionPane.showMessageDialog(null, "You didn't choose a map.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnCreate.setBounds(397, 157, 105, 29);

		list = new JList(listModel);

		btnJoinGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (list.getSelectedValue() != null) {
					Game game = (Game)list.getSelectedValue();
					ServerCommunicator.getInstance().sendObject(new JoinGameRequest(game.getId()));
					enableDisableControls(false);
				} else {
					JOptionPane.showMessageDialog(null, "You didn't choose a game.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		contentPane.add(btnCreate);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(6, 29, 330, 203);
		contentPane.add(scrollPane);
		scrollPane.setViewportView(list);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		contentPane.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{lblGames, list, btnJoinGame, lblNewGame}));
		ServerCommunicator.getInstance().sendObject(new GetMapsRequest());
		updateGames();
	}
	
	public void enableDisableControls(boolean enabled) {
		if (enabled) {
			btnCreate.setEnabled(true);
			btnJoinGame.setEnabled(true);
			comboBox.setEnabled(true);
			spinner.setEnabled(true);
		} else {
			btnCreate.setEnabled(false);
			btnJoinGame.setEnabled(false);
			comboBox.setEnabled(false);
			spinner.setEnabled(false);
		}
	}
	
	public void updateGames() {
		ServerCommunicator.getInstance().sendObject(new GetGamesRequest());
	}
}
