package logic;

import java.net.Socket;
import java.util.List;

public class ServeRequest implements Runnable {

	private Socket client;
	private List<String> engList;

	public ServeRequest(Socket client, List<String> engList) {
		this.client = client;
		this.engList = engList;
	}

	public void run() {
		
	}
}
