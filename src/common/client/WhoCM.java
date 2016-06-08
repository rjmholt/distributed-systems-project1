package common.client;

import server.ServerState;

public class WhoCM extends ClientMessage
{
    public final String roomid;

    @SuppressWarnings("unused")
	private WhoCM()
    {
    	super();
    	this.roomid = "";
    }
    
    public WhoCM(String roomid)
    {
        //super("who");
    	super();
        this.roomid = roomid;
    }
    
    public void operate(String identity, ServerState state)
    {
    	state.who(identity, roomid);
    }
}
