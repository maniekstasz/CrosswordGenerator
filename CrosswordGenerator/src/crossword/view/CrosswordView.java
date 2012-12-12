package crossword.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import crossword.controller.Controller;
import crossword.crossword.Board;
import crossword.crossword.CwEntry;
import crossword.crossword.CwEntry.Direction;
import crossword.ext.TextPrintable;

public class CrosswordView implements Printable {

	private int cellSize = 30;
	private JTextField[][] board;
	private final int boardCluesGap = 20;
	// private JTextComponent cluesPane = new JTextArea();
	private TextPrintable textPrintable;
	private JPanel boardPanel = new JPanel();
	private int boardWidth;
	private int boardHeight;
	private boolean generated = false;
	private Controller controller;
	public CrosswordView(int boardWidth, int boardHeight, Controller controller) {
		super();
		this.boardHeight = boardHeight;
		this.boardWidth = boardWidth;
		boardPanel.setLayout(null);
		boardPanel.setBackground(Color.white);
		boardPanel.setBounds(0, 0, (boardWidth) * cellSize, boardHeight
				* cellSize);
		// cluesPane.setBounds(0, boardPanel.getBounds().height, 600, 600);
		this.controller = controller;
	}

	public void solveCrossword(Board modelBoard) {
		if (!generated)
			return;
		for (int i = 0; i < boardWidth; i++) {
			for (int j = 0; j < boardHeight; j++) {
				if (modelBoard.getCell(i, j).getContent() != '\0') {
					board[i][j].setText(""
							+ modelBoard.getCell(i, j).getContent());
				}
			}
		}
	}

	public void prepareEmptyCrossword(Board modelBoard, List<CwEntry> entries) {

		board = new JTextField[boardWidth][boardHeight];
		for (int i = 0; i < boardWidth; i++) {
			for (int j = 0; j < boardHeight; j++) {
				if (modelBoard.getCell(i, j).getContent() != '\0'
						|| !modelBoard.getCell(i, j).isEnabled()) {
					board[i][j] = new JTextField();
					board[i][j].setHorizontalAlignment(JTextField.CENTER);

					board[i][j].setBounds(i * (cellSize), j * (cellSize),
							cellSize, cellSize);
					board[i][j]
							.setEnabled(modelBoard.getCell(i, j).isEnabled());

					boardPanel.add(board[i][j]);
				}
				if (modelBoard.getCell(i, j).getContent() != '\0') {
					board[i][j].setDocument(new CrosswordDocument(1));
				}
				if (!modelBoard.getCell(i, j).isEnabled()) {
					board[i][j].setVisible(false);
				}
			}
		}
		int horiz = 1;
		int vert = 1;
		for (CwEntry entry : entries) {
			if (entry.getDirection() == Direction.HORIZ) {
				board[entry.getX() - 1][entry.getY()].setText("" + horiz++);
				board[entry.getX() - 1][entry.getY()].setVisible(true);
				board[entry.getX() - 1][entry.getY()]
						.setBackground(Color.YELLOW);
				board[entry.getX() - 1][entry.getY()]
						.setToolTipText("Poziomo: " + entry.getClue());
			} else {
				board[entry.getX()][entry.getY() - 1].setText("" + vert++);
				board[entry.getX()][entry.getY() - 1].setVisible(true);
				board[entry.getX()][entry.getY() - 1]
						.setBackground(Color.YELLOW);
				board[entry.getX()][entry.getY() - 1]
						.setToolTipText("Pionowo: " + entry.getClue());
			}
		}
		generated = true;
	}


	@Override
	public int print(Graphics g, PageFormat pf, int pageIndex)
			throws PrinterException {
		if (pageIndex == 0) {
			double size = Math.min(pf.getImageableWidth(), pf.getImageableHeight());
			if (boardPanel.getWidth() > size || boardPanel.getHeight() > size) {

				BufferedImage img = new BufferedImage(boardPanel.getWidth(),
						boardPanel.getHeight(),
						BufferedImage.TYPE_4BYTE_ABGR_PRE);
				Graphics2D g2 = img.createGraphics();
				g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
						RenderingHints.VALUE_INTERPOLATION_BILINEAR);
				g2.setRenderingHint(RenderingHints.KEY_RENDERING,
						RenderingHints.VALUE_RENDER_QUALITY);
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);
				boardPanel.paint(g2);
				int w = img.getWidth();
				int h = img.getHeight();
				double size2 = Math.max((double) w, (double) h);
				int newW = w - (int) (w * ((size2 - size) / size2));
				int newH = h - (int) (h * ((size2 - size) / size2));
				g2.dispose();
				((Graphics2D) g).setRenderingHint(
						RenderingHints.KEY_INTERPOLATION,
						RenderingHints.VALUE_INTERPOLATION_BILINEAR);
				((Graphics2D) g).setRenderingHint(RenderingHints.KEY_RENDERING,
						RenderingHints.VALUE_RENDER_QUALITY);
				((Graphics2D) g).setRenderingHint(
						RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);
				g.drawImage(img, 0, 0, newW, newH, null);

			} else {
				Graphics2D g2d = (Graphics2D) g;
				g2d.translate(pf.getImageableX(), pf.getImageableY());
				boardPanel.printAll(g2d);
			}
		} else {
			if(pageIndex == 1){
				String clues = controller.getCluesText();
				textPrintable = new TextPrintable(clues, 30, 30, pf,g);
			}
			return textPrintable.print(g, pf, pageIndex);
		}
		return PAGE_EXISTS;
	}



	public int getBoardWidth() {
		return boardWidth;
	}

	public int getBoardHeight() {
		return boardHeight;
	}

	public int getBoardCluesGap() {
		return boardCluesGap;
	}

	// public JTextPane getCluesPane() {
	// return cluesPane;
	// }

	public JPanel getBoardPanel() {
		return boardPanel;
	}

	private class CrosswordDocument extends PlainDocument {
		private static final long serialVersionUID = -8841527371009503029L;
		private int maxChars;

		public CrosswordDocument(int maxChars) {
			this.maxChars = maxChars;
		}

		@Override
		public void insertString(int offs, String str, AttributeSet a)
				throws BadLocationException {
			if (str != null && (getLength() + str.length() <= maxChars)) {
				super.insertString(offs, str.toUpperCase(), a);
			}
		}

	}

}
