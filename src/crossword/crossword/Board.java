package crossword.crossword;

public class Board {
	private BoardCell[][] board;
	private int width;
	private int height;

	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		board = new BoardCell[width][height];
		for(int i = 0; i<height;i++){
			for(int j=0;j<width;j++){
				board[j][i] = new BoardCell();
			}
		}
	}
	
	public BoardCell getCell(int x, int y){
		return board[x][y];
	}
	public void setCell(int x, int y, BoardCell cell){
		board[x][y] = cell;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
}
