import java.util.Arrays;
import java.util.StringTokenizer;

public class Game {
	private int[][] board;
	private int boardSize;
	private int turn;
	private int[][] directions = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
	private int[] potentialKo = { -1, -1 };
	private int numberCaptured = 1;
	private int lastMove;
	private boolean isEnd;
	private int loser;

	public Game(int boardSize) {
		isEnd = false;
		board = new int[boardSize][boardSize];
		turn = Constants.BLACK_TURN;
		this.boardSize = boardSize;
	}

	public Game(int[][] board, int turn, int lastMove, int loser) {
		this.board = board;
		this.turn = turn;
		this.boardSize = board.length;
		this.lastMove = lastMove;
		this.loser = loser;
	}

	public int getBoardSize() {
		return boardSize;
	}

	public int getLastMove() {
		return lastMove;
	}

	public int getTurn() {
		return turn;
	}

	public int[][] getBoard() {
		return board;
	}

	public boolean isEnd() {
		return isEnd;
	}

	public void end(int role) {
		isEnd = true;
		loser = role;
	}

	public int getLoser() {
		return loser;
	}

	public void play(int x, int y) {
		// Check if spot is in Ko
		if (board[y][x] == Constants.KO_SPACE) {
			return;
		}

		// reset Ko
		for (int r = 0; r < boardSize; r++) {
			for (int c = 0; c < boardSize; c++) {
				if (board[r][c] == Constants.KO_SPACE) {
					board[r][c] = Constants.EMPTY_SPACE;
				}
			}
		}
		// Place the stone
		board[y][x] = turn;
		captureOpponents(x, y, turn);
		if (!hasLiberty(x, y)) {
			board[y][x] = Constants.EMPTY_SPACE;
			return;
		}

		// Create Ko spot if applicable
		// TODO IFF there is a one stone capture marked, check if next turn captures
		// anything if placed in that empty space
		// TODO if so, mark as ko

		if (potentialKo[0] != -1) {
			int opponentTurn = (turn == Constants.BLACK_PLAYER) ? Constants.WHITE_PLAYER : Constants.BLACK_PLAYER;
			board[potentialKo[1]][potentialKo[0]] = opponentTurn;
			if (canCapture(potentialKo[0], potentialKo[1], opponentTurn)) {
				board[potentialKo[1]][potentialKo[0]] = Constants.KO_SPACE;
			} else {
				board[potentialKo[1]][potentialKo[0]] = Constants.EMPTY_SPACE;
			}

			System.out.println(Arrays.toString(potentialKo));
			System.out.println(board[potentialKo[1]][potentialKo[0]]);
			potentialKo = new int[] { -1, -1 };
		}

		turn = (turn == Constants.BLACK_PLAYER) ? Constants.WHITE_PLAYER : Constants.BLACK_PLAYER;
		lastMove = util.flatten(x, y, boardSize);
	}

	public boolean hasLiberty(int x, int y) {
		MyHashSet<Integer> visited = new MyHashSet<>();
		return hasLiberty(x, y, board[y][x], visited);
	}

	public void reset() {
		isEnd = false;
		board = new int[boardSize][boardSize];
		turn = Constants.BLACK_TURN;
		loser = 0;
	}

	private boolean hasLiberty(int x, int y, int color, MyHashSet<Integer> visited) {
		Queue<int[]> queue = new Queue<int[]>();
		queue.add(new int[] { x, y });
		visited.add(y * boardSize + x);

		while (!queue.isEmpty()) {
			int[] current = queue.poll();
			int curX = current[0];
			int curY = current[1];

			for (int[] dir : directions) {
				int nx = curX + dir[0];
				int ny = curY + dir[1];

				if (!isOnBoard(nx, ny)) {
					continue;
				}
				if (board[ny][nx] == Constants.EMPTY_SPACE) {
					return true;
				}
				if (board[ny][nx] == color && !visited.contains(ny * boardSize + nx)) {
					visited.add(ny * boardSize + nx);
					queue.add(new int[] { nx, ny });
				}
			}
		}
		return false;
	}

	private boolean isOnBoard(int x, int y) {
		return x >= 0 && x < boardSize && y >= 0 && y < boardSize;
	}

	public boolean canCapture(int x, int y, int color) {
		int opponentColor = (color == Constants.BLACK_PLAYER) ? Constants.WHITE_PLAYER : Constants.BLACK_PLAYER;
		for (int[] dir : directions) {
			int nx = x + dir[0];
			int ny = y + dir[1];
			if (isOnBoard(nx, ny) && board[ny][nx] == opponentColor) {
				if (!hasLiberty(nx, ny, opponentColor, new MyHashSet<Integer>())) {
					return true;
				}
			}
		}
		return false;
	}

	private void captureOpponents(int x, int y, int color) {
		int opponentColor = (color == Constants.BLACK_PLAYER) ? Constants.WHITE_PLAYER : Constants.BLACK_PLAYER;
		for (int[] dir : directions) {
			int nx = x + dir[0];
			int ny = y + dir[1];
			if (isOnBoard(nx, ny) && board[ny][nx] == opponentColor) {
				if (!hasLiberty(nx, ny, opponentColor, new MyHashSet<Integer>())) {
					removeStones(nx, ny, opponentColor);
					if (numberCaptured == 1)
						potentialKo = new int[] { nx, ny };
					numberCaptured = 1;
				}
			}
		}
	}

	private void removeStones(int x, int y, int color) {
		board[y][x] = Constants.EMPTY_SPACE;
		for (int[] dir : directions) {
			int nx = x + dir[0];
			int ny = y + dir[1];
			if (!isOnBoard(nx, ny))
				continue;
			if (board[ny][nx] == color) {
				numberCaptured++;
				removeStones(nx, ny, color);
			}
		}
	}

	public String toString() {
		String a = "";
		for (int[] r : board) {
			for (int c : r) {
				a += c + "";
			}
		}
		return a + ":" + turn + ":" + lastMove + ":" + loser;
	}

	public static Game fromString(String g) {
		StringTokenizer st = new StringTokenizer(g, ":");

		String b = st.nextToken();
		int size = (int) Math.sqrt(b.length());
		int[][] nb = new int[size][size];

		int turn = Integer.parseInt(st.nextToken());
		int lastMove = Integer.parseInt(st.nextToken());
		int loser = Integer.parseInt(st.nextToken());

		int i = 0;
		for (int r = 0; r < size; r++) {
			for (int c = 0; c < size; c++) {
				nb[r][c] = b.charAt(i) - '0';
				i++;
			}
		}

		return new Game(nb, turn, lastMove, loser);
	}
}