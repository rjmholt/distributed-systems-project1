package server;

import java.util.concurrent.LinkedBlockingQueue;

public class ServerActor implements Runnable
{
	private ServerState state;
	private LinkedBlockingQueue<SenderMessage> inputQueue;
	
	public ServerActor(ServerState state, LinkedBlockingQueue<SenderMessage> inputQueue)
	{
		this.state = state;
		this.inputQueue = inputQueue;
	}

	@Override
	public void run() {
		try {
			while (true) {
				SenderMessage m = inputQueue.take();
				m.message.operate(m.sender, state);
			}
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
