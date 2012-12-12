package crossword.view;

import java.awt.Component;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;

import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JButton;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import java.awt.Rectangle;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.border.TitledBorder;
import javax.swing.JLabel;
import javax.swing.JSpinner;

import crossword.controller.Controller;
import crossword.core.ApplicationContext;
import crossword.ext.ExtensionFileFilter;
import crossword.handler.ExceptionHandler;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import java.io.File;

public class MainWindow extends JFrame {
	private static final long serialVersionUID = -7259211910693108464L;
	public JPanel content = new JPanel();
	public JPanel menu = new JPanel();
	private Controller controller;
	private JFileChooser fileChooser;
	private ButtonGroup cwsSelect;
	private ExceptionHandler exceptionHandler = ApplicationContext.exceptionHandler;

	public MainWindow(Controller controller) {
		this.controller = controller;
		initialize();

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		content.setLayout(null);
		content.setBackground(Color.white);
		this.setBounds(100, 100, 952, 663);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.getContentPane().add(content, BorderLayout.CENTER);

		this.getContentPane().add(menu, BorderLayout.NORTH);
		menu.setPreferredSize(new Dimension(250, 55));
		menu.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)),
				"Menu", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0,
						0, 0)));
		menu.setBounds(new Rectangle(0, 0, 100, 200));
		menu.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JLabel lblWysokosc = new JLabel("wysokość");
		menu.add(lblWysokosc);
		SpinnerModel sm1 = new SpinnerNumberModel(10, 10, 30, 1);
		final JSpinner spinnerHeight = new JSpinner(sm1);
		menu.add(spinnerHeight);

		JLabel lblSzerokosc = new JLabel("szerokość");
		menu.add(lblSzerokosc);
		SpinnerModel sm2 = new SpinnerNumberModel(10, 10, 30, 1);
		final JSpinner spinnerWidth = new JSpinner(sm2);
		menu.add(spinnerWidth);

		JRadioButton simpleRadio = new JRadioButton("Prosta");
		simpleRadio.setActionCommand("0");
		simpleRadio.setSelected(true);
		JRadioButton complexRadio = new JRadioButton("Skomplikowana");
		complexRadio.setActionCommand("1");
		cwsSelect = new ButtonGroup();
		cwsSelect.add(simpleRadio);
		cwsSelect.add(complexRadio);
		menu.add(simpleRadio);
		menu.add(complexRadio);
		JButton btnGeneruj = new JButton("Generuj");
		btnGeneruj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.generateBtnAction((Integer) spinnerWidth.getValue(),
						(Integer) spinnerHeight.getValue(), new Integer(
								cwsSelect.getSelection().getActionCommand()));
			}
		});
		menu.add(btnGeneruj);

		JButton btnNext = new JButton("Następna");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.showNext();
			}
		});
		menu.add(btnNext);

		JButton btnPrev = new JButton("Poprzednia");
		btnPrev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.showPrev();
			}
		});
		menu.add(btnPrev);

		JButton bntPrint = new JButton("Drukuj");
		bntPrint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.printCrossword();
			}
		});

		menu.add(bntPrint);

		JButton bntSave = new JButton("Zapisz");
		bntSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File file = showSaveCrosswordDialog();
				if (file != null) {
					controller.saveCurrentCrosswordToFile(file);
				}
			}
		});
		menu.add(bntSave);
		JButton bntSaveAll = new JButton("Zapisz wszystkie");
		bntSaveAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File file = showSaveCrosswordDialog();
				if (file != null) {
					controller.saveAllCrosswords(file);
				}
			}
		});
		menu.add(bntSaveAll);
		JButton bntSolve = new JButton("Rozwiąż");
		bntSolve.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.solveCrossword();
			}
		});
		menu.add(bntSolve);

		JButton btnOpenFile = new JButton("Wczytaj plik");
		btnOpenFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				fileChooser = new JFileChooser();
				fileChooser.addChoosableFileFilter(new ExtensionFileFilter(
						"Crs", "crs"));
				fileChooser.addChoosableFileFilter(new ExtensionFileFilter(
						"Txt", "txt"));
				fileChooser
						.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				fileChooser.setAcceptAllFileFilterUsed(false);

				int returnVal = fileChooser.showDialog(MainWindow.this,
						"Wybierz");

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					controller.handleFileChoose(file);
				}
				fileChooser.setSelectedFile(null);
			}
		});
		menu.add(btnOpenFile);
	}

	public void setWindowsLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			exceptionHandler.handleException(e);
		}
	}

	public void clearContent() {
		content.removeAll();
		content.repaint();
	}

	public void addToContent(Component component) {
		content.add(component);
	}

	public int showSaveCrosswordConfirmDialog() {
		Object[] options = { "Tak", "Nie", "Anuluj" };
		int n = JOptionPane.showOptionDialog(MainWindow.this,
				"Obecnie wyświetlana krzyżówka jest nie zapisana. Zapisać?",
				"Zapisać", JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
		return n;
	}

	public File showSaveCrosswordDialog() {
		fileChooser = new JFileChooser();
		fileChooser
				.addChoosableFileFilter(new ExtensionFileFilter("Crs", "crs"));
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setAcceptAllFileFilterUsed(false);
		int returnVal = fileChooser.showSaveDialog(MainWindow.this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			return file;
		}
		return null;
	}

	public void showErrorMessage(String msg) {
		JOptionPane.showMessageDialog(this, msg);
	}
}
