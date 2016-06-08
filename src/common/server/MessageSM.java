package common.server;

import client.ClientState;

public class MessageSM extends ServerMessage
{
    public final String identity;
    public final String content;
    
    @SuppressWarnings("unused")
	private MessageSM()
    {
    	super();
    	this.identity = "";
    	this.content = "";
    }

    public MessageSM(String identity, String content)
    {
        //super("message");
    	super();
        this.identity = identity;
        this.content = content;
    }
    
    public void operate(ClientState state)
    {
    	state.message(identity, content);
    }
}
