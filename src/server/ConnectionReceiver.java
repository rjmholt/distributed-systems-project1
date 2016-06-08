package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

import common.MessageJsonInterface;

public class ConnectionReceiver implements Runnable
{
	private MessageJsonInterface jsonInterface;
	private LinkedBlockingQueue<SenderMessage> inQ;
	private BufferedReader in;
	private String owner;

	public ConnectionReceiver(Socket sock, LinkedBlockingQueue<SenderMessage> inQ, String owner) {
		this.owner = owner;
		this.inQ = inQ;
		
		try {
			in = new BufferedReader(
					new InputStreamReader(
							sock.getInputStream(), "UTF8"));
			
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		this.jsonInterface = new MessageJsonInterface();
	}

	@Override
	public void run() {
		while (!Thread.currentThread().isInterrupted()) {
			try {
				if (in.ready()) {
					String data = in.readLine();
					inQ.add(new SenderMessage(owner, jsonInterface.jsonToClientMessage(data)));
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		shutdown();
	}
	
	public void shutdown()
	{
		try {
			in.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

}
