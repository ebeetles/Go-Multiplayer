import javax.swing.JFrame;
import java.io.*;

public class Client {

    public static void main(String args[]) throws IOException {

		JFrame frame = new JFrame("Client");

		ClientScreen sc = new ClientScreen(frame);
		frame.add(sc);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);

		sc.connect(args.length > 0 ? args[0] : Constants.DEFAULT_SERVER_HOST);
    }
}