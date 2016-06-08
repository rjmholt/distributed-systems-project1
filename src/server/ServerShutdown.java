package server;

public class ServerShutdown implements Runnable
{
	private final ServerState state;
	
	public ServerShutdown(ServerState state)
	{
		this.state = state;
	}

	@Override
	public void run() {
		state.shutdown();
	}

}
