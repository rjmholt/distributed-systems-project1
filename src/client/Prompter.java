package client;
import java.util.Scanner;

/**
 * Extension of java.util.Scanner which prints a prompt string before
 * taking user input. Also provides a public method to reprint the prompt
 * when the client takes a new message.
 * @author rob
 *
 */
public class Prompter
{
    private final String P_SYMB = "> ";
    private Scanner keyboard;
    private ClientState state;

    public Prompter(ClientState state)
    {
    	this.state = state;
        keyboard = new Scanner(System.in);
    }

    /**
     * Print a prompt and collect user input with Scanner.
     * @return	user input to be parsed into a message and sent as JSON to the server
     */
    public String nextLine()
    {
        String input;

        printPrompt();
        input = keyboard.nextLine();

        return input;
    }
    
    /**
     * Print the prompt as currently set, without taking user input (useful to
     * update the prompt on message receipt).
     */
    public void printPrompt()
    {
    	if (state.getRoomID() == null || state.getIdentity() == null) {
    		return;
    	}
    	System.out.print("[" + state.getRoomID() + "] " + state.getIdentity() + P_SYMB);
    }
}
