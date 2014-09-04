package crossword.model;

public class Entry {
	protected Long id;
	protected String word;

	protected String clue;
	
	public Entry(String word, String clue) {
		this.word = word;
		this.clue = clue;
	}
	public Entry(Long id, String word, String clue) {
		this.id = id;
		this.word = word.toUpperCase();
		this.clue = clue;
	}
	
	public Entry(){
		
	}
	public String getWord() {
		return word;
	}
	public String getClue() {
		return clue;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public void setClue(String clue) {
		this.clue = clue;
	}
}
