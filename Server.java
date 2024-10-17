import java.net.*;
import java.io.*;

public class Server {
	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = new ServerSocket(Constants.PORT_NUMBER);
		Game game = new Game(Constants.BOARD_SIZE);
		int nextRole = Constants.BLACK_PLAYER;
		BufferedReader in;

		// This loop will run and wait for one connection at a time.
		while (true) {
			System.out.println("Waiting for a connection");

			// Wait for a connection.
			Socket clientSocket = serverSocket.accept();
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			// Once a connection is made, run the socket in a ServerThread.
			ServerThread st = new ServerThread(clientSocket, game, nextRole);
			if (nextRole == Constants.BLACK_PLAYER) {
				nextRole = Constants.WHITE_PLAYER;
			} else if (nextRole == Constants.WHITE_PLAYER) {
				nextRole = Constants.OBSERVER;
			}
			Manager.addThread(st);
			Thread thread = new Thread(st);
			thread.start();

		}
	}
}
