package be.lvduo.othello.player;

import be.lvduo.othello.Piece;

public enum OpponentType {
	
	ONLINE(OnlinePlayer::new);
	
	private IPlayerCreator creator;
	OpponentType(IPlayerCreator creator) {
		this.creator = creator;
	}
	
	public Player createInstance(Piece color) {
		return this.creator.create(color);
	}
	
	private static interface IPlayerCreator {
		Player create(Piece color);
	}
}
