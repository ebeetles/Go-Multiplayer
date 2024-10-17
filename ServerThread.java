import java.net.*;
import java.io.*;
import java.util.*;

public class ServerThread implements Runnable {
	private PrintWriter out;
	private BufferedReader in;
	private Game game;
	private int role;

	public ServerThread(Socket clientSocket, Game game, int role) throws IOException {
		out = new PrintWriter(clientSocket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		this.game = game;
		this.role = role;
	}

	public void run() {
		System.out.println(Thread.currentThread().getName() + ": connection opened.");
		try {
			sendGame(game);

			while (true) {
				String command = in.readLine();
				if (command == null) {
					continue;
				} else if (command.equals(Constants.CHAT_COMMAND)) {
					Manager.sendChat(in.readLine());
				} else if (command.equals(Constants.GAME_COMMAND)) {
					int x = Integer.parseInt(in.readLine());
					int y = Integer.parseInt(in.readLine());

					if (!game.isEnd()) {
						game.play(x, y);
						Manager.sendGame(game);
					}
				} else if (command.equals(Constants.RESTART_COMMAND)) {
					game.reset();
					Manager.sendGame(game);
				} else if (command.equals(Constants.END_COMMAND)) {
					if (!game.isEnd()) {
						game.end(role);
						Manager.sendGame(game);
					}
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			System.out.println("Error listening for a connection1");
			System.out.println(ex.getMessage());
		}
	}

	public void sendChat(String line) {
		out.println(Constants.CHAT_COMMAND);
		out.println(line);
		out.flush();
	}

	public void sendGame(Game game) {
		out.println(Constants.GAME_COMMAND);
		out.println(game);
		out.println(role);
		out.flush();
	}
}