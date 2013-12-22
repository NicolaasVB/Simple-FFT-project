package client;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JCheckBox;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JButton;

import Application.Configuration;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ConfigurationWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 208619245904614496L;
	private JPanel contentPane;
	private static ConfigurationWindow INSTANCE;
	
	public static ConfigurationWindow getInstance()
	{
		if(INSTANCE == null)
			INSTANCE = new ConfigurationWindow();
		return INSTANCE;
	}

	/**
	 * Create the frame.
	 */
	private ConfigurationWindow() {
		Configuration.readConfigFile();
		
		setResizable(false);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 318, 159);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblCharacterMoveTime = new JLabel("Character Move Time:");
		lblCharacterMoveTime.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCharacterMoveTime.setBounds(6, 19, 172, 16);
		contentPane.add(lblCharacterMoveTime);
		
		JLabel lblFps = new JLabel("FPS:");
		lblFps.setHorizontalAlignment(SwingConstants.RIGHT);
		lblFps.setBounds(16, 47, 162, 16);
		contentPane.add(lblFps);
		
		final JCheckBox chckbxUseLessResources = new JCheckBox("Use Less Resources");
		chckbxUseLessResources.setBounds(157, 75, 183, 23);
		chckbxUseLessResources.setSelected(Configuration.useLessResources());
		contentPane.add(chckbxUseLessResources);
		
		final JSpinner spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(Configuration.getCharacterMoveTime(), 0, 600, 50));
		spinner.setBounds(182, 13, 55, 28);
		contentPane.add(spinner);
		
		final JSpinner spinner_1 = new JSpinner();
		spinner_1.setModel(new SpinnerNumberModel(Configuration.getFPS(), 20, 75, 1));
		spinner_1.setBounds(182, 43, 55, 28);
		contentPane.add(spinner_1);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Configuration.setFps(Integer.parseInt("" + spinner_1.getValue()));
				Configuration.setCharacterMoveTime(Integer.parseInt("" + spinner.getValue()));
				Configuration.setLuseResources(chckbxUseLessResources.isSelected());
				Configuration.writeConfigFile();
				dispose();
			}
		});
		btnSave.setBounds(182, 102, 105, 29);
		contentPane.add(btnSave);
		
		JLabel lblMs = new JLabel("ms");
		lblMs.setBounds(238, 18, 61, 16);
		contentPane.add(lblMs);
	}
}
