package be.lvduo.othello;

public enum Direction {
	
	LEFT(-1,0),
	RIGHT(1,0),
	UP(0,1),
	DOWN(0,-1),
	UP_LEFT(1,1),
	UP_RIGHT(-1,1),
	DOWN_LEFT(-1,-1),
	DOWN_RIGHT(1,-1);
	
	public final int dirX;
	public final int dirY;
	
	Direction(int x, int y) {
		this.dirX = x;
		this.dirY = y;
	}
	
	
	
}
