package server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import common.RoomCount;
import common.server.*;

public class MessageDispatcher
{
	private ConcurrentHashMap<String,User> userMap;
	private ConcurrentHashMap<String,Room> roomMap;
	
	public MessageDispatcher(ConcurrentHashMap<String,User> userMap,
							 ConcurrentHashMap<String,Room> roomMap)
	{
		this.userMap = userMap;
		this.roomMap = roomMap;
	}
	
	public void message(String identity, String content, String roomid)
	{
		MessageSM m = new MessageSM(identity, content);
		messageRoom(m, roomid);
	}
	
	public void newIdentity(String former, String identity)
	{
		NewIdentitySM m = new NewIdentitySM(former, identity);
		if (former.equals(identity)) {
			messageUser(m, identity);
		}
		else {
			messageAll(m);
		}
	}
	
	public void roomChange(String identity, String former, String roomid)
	{
		RoomChangeSM m = new RoomChangeSM(identity, former, roomid);
		if (former.equals(roomid)) {
			messageUser(m, identity);
		}
		else {
			if (!former.equals("")) {
				messageRoom(m, former);
			}
			
			if (!roomid.equals("")) {
				messageRoom(m, roomid);
			}
			else {
				messageUser(m, identity);
			}
		}
	}
	
	public void roomContents(String identity, String roomid)
	{
		Room r = roomMap.get(roomid);
		RoomContentsSM m = new RoomContentsSM(roomid, r.getOccupants(), r.getOwner() != null ? r.getOwner().getIdentity() : "");
		
		messageUser(m, identity);
	}
	
	public void roomList(String identity)
	{
		List<RoomCount> rcList = new ArrayList<>();
		for (Room r: roomMap.values()) {
			rcList.add(r.getRoomCount());
		}
		
		RoomCount[] rcArr = new RoomCount[rcList.size()];
		RoomListSM m = new RoomListSM(rcList.toArray(rcArr));
		
		messageUser(m, identity);
	}
	
	private void messageUser(ServerMessage m, String identity)
	{
		userMap.get(identity).addMessage(m);
	}
	
	private void messageRoom(ServerMessage m, String roomid)
	{
		for (String identity: roomMap.get(roomid).getOccupants()) {
			messageUser(m, identity);
		}
	}
	
	private void messageAll(ServerMessage m)
	{
		for (User u: userMap.values()) {
			u.addMessage(m);
		}
	}
}
