package be.lvduo.othello;

import be.lvduo.othello.network.NetworkManager;

public class User implements Comparable<User> {
	
	private NetworkManager manager;
	private String name;
	private int victories;
	private int defeats;
	private boolean inGame;
	
	
	public User(NetworkManager manager) {
		this.manager = manager;
		this.name = null;
		this.victories = this.defeats = 0;
		this.inGame = false;
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

	@Override
	public int compareTo(User user) {
		return user.getPoints() - this.getPoints();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof User))
			return false;
		
		return ((User) obj).getNickname().equals(this.name);
	}
}
