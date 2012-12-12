package crossword.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import crossword.core.ApplicationContext;
import crossword.crossword.Board;
import crossword.crossword.CwEntry;
import crossword.crossword.CwEntry.Direction;
import crossword.model.CrosswordService;
import crossword.model.Entry;

public class ComplexGenerator extends AbstractGenerator {

	private CrosswordService crosswordService = ApplicationContext.crosswordService;

	private List<String> crossingCellsH = new ArrayList<String>();
	private List<String> crossingCellsV = new ArrayList<String>();

	private final Random generator = new Random();
	private void handleDirGeneration(Direction d) throws Exception{
		final List<String> crossingCells;
		int rand;
		String cellCord;
		if (d == Direction.HORIZ) {
			rand = generator.nextInt(crossingCellsH.size());
			cellCord = crossingCellsH.get(rand);
			crossingCells = crossingCellsH;
		} else {
			rand = generator.nextInt(crossingCellsV.size());
			cellCord = crossingCellsV.get(rand);
			crossingCells = crossingCellsV;
		}
		final String cord[] = cellCord.split(";");
		int x = Integer.parseInt(cord[0]);
		int y = Integer.parseInt(cord[1]);
		final String pattern = generatePattern(x, y, d, PatternWays.BOTH, false);

		if (pattern == null) {
			crossingCells.remove(rand);
			return;
		}
		final Entry entry = crosswordService.getRandom(pattern);
		if (entry == null) {
			crossingCells.remove(rand);
			return;
		}
		int i = 0;
		int index = 1;
		String afterPattern = pattern.replaceFirst("^((\\(.*\\|.*\\))|(\\^))" + crossword.getBoard().getCell(x, y).getContent(), "");
		String beforePattern = pattern.substring(0, pattern.length() - afterPattern.length()-1);
		String word = entry.getWord();
		while (true) {
			index = word.indexOf(crossword.getBoard().getCell(x, y)
					.getContent(), i != index ? 0 : i + 1);
			String after = word.substring(index + 1);
			String before = word.substring(0,index);
			if(after.matches(afterPattern) && before.matches(beforePattern)){
				break;
			}
			i=index;
		}
		final CwEntry cwEntry;

		if (d == Direction.HORIZ) {
			cwEntry = new CwEntry(entry.getWord(), entry.getClue(), x - index,
					y, d);
		} else {
			cwEntry = new CwEntry(entry.getWord(), entry.getClue(), x, y
					- index, d);
		}

		setWordOnTheBoard(cwEntry);
		crossword.addEntry(cwEntry);
		updateCrossingCells(cwEntry);
	}

	@Override
	public void generateCrossword() throws Exception {
		Board board = crossword.getBoard();
		int length = board.getHeight() - 1;
		Entry firstEntry;
		do {
			firstEntry = crosswordService.getRandom(length, false);
			length--;
		} while (firstEntry == null);
		final CwEntry firstCwEntry = new CwEntry(firstEntry.getWord(),
				firstEntry.getClue(), board.getWidth()/2, 1, Direction.VERT);
		setWordOnTheBoard(firstCwEntry);
		crossword.addEntry(firstCwEntry);
		updateCrossingCells(firstCwEntry);

		boolean stopRand = false;
		int d = 0;
		while (crossingCellsV.size() > 0 || crossingCellsH.size() > 0) {
			if (!stopRand)
				d = generator.nextInt(2);
			if (crossingCellsV.size() == 0) {
				d = 0;
			}
			if (crossingCellsH.size() == 0) {
				d = 1;
			}
			handleDirGeneration(Direction.values()[d]);
		}

	}

	private void updateCrossingCells(CwEntry cwEntry) {
		int x = cwEntry.getX();
		int y = cwEntry.getY();
		if (cwEntry.getDirection() == Direction.VERT) {
			for (int i = y; i < y + cwEntry.getWord().length(); i++) {
				if (crossingCellsV.contains(x + ";" + i)) {
					crossingCellsH.remove(x + ";" + i);
				} else {
					crossingCellsH.add(x + ";" + i);
				}
			}
		} else {
			for (int i = x; i < x + cwEntry.getWord().length(); i++) {
				if (crossingCellsH.contains(i + ";" + y)) {
					crossingCellsV.remove(i + ";" + y);
				} else {
					crossingCellsV.add(i + ";" + y);
				}
			}
		}
	}
}
