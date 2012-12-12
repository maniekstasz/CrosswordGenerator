package crossword.model;

import java.util.List;

public interface InteliService {
	public List<Entry> findAll(String pattern) throws Exception;
	public Entry getRandom() throws Exception;
	public Entry getRandom(int length, boolean fraction) throws Exception;
	public Entry getRandom(String pattern) throws Exception;
	public boolean checkIfEmpty() throws Exception;
}
