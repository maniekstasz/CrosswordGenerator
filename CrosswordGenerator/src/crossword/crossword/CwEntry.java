package crossword.crossword;

import crossword.model.Entry;

public class CwEntry extends Entry{
	public enum Direction {HORIZ, VERT};
	private int x;
	private int y;
	private Direction direction;
	
	public CwEntry(String word, String clue, int x, int y,Direction direction) {
		super(word, clue);
		this.x = x;
		this.y = y;
		this.direction = direction;
	}
	
	public CwEntry(){
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	
}
