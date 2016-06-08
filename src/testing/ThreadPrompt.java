package testing;

public class ThreadPrompt
{
	public static void main(String[] args)
	{
		State state = new State("rob");
		(new Thread(new StateModThread(state))).start();
		Thread t = new Thread(new PromptThread(state));
		state.setReadThread(t);
		t.run();
	}
}
