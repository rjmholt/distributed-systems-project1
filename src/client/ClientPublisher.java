package client;

import java.io.*;
import java.net.Socket;

import common.MessageJsonInterface;
import common.client.ClientMessage;

public class ClientPublisher
{
    private Prompter prompt;
    private PrintWriter out;
    private MessageJsonInterface messageInterface;

    public ClientPublisher(Socket sock, ClientState state)
    {        
        try {
        	out = new PrintWriter(
        			new BufferedWriter(
        					new OutputStreamWriter(
        							sock.getOutputStream(), "UTF8")), true);
        }
        catch (IOException e) {
        	System.err.println(e.getMessage());
        }

        prompt = new Prompter(state);
        state.setPrompt(prompt);
        messageInterface = new MessageJsonInterface();
        
    }

	public void doPromptPublish()
    {	
        while (true) {
        	String input = prompt.nextLine();
        	try {
        		ClientMessage m = InputToJSONParser.lineToMessage(input);
        		String data = messageInterface.clientMessageToJson(m);
        		out.println(data);
            }
        	catch (InvalidCommandException e) {
        		System.out.println(e.getMessage());
        	}
        }
    }
}
