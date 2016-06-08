package client;

import java.io.IOException;
import java.net.Socket;

import common.RoomCount;

public class ClientState
{
	private String myIdentity;
	private String myRoomid;
	private String host;
	private Prompter prompt;
	private Socket sock;
	private Thread recvThread;
	
	public ClientState(String host, Socket sock)
	{
		this.myIdentity = null;
		this.myRoomid = null;
		this.host = host;
		this.sock = sock;
	}
	
	public void message(String identity, String content)
	{
		System.out.println(identity + ": " + content);
		prompt.printPrompt();
	}
	
	public void newIdentity(String former, String identity)
	{
		if (!former.equals(identity)) {
			if (former.length() == 0) {
				System.out.println("Connected to " + host + " as " + identity);
			}
			this.myIdentity = identity;
		}
		else {
			System.out.println("Requested identity invalid or in use");
		}
		if (prompt != null) {
			prompt.printPrompt();
		}

	}
	
	public void roomChange(String identity, String former, String roomid)
	{
		if (identity.equals(myIdentity)) {
			if (roomid.equals("")) {
				shutdown();
				System.exit(0);;
			}
			else {
				this.myRoomid = roomid;
			}
		}
		
		if (former.equals("")) {
			System.out.println(identity + " moves to " + roomid);
		}
		else if (roomid == "") {
			System.out.println(identity + " leaves " + former);
		}
		else {
			System.out.println(identity + " moves from " + former + " to " + roomid);

		}
		if (prompt != null) {
			prompt.printPrompt();
		}
	}
	
	public void roomContents(String roomid, String[] identities, String owner)
	{
		String contentsMessage = roomid + " contains";
		if (identities.length == 0) {
			contentsMessage += " ...";
		}
		else {
			for (String ident: identities) {
				contentsMessage += " " + ident;
				if(ident.equals(owner)) {
					contentsMessage += "*";
				}
			}
		}
		System.out.println(contentsMessage);
		if (prompt != null) {
			prompt.printPrompt();
		}
	}
	
	public void roomList(RoomCount[] rooms)
	{
		for (RoomCount room: rooms) {
			System.out.println(room.roomid 
							   + ": "
							   + room.count
							   + " guest"
							   + (room.count == 1 ? "" : "s"));
		}
		if (prompt != null) {
			prompt.printPrompt();
		}
	}
	
	public void shutdown()
	{
		this.recvThread.interrupt();
		try {
			this.sock.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		//System.exit(0);
	}
	
	public Socket getSocket()
	{
		return this.sock;
	}
	
	public String getRoomID()
	{
		return this.myRoomid;
	}
	
	public String getIdentity()
	{
		return this.myIdentity;
	}
	
	public void setPrompt(Prompter prompt)
	{
		this.prompt = prompt;
	}
	
	public void setRecvThread(Thread recvThread)
	{
		this.recvThread = recvThread;
	}
}
