package common.client;

import server.ServerState;

public class MessageCM extends ClientMessage
{
    public final String content;
    
    @SuppressWarnings("unused")
	private MessageCM()
    {
    	super();
    	this.content = "";
    }

    public MessageCM(String content)
    {
        //super("message");
    	super();
        this.content = content;
    }
    
    public void operate(String identity, ServerState state)
    {
    	state.message(identity, content);
    }
}
