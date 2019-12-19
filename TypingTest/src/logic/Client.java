package logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;

public class Client {

//	private static Socket socket;

	public static boolean sendScores(String usr, Integer[] values) {
		boolean b = true;
		try (Socket socket = new Socket("", 8080);
				Writer w = new OutputStreamWriter(socket.getOutputStream());
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));) {
			System.out.println("Sending username");
			w.write("Username:" + usr + "\r\n");
			w.flush();
			String linea;
			if ((linea = br.readLine()).equals("OK")) {
				System.out.println("Read OK");
				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
				oos.writeObject(values);
				oos.flush();
				oos.close();
				br.close();
			} else if (linea.equals("Unavailable")) {
				System.out.println("Read unavailable");
				b = false;
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return b;
	}

	public static ConcurrentHashMap<String, Integer> getTable(String name) {
		ConcurrentHashMap<String, Integer> table = null;
		try (Socket socket = new Socket("", 8080);
				Writer w = new OutputStreamWriter(socket.getOutputStream());
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));) {
			System.out.println("Sending difficulty");
			w.write(name + "\r\n");
			w.flush();
			String linea;
			linea = br.readLine();
			System.out.println(linea);
			if (linea.equals("OK")) {
				System.out.println("Read OK");
				w.write("SEND" + "\r\n");
				w.flush();
				ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
				table = (ConcurrentHashMap<String, Integer>) ois.readObject();
				ois.close();
			} else if (linea.equals("Unavailable")) {
				System.out.println("Read unavailable");
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return table;
	}

//	public static void main(String[] args) {
//		try (Socket socket = new Socket("", 8080);
//				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//				Writer w = new OutputStreamWriter(socket.getOutputStream());
//				Scanner sc = new Scanner(System.in)) {
//			String linea;
//			System.out.println(sc.delimiter());
//			while (true) {
//				if ((linea = br.readLine()).startsWith("Correcto!") || linea.startsWith("Fallaste!")) {
//					System.out.println(linea);
//					linea = br.readLine();
//				}
//				System.out.println(linea);
//				String written = sc.nextLine();
//				w.write(written + "\r\n");
//				w.flush();
//			}
//
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//	}

}
