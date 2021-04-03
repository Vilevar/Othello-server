package be.lvduo.othello;

import java.awt.Point;
import java.util.HashMap;
import java.util.List;

import be.lvduo.othello.player.Player;

public class Game {

	private Board board = new Board();

	
	public final User u1;
	public final User u2;
	private Player player1;
	private Player player2;
	
	private Player current;
	private HashMap<Point, List<Direction>> availableShots;
	
	private int winner = -1;
	
	
	public Game(User user1, User user2, Player player1, Player player2) {
		this.board = new Board();
		
		if(Math.random() < 0.5) {
			this.player1 = player1;
			this.player2 = player2;
			this.u1 = user1;
			this.u2 = user2;
		} else {
			this.player1 = player2;
			this.player2 = player1;
			this.u1 = user2;
			this.u2 = user1;
		}
		this.current = this.player1;
		
	}
	
	public void play(Point shot) {
		if(this.getPossiblesShots(this.current).containsKey(shot)) {
			this.board.togglePieces(shot, this.current.getColor(), this.getPossiblesShots(this.current).get(shot));
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
				else if(p == this.player1.getColor())
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
	}
	
	public int getWinner() {
		return winner;
	}
	
	public HashMap<Point, List<Direction>> getPossiblesShots(Player player) {
		if(this.availableShots != null)
			return this.availableShots;
		return this.availableShots = this.board.getPossibilities(player.getColor());
	}
	
	public Player toggleCurrent() {
		if(this.current == this.player1) {
			this.current = this.player2;
		} else if(this.current == this.player2) {
			this.current = this.player1;
		} else {
			this.current = this.player1;
		}
		this.availableShots = null;
		//you turn
		return this.current;
	}
	
	public Player getOther(Player p) {
		return p.equals(this.player1)?this.player2:this.player1;
	}
	public User getOther(User u) {
		return u.equals(this.u1)?this.u2:this.u1;
	}
	
	public Player getCurrent() {
		return current;
	}
	
	public Player getPlayer(int i) {
		switch (i) {
		case 0:
			return player1;
		case 1:
			return player2;
		default:
			return null;
		}
	}
	
	public User getUser(Player player) {
		if(player.equals(player2))
			return u2;
		else
			return u1;
	}
	
	public Board getBoard() {
		return board;
	}

	public boolean hasUser(User user) {
		if(user.equals(this.u1) || user.equals(this.u2))
			return true;
		return false;
	}
}
