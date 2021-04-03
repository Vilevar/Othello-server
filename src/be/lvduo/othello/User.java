package be.lvduo.othello;

import be.lvduo.othello.network.NetworkManager;
import be.lvduo.othello.player.Player;

public class User {
	
	private NetworkManager manager;
	private String name;
	private int victories;
	private int defeats;
	private boolean inGame;
	private Player player;
	
	
	public User(NetworkManager manager) {
		this.manager = manager;
		this.name = null;
		this.victories = this.defeats = 0;
		this.inGame = false;
		this.player = null;
	}
	
	public NetworkManager getManager() {
		return this.manager;
	}
	
	public String getNickname() {
		return this.name;
	}
	
	public void setNickname(String nickname) {
		this.name = nickname;
	}
	
	public double getRatio() {
		if(this.victories == 0 && this.defeats == 0)
			return 0;
		return this.victories / (this.victories + this.defeats);
	}
	
	public int getPoints() {
		return Math.max(0, 100*this.victories - 50*this.defeats);
	}
	
	public int getVictories() {
		return this.victories;
	}
	
	public int addVictories(int add) {
		return this.victories += add;
	}
	
	public int getDefeats() {
		return this.defeats;
	}
	
	public int addDefeats(int add) {
		return this.defeats += add;
	}
	
	public boolean setStatus(boolean inGame) {
		return this.inGame = inGame;
	}
	
	public boolean isInGame() {
		return this.inGame;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public Player getPlayer() {
		return this.player;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof User))
			return false;
		
		return ((User) obj).getNickname().equals(this.name);
	}
	
	@Override
	public String toString() {
		return "[User "+name+"]";
	}
}
