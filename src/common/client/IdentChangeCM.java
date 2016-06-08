package common.client;

import server.ServerState;

public class IdentChangeCM extends ClientMessage
{
    public final String identity;

    @SuppressWarnings("unused")
	private IdentChangeCM()
    {
    	super();
    	this.identity = "";
    }
    
    public IdentChangeCM(String newIdent)
    {
        //super("identitychange");
    	super();
        this.identity = newIdent;
    }
    
    public void operate(String former, ServerState state)
    {
    	state.identityChange(former, identity);
    }
}
