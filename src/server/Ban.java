package server;

public class Ban
{
	public final int endTime;
	public final String identity;
	public final String roomid;
	
	public Ban(String identity, String roomid, int duration)
	{
		this.identity = identity;
		this.roomid = roomid;
		this.endTime = (int) (System.currentTimeMillis() / 1000L) + duration;
	}
}
