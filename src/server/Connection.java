package server;

import java.io.*;
import java.net.*;
import java.util.concurrent.LinkedBlockingQueue;

import common.MessageJsonInterface;
import common.server.NoOpSM;
import common.server.ServerMessage;

public class Connection implements Runnable
{
	private Socket sock;
	private Thread receiverThread;
	private LinkedBlockingQueue<ServerMessage> outQ;
	private MessageJsonInterface jsonInterface;
	private PrintWriter out;
	
	public Connection(Socket sock, User user, LinkedBlockingQueue<SenderMessage> inQ)
	{
		this.sock = sock;
		
		receiverThread = new Thread(new ConnectionReceiver(sock, inQ, user.getIdentity()));
		
		this.outQ = user.getQueue();
		this.jsonInterface = new MessageJsonInterface();
		
		try {
			this.out = new PrintWriter(
						new BufferedWriter(
							new OutputStreamWriter(
									sock.getOutputStream(), "UTF8")), true);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run()
	{
		receiverThread.start();

		String data = null;
		while (!Thread.currentThread().isInterrupted()) {
			try {
				data = jsonInterface.serverMessageToJson(outQ.take());
			}
			catch (InterruptedException e) {
				break;
			}
			out.println(data);
		}

		shutdownConnection();
	}
	
	private void shutdownConnection()
	{
		receiverThread.interrupt();
		try {
			out.close();
			sock.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized boolean pingClient()
	{
		out.println(jsonInterface.serverMessageToJson(new NoOpSM()));
		
		return out.checkError();
	}
}
