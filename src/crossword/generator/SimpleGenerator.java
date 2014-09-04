package crossword.generator;

import crossword.core.ApplicationContext;
import crossword.crossword.Board;
import crossword.crossword.CwEntry;
import crossword.crossword.CwEntry.Direction;
import crossword.model.CrosswordService;
import crossword.model.Entry;

public class SimpleGenerator extends AbstractGenerator {

	private CrosswordService crosswordService = ApplicationContext.crosswordService;

	public SimpleGenerator() {
	}

	@Override
	public void generateCrossword() throws Exception {
		Board board = crossword.getBoard();
		int length = board.getHeight()-1;
		Entry firstEntry;
		do {
			firstEntry = crosswordService.getRandom(length, false);
			length--;
		} while (firstEntry == null);
		
		final CwEntry firstCwEntry = new CwEntry(firstEntry.getWord(),firstEntry.getClue(),1,1,Direction.VERT);
		setWordOnTheBoard(firstCwEntry);
		crossword.addEntry(firstCwEntry);
		
		int x = 1;
		int y = 1;
		for(int i=0;i<firstCwEntry.getWord().length();i++){
			final String pattern = generatePattern(x, y, Direction.HORIZ, PatternWays.AFTER, true);
			final Entry entry = crosswordService.getRandom(pattern);
			if(entry == null){
				y++;
				continue;
				
			}
			final CwEntry cwEntry = new CwEntry(entry.getWord(),entry.getClue(),x,y,Direction.HORIZ);
			setWordOnTheBoard(cwEntry);
			crossword.addEntry(cwEntry);
			y++;
		}
	}

}
