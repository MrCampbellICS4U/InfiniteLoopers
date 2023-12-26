package client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

class Client {
	public static void main(String[] args) {
		new Client("127.0.0.1", 2000);
	}

	Client(String ip, int port) {
		try (Socket clientSocket = new Socket(ip, port);
			 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		     Scanner sc = new Scanner(System.in)) {
			while (true) {
				out.println(sc.next());
				String response = in.readLine();
				System.out.println(response);
				if (response.equals("goodbye")) break;
			}
		} catch (IOException e) {
			System.out.println("IOException:");
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}

}
