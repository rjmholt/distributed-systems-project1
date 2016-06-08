package common.client;

import server.ServerState;

public class KickCM extends ClientMessage
{
    public final String roomid;
    public final int time;
    public final String identity;

    @SuppressWarnings("unused")
	private KickCM()
    {
    	super();
    	this.roomid = "";
    	this.time = 0;
    	this.identity = "";
    }
    
    public KickCM(String roomid, int time, String identity)
    {
        //super("kick");
    	super();
        this.roomid = roomid;
        this.time = time;
        this.identity = identity;
    }
    
    public void operate(String requester, ServerState state)
    {
    	state.kick(requester, roomid, time, identity);
    }
}
