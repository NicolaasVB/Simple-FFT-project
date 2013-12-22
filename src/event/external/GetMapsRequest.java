package event.external;

import java.io.Serializable;
import java.util.Hashtable;

import map.Map;
import server.Server;
import client.Lobby;
import event.RequestGameEvent;

public class GetMapsRequest extends RequestGameEvent implements Serializable {
	private static final long serialVersionUID = -5114018125830724212L;
	private Hashtable<String, String[]> maps;
	
	public GetMapsRequest() {
		setType(RequestGameEvent.Type.UNICAST);
	}
	
	public Hashtable<String, String[]> getMaps() {
		return maps;
	}
	
	public void setMaps(Hashtable<String, String[]> maps) {
		this.maps = maps;
	}

	@Override
	public void process() {
		Hashtable<String, Map> maps = new Hashtable<String, Map>();
		for (String[] mapString : this.maps.values()) {
			Map map = Map.mapFromString(mapString);
			maps.put(map.getName(), map);
		}
		Lobby.getInstance().setMaps(maps);
	}

	@Override
	public boolean verify() {
		maps = new Hashtable<String, String[]>();
		for (Map map : Server.getMaps().values()) {
			maps.put(map.getName(), map.mapToString());
		}
		return true;
	}

}
