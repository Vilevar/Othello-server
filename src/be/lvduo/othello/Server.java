package be.lvduo.othello;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.lvduo.othello.network.NetworkSystem;
import be.lvduo.othello.network.packets.CPacketAIPlayer;
import be.lvduo.othello.network.packets.CPacketName;
import be.lvduo.othello.network.packets.CPacketRandom;
import be.lvduo.othello.network.packets.CPacketRequest;
import be.lvduo.othello.network.packets.CPacketResponse;
import be.lvduo.othello.network.packets.CPacketSendAction;
import be.lvduo.othello.network.packets.CPacketWait;
import be.lvduo.othello.network.packets.SPacketBeginner;
import be.lvduo.othello.network.packets.SPacketGameOver;
import be.lvduo.othello.network.packets.SPacketName;
import be.lvduo.othello.network.packets.SPacketPlayerDenies;
import be.lvduo.othello.network.packets.SPacketPlayerList;
import be.lvduo.othello.network.packets.SPacketRequest;
import be.lvduo.othello.network.packets.SPacketSendAction;
import be.lvduo.othello.network.packets.SPacketStartGame;
import be.lvduo.othello.player.OnlinePlayer;
import be.lvduo.othello.player.Player;

public class Server {
	
	private final String host;
	private final int port;
	
	private NetworkSystem network;
	
	
	private List<User> users = new ArrayList<>();
	private List<User> randomMode = new ArrayList<>();
	private List<Game> games = new ArrayList<>();
	private Map<User, User> proposals = new HashMap<>();
	
	public Server(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	public void start() {
		new Thread(() -> {
			try {
				Thread.sleep(16);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			network = new NetworkSystem(this);
			System.out.println("Starting server...");
			
			network.registerPacketType(CPacketName.class);
			network.registerPacketType(SPacketName.class);
			network.registerPacketType(CPacketWait.class);
			network.registerPacketType(SPacketPlayerList.class);
			network.registerPacketType(CPacketRequest.class);
			network.registerPacketType(SPacketRequest.class);
			network.registerPacketType(CPacketResponse.class); 
			network.registerPacketType(SPacketPlayerDenies.class); 
			network.registerPacketType(CPacketRandom.class);
			network.registerPacketType(CPacketAIPlayer.class);
			network.registerPacketType(SPacketStartGame.class);
			network.registerPacketType(SPacketBeginner.class);
			network.registerPacketType(CPacketSendAction.class);
			network.registerPacketType(SPacketSendAction.class);
			network.registerPacketType(SPacketGameOver.class);
			
			network.start(this.host, this.port);
		}).start();
		
	}
	
	public void stop() {
		System.out.println("Stopping the server...");
		System.exit(0);
	}
	
	public void userDisconnect(User user) {
		this.users.remove(user);
		this.randomMode.remove(user);
		if(user.isInGame())
			for(Game g : this.games) {
				if(g.hasUser(user)) {
					this.gameOver(g, user.equals(g.getUser(g.getPlayer(1)))? 0 : 1, true);
					break;
				}
			}
		this.users.forEach(a -> {
			try {
				a.getManager().sendPacket(new SPacketPlayerList(this.users));
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
	
	public void randomMode(boolean randomMode, User user) {
		if(randomMode)
			this.randomMode.add(user);
		else
			this.randomMode.remove(user);
		
		if(this.randomMode.size() >= 2) {
			
			User u1 = this.randomMode.remove(0);
			User u2 = this.randomMode.remove(0);
			
			this.newGame(u1, u2);
		}
	}
	


	public void newGame(User u1, User u2) {
		Game game = null;
		try {
			u1.getManager().sendPacket(new SPacketStartGame(u2.getNickname()));
			u2.getManager().sendPacket(new SPacketStartGame(u1.getNickname()));
			
			u1.setStatus(true);
			u2.setStatus(true);
			
			game = new Game(u1, u2, new OnlinePlayer(Piece.BLACK_PIECE), new OnlinePlayer(Piece.WHITE_PIECE));
			this.games.add(game);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.users.forEach(a -> {
			if(!a.isInGame()) {
				try {
					a.getManager().sendPacket(new SPacketPlayerList(this.users));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		while(game == null) {
			try {
				Thread.sleep(250);
			} catch (InterruptedException ie) {
			}
		}
		User current = game.getUser(game.getCurrent());
		try {
			current.getManager().sendPacket(new SPacketBeginner());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void gameOver(Game game, int winner, boolean packet) {
		if(winner != 2) {
			Player w = game.getPlayer(winner);
			Player l = game.getOther(w);
			game.getUser(w).addVictories(1);
			game.getUser(l).addDefeats(1);
			if(packet) {
				if(this.users.contains(game.getUser(w))) {
					try {
						game.getUser(w).getManager().sendPacket(new SPacketGameOver(true));
					} catch (Exception e) {
					}
				}
				if(this.users.contains(game.getUser(l))) {
					try {
						game.getUser(l).getManager().sendPacket(new SPacketGameOver(false));
					} catch (Exception e) {
					}
				}
			}
		}
		this.games.remove(game);
	}
	
	public List<User> getUsers() {
		return this.users;
	}

	public List<User> getRandomModePlayers() {
		return this.randomMode;
	}
	
	public List<Game> getGames() {
		return this.games;
	}
	
	public Map<User, User> getProposals() {
		return this.proposals;
	}
}
