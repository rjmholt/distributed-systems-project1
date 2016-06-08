package server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import common.RoomCount;

public class Room
{
	public final String roomid;
	private User owner;
	private List<User> occupants;
	private ConcurrentHashMap<String, Ban> bans;
	
	public Room(String roomid, User owner) {
		this.roomid = roomid;
		this.owner = owner;
		occupants = Collections.synchronizedList(new ArrayList<User>());
		bans = new ConcurrentHashMap<String,Ban>();
	}
	
	public synchronized User getOwner()
	{
		return owner;
	}
	
	public synchronized void removeOwner()
	{
		this.owner = null;
	}
	
	public synchronized String[] getOccupants()
	{
		if (occupants.size() == 0) {
			return new String [] {};
		}
		
		String[] occArray = new String[occupants.size()];
		
		for (int i=0;i<occupants.size();i++) {
			occArray[i] = occupants.get(i).getIdentity();
		}

		return occArray;
	}
	
	public boolean willAcceptUser(User u)
	{
		if (occupants.contains(u)) {
			return false;
		}
		
		if (isBanned(u.getIdentity())) {
			return false;
		}
		return true;
	}
	
	public synchronized void addOccupant(User u)
	{
		occupants.add(u);
	}
	
	public synchronized void removeOccupant(User u)
	{
		occupants.remove(u);
	}
	
	public synchronized void addBan(String identity, int banTime)
	{
		bans.putIfAbsent(identity, new Ban(identity, roomid, banTime));
	}
	
	public synchronized boolean isBanned(String identity)
	{
		Ban b = bans.get(identity);
		
		if (b == null) {
			return false;
		}
		
		if (b.endTime <= (int) (System.currentTimeMillis() / 1000L)) {
			bans.remove(identity);
			return false;
		}
		
		return true;
	}
	
	public synchronized RoomCount getRoomCount()
	{
		return new RoomCount(roomid, occupants.size());
	}
}
