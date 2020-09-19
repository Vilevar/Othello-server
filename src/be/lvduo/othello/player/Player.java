package be.lvduo.othello.player;

import java.awt.Point;

import be.lvduo.othello.Board;
import be.lvduo.othello.Piece;

public interface Player {
	
	boolean isHuman();
	Piece getColor();
	Point play(Board board);

}
