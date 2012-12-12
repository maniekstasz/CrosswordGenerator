package crossword.generator;

import java.util.List;

import crossword.crossword.Crossword;

public interface Reader {
	public List<Crossword> getAllCws(String path) throws Exception;
}
