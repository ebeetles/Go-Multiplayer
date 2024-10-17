import java.util.*;

public class Manager {
     private static ArrayList<ServerThread> threads = new ArrayList<ServerThread>();

     public static void addThread(ServerThread t) {
          threads.add(t);
     }

     public static void sendChat(String text) {
          for (ServerThread thread : threads) {
               thread.sendChat(text);
          }
     }
     
     public static void sendGame(Game game) {
    	 for (ServerThread thread : threads) {
    		 thread.sendGame(game);
    	 }
     }
}
