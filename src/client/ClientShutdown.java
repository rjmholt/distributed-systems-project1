package client;

/**
 * Thread which shuts down the client cleanly. Called by the shutdown hook.
 * @author rob
 *
 */
public class ClientShutdown implements Runnable
{
	private ClientState state;
	
	public ClientShutdown(ClientState state)
	{
		this.state = state;
	}

	/**
	 *  Call the state's shutdown method.
	 */
	@Override
	public void run() {
		state.shutdown();
	}
}
