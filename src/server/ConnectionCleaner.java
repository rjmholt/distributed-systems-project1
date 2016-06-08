package server;

import java.util.Iterator;

public class ConnectionCleaner implements Runnable
{
	private ServerState state;
	
	public ConnectionCleaner(ServerState state)
	{
		this.state = state;
	}
	
	@Override
	public void run()
	{
		try {
			while(!Thread.currentThread().isInterrupted()) {
				Thread.sleep(500);
				removeDroppedConnections();
			}
		}
		catch (InterruptedException e) {
			// Do nothing - it's time to sleep
		}
	}

	private void removeDroppedConnections() {
		Iterator<User> iter = state.getUserIterator();
		
		while(iter.hasNext()){
			User u = iter.next();	
			if (u.hasDroppedConnection()) {
				iter.remove();
				u.closeConnection();
				state.endUserSession(u.getIdentity(), false);
			}
		}
	}
}
