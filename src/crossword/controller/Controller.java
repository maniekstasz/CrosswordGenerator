package crossword.controller;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.util.List;

import javax.swing.JFrame;
import crossword.core.ApplicationContext;
import crossword.crossword.Crossword;
import crossword.crossword.CwEntry;
import crossword.crossword.CwEntry.Direction;
import crossword.generator.AbstractGenerator;
import crossword.generator.ComplexGenerator;
import crossword.generator.CwsBrowser;
import crossword.generator.SimpleGenerator;
import crossword.handler.ExceptionHandler;
import crossword.model.CrosswordService;
import crossword.model.Entry;
import crossword.model.FileService;
import crossword.view.CrosswordView;
import crossword.view.MainWindow;

public class Controller {

	private MainWindow mainWindow;
	private CrosswordService crosswordService = ApplicationContext.crosswordService;
	private AbstractGenerator simpleGenerator = new SimpleGenerator();
	private AbstractGenerator complexGenerator = new ComplexGenerator();
	private ExceptionHandler exceptionHandler = ApplicationContext.exceptionHandler;

	private Crossword currentCrossword;
	private CrosswordView currentCrosswordView;
	private CwsBrowser cwsBrowser = new CwsBrowser();

	public void init() {
		mainWindow = new MainWindow(this);
		exceptionHandler.setMainWindow(mainWindow);
		ApplicationContext.dbHandler.init();
		mainWindow.setWindowsLookAndFeel();
		mainWindow.setVisible(true);
		mainWindow.setExtendedState(mainWindow.getExtendedState()
				| JFrame.MAXIMIZED_BOTH);
	}

	public Crossword generateCrossword(int width, int height, int gen)
			throws Exception {
		if (crosswordService.checkIfEmpty()) {
			exceptionHandler.handleException(new Exception(
					"Baza danych pusta, proszę wczytać słownik"));
		}
		Crossword crossword = null;
		if (gen == 0) {
			simpleGenerator.initCrossword(width, height);
			simpleGenerator.generateCrossword();
			crossword = simpleGenerator.getCrossword();
		} else {
			complexGenerator.initCrossword(width, height);
			complexGenerator.generateCrossword();
			crossword = complexGenerator.getCrossword();
		}
		return crossword;
	}

	public Crossword deserializeCrossword(String path) throws Exception {
		FileService ef = new FileService();
		return ef.deserializeCrossword(path);
	}

	public void printCrossword() {
		if (currentCrossword == null)
			return;
		PrinterJob job = PrinterJob.getPrinterJob();
		job.setPrintable(currentCrosswordView);
		boolean ok = job.printDialog();
		if (ok) {
			try {
				job.print();
			} catch (PrinterException e) {
				exceptionHandler.handleException(e);
			}
		}
	}

	public void displayCrossword(Crossword crossword) {
		currentCrossword = crossword;
		currentCrosswordView = new CrosswordView(crossword.getBoard()
				.getWidth(), crossword.getBoard().getHeight(), this);
		currentCrosswordView.prepareEmptyCrossword(crossword.getBoard(),
				crossword.getEntries());
//		currentCrosswordView.prepareClues(crossword.getEntries());
		mainWindow.clearContent();

		mainWindow.addToContent(currentCrosswordView.getBoardPanel());
		// mainWindow.addToContent(currentCrosswordView.getCluesPane());

	}

	public String getCluesText() {
		StringBuffer vertS = new StringBuffer();
		StringBuffer horizS = new StringBuffer();
		int horiz = 1;
		int vert = 1;
		for (CwEntry entry : currentCrossword.getEntries()) {
			if (entry.getDirection() == Direction.HORIZ) {
				horizS.append(horiz++ + ". " + entry.getClue() + "\n");
			} else {
				vertS.append(vert++ + ". " + entry.getClue() + "\n");
			}

		}
		String clues = "Poziomo:\n" + horizS.toString() + "\nPionowo:\n"
				+ vertS.toString() + "\n";
		return clues;
	}
	
	public void saveCurrentCrosswordToFile(File dir) {
		try {
			String path = dir.getAbsolutePath();
			cwsBrowser.saveAllUnsavedCrosswords(path);
		} catch (Exception e) {
			exceptionHandler.handleException(e);
		}
	}

	public void saveAllCrosswords(File dir) {
		try {
			String path = dir.getAbsolutePath();
			cwsBrowser.saveAllUnsavedCrosswords(path);
		} catch (Exception e) {
			exceptionHandler.handleException(e);
		}
	}

	public void deserializeDictFile(String path) {
		try {
			FileService ef = new FileService();
			List<Entry> entries = ef.deserializeDictFile(path);

			crosswordService.addAll(entries);
		} catch (Exception e) {
			exceptionHandler.handleException(e);
		}
	}

	public void generateBtnAction(int width, int height, int gen) {
		// if (!checkIfCanCreateNewCrossword()) {
		// return;
		// }
		Crossword crossword;
		try {
			crossword = generateCrossword(width, height, gen);
			cwsBrowser.addCrossword(crossword);
			displayCrossword(crossword);
		} catch (Exception e) {
			exceptionHandler.handleException(e);
		}
	}

	public void solveCrossword() {
		if (currentCrossword != null)
			currentCrosswordView.solveCrossword(currentCrossword.getBoard());
	}

	public void handleCrosswordOpening(String path) {
		try {
			Crossword crossword = cwsBrowser.openCrossword(path);
			displayCrossword(crossword);
		} catch (Exception e) {
			exceptionHandler.handleException(e);
		}
	}

	public void handleManyCrosswordOpening(String path) {
		try {
			cwsBrowser.readAllCws(path);
		} catch (Exception e) {
			exceptionHandler.handleException(e);
		}
	}

	public void readAllCwsFromDir(String path) {
		try {
			cwsBrowser.readAllCws(path);
		} catch (Exception e) {
			exceptionHandler.handleException(e);
		}
	}

	public void handleFileChoose(File file) {
		String extension = file.getPath().substring(
				file.getPath().indexOf(".") + 1);
		if ("txt".equals(extension)) {
			deserializeDictFile(file.getAbsolutePath());
		} else if ("crs".equals(extension)) {
			handleCrosswordOpening(file.getAbsolutePath());
		} else if (file.isDirectory()) {
			handleManyCrosswordOpening(file.getAbsolutePath());
		}
	}

	// public boolean checkIfCanCreateNewCrossword() {
	// if (currentCrossword != null && !currentCrossword.isSaved()) {
	// int returnValue = mainWindow.showSaveCrosswordConfirmDialog();
	// if (returnValue == JOptionPane.YES_OPTION) {
	// File saveFile = mainWindow.showSaveCrosswordDialog();
	// if (saveFile != null) {
	// saveCurrentCrosswordToFile(saveFile);
	// }
	// } else if (returnValue == JOptionPane.CANCEL_OPTION) {
	// return false;
	// }
	// }
	// return true;
	// }
	public void showNext() {
		Crossword crossword = cwsBrowser.next();
		if (crossword != null) {
			displayCrossword(crossword);
		}
	}

	public void showPrev() {
		Crossword crossword = cwsBrowser.prev();
		if (crossword != null) {
			displayCrossword(crossword);
		}
	}
}
