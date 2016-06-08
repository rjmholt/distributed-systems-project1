package testing;

import java.io.*;

public class PromptThread implements Runnable {

	State state;
	BufferedReader reader;
	
	public PromptThread(State state)
	{
		this.reader = new BufferedReader(new InputStreamReader(System.in));
		this.state = state;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			System.out.print("[" + state.getRoom() + "] " + state.getName() + ": ");
			while (!Thread.currentThread().isInterrupted()) {
				try {
					if (reader.ready()) {
						System.out.println(reader.readLine());
						break;
					}
				}
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				throw new InterruptedException();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
			}
		}
	}

}
