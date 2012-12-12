package crossword.model;

import java.util.Arrays;
import java.util.List;

import crossword.core.ApplicationContext;

public class CrosswordService implements EntryService, InteliService {

	private EntryDao entryDao = ApplicationContext.entryDao;
	private InteliDao inteliDao = ApplicationContext.inteliDao;

	@Override
	public List<Entry> findAll(String pattern) throws Exception {
		List<Entry> entries  = inteliDao.findAll(pattern);
		return entries;
	}

	@Override
	public Entry getRandom() throws Exception {
		Entry entry= inteliDao.getRandom();
		return entry;
	}

	@Override
	public Entry getRandom(int length, boolean fraction) throws Exception {
		if (fraction) {
			return getRandom("^.{2," + length + "}$");
		}
		return getRandom("^.{" + length + "}$");
	}

	@Override
	public Entry getRandom(String pattern) throws Exception {
		Entry entry = inteliDao.getRandom(pattern);
		return entry;
	}

	@Override
	public Entry add(String word, String clue) throws Exception {
		Entry entry = new Entry(word, clue);
		entryDao.save(Arrays.asList(entry));
		return entry;
	}

	@Override
	public Entry get(String word) throws Exception {
		Entry entry =  entryDao.get(word);
		return entry;
	}

	@Override
	public void remove(String word) throws Exception {
		entryDao.remove(word);
	}

	@Deprecated
	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void addAll(List<Entry> entries) throws Exception {
		entryDao.save(entries);
	}

	@Override
	public boolean checkIfEmpty() throws Exception {
		boolean empty = false;
		empty = inteliDao.checkIfEmpty();
		return empty;
	}
}
