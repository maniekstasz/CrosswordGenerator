package crossword.crossword;

public class BoardCell {
	public enum Type {START, STOP, INNER};
	private char content = '\0';
	private boolean enabled = true;
	
	public char getContent() {
		return content;
	}
	public void setContent(char content) {
		this.content = content;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
