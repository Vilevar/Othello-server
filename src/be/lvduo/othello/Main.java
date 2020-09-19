package be.lvduo.othello;

public class Main {
	
	public static Server server;
	
	private final static String host = "127.0.0.1";
	private final static int port = 8080;
	
	public static void main(String[] args) {
		
		server = new Server(host, port);
		server.start();
	}

}
