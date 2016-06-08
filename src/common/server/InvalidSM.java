package common.server;

import client.ClientState;

public class InvalidSM extends ServerMessage
{
	public final String message;
	
	@SuppressWarnings("unused")
	private InvalidSM()
	{
		this.message = "";
	}
	
	public InvalidSM(String message)
	{
		this.message = message;
	}
	
	public void operate(ClientState state)
	{
		System.out.println("ERROR: Invalid message");
	}
}
