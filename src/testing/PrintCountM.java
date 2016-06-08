package testing;

public class PrintCountM extends AbstractMessage
{
	public PrintCountM()
	{
		super("printcount");
	}
	
	public void execute(State state)
	{
		state.doCountPrint();
	}
}
