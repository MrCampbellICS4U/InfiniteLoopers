package server;

import java.io.*;
import java.net.*;

class Client extends Thread {
	private Socket clientSocket;

	Client(Socket socket) {
		clientSocket = socket;
	}

	public void run() {
		try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
			 String inputLine;
			while ((inputLine = in.readLine()) != null) {
				if (".".equals(inputLine)) {
					out.println("goodbye");
					break;
				}
				out.println(inputLine);
			}

			clientSocket.close();
		} catch (IOException e) {
			System.out.println("IOException:");
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}
}
