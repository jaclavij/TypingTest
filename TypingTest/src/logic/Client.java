package logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client{

	public static void main(String[] args) {
		try (Socket socket = new Socket("", 8080);
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				Writer w = new OutputStreamWriter(socket.getOutputStream());
				Scanner sc = new Scanner(System.in)) {
			String linea;
			System.out.println(sc.delimiter());
			while (true) {
				if ((linea = br.readLine()).startsWith("Correcto!") || linea.startsWith("Fallaste!")) {
					System.out.println(linea);
					linea = br.readLine();
				}
				System.out.println(linea);
				String written = sc.nextLine();
				w.write(written + "\r\n");
				w.flush();
			}

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
