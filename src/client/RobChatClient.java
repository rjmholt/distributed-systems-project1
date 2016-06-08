package client;
import java.io.IOException;
import java.net.*;

import com.beust.jcommander.JCommander;

/**
 * The main/executable thread for the chat client. Executing this file
 * will run the client, connecting to the server, prompting input and
 * sending/receiving messages.
 * 
 * @author rob
 *
 */
public class RobChatClient
{
	// Default values used for testing in Eclipse
    public static final int DEFAULT_PORT = 4444;
    public static final String DEFAULT_HOST = "localhost";

    public static void main(String[] args) throws InterruptedException
    {
    	// Set the host and port from command line options
    	CmdLineArgs settings = new CmdLineArgs();
    	new JCommander(settings, args);
    	
    	// If the host has not been set, use the default value
    	String host = !settings.host.isEmpty() ? settings.host.get(0) : DEFAULT_HOST;
    	
    	// Create a new socket with the provided settings
        Socket sock = createSocket(host, settings.port);
        
        // Initialise the state of the client
        ClientState state = new ClientState(host, sock);
        
        // Set up a hook to make the client close cleanly
    	addKeyboardInterruptHook(state);
    	
    	// Connect to the server and begin processing incoming messages
    	beginReceiving(state);
        
    	// Start prompting the user and sending messages to the server
        beginSending(state);
    }
    
    /**
     * Try to create a new socket for connecting to the server
     * (in and out). Will print a stack trace on failure.
     * 
     * @param host	hostname of the host to connect to
     * @param port	port number of the connection
     * @return		new socket connecting to the given host over the given port
     */
    private static Socket createSocket(String host, int port)
    {
    	Socket sock = null;
        try {
        	sock = new Socket(host, port);
        }
        catch (UnknownHostException e) {
        	e.printStackTrace();
        }
        catch (IOException e) {
        	e.printStackTrace();
        }
        
        return sock;
    }
    
    /**
     * Create a new receiver thread with the state and socket, add the
     * thread reference to the state, and start the thread connecting and
     * receiving from the server.
     * 
     * @param state	client state to receive to and operate on
     */
    private static void beginReceiving(ClientState state)
    {
    	Thread receiver = new Thread(new ClientReceiver(state.getSocket(), state));
        
        state.setRecvThread(receiver);
        
        receiver.start();  
    }
    
    /**
     * Create a new publisher (prompter/message generator/json sender), wait
     * for the server to send messages to set the identity and roomid, and 
     * commence prompting the user for input.
     * 
     * @param state	client state to take data to send from
     */
    private static void beginSending(ClientState state)
    {
    	ClientPublisher publisher = new ClientPublisher(state.getSocket(), state);
                
        publisher.doPromptPublish();
    }
    
    /**
     * Add shutdown hooks (with a ClientCleaner object) to ensure the
     * program shuts down cleanly in (almost) all circumstances.
     * 
     * @param state the client state (which holds references to disparate parts of the client)
     */
    private static void addKeyboardInterruptHook(ClientState state)
    {
    	Runtime.getRuntime().addShutdownHook(new Thread(new ClientShutdown(state)));
    }
}
