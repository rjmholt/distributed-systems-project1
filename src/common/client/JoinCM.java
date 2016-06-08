package common.client;

import server.ServerState;

public class JoinCM extends ClientMessage
{
    public final String roomid;

    @SuppressWarnings("unused")
	private JoinCM()
    {
    	super();
    	this.roomid = "";
    }
    
    public JoinCM(String roomid)
    {
        //super("join");
    	super();
        this.roomid = roomid;
    }
    
    public void operate(String identity, ServerState state)
    {
    	state.join(identity, roomid);
    }
}
