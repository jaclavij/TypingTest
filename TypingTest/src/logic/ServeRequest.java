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
			System.out.println("Recibo " + linea);
			if (linea.startsWith("Username")) {
				System.out.println("Got username");
				String username = linea.substring(9);
				System.out.println("Username: " + username);
				if (HighScore.usernameAvailable(username)) {
					System.out.println("Username available");
					w.write("OK" + "\r\n");
					w.flush();
					ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
					Integer[] values = (Integer[]) ois.readObject();
					System.out.println("Read values");
					HighScore.putScore(username, values);
				} else {
					System.out.println("Username unavailable");
					w.write("Unavailable" + "\r\n");
				}
			} else if (linea.equals("EASY")) {
				w.write("OK" + "\r\n");
				w.flush();
				linea = br.readLine();
				System.out.println("Leo " + linea);
				if (linea.equals("SEND")) {
					ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
					System.out.println("Tabla enviada");
					oos.writeObject(HighScore.getTableEasy());
					oos.flush();
					oos.close();
				}
			} else if (linea.equals("MEDIUM")) {
				w.write("OK" + "\r\n");
				w.flush();
				linea = br.readLine();
				System.out.println("Leo " + linea);
				if (linea.equals("SEND")) {
					ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
					System.out.println("Tabla enviada");
					oos.writeObject(HighScore.getTableMedium());
					oos.flush();
					oos.close();
				}
			} else if (linea.equals("HARD")) {
				System.out.println("Envío OK");
				w.write("OK" + "\r\n");
				w.flush();
				linea = br.readLine();
				System.out.println("Leo " + linea);
				if (linea.equals("SEND")) {
					ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
					System.out.println("Tabla enviada");
					oos.writeObject(HighScore.getTableHard());
					oos.flush();
					oos.close();
				}
			} else if (linea.equals("GOD")) {
				w.write("OK" + "\r\n");
				w.flush();
				linea = br.readLine();
				System.out.println("Leo " + linea);
				if (linea.equals("SEND")) {
					ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
					System.out.println("Tabla enviada");
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
