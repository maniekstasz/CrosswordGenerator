package crossword.model;

import java.util.List;


public interface EntryService {
	public Entry add(String word, String clue) throws Exception;
	public Entry get(String word) throws Exception;
	public void remove(String word) throws Exception;
	public int getSize();
	public void addAll(List<Entry> entries) throws Exception;
}
