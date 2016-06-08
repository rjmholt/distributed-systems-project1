package client;
import java.net.Socket;

import common.MessageJsonInterface;
import common.server.ServerMessage;

import java.io.*;

public class ClientReceiver implements Runnable
{
	private Socket sock;
    private ClientState state;
    private MessageJsonInterface messageInterface;
    private BufferedReader in;

    public ClientReceiver(Socket sock, ClientState state)
    {
        this.state = state;
        this.sock = sock;

        this.messageInterface = new MessageJsonInterface();
    }

    @Override
	public void run()
    {
    	connect();    	
    	
    	while (!Thread.currentThread().isInterrupted()) {
	    	receive();
    	}
    }

    public void receive()
    {
    	String data = null;
    	
    	try {
    		data = in.readLine();
    	}
    	catch (IOException e) {
    		e.printStackTrace();
    	}
    	catch (NullPointerException e) {
    		e.printStackTrace();
    	}
    	    	
    	ServerMessage obj = messageInterface.jsonToServerMessage(data);
    	
    	obj.operate(state);
    }
    
    public void connect()
    {
    	try {
    		in = new BufferedReader(
    				new InputStreamReader(
    						sock.getInputStream(), "UTF8"));
        }
        catch (IOException e) {
         	System.err.println(e.getMessage());
        }
    }
}
