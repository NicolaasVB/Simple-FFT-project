package server;

import event.RequestGameEvent;
import event.external.ChatMessageRequest;
import event.external.HeartBeat;
import event.external.StopGameRequest;
import game.Game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientHandler implements Runnable
{
	private Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private Game game;
	private String nickname = "";
	private boolean alive;
	private int numCharacters = 0;
	private int team = 0;
	private long lastHeartBeat;
	private static final int TIMEOUT = 15000;
	private static final int HEARTBEAT_INTERVAL = 1000;
	private boolean listening = true;
	
	public int getNumCharacters() {
		return numCharacters;
	}

	public void setNumCharacters(int numCharacters) {
		this.numCharacters = numCharacters;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public String getNickname()
	{
		return nickname.equals("") ? "anonymous" : nickname;
	}
	
	public void setGame(Game game)
	{
		this.game = game;
	}
	
	public Game getGame()
	{
		return game;
	}
	
	public void kill()
	{
		this.alive = false;
	}
	
	public ClientHandler(Socket socket)
	{
		this.socket = socket;
	}
	
	@Override
	public void run() 
	{	
		alive = true;
		
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
				
				if(new Date().getTime() - lastHeartBeat > TIMEOUT)
				{
					listening = false;
					alive = false;
					System.out.println("Lost connection to client: Didn't receive any heartbeats for 3 seconds.");
					Logger.getLogger("log").fine("Lost connection to client: Didn't receive any heartbeats for 3 seconds.");
				}
			}
			
		}, new Date(), HEARTBEAT_INTERVAL);
		
		System.out.println("Thread is trying to create input and output streams");
		Logger.getLogger("log").fine("Thread is trying to create input and output streams");
		try
		{
			output = new ObjectOutputStream(socket.getOutputStream());
			output.flush();
			input = new ObjectInputStream(socket.getInputStream());
		}
		catch(IOException e)
		{
			System.out.println("Couldn't create input and/or output stream: " + e.getMessage());
			Logger.getLogger("log").log(Level.WARNING, "Couldn't create input and/or output stream");
		}
		
		System.out.println("Waiting for an object to be sent by the client...");
		Logger.getLogger("log").fine("Waiting for an object to be sent by the client...");
		try
		{
			while(listening)
			{
				if(!alive)
					return;
				
				Object o = input.readObject();
				if(o instanceof ChatMessageRequest)
				{
					Logger.getLogger("chat").fine("Received chatmessage from " + this + " with message: " + ((ChatMessageRequest)o).getMessage());
					System.out.println("Received chatmessage from " + this + " with message: " + ((ChatMessageRequest)o).getMessage());
				}
				else if(!(o instanceof HeartBeat))
				{
					Logger.getLogger("log").fine("Received message of type " + o.getClass().getName() + " from " + this);
					System.out.println("Received message of type " + o.getClass().getName() + " from " + this);
				}
				
				if(o instanceof RequestGameEvent)
				{
					RequestGameEvent req = (RequestGameEvent) o;
					req.setClientHandler(this);
					if(req.verify())
					{
						sendRequest(req);
						if(!(o instanceof HeartBeat))
						{
							Logger.getLogger("log").fine("Verified message of type " + o.getClass().getName());
							System.out.println("Verified message of type " + o.getClass().getName());
						}
					}
					else 
					{
						if(!(o instanceof HeartBeat))
						{
							Logger.getLogger("log").fine("Didn't verify message of type " + o.getClass().getName());
							System.out.println("Didn't verify message of type " + o.getClass().getName());
						}
					}
				}
				output.flush();
			}
		}
		catch(IOException e)
		{
			System.out.println("Client (" + this + ") connection terminated with an IOException: " + e.getMessage());
			Logger.getLogger("log").fine("Client (" + this + ") connection terminated with an IOException");
			listening = false;
			alive = false;
			if(game != null)
			{
				if(game.getPlayers().size() == 2)
				{
					Server.getGames().remove(game);
				}
				else
				{
					game.getPlayers().remove(this);
				}
				sendRequest(new StopGameRequest(game.getId()));
			}
			return;
		}
		catch(ClassNotFoundException e)
		{
			Logger.getLogger("log").log(Level.WARNING, "The received object is of an unknown type", e);
			System.out.println("The received object is of an unknown type");
		}
		finally
		{
			try 
			{
				output.close();
				input.close();
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	public void sendRequest(RequestGameEvent req)
	{
		switch(req.getType())
		{
			case UNICAST:
				unicastObject(req);
				break;
			case MULTICAST_GAME:
				multicastObjectToPlayersInGame(req);
				break;
			case BROADCAST:
				broadcastObject(req);
				break;
		}
	}
	
	private void unicastObject(Object o)
	{
		try
		{
			if(alive)
			{
				output.writeObject(o);
				if(!(o instanceof HeartBeat))
				{
					Logger.getLogger("log").fine("Sent message of type " + o.getClass().getName() + " to " + this + " (unicast)");
					System.out.println("Sent message of type " + o.getClass().getName() + " to " + this + " (unicast)");
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void multicastObjectToPlayersInGame(Object o)
	{
		try
		{
			if(game != null)
			{
				for(ClientHandler handler : game.getPlayers())
				{
					if(handler.alive)
					{
						handler.output.writeObject(o);
						if(!(o instanceof HeartBeat))
						{
							Logger.getLogger("log").fine("Sent message of type " + o.getClass().getName() + " to " + handler + " (multicast)");
							System.out.println("Sent message of type " + o.getClass().getName() + " to " + handler + " (multicast)");
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void broadcastObject(Object o)
	{
		try
		{
			for(ClientHandler handler : Server.getClients())
			{
				if(handler.alive)
				{
					handler.output.writeObject(o);
					if(!(o instanceof HeartBeat))
					{
						Logger.getLogger("log").fine("Sent message of type " + o.getClass().getName() + " to " + handler + " (broadcast)");
						System.out.println("Sent message of type " + o.getClass().getName() + " to " + handler + " (broadcast)");
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void heartBeatReceived()
	{
		lastHeartBeat = new Date().getTime();
	}
	
	@Override
	public String toString()
	{
		return getNickname() + "(" + socket.getInetAddress() + ":" + socket.getPort() + ")";
	}

	public int getTeam() {
		return team;
	}

	public void setTeam(int team) {
		this.team = team;
	}
	
}
