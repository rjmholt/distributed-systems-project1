package common.server;

import client.ClientState;

public class NewIdentitySM extends ServerMessage
{
    public final String former;
    public final String identity;

    @SuppressWarnings("unused")
	private NewIdentitySM()
    {
    	super();
    	this.former = "";
    	this.identity = "";
    }
    
    public NewIdentitySM(String former, String identity)
    {
        //super("newidentity");
    	super();
        this.former = former;
        this.identity = identity;
    }
    
    public void operate(ClientState state)
    {
    	state.newIdentity(former, identity);
    }
}
