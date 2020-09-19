package be.lvduo.othello.player;

import java.awt.Point;

import be.lvduo.othello.Board;
import be.lvduo.othello.Piece;

public class OnlinePlayer implements Player {
	
	private Piece color;

	public OnlinePlayer(Piece color) {
		this.color = color;
	}
	
	@Override
	public boolean isHuman() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Piece getColor() {
		return color;
	}

	@Override
	public Point play(Board board) {
		return null;
	}

}
