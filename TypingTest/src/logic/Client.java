package logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;

public class Client {

	public static void sendScores(String usr, Integer[] values) {
		try (Socket socket = new Socket("", 8080);
				Writer w = new OutputStreamWriter(socket.getOutputStream());
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));) {
			w.write("Username:" + usr + "\r\n");
			w.flush();
			if (br.readLine().equals("OK")) {
				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
				oos.writeObject(values);
				oos.flush();
				oos.close();
				br.close();
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static ConcurrentHashMap<String, Integer> getTable(String name) {
		ConcurrentHashMap<String, Integer> table = null;
		try (Socket socket = new Socket("", 8080);
				Writer w = new OutputStreamWriter(socket.getOutputStream());
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));) {
			w.write(name + "\r\n");
			w.flush();
			if (br.readLine().equals("OK")) {
				w.write("SEND" + "\r\n");
				w.flush();
				ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
				table = (ConcurrentHashMap<String, Integer>) ois.readObject();
				ois.close();
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (ConnectException e) {
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return table;
	}

}
