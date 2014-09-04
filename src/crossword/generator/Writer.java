package crossword.generator;

import crossword.crossword.Crossword;

public interface Writer {
	
	public void write(Crossword crossword, String path) throws Exception;
	
}
