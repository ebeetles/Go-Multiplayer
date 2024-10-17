import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ClientScreen extends JPanel implements ActionListener, MouseListener {

	private JTextField textInput;
	private JButton sendButton;
	private JButton instructionsButton;
	private JButton resignButton;
	private JButton restartButton;
	private String allMessages = "";
	private JTextArea messages;
	private Socket serverSocket;
	private BufferedReader in;
	private PrintWriter out;
	private Game game;
	private int role;
	private int gridSize;
	private JFrame frame;

	public ClientScreen(JFrame frame) {

		this.setLayout(null);
		this.frame = frame;
		messages = new JTextArea();
		messages.setBounds(20, 50, 200, 350);
		messages.setEditable(true);
		this.add(messages);

		textInput = new JTextField();
		textInput.setBounds(20, 410, 200, 30);
		this.add(textInput);

		sendButton = new JButton("Send");
		sendButton.setBounds(20, 450, 200, 30);
		this.add(sendButton);
		sendButton.addActionListener(this);

		instructionsButton = new JButton("Instructions");
		instructionsButton.setBounds(20, 490, 200, 30);
		this.add(instructionsButton);
		instructionsButton.addActionListener(this);

		resignButton = new JButton("Resign");
		resignButton.setBounds(20, 530, 200, 30);
		this.add(resignButton);
		resignButton.addActionListener(this);

		restartButton = new JButton("Restart");
		restartButton.setBounds(20, 570, 200, 30);
		this.add(restartButton);
		restartButton.addActionListener(this);
		addMouseListener(this);
		this.setFocusable(true);
	}

	public Dimension getPreferredSize() {

		return new Dimension(800, 600);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		String a;
		if (role == Constants.BLACK_PLAYER) {
			a = "Black";
		} else if (role == Constants.WHITE_PLAYER) {
			a = "White";
		} else {
			a = "Observer";
		}

		g.drawString("You are: " + a, Constants.BOARD_LEFT + 190, 20);
		g.drawString("Chat", 100, 40);
		g.setColor(new Color(205, 170, 125));
		g.fillRect(Constants.BOARD_LEFT - Constants.BUFFER, Constants.BOARD_TOP - Constants.BUFFER,
				Constants.MAX_SIZE + (Constants.BUFFER * 2), Constants.MAX_SIZE + (Constants.BUFFER * 2));
		g.setColor(Color.black);

		gridSize = Constants.MAX_SIZE / (Constants.BOARD_SIZE - 1);

		int right = Constants.BOARD_LEFT + (Constants.BOARD_SIZE - 1) * gridSize;
		for (int r = 0; r < Constants.BOARD_SIZE; r++) {
			int y = Constants.BOARD_TOP + r * gridSize;
			g.drawLine(Constants.BOARD_LEFT, y, right, y);
		}

		int bottom = Constants.BOARD_TOP + (Constants.BOARD_SIZE - 1) * gridSize;
		for (int c = 0; c < Constants.BOARD_SIZE; c++) {
			int x = Constants.BOARD_LEFT + c * gridSize;
			g.drawLine(x, Constants.BOARD_TOP, x, bottom);
		}

		int dotRadius = 4;
		for (int r = 0; r < 3; r++) {
			for (int c = 0; c < 3; c++) {
				int x = Constants.BOARD_LEFT + 2 * (c + 1) * gridSize;
				int y = Constants.BOARD_TOP + 2 * (r + 1) * gridSize;
				g.fillOval(x - dotRadius, y - dotRadius, dotRadius * 2, dotRadius * 2);
			}
		}

		if (game == null)
			return;
		int[][] board = game.getBoard();
		int stoneRadius = gridSize / 2;
		for (int r = 0; r < Constants.BOARD_SIZE; r++) {
			for (int c = 0; c < Constants.BOARD_SIZE; c++) {
				if (board[r][c] == Constants.BLACK_PLAYER) {
					g.setColor(Color.black);
				} else if (board[r][c] == Constants.WHITE_PLAYER) {
					g.setColor(Color.white);
				} else if (board[r][c] == Constants.KO_SPACE) {
					g.setColor(Color.black);
					int x = Constants.BOARD_LEFT + c * gridSize;
					int y = Constants.BOARD_TOP + r * gridSize;
					g.drawRect(x - stoneRadius, y - stoneRadius, stoneRadius * 2, stoneRadius * 2);
					continue;
				} else {
					continue;
				}

				int x = Constants.BOARD_LEFT + c * gridSize;
				int y = Constants.BOARD_TOP + r * gridSize;
				g.fillOval(x - stoneRadius, y - stoneRadius, stoneRadius * 2, stoneRadius * 2);
				int[] lastMove = util.expand(game.getLastMove(), Constants.BOARD_SIZE);
				if (r == lastMove[1] && c == lastMove[0]) {
					g.setColor(Color.red);
					g.drawOval(x - stoneRadius / 2, y - stoneRadius / 2, stoneRadius, stoneRadius);
				}
			}
		}
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == sendButton) {
			sendMessage();
		} else if (e.getSource() == instructionsButton) {
			JOptionPane.showMessageDialog(frame,
					"<html><b>How to Play</b>: <p>Each player takes turns placing stones on the intersections of the board, with black going first.<br>"
							+ "You cannot place a stone where another stone already exists. The 9 black dots on the board are <br>purely for orientation"
							+ "and have no meaning to the game.</p> <p>There are 3 rules:" + "<ol>"
							+ "<li>Any stone or groups of stones completeley surrounded adjacently by either the edge <br>of the board or the opponents stones"
							+ " is deemed captured, and removed from the board</li>"
							+ "<li>If there is such an orientation that both sides can repeatedly capture each other infinitely, <br>it is deemed as a ko, and one"
							+ " must go somewhere else first</li>"
							+ "<li>A player cannot suicide, meaning they cannot go somewhere that makes their own group <br>be captured</li>"
							+ "</ol></p>"
							+ "<p>For more information, please see: <a href=\"https://www.cs.cmu.edu/~wjh/go/rules/Chinese.html\">https://www.cs.cmu.edu/~wjh/go/rules/Chinese.html</a></p></html>");

		} else if (e.getSource() == resignButton) {
			ended();
		} else if (e.getSource() == restartButton) {
			restart();
		}

	}

	private void restart() {
		if (out != null) {
			out.println(Constants.RESTART_COMMAND);
		}
	}

	private void ended() {
		if (out != null) {
			out.println(Constants.END_COMMAND);
		}
	}

	private void playSound() {

		try {
			URL url = this.getClass().getClassLoader().getResource("stone.wav");
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(url));
			clip.start();
		} catch (Exception exc) {
			exc.printStackTrace(System.out);
		}
	}

	private void sendMessage() {
		String sendText = textInput.getText();
		if (out != null) {
			out.println(Constants.CHAT_COMMAND);
			out.println(sendText);
		}
	}

	public void connect(String serverHost) throws IOException {
		serverSocket = new Socket(serverHost, Constants.PORT_NUMBER);
		in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
		out = new PrintWriter(serverSocket.getOutputStream(), true);

		// listens for inputs
		try {
			while (true) {
				String command = in.readLine();
				if (command == null) {
					continue;
				} else if (command.equals(Constants.CHAT_COMMAND)) {
					String chatMessage = in.readLine();
					if (chatMessage != null) {
						allMessages += chatMessage + "\n";
						messages.setText(allMessages);
					}
				} else if (command.equals(Constants.GAME_COMMAND)) {
					game = Game.fromString(in.readLine());
					role = Integer.parseInt(in.readLine());
					if (role == Constants.OBSERVER) {
						resignButton.setVisible(false);
						restartButton.setVisible(false);
					}
					repaint();

					if (game.getLoser() != 0) {

						String s = (game.getLoser() == Constants.BLACK_PLAYER) ? "Black" : "White";
						JOptionPane.showMessageDialog(frame, "<html><b>" + s + " Resigned! </b></html>");
					}
				}

			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.err.println("Host unkown: " + serverHost);
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Couldn't get I/O for the connection to " + serverHost);
			System.exit(1);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int x = Math.round((e.getX() - Constants.BOARD_LEFT) / (float) gridSize);
		int y = Math.round((e.getY() - Constants.BOARD_TOP) / (float) gridSize);

		if (canPlay(x, y)) {
			this.playSound();
			out.println(Constants.GAME_COMMAND);
			out.println(x);
			out.println(y);
			out.flush();
		}
	}

	private boolean canPlay(int x, int y) {
		if (x >= 0 && x < Constants.BOARD_SIZE && y >= 0 && y < Constants.BOARD_SIZE && role == game.getTurn()
				&& game.getBoard()[y][x] == Constants.EMPTY_SPACE) {
			return true;
		}
		return false;
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
}
