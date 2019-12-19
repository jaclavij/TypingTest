package logic;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import data.HighScore;

public class Server {

	public static void main(String[] args) {

		HighScore.load(); // Cargo las tablas de highscores desde los archivos txt;
		HighScore.count();
//		HighScore.putGod2("Jaime", 1);
//		HighScore.putGod2("L", 2);
//		HighScore.putGod2("Z", 2);
//		HighScore.putGod2("M", 7);
//		HighScore.putGod2("A", 5);
//		HighScore.getTableEasy().put("Jaime", 5);

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
					pool.execute(new ServeRequest(client));
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
