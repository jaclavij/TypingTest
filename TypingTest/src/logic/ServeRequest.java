package logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.List;
import java.util.Random;

public class ServeRequest implements Runnable {

	private Socket client;
	private List<String> engList;
	boolean gameActive = true;

	public ServeRequest(Socket client, List<String> engList) {
		this.client = client;
		this.engList = engList;
	}

	public void run() {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
				Writer w = new OutputStreamWriter(client.getOutputStream())) {
			Random ran = new Random();
			int score = 0;
			while (true) {
				String word = engList.get(ran.nextInt(engList.size()));
				w.write(word + "\r\n");
				w.flush();
				if (br.readLine().equals(word) && gameActive) {
					score++;
					w.write("Correcto! Score: " + score + "\r\n");
				} else {
					w.write("Fallaste! Score: " + score + "\r\n");
					score = 0;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
