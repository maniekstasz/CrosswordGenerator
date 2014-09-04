package crossword.main;
import java.awt.EventQueue;
import org.apache.commons.lang.WordUtils;

import crossword.controller.Controller;
import crossword.core.ApplicationContext;


@SuppressWarnings("unused")
public class Main {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ApplicationContext ctx = new ApplicationContext();
					Controller controller = new Controller();
					controller.init();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
