package testing;

public class SilentIncM extends AbstractMessage
{
	public final int n;
	
	public SilentIncM(int n)
	{
		super("slientinc");
		this.n = n;
	}
	
	public void execute(State state)
	{
		state.doAdd(n);
	}
}
