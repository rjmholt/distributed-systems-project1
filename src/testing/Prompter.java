package testing;
import java.util.Scanner;

public class Prompter
{
    private final String P_SYMB = "> ";
    private Scanner keyboard;
    private State state;

    public Prompter(State state)
    {
    	this.state = state;
        keyboard = new Scanner(System.in);
    }

    public String nextLine()
    {
        String input;

        System.out.print("[" + state.getRoom() + "] " + state.getName() + P_SYMB);
        input = keyboard.nextLine();

        return input;
    }
}
