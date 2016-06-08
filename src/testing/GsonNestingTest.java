package testing;

import java.io.*;

import common.MessageJsonInterface;
import common.RoomCount;
import common.server.*;

public class GsonNestingTest
{
	private static final String FILEPATH = "/tmp/gson_test.txt";

	public static void main(String[] args)
	{
		MessageJsonInterface jsonInterface = new MessageJsonInterface();
		
		RoomCount rc1 = new RoomCount("MainHall", 3);
		RoomCount rc2 = new RoomCount("room1", 1);
		RoomCount rc3 = new RoomCount("lounge", 0);
		
		RoomListSM m = new RoomListSM(new RoomCount[] {rc1, rc2, rc3});
		
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
			String data = jsonInterface.serverMessageToJson(m);
			out.writeUTF(data);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		RoomListSM recM = null;
		try {
			recM = (RoomListSM) jsonInterface.jsonToServerMessage(in.readUTF());
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		for (RoomCount rc: recM.rooms) {
			System.out.println(rc.roomid + ": " + rc.count);
		}
	}
}
