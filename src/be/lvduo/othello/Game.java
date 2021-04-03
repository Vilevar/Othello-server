package be.lvduo.othello;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import be.lvduo.othello.player.Player;

public class Game {

	private Board board = new Board();

	private final List<User> users;
	
	private User current;
	private HashMap<Point, List<Direction>> availableShots;
	
	private int winner = -1;
	
	
	public Game(User user1, User user2, Player player1, Player player2) {
		this.board = new Board();
		
		user1.setPlayer(player1);
		user2.setPlayer(player2);
		
		boolean b = Math.random() < 0.5;
		this.users = new ArrayList<>();
		this.users.add(b ? user1 : user2);
		this.users.add(b ? user2 : user1);
		
		this.current = user1;
		
	}
	
	public void play(Point shot) {
		if(this.getPossiblesShots(this.current).containsKey(shot)) {
			this.board.togglePieces(shot, this.current.getPlayer().getColor(), this.getPossiblesShots(this.current).get(shot));
		//	try {this.getUser(this.getOther(this.current)).getManager().sendPacket(new SPacketSendAction(shot));} catch (Exception e) {e.printStackTrace();}
			if(this.getPossiblesShots(this.toggleCurrent()).isEmpty() && this.getPossiblesShots(this.toggleCurrent()).isEmpty())
				this.gameOver();
		}		
	}
	
	public boolean isOver() {
		return winner != -1;
	}
	
	public void gameOver() {
		int nBlanks = 0;
		double nPlayer1 = 0.0;
		for(int x = 0; x < Board.WIDTH; x++) {
			for(int y = 0; y < Board.HEIGHT; y++) {
				Piece p = this.board.getPiece(x, y);
				if(!p.isPiece())
					nBlanks++;
				else if(p == this.users.get(0).getPlayer().getColor())
					nPlayer1++;
			}
		}
		double halfAvailablePieces = (Board.WIDTH*Board.HEIGHT - nBlanks) / 2.0;
		if(nPlayer1 > halfAvailablePieces) {
			this.winner = 0;
		} else if(nPlayer1 == halfAvailablePieces) {
			this.winner = 2;
		} else {
			this.winner = 1;
		}
		Main.server.gameOver(this, winner, false);
		
		for(User u : this.users) {
			u.setPlayer(null);
		}
	}
	
	public int getWinner() {
		return winner;
	}
	
	public HashMap<Point, List<Direction>> getPossiblesShots(User user) {
		if(this.availableShots != null)
			return this.availableShots;
		return this.availableShots = this.board.getPossibilities(user.getPlayer().getColor());
	}
	
	public User toggleCurrent() {
		Player current = this.current.getPlayer();
		if(this.current.equals(this.users.get(0))) {
			this.current = this.users.get(1);
		} else {
			this.current = this.users.get(0);
		}
		this.availableShots = null;
		//you turn
		System.out.println("Change current : from "+current+" to "+this.current);
		return this.current;
	}
	
	public User getOther(User u) {
		return u.equals(this.users.get(0)) ? this.users.get(0) : this.users.get(1);
	}
	
	public User getCurrent() {
		return current;
	}
	
	public User getPlayer(int i) {
		return this.users.get(i);
	}
	
	public List<User> getUsers() {
		return this.users;
	}
	
	public Board getBoard() {
		return board;
	}

	public boolean hasUser(User user) {
		return users.contains(user);
	}
}
