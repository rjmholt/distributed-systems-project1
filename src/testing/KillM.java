package testing;

public class KillM extends AbstractMessage
{
	public final String target;
	
	public KillM(String target)
	{
		super("kill");
		this.target = target;
	}
	
	public void execute(State state)
	{
		state.doKill(target);
	}
}
