package logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

	public static void main(String[] args) {

		// Preparacion de las listas de palabras
		File engFile = new File("wordData/1-1000.txt");
		List<String> engList = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(engFile)));) {
			System.out.println(engFile);
			String linea;
			while ((linea = br.readLine()) != null) {
				engList.add(linea);
			}
			for (int i = engList.size() - 1; i >= 0; i--) {
				if (engList.get(i).length() < 5)
					engList.remove(engList.get(i));
			}
			System.out.println(engList.size());
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
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
					pool.execute(new ServeRequest(client, engList));
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
