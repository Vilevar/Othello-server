package be.lvduo.othello;

import java.io.File;

public class Main {
	
	public static Server server;
	
	private final static String host = "127.0.0.1";
	private final static int port = 27585;
	
	public static void main(String[] args) throws Exception {
		
		server = new Server(host, port);
		server.start();
		
		String fileName = "othello-server-started.txt";
		File file = new File(fileName);
		file.createNewFile();
		
		new Thread(() -> {
			while(true) {
				File test = new File(fileName);
				if(!test.exists()) {
					server.stop();
				}
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}, "Reading").start();
	}

}
