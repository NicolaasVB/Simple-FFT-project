package client;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;

import event.external.ChatMessageRequest;
import event.external.ServerCommunicator;

public class ChatWindow extends JFrame {

	private static final long serialVersionUID = -3568872395109010650L;
	private JPanel contentPane;
	private static ChatWindow instance;
	private JEditorPane editorPane;
	private ArrayList<String> chatmessages;
	private final int maxMessages = 20;
	private JTextField textField;
	
	public static ChatWindow getInstance() {
		if(instance == null)
			instance = new ChatWindow();
		return instance;
	}
	

	/**
	 * Create the frame.
	 */
	private ChatWindow() {
		super("Chat");
		setResizable(false);
		chatmessages = new ArrayList<String>();
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(6, 6, 438, 228);
		contentPane.add(scrollPane);
		
		editorPane = new JEditorPane();
		editorPane.setEditable(false);
		editorPane.setContentType("text/html");
		scrollPane.setViewportView(editorPane);
		
		textField = new JTextField();
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ServerCommunicator.getInstance().sendObject(new ChatMessageRequest(textField.getText()));
				textField.setText("");
			}
		});
		textField.setBounds(6, 246, 359, 26);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ServerCommunicator.getInstance().sendObject(new ChatMessageRequest(textField.getText()));
				textField.setText("");
			}
		});
		btnSend.setBounds(377, 246, 67, 29);
		contentPane.add(btnSend);
	}
	
	public void appendMessage(String sender, String message, boolean isOwner) {
		chatmessages.add("<font color=\"" + (isOwner ? "red" : "green") + "\"><b>" + sender + "</b>: " + message + "</font>");
		display();
	}
	
	public void clear() {
		chatmessages.clear();
		display();
	}
	
	private void display() {
		StringBuilder builder = new StringBuilder();
		for (int i = Math.max(0, chatmessages.size() - maxMessages); i < chatmessages.size(); i++) {
			builder.append(chatmessages.get(i));
			builder.append("<br/>");
		}
		editorPane.setText(builder.toString());
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					int endPosition = editorPane.getDocument().getLength();
					Rectangle bottom = editorPane.modelToView(endPosition);
					editorPane.scrollRectToVisible(bottom);
				} catch (BadLocationException e) {
					System.err.println("Could not scroll to " + e);
				}
			}
		});
	}
}
