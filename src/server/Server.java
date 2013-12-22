package server;

import game.Game;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import Application.ServerConfiguration;

import map.Map;

public class Server{
	private static ServerSocket serverSocket;
	private static int PORT;
	private static ArrayList<ClientHandler> clients;
	private static Hashtable<String, Map> maps;
	private static ArrayList<Game> games;
	private static int highestGameId = 0;
	
	public static synchronized int getNextGameId()
	{
		highestGameId++;
		return highestGameId;
	}
	
	public static synchronized ArrayList<Game> getGamesWithoutEnoughPlayers()
	{
		ArrayList<Game> games = new ArrayList<Game>();
		for(Game game : getGames())
		{
			if(game.getPlayers().size() < 2)
				games.add(game);
		}
		return games;
	}
 
	public static synchronized ArrayList<Game> getGames() 
	{
		return games;
	}
	
	public static synchronized ArrayList<ClientHandler> getClients()
	{
		return clients;
	}
	
	public static synchronized Hashtable<String, Map> getMaps()
	{
		return maps;
	}

	public static void main(String[] args) 
	{
		PORT = ServerConfiguration.getPort();
		
		games = new ArrayList<Game>();
		// Alle maps inladen
		loadMaps();

		// Logging starten
		try {
			File directory = new File("Logging");
			if(!directory.exists())
				directory.mkdirs();
			FileHandler logHandler = new FileHandler("Logging/Log" + new Date().getTime() + ".txt", true);
			logHandler.setFormatter(new SimpleFormatter());
			Logger.getLogger("log").addHandler(logHandler);
			Logger.getLogger("log").setLevel(Level.ALL);
			FileHandler chatLogger = new FileHandler("Logging/Chat" + new Date().getTime() + ".txt", true);
			chatLogger.setFormatter(new SimpleFormatter());
			Logger.getLogger("chat").addHandler(chatLogger);
			Logger.getLogger("chat").setLevel(Level.ALL);
		} catch (SecurityException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		// Client ArrayList initialiseren
		clients = new ArrayList<ClientHandler>();
		try
		{
			serverSocket = new ServerSocket(PORT);
			System.out.println("Server is waiting for clients on port: " + serverSocket.getLocalPort());
			Logger.getLogger("log").fine("Server is waiting for clients on port: " + serverSocket.getLocalPort());
			
			while(true)
			{
				Socket socket = serverSocket.accept();
				
				ClientHandler handler = new ClientHandler(socket);
				clients.add(handler);
				Thread t = new Thread(handler);
				t.start();
				System.out.println("Client " + handler + " successfully connected to the server");
				Logger.getLogger("log").fine("Client " + handler + " successfully connected to the server");
			}
		}
		catch(IOException e)
		{
			System.out.println("IO Exception : " + e);
			Logger.getLogger("log").log(Level.WARNING, "Error while setting up the server", e);
		}
	}
	
	private static void loadMaps()
	{
		maps = new Hashtable<String, Map>();
		
		File directory = new File("Maps");
		if(!directory.exists())
			directory.mkdirs();
		
		for(File file : directory.listFiles())
		{
			if(file.getName().substring(file.getName().length() - 4).equals(".imf"))
			{
				Map map = Map.loadMap(file);
				maps.put(map.getName(), map);
			}
		}
		
		System.out.println(maps.size() + " maps were loaded");
		Logger.getLogger("log").fine(maps.size() + " maps were loaded");
	}
}
