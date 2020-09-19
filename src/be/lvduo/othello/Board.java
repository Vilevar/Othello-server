package be.lvduo.othello;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Board implements Cloneable {
	
	public final static int WIDTH = 8;
	public final static int HEIGHT = 8;
	
	private Piece[][] board;
	
	public Board() {
		this.board = new Piece[HEIGHT][WIDTH];
		
		for(int y = 0; y < HEIGHT; y++) {
			for(int x = 0; x < HEIGHT; x++) {
				this.board[y][x] = Piece.BLANK;
			}
		}
		this.createBoard();
	}
	
	public void createBoard() {
		this.setPiece(Piece.BLACK_PIECE, (WIDTH/2) - 1, (HEIGHT/2) - 1);
		this.setPiece(Piece.BLACK_PIECE, WIDTH/2, HEIGHT/2);
		this.setPiece(Piece.WHITE_PIECE, (WIDTH/2) - 1, HEIGHT/2);
		this.setPiece(Piece.WHITE_PIECE, WIDTH/2, (HEIGHT/2) - 1);
	}
	
	public void setPiece(Piece piece, Point point) {
		this.setPiece(piece, point.x, point.y);
	}
	public void setPiece(Piece piece, int x, int y) {
		this.board[y][x] = piece;
	}
	
	public Piece getPiece(Point point) {
		return this.getPiece(point.x, point.y);
	}
	
	public Piece getPiece(int x, int y) {
		if(!isInBoard(x, y))
			return Piece.UNDIFINED;
		return this.board[y][x];
	}
	
	public HashMap<Point, List<Direction>> getPossibilities(Piece color) {
		HashMap<Point, List<Direction>> squares = new HashMap<>();
		for(int y = 0; y < Board.HEIGHT; y++) {
			for(int x = 0; x < Board.WIDTH; x++) {
				if(Board.isInBoard(x, y)) {
					if(this.getPiece(x, y) == color.getOpposite()) {
		
						dir: for(Direction direction : Direction.values()) {
							Point point = new Point(x - direction.dirX, y - direction.dirY);
							if(this.getPiece(point) == Piece.BLANK) {
								for(int i = x + direction.dirX, j = y + direction.dirY; isInBoard(i, j); i += direction.dirX, j += direction.dirY) {
									if(this.getPiece(i, j) != color.getOpposite()) {
										if(this.getPiece(i, j) == color) {
											List<Direction> availableDirections = squares.getOrDefault(point, new ArrayList<>());
											availableDirections.add(direction);
											squares.put(point, availableDirections);
										}
										continue dir;
									}
								}
							}
						}
					}
				}
			}
		}
		return squares;
	}
	
	public boolean isDone() {
		return getPossibilities(Piece.BLACK_PIECE).isEmpty() && getPossibilities(Piece.WHITE_PIECE).isEmpty();
	}
	
	public int getNPieces(Piece color) {
		int n = 0;
		for(int x = 0; x < WIDTH; x++) {
			for(int y = 0; y < HEIGHT; y++) {
				if(this.getPiece(x, y) == color) {
					n += 1;
				}
			}
		}
		return n;
	}
	
	public void togglePieces(Point point, Piece color, List<Direction> directions) {
		this.setPiece(color, point);
		for(Direction dir : directions) {
			for(Point copy = new Point(point.x + dir.dirX, point.y + dir.dirY); this.getPiece(copy) != color; 
					copy = new Point(copy.x + dir.dirX, copy.y + dir.dirY)) {
				this.setPiece(color, copy);
			}
		}
	}
	
	public static boolean isInBoard(Point pt) {
		return isInBoard(pt.x, pt.y);
	}
	
	public static boolean isInBoard(int x, int y) {
		return x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT;
	}
	
	public Board clone() {
		Board other = new Board();
		for(int i = 0; i < other.board.length; i++) {
			other.board[i] = Arrays.copyOf(this.board[i], this.board[i].length);
		}
		return other;
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder("[");
		for(int y = 1; y <= HEIGHT; y++) {
			str.append(Arrays.toString(this.board[HEIGHT - y])).append(y == HEIGHT ? "]" : ", ");
		}
		return str.toString();
	}

	public String toProperString() {
		StringBuilder str = new StringBuilder("[");
		for(int y = 1; y <= HEIGHT; y++) {
			str.append(Arrays.toString(this.board[HEIGHT - y])).append(y == HEIGHT ? "]" : ",\n");
		}
		return str.toString();
	}
}

