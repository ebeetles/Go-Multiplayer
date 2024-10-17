
public class util {
	public static int flatten(int x, int y, int boardSize) {
		return y * boardSize + x;
	}

	public static int[] expand(int n, int boardSize) {
		int[] a = new int[2];
		a[0] = n % boardSize;
		a[1] = n / boardSize;
		return a;
	}
}
