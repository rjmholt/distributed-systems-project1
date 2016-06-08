package testing;

public class State
{
	private int count;
	private String name;
	private String room;
	private Thread readThread;
	
	public State(String name)
	{
		this.setRoom("MainHall");
		this.setName(name);
		this.count = 0;
	}
	
	public void doKill(String target)
	{
		System.out.println("I will kill " + target);
	}
	
	public void doAdd(int n)
	{
		count += n;
	}
	
	public void doAddPrint(int n)
	{
		count += n;
		System.out.println("count = " + count);
	}
	
	public void doCountPrint()
	{
		System.out.println("count = " + count);
	}

	public synchronized String getName() {
		return name;
	}

	public synchronized void setName(String name) {
		this.name = name;
	}

	public synchronized String getRoom() {
		return room;
	}

	public synchronized void setRoom(String room) {
		this.room = room;
	}
	
	public void setReadThread(Thread t)
	{
		this.readThread = t;
	}
	
	public void interruptReadThread()
	{
		readThread.interrupt();
	}
}
