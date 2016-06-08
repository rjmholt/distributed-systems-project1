package server;

import java.util.concurrent.LinkedBlockingQueue;

import common.server.ServerMessage;

public class User {

	private String identity;
	private Room room;
	private final LinkedBlockingQueue<ServerMessage> userQ;
	private Connection connection;
	private Thread cThread;
	
	public User(String identity)
	{
		this.changeIdentity(identity);
		this.userQ = new LinkedBlockingQueue<ServerMessage>();
		this.room = null;
	}

	public String getIdentity() {
		return identity;
	}

	public void changeIdentity(String identity) {
		this.identity = identity;
	}
	
	public Room getRoom()
	{
		return this.room;
	}
	
	public String getRoomID()
	{
		if (this.room == null) {
			return "";
		}
		
		return this.room.roomid;
	}
	
	public void changeRoom(Room newRoom)
	{
		this.room = newRoom;
	}

	public LinkedBlockingQueue<ServerMessage> getQueue()
	{
		return this.userQ;
	}
	
	public void addMessage(ServerMessage m)
	{
		// Could use offer() here in a while loop, but
		// exception means scalability is an issue...
		userQ.add(m);
	}
	
	public void bindConnectionThread(Connection c, Thread t)
	{
		this.connection = c;
		this.cThread = t;
	}
	
	public boolean hasDroppedConnection()
	{
		return connection.pingClient();
	}
	
	public void closeConnection()
	{
		this.cThread.interrupt();
	}
}
