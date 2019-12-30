package logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;

import data.HighScore;

public class ServeRequest implements Runnable {

	private Socket client;

	public ServeRequest(Socket client) {
		this.client = client;
	}

	public void run() {
		try (Writer w = new OutputStreamWriter(client.getOutputStream());
				BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()))) {
			String linea;
			linea = br.readLine();
			// Caso recibir puntuaciones
			if (linea.startsWith("Username")) {
				String username = linea.substring(9);
				w.write("OK" + "\r\n");
				w.flush();
				ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
				Integer[] values = (Integer[]) ois.readObject();
				HighScore.putScore(username, values);

				// Casos de recibir petiones de las tablas
			} else if (linea.equals("EASY")) {
				w.write("OK" + "\r\n");
				w.flush();
				linea = br.readLine();
				if (linea.equals("SEND")) {
					ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
					oos.writeObject(HighScore.getTableEasy());
					oos.flush();
					oos.close();
				}
			} else if (linea.equals("MEDIUM")) {
				w.write("OK" + "\r\n");
				w.flush();
				linea = br.readLine();
				if (linea.equals("SEND")) {
					ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
					oos.writeObject(HighScore.getTableMedium());
					oos.flush();
					oos.close();
				}
			} else if (linea.equals("HARD")) {
				w.write("OK" + "\r\n");
				w.flush();
				linea = br.readLine();
				if (linea.equals("SEND")) {
					ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
					oos.writeObject(HighScore.getTableHard());
					oos.flush();
					oos.close();
				}
			} else if (linea.equals("GOD")) {
				w.write("OK" + "\r\n");
				w.flush();
				linea = br.readLine();
				if (linea.equals("SEND")) {
					ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
					oos.writeObject(HighScore.getTableGod());
					oos.flush();
					oos.close();
				}
			}
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
