package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import event.external.ServerCommunicator;
import event.external.StopGameRequest;

public class WaitForOtherPlayerWindow extends JFrame {

	private static final long serialVersionUID = 5455677382001753707L;
	private JPanel contentPane;
	private static WaitForOtherPlayerWindow instance;
	private int gameId;

	public static WaitForOtherPlayerWindow getInstance() {
		if (instance == null)
			instance = new WaitForOtherPlayerWindow();
		return instance;
	}

	/**
	 * Create the frame.
	 */
	private WaitForOtherPlayerWindow() {
		setTitle("Waiting...");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 382, 103);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblWaitingForOther = new JLabel("Waiting for another player to join the game...");
		lblWaitingForOther.setHorizontalAlignment(SwingConstants.CENTER);
		lblWaitingForOther.setBounds(6, 6, 370, 16);
		contentPane.add(lblWaitingForOther);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ServerCommunicator.getInstance().sendObject(new StopGameRequest(gameId));
			}
		});
		btnCancel.setBounds(137, 34, 117, 29);
		contentPane.add(btnCancel);
	}

	public void setGameId(int gameId)  {
		this.gameId = gameId;
	}
	
	public int getGameId() {
		return gameId;
	}

}
