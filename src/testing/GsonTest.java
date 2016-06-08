package testing;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import com.google.gson.Gson;

import common.server.*;

public class GsonTest
{
	private static final String FILEPATH = "/tmp/gson_test.txt";
	
	public static void main(String[] args)
	{
		Gson gson = new Gson();
		LinkedBlockingQueue<ServerMessage> mQueue = new LinkedBlockingQueue<>();
		List<ServerMessage> mList = new ArrayList<>();
		
		mQueue.add(new MessageSM("rob", "Hi!"));
		mQueue.add(new RoomContentsSM("room1", new String[] {"brian", "rob"}, "rob"));
		mQueue.add(new NewIdentitySM("rob", "banana"));
		
		DataOutputStream out = null;
		DataInputStream in = null;
		try {
			out = new DataOutputStream(new FileOutputStream(FILEPATH));
			in = new DataInputStream(new FileInputStream(FILEPATH));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			String data = gson.toJson(mQueue.poll(), MessageSM.class);
			out.writeUTF(data + "\n");
			data = gson.toJson(mQueue.poll(), RoomContentsSM.class);
			out.writeUTF(data + "\n");
			data = gson.toJson(mQueue.poll(), NewIdentitySM.class);
			out.writeUTF(data);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			String s = in.readUTF();
			mList.add(gson.fromJson(s, MessageSM.class));
			s = in.readUTF();
			mList.add(gson.fromJson(s, RoomContentsSM.class));
			s = in.readUTF();
			mList.add(gson.fromJson(s, NewIdentitySM.class));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println(((MessageSM) mList.get(0)).content);
	}
}
