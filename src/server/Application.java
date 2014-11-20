package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class Application extends Thread
{
	private Board board;
	private InputStream is;
	private OutputStream os;
	private BufferedReader br;
	
	public Application(Socket s)
	{
		try
		{
			this.is = s.getInputStream();
			this.os = s.getOutputStream();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void run()
	{
		br = new BufferedReader(new InputStreamReader(is));
		while(true)
		{
			//anfragenprotokoll
			//benutzererkennung sollte im appteil eigl egal sein
			//js clientseitig -> eigl kein problem?!?
		}
	}
}