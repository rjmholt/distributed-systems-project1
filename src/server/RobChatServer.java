package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

import com.beust.jcommander.JCommander;

import server.CmdLineArgs;
import server.ServerState;

public class RobChatServer
{
	public static void main(String[] args)
	{		
		CmdLineArgs settings = new CmdLineArgs();
    	new JCommander(settings, args);
    	
    	ServerState state = new ServerState();
    	addKeyboardInterruptHook(state);

    	LinkedBlockingQueue<SenderMessage> recvQ = new LinkedBlockingQueue<>();
    	
    	initiateMessageActor(state, recvQ);
    	    	
    	startSocketListening(state, recvQ, settings.port);
    	
    	startConnectionCleaner(state);
	}
	
	private static void initiateMessageActor(ServerState state,
			LinkedBlockingQueue<SenderMessage> recvQ)
	{
		Thread actor = (new Thread(new ServerActor(state, recvQ)));
    	
    	state.setActorThread(actor);
    	
    	actor.start();
	}
	
	private static void startSocketListening(ServerState state,
			LinkedBlockingQueue<SenderMessage> recvQ, int port)
	{
		ServerSocket sSock = null;
    	try {
    		sSock = new ServerSocket(port);
    		
    		while (true) {
    			Socket cSock = sSock.accept();
    			User u = state.getUser(state.startNewUserSession());
    			Connection c = new Connection(cSock, u, recvQ);
    			Thread t = new Thread(c);
    			u.bindConnectionThread(c, t);
    			t.start();
    		}
    	}
    	catch (IOException e) {
    		e.printStackTrace();
    	}
	}
	
	private static void startConnectionCleaner(ServerState state)
	{
		Thread cleaner = new Thread(new ConnectionCleaner(state));
		
		state.setCleanerThread(cleaner);
		
		cleaner.start();
	}
	
	private static void addKeyboardInterruptHook(ServerState state)
    {
    	Runtime.getRuntime().addShutdownHook(new Thread(new ServerShutdown(state)));
    }
}
