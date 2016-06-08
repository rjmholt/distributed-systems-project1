package server;

import common.client.ClientMessage;

public class SenderMessage
{
	public final String sender;
	public final ClientMessage message;
	
	public SenderMessage(String sender, ClientMessage message)
	{
		this.sender = sender;
		this.message = message;
	}
}
