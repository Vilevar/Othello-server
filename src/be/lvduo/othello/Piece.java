package be.lvduo.othello;

public enum Piece {
	
	UNDIFINED(),
	BLANK(),
	WHITE_PIECE(),
	BLACK_PIECE();

	Piece() {}
	
	public boolean isPiece() {
		return (this == BLACK_PIECE || this == WHITE_PIECE);
	}
	
	public Piece getOpposite() {
		  switch(this) {
		    case BLACK_PIECE: return WHITE_PIECE;
		    case WHITE_PIECE: return BLACK_PIECE;
		    default: return this;
		  }
		}
}
