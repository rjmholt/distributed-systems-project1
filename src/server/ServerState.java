package server;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class ServerState
{
	private final String MAIN_HALL = "MainHall";
	private final String GUEST_PREFIX = "guest";
	
	private int guestNum;
	
	private Thread actor;
	private Thread cleaner;
	private final MessageDispatcher messenger;
	
	private ConcurrentSkipListSet<String> deletedIDs;
	private ConcurrentHashMap<String,User> userMap;
	private ConcurrentHashMap<String,Room> roomMap;
	
	private Room mainHall;
	
	public ServerState()
	{
		userMap = new ConcurrentHashMap<String,User>();
		roomMap = new ConcurrentHashMap<String,Room>();
		deletedIDs = new ConcurrentSkipListSet<String>();
		
		messenger = new MessageDispatcher(userMap, roomMap);
		
		mainHall = new Room(MAIN_HALL, null);
		
		roomMap.putIfAbsent(MAIN_HALL, mainHall);
		
		guestNum = 0;
	}
	
	public void setActorThread(Thread t)
	{
		this.actor = t;
	}
	
	public void setCleanerThread(Thread t)
	{
		this.cleaner = t;
	}
	
	public User getUser(String identity)
	{
		return userMap.get(identity);
	}

	public Iterator<User> getUserIterator()
	{
		return userMap.values().iterator();
	}
	
	public void shutdown()
	{
		Iterator<String> uIter = userMap.keySet().iterator();
		while (uIter.hasNext()) {
			endUserSession(uIter.next(), true);
		}
		
		if (actor != null) {
			actor.interrupt();
		}
		if (cleaner != null) {
			cleaner.interrupt();
		}
	}
	
	public void endUserSession(String identity, boolean isConnected)
	{
		User u = userMap.get(identity);
		Room r = u.getRoom();
		r.removeOccupant(u);
		r.removeOwner();
		
		if (isConnected) {
			messenger.roomChange(identity, r.roomid, "");
		}
		
		userMap.remove(identity);
		
		u.closeConnection();
		garbageCollectGuestID(u.getIdentity());
		garbageCollectRoom(r.roomid);
	}
	
	public synchronized String startNewUserSession()
	{
		String guestName = generateNewGuestID();
				
		identityChange("", guestName);
		join(guestName, MAIN_HALL);
		
		return guestName;
	}
		
	public void createRoom(String identity, String roomid)
	{
		if (isValidRoomID(roomid)) {
			roomMap.put(roomid, new Room(roomid, userMap.get(identity)));
		}
		messenger.roomList(identity);
	}
	
	public void delete(String identity, String roomid)
	{
		if (roomMap.containsKey(roomid)) {
			Room r = roomMap.get(roomid);
			if (r.getOwner() == userMap.get(identity)) {
				for (String occupant: r.getOccupants()) {
					join(occupant, MAIN_HALL);
				}
				roomMap.remove(roomid);
			}
		}
		messenger.roomList(identity);
	}
	
	public void identityChange(String former, String identity)
	{
		if (!isValidIdentity(identity)) {
			messenger.newIdentity(former, former);
			return;
		}
		
		if (former.isEmpty()) {
			createNewUserInstance(identity);
		}
		else {
			updateUserIdentity(former, identity);
		}
		
		messenger.newIdentity(former, identity);

		garbageCollectGuestID(former);
	}
	
	public void join(String identity, String roomid)
	{
		User u = userMap.get(identity);
		Room formerRoom = u.getRoom();
		Room newRoom = !roomid.isEmpty() ? roomMap.get(roomid) : null;
		
		if (!canChangeRoom(roomid, u)) {
			messenger.roomChange(identity, formerRoom.roomid, formerRoom.roomid);
			return;
		}
		
		moveUser(formerRoom, newRoom, u);

		messenger.roomChange(identity, formerRoom != null ? formerRoom.roomid : "", roomid);
		
		if (roomid.equals(MAIN_HALL)) {
			messenger.roomContents(identity, roomid);
			messenger.roomList(identity);
		}
		
		garbageCollectRoom(roomid);
	}
	
	public void kick(String requester, String roomid, int time, String identity)
	{
		if (roomMap.containsKey(roomid)) {
			Room r = roomMap.get(roomid);
			if (requester.equals(r.getOwner().getIdentity())) {
				r.addBan(identity, time);
				join(identity, MAIN_HALL);
			}
		}
	}
	
	public void list(String identity)
	{
		messenger.roomList(identity);
	}
	
	public void message(String identity, String content)
	{
		messenger.message(identity, content, userMap.get(identity).getRoom().roomid);
	}
	
	public void quit(String identity)
	{
		endUserSession(identity, true);
	}
	
	public void who(String identity, String roomid)
	{
		messenger.roomContents(identity, roomid);
	}
	
	private String generateNewGuestID()
	{
		String guestName;
		if (!deletedIDs.isEmpty()) {
			guestName = deletedIDs.first();
		}
		else {
			guestNum++;
			guestName = GUEST_PREFIX + Integer.toString(guestNum);
		}
		return guestName;
	}
	
	private User createNewUserInstance(String identity)
	{
		User u = new User(identity);
		userMap.put(identity, u);
		
		return u;
	}
	
	private void updateUserIdentity(String former, String identity)
	{
		User u = userMap.remove(former);
		u.changeIdentity(identity);
		userMap.put(identity, u);		
	}
	
	private void moveUser(Room formerRoom, Room newRoom, User u)
	{
		if (formerRoom != null) {
			formerRoom.removeOccupant(u);
		}
		u.changeRoom(newRoom);
		if (newRoom != null) {
			newRoom.addOccupant(u);
		}
	}
	
	private void garbageCollectRoom(String roomid)
	{
		Room r = roomMap.get(roomid);
		
		boolean disused = r.getOwner() == null;
		disused &= r.getOccupants().length == 0;
		disused &= !MAIN_HALL.equals(r.roomid);
		
		if (disused) {
			roomMap.remove(roomid);
		}
	}
	
	private void garbageCollectGuestID(String guestID)
	{
		if (guestID.matches("guest\\d+")) {
			deletedIDs.add(guestID);
		}
	}
	
	private boolean isValidIdentity(String identity)
	{
		boolean isAvailable = !userMap.containsKey(identity);
		
		return identity.matches("[a-zA-Z][a-zA-Z0-9]{5,15}") && isAvailable;
	}
	
	private boolean isValidRoomID(String roomid)
	{
		boolean isAvailable = !roomMap.containsKey(roomid);
		
		return roomid.matches("[a-zA-Z][a-zA-Z0-9]{2,31}") && isAvailable;
	}
	
	private boolean canChangeRoom(String roomid, User u)
	{
		return roomid.isEmpty() || (roomMap.containsKey(roomid) && roomMap.get(roomid).willAcceptUser(u));
	}
}
