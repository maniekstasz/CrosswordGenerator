package crossword.core;

import crossword.generator.AbstractGenerator;
import crossword.generator.SimpleGenerator;
import crossword.handler.DBHandler;
import crossword.handler.ExceptionHandler;
import crossword.model.CrosswordService;
import crossword.model.EntryDao;
import crossword.model.InteliDao;



public class ApplicationContext {
	public static final ExceptionHandler exceptionHandler = new ExceptionHandler();
	public static final DBHandler dbHandler = new DBHandler();
	public static final EntryDao entryDao = new EntryDao();
	public static final InteliDao inteliDao = new InteliDao();

	public static final CrosswordService crosswordService = new CrosswordService();
	
	public static final AbstractGenerator generator = new SimpleGenerator();

	
}