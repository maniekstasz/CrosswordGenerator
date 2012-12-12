package crossword.generator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import crossword.core.ApplicationContext;
import crossword.crossword.Board;
import crossword.crossword.BoardCell;
import crossword.crossword.Crossword;
import crossword.crossword.CwEntry;
import crossword.crossword.CwEntry.Direction;
import crossword.model.InteliService;

public abstract class AbstractGenerator {
	protected InteliService inteliService = ApplicationContext.crosswordService;
	protected Crossword crossword;

	public enum PatternWays {
		AFTER, BEFORE, BOTH
	};

	public void initCrossword(int width, int height, long id) {
		Board b = new Board(width, height);
		crossword = new Crossword(id);
		crossword.setBoard(b);
	}
	
	public void initCrossword(int width, int height) {
		Board b = new Board(width, height);
		crossword = new Crossword(new Date().getTime());
		crossword.setBoard(b);
	}

	public String generatePatternBefore(int startx, int starty, Direction d, boolean ignoreNear) {
		int diff = 0;
		short x = 0, y = 0, a = 0, b = 0;
		int freeCount = 0;
		boolean save = false;
		List<String> possibilities = new ArrayList<String>();
		StringBuffer result = new StringBuffer();
		StringBuffer temp = new StringBuffer();

		if (d == Direction.HORIZ) {
			diff = startx;
			x = -1;
			b = 1;
		} else {
			diff = starty;
			y = -1;
			a = 1;
		}
		for (int i = 1; i <= diff; i++) {
			final BoardCell cell = crossword.getBoard().getCell(
					startx + (i * x), starty + (i * y));

			char sign = cell.getContent();
			if (sign == '\0') {
				freeCount++;
				try {
					if (crossword
							.getBoard()
							.getCell(startx + (i * x) + a, starty + (i * y) + b)
							.getContent() != '\0' && !ignoreNear)
						break;
				} catch (ArrayIndexOutOfBoundsException e) {

				}
				try{
					if (crossword
							.getBoard()
							.getCell(startx + (i * x) - a, starty + (i * y) - b)
							.getContent() != '\0'&& !ignoreNear) {
						break;
					}
				}catch (ArrayIndexOutOfBoundsException e){
					
				}
				if (!cell.isEnabled()) {
					break;
				}
				if (save) {
					possibilities.add(0, temp.toString());
					save = false;
				}
			} else {
				if (freeCount > 1) {
					possibilities.add(0, ".{1,"
							+ (freeCount - 1) + "}" + temp.toString());
				}
				if (freeCount > 0) {
					temp.insert(0, sign + ".{" + freeCount + "}");
					save = true;
					freeCount = 0;
				}
			}
		}
		if (freeCount > 1) {
			possibilities.add(0,
					".{1," + (freeCount - 1) + "}" + temp.toString());
		}
		result.append("(");
		for (int i = 0; i < possibilities.size(); i++) {
			if (i > 0) {
				result.append("|");
			}
			result.append("(^" + possibilities.get(i) + ")");
		}
		if (result.toString().equals(new String("("))) {
			return null;
		}
		return result.toString();
	}

	private String generatePatternAfter(int startx, int starty, Direction d, boolean ignoreNear) {
		int diff = 0;
		short x = 0, y = 0, a = 0, b = 0;
		int freeCount = 0;
		boolean save = false;
		List<String> possibilities = new ArrayList<String>();
		StringBuffer result = new StringBuffer();
		StringBuffer temp = new StringBuffer();

		if (d == Direction.HORIZ) {
			diff = crossword.getBoard().getWidth() - startx - 1;
			x = 1;
			b = 1;
		} else {
			diff = crossword.getBoard().getHeight() - starty - 1;
			y = 1;
			a = 1;
		}
		for (int i = 1; i <= diff; i++) {
			final BoardCell cell = crossword.getBoard().getCell(
					startx + (i * x), starty + (i * y));

			char sign = cell.getContent();
			if (sign == '\0') {
				try {
					if (crossword
							.getBoard()
							.getCell(startx + (i * x) + a, starty + (i * y) + b)
							.getContent() != '\0' && !ignoreNear)
						break;
				} catch (ArrayIndexOutOfBoundsException e) {

				}
				try{
					if (crossword
							.getBoard()
							.getCell(startx + (i * x) - a, starty + (i * y) - b)
							.getContent() != '\0'&& !ignoreNear) {
						break;
					}
				}catch (ArrayIndexOutOfBoundsException e){
					
				}
				if (!cell.isEnabled()) {
					break;
				}
				freeCount++;
				if (save) {
					possibilities.add(temp.toString());
					save = false;
				}
			} else {
				if (freeCount > 1) {
					possibilities.add(temp.toString() + ".{1,"
							+ (freeCount - 1) + "}");
				}
				if (freeCount > 0) {
					temp.append(".{" + freeCount + "}" + sign);
					save = true;
					freeCount = 0;
				}
			}
		}
		if (save) {
			possibilities.add(temp.toString());
		} else if (freeCount > 0) {
			possibilities.add(temp.toString() + ".{1," + freeCount + "}");
		}
		result.append("(");
		for (int i = 0; i < possibilities.size(); i++) {
			if (i > 0) {
				result.append("|");
			}
			result.append("(" + possibilities.get(i) + "$)");
		}
		if (result.toString().equals(new String("("))) {
			return null;
		}
		return result.toString();
	}

	public String generatePattern(int startx, int starty, Direction d,
			PatternWays ways, boolean ignoreNear) {
		String result;
		if (ways == PatternWays.AFTER) {
			String after = generatePatternAfter(startx, starty, d,ignoreNear);
			if (after == null)
				return null;
			result = "^"
					+ crossword.getBoard().getCell(startx, starty).getContent();
			result += after + ")";
		} else if (ways == PatternWays.BEFORE) {
			String before = generatePatternBefore(startx, starty, d, ignoreNear);
			if (before == null)
				return null;
			result = before + ")";
			result += crossword.getBoard().getCell(startx, starty).getContent()
					+ "$";
		} else {
			String after = generatePatternAfter(startx, starty, d,ignoreNear);
			String before = generatePatternBefore(startx, starty, d,ignoreNear);
			if (after == null && before == null)
				return null;
			if (before == null) {
				result = "^";
			} else {
				result = generatePatternBefore(startx, starty, d,ignoreNear) + "|(^))";
			}
			result += crossword.getBoard().getCell(startx, starty).getContent();
			if (after == null) {
				result += "$";
			} else {
				result += generatePatternAfter(startx, starty, d,ignoreNear) + "|($))";
			}
		}
		return result;
	}

	public abstract void generateCrossword() throws Exception;

	public void setWordOnTheBoard(CwEntry cwEntry) {
		int x = cwEntry.getX();
		int y = cwEntry.getY();
		try {
			if (cwEntry.getDirection() == Direction.VERT) {
				crossword.getBoard().getCell(x, y - 1).setEnabled(false);
				crossword.getBoard().getCell(x, y + cwEntry.getWord().length())
						.setEnabled(false);
			} else {
				crossword.getBoard().getCell(x - 1, y).setEnabled(false);
				crossword.getBoard().getCell(x + cwEntry.getWord().length(), y)
						.setEnabled(false);
			}
		} catch (IndexOutOfBoundsException e) {
			// ignore
		}
		for (int i = 0; i < cwEntry.getWord().length(); i++) {
			final BoardCell cell = new BoardCell();
			cell.setContent(cwEntry.getWord().charAt(i));
			crossword.getBoard().setCell(x, y, cell);
			if (cwEntry.getDirection() == Direction.VERT) {
				y++;
			} else {
				x++;
			}
		}
	}

	public Crossword getCrossword() {
		return crossword;
	}
}
