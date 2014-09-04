package crossword.crossword;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Crossword{
	private List<CwEntry> entries = new ArrayList<CwEntry>();
	private Board board;
	private final long id;
	private boolean saved = false;
	
	public Crossword(Board board, long id){
		this.board = board;
		this.id = id;
	}
	
	public Crossword(){
		this.id = -1;
	}
	
	public Crossword(long id){
		this.id = id;
	}
	
	public void addEntry(CwEntry entry){
		entries.add(entry);
	}
	
	public Iterator<CwEntry> getEntriesIterator(){
		return entries.iterator();
	}
	
	public Board getBoard(){
		return board;
	}
	public void setBoard(Board board){
		this.board = board;
	}

	public List<CwEntry> getEntries() {
		return entries;
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("{\r\n");
		sb.append("width:").append(board.getWidth()).append("\r\n");
		sb.append("height:").append(board.getWidth()).append("\r\n");
		sb.append("id:").append(id).append("\r\n");
		sb.append("entries:").append("\r\n").append("[").append("\r\n");
		for(CwEntry entry: entries){
			sb.append("{").append("\r\n");
			sb.append("word:").append(entry.getWord()).append("\r\n");
			sb.append("clue:").append(entry.getClue()).append("\r\n");
			sb.append("x:").append(entry.getX()).append("\r\n");
			sb.append("y:").append(entry.getY()).append("\r\n");
			sb.append("direction:").append(entry.getDirection().ordinal()).append("\r\n");
			sb.append("}").append("\r\n");
		}
		sb.append("]").append("\r\n");
		sb.append("}");
		return sb.toString();
	}

	public boolean isSaved() {
		return saved;
	}

	public void setSaved(boolean saved) {
		this.saved = saved;
	}
	
	public Long getId(){
		return id;
	}
	
}