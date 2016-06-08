package server;

import com.beust.jcommander.Parameter;

public class CmdLineArgs
{
	@Parameter(names = "-p", description = "Port for server to listen on")
	public int port = 4444;
}
