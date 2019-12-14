package logic;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import data.Words;

public class Server {

	public static void main(String[] args) {

		// Preparacion de las listas de palabras
		Words.load();
		// Ejecución del servidor multihilo
		ExecutorService pool = Executors.newCachedThreadPool();
		ServerSocket ss = null;
		int i = 1;

		try {
			ss = new ServerSocket(8080);
			System.out.println(ss.getLocalSocketAddress());
			System.out.println("Server running");
			while (true) {
				try {
					Socket client = ss.accept();
					System.out.println("Serving client " + i);
					i++;
					pool.execute(new ServeRequest(client, Words.getEngList()));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ss != null)
					ss.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
