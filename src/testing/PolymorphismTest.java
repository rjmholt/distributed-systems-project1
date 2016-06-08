package testing;

import java.util.ArrayList;
import java.util.List;

public class PolymorphismTest
{
	public static void main(String[] args)
	{
		List<AbstractMessage> mList = new ArrayList<>();
		mList.add((AbstractMessage) (new KillM("Tony Jones")));
		mList.add((AbstractMessage) (new IncM(3)));
		mList.add((AbstractMessage) (new SilentIncM(4)));
		mList.add((AbstractMessage) (new PrintCountM()));
		
		State s = new State("");
		for (AbstractMessage m: mList) {
			m.execute(s);
		}
	}
}
