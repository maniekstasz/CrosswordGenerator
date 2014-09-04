package crossword.generator;

import crossword.crossword.CwEntry;

public class FromFileGenerator extends AbstractGenerator {


	public void initCrossword(int width, int height, long id) {
		super.initCrossword(width, height, id);
		crossword.setSaved(true);
	}
	
	public void addEntry(CwEntry entry){
		crossword.addEntry(entry);
	}
	
	
	
	@Deprecated
	@Override
	public void generateCrossword() {
	}


}
