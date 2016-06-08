package testing;

public abstract class AbstractMessage
{
	public final String type;
	
	public AbstractMessage(String type)
	{
		this.type = type;
	}
	
	public abstract void execute(State state);
}
