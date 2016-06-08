package common.client;

import server.ServerState;

public class DeleteCM extends ClientMessage
{
    public final String roomid;
    
    @SuppressWarnings("unused")
	private DeleteCM()
    {
    	super();
    	this.roomid = "";
    }
    
    public DeleteCM(String roomid)
    {
        //super("delete");
    	super();
        this.roomid = roomid;
    }
    
    public void operate(String identity, ServerState state)
    {
    	state.delete(identity, roomid);
    }
}
