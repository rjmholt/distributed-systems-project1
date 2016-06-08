package common.server;

import client.ClientState;

public class RoomContentsSM extends ServerMessage
{
    public final String roomid;
    public final String[] identities;
    public final String owner;

    @SuppressWarnings("unused")
	private RoomContentsSM()
    {
    	super();
    	this.roomid = "";
    	this.identities = new String[] {};
    	this.owner = "";
    }
    
    public RoomContentsSM(String roomid, String[] identities, String owner)
    {
        //super("roomcontents");
    	super();
        this.roomid = roomid;
        this.identities = identities;
        this.owner = owner;
    }
    
    public void operate(ClientState state)
    {
    	state.roomContents(roomid, identities, owner);
    }
}
