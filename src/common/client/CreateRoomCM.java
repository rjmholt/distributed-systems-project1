package common.client;

import server.ServerState;

public class CreateRoomCM extends ClientMessage
{
    public final String roomid;

    @SuppressWarnings("unused")
	private CreateRoomCM()
    {
    	super();
    	this.roomid = "";
    }
    
    public CreateRoomCM(String roomid)
    {
        //super("createroom");
    	super();
        this.roomid = roomid;
    }
    
    public void operate(String identity, ServerState state)
    {
    	state.createRoom(identity, roomid);
    }
}
