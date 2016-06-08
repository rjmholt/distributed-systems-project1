package testing;

public class StateModThread implements Runnable {

	State state;
	
	public StateModThread(State state)
	{
		this.state = state;
	}
	
	@Override
	public void run() {
		try {
			Thread.sleep(5000);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		state.setName("punk");
		state.interruptReadThread();
		System.out.println("Changed your name...");
		try {
			Thread.sleep(5000);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		state.setRoom("dump");
		state.interruptReadThread();
		System.out.println("Changed your room...");
	}
}
