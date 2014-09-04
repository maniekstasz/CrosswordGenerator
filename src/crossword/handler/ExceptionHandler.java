package crossword.handler;

import crossword.view.MainWindow;


public class ExceptionHandler {
	
	private MainWindow mainWindow;
	
	public void setMainWindow(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
	}
	
	public void handleException(Exception e){
		mainWindow.showErrorMessage(e.getMessage());
		throw new RuntimeException(e);
	}
	
	public void handleCriticalException(Exception e){
		mainWindow.showErrorMessage(e.getMessage());
		mainWindow.clearContent();
		System.exit(0);
	}
	
}