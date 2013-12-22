package client;




import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import Application.Configuration;
import event.external.EstablishConnectionRequest;
import event.external.ServerCommunicator;

public class LoginWindow extends JFrame {

	private static final long serialVersionUID = -1052650142652095062L;
	private JPanel contentPane;
	private JTextField txtNickname;
	private JTextField txtServer;
	private static LoginWindow instance;
	
	public static LoginWindow getInstance() {
		if (instance == null)
			instance = new LoginWindow();
		return instance;
	}

	/**
	 * Create the frame.
	 */
	private LoginWindow() {
		setTitle("Team Tactics Login");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 268, 182);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNickname = new JLabel("Nickname:");
		lblNickname.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNickname.setBounds(6, 12, 106, 16);
		contentPane.add(lblNickname);
		
		txtNickname = new JTextField();
		txtNickname.setText(Configuration.getNickname());
		txtNickname.setBounds(124, 6, 134, 28);
		contentPane.add(txtNickname);
		txtNickname.setColumns(10);
		
		txtServer = new JTextField();
		txtServer.setText(Configuration.getIpAddress());
		txtServer.setBounds(124, 46, 134, 28);
		contentPane.add(txtServer);
		txtServer.setColumns(10);
		
		JLabel lblServerAddress = new JLabel("Server Address:");
		lblServerAddress.setHorizontalAlignment(SwingConstants.RIGHT);
		lblServerAddress.setBounds(6, 52, 106, 16);
		contentPane.add(lblServerAddress);
		
		JLabel lblServerPort = new JLabel("Server Port:");
		lblServerPort.setHorizontalAlignment(SwingConstants.RIGHT);
		lblServerPort.setBounds(6, 92, 106, 16);
		contentPane.add(lblServerPort);
		
		final JSpinner spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(Configuration.getPort(), 1025, 65535, 1));
		spinner.setBounds(124, 86, 134, 28);
		contentPane.add(spinner);
		
		final JButton btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Configuration.setNickname(txtNickname.getText());
				Configuration.setIpAddress(txtServer.getText());
				Configuration.setPort(Integer.parseInt("" + spinner.getValue()));
				Configuration.writeConfigFile();
				new Thread(ServerCommunicator.getInstance()).start();
				if(ServerCommunicator.getInstance().isRunning())
					ServerCommunicator.getInstance().sendObject(new EstablishConnectionRequest(Configuration.getNickname()));
				else
					JOptionPane.showMessageDialog(null, "The program couldn't make a connection with " + txtServer.getText() + ":" + spinner.getValue(), "Connection error", JOptionPane.ERROR_MESSAGE);
			}
		});
		btnConnect.setBounds(141, 126, 117, 29);
		contentPane.add(btnConnect);
	}
}
