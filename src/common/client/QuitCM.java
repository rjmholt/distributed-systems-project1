package common.client;

import server.ServerState;

public class QuitCM extends ClientMessage
{
    public QuitCM()
    {
        //super("quit");
    	super();
    }
    
    public void operate(String identity, ServerState state)
    {
    	state.quit(identity);
    }
}
