package common.client;

import server.ServerState;

public class ListCM extends ClientMessage
{
	
    public ListCM()
    {
        //super("list");
    	super();
    }
    
    public void operate(String identity, ServerState state)
    {
    	state.list(identity);
    }
}
