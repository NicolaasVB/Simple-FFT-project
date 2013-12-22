package event.external;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JOptionPane;

import Application.Configuration;
import event.RequestGameEvent;

public class ServerCommunicator implements Runnable {
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private Socket socket;
	private static ServerCommunicator INSTANCE;
	private boolean listening = true;
	private boolean running = false;
	private long lastHeartBeat;
	private static final int TIMEOUT = 15000;
	private static final int HEARTBEAT_INTERVAL	 = 1000;
	
	public static ServerCommunicator getInstance() {
		if (INSTANCE == null)
			INSTANCE = new ServerCommunicator();
		return INSTANCE;
	}
	
	private ServerCommunicator() {
		try {
			socket = new Socket(Configuration.getIpAddress(), Configuration.getPort());
		} catch (Exception e) {
			System.out.println("Kon geen verbinding maken met de server: " + e);
			return;
		}
		
		System.out.println("Connectie gemaakt met server " + socket.getInetAddress() + ":" + socket.getPort());
		
		try {
			input = new ObjectInputStream(socket.getInputStream());
			output = new ObjectOutputStream(socket.getOutputStream());
			System.out.println("Input en outputstreams werden aangemaakt");
			running = true;
		} catch(IOException e) {
			System.out.println("Kon geen input of outputstream maken: " + e);
			running = false;
			return;
		}
	}
	
	public void sendObject(Object o) {
		try {
			output.writeObject(o);
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Object receiveObject() {
		try {
			return input.readObject();
		} catch (IOException e) {
			listening = false;
			JOptionPane.showMessageDialog(null, "The game lost its connection to the server.\nRestart the game to reconnect.", "Lost connection", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void heartBeatReceived()
	{
		lastHeartBeat = new Date().getTime();
	}

	@Override
	public void run() 
	{
		if(!running)
		{
			INSTANCE = null;
			return;
		}
		
		heartBeatReceived();
		Timer t = new Timer();
		t.scheduleAtFixedRate(new TimerTask(){

			@Override
			public void run() {
				if(!listening)
				{
					this.cancel();
					return;
				}
				
				ServerCommunicator.getInstance().sendObject(new HeartBeat());
				
				if(new Date().getTime() - lastHeartBeat > TIMEOUT)
				{
					listening = false;
					System.out.println("Lost connection to server: Didn't receive any heartbeats for 3 seconds.");
					JOptionPane.showMessageDialog(null, "The game lost its connection to the server.\nRestart the game to reconnect.", "Lost connection", JOptionPane.ERROR_MESSAGE);
					System.exit(0);
				}
			}
			
		}, new Date(new Date().getTime() + 1000), HEARTBEAT_INTERVAL);
		
		while (listening) {
			Object o = receiveObject();
			if (o instanceof RequestGameEvent) {
				if(!(o instanceof HeartBeat))
					System.out.println("received object from server (" + o.getClass().getName() + ")");
				RequestGameEvent req = (RequestGameEvent) o;
				req.process();
			}
		}
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
}
