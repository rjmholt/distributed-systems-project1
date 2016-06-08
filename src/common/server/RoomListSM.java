package common.server;

import client.ClientState;
import common.RoomCount;

public class RoomListSM extends ServerMessage
{
    public final RoomCount[] rooms;

    @SuppressWarnings("unused")
	private RoomListSM()
    {
    	super();
    	this.rooms = new RoomCount[] {};
    }
    
    public RoomListSM(RoomCount[] rooms)
    {
        //super("roomlist");
    	super();
        this.rooms = rooms;
    }
    
    public void operate(ClientState state)
    {
    	state.roomList(rooms);
    }
}
