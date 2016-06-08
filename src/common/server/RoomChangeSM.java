package common.server;

import client.ClientState;

public class RoomChangeSM extends ServerMessage
{
    public final String identity;
    public final String former;
    public final String roomid;
    
    @SuppressWarnings("unused")
	private RoomChangeSM()
    {
    	super();
    	this.identity = "";
    	this.former = "";
    	this.roomid = "";
    }

    public RoomChangeSM(String identity, String former, String roomid)
    {
        //super("roomchange");
    	super();
        this.identity = identity;
        this.former = former;
        this.roomid = roomid;
    }
    
    public void operate(ClientState state)
    {
    	state.roomChange(identity, former, roomid);
    }
}
