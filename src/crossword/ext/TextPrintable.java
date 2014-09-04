package crossword.ext;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.WordUtils;

public class TextPrintable implements Printable {
	
	private List<String> processedText;
	private int marginY = 0;
	private int marginX = 0;
	public TextPrintable(String text,int marginY,int marginX,PageFormat pf, Graphics g){
		this.marginX = marginX;
		this.marginY = marginY;
		processText(text, pf,g);
	}
	
	private void processText(String text, PageFormat pf, Graphics g){
		FontMetrics fm = g.getFontMetrics();
		String[] temp = text.split("\n");
		processedText = new ArrayList<String>();
		for(int i = 0; i< temp.length;i++){
			if(temp[i].length() != 0){
				int cellSize = (fm.stringWidth(temp[i])/temp[i].length());
				String combinedLines = WordUtils.wrap(temp[i], (int)((pf.getWidth()-(6*marginX))/cellSize),"\n",true);
				processedText.addAll(Arrays.asList(combinedLines.split("\n")));
			}else{
				processedText.add(temp[i]);
			}
		}
	}

	@Override
	public int print(Graphics g, PageFormat pf, int pageIndex)
			throws PrinterException {
		FontMetrics fm = g.getFontMetrics();
		int yStart = marginY;
		int cluesPerPage = (int)((pf.getHeight()-(2*marginY))/fm.getHeight());
		int i = -1;
		for (i = (pageIndex-1)*cluesPerPage; i < (pageIndex)*cluesPerPage && i < processedText.size(); i++) {
			g.drawString(processedText.get(i), marginX, yStart);
			yStart += fm.getHeight();
		}
		
		if(i > processedText.size() || i == -1){
			return NO_SUCH_PAGE;
		}
		return PAGE_EXISTS;
	}
}
