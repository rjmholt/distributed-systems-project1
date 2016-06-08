package testing;

public class IncM extends AbstractMessage
{
	public final int n;
	
	public IncM(int n)
	{
		super("inc");
		this.n = n;
	}
	
	public void execute(State state)
	{
		state.doAddPrint(n);
	}
}
