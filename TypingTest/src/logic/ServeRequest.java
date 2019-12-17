package logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;

public class ServeRequest implements Runnable {

	private Socket client;

	public ServeRequest(Socket client) {
		this.client = client;
	}

	public void run() {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
				Writer w = new OutputStreamWriter(client.getOutputStream())) {
			while (true) {
				
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
