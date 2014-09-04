package crossword.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import crossword.crossword.Crossword;
import crossword.crossword.CwEntry;
import crossword.crossword.CwEntry.Direction;
import crossword.ext.ExtensionFileFilter;
import crossword.generator.FromFileGenerator;

public class FileService {


	public List<Entry> deserializeDictFile(String path) throws Exception{
		BufferedReader bf = null;
		try {
			FileInputStream in = new FileInputStream(new File(path));
			DataInputStream input = new DataInputStream(in);
			bf = new BufferedReader(new InputStreamReader(input));
			String word;
			String clue;
			List<Entry> entries = new ArrayList<Entry>();
			while ((word = bf.readLine()) != null) {
				clue = bf.readLine();
				entries.add(new Entry(word, clue));
				// TODO: check file for wrong structure
			}
			return entries;
		} catch (Exception e) {
			throw new Exception("Problem przy wczytywaniu pliku");
		} finally {
			try {
				if (bf != null) {
					bf.close();
				}
				bf = null;
			} catch (IOException e) {
				// ignore
			}
		}
	}

	public void saveCrosswordToFile(String serializedCrossword, String path)throws Exception {
		BufferedWriter out = null;
		try {
			FileWriter fstream = new FileWriter(path);
			out = new BufferedWriter(fstream);
			out.write(serializedCrossword);
		} catch (FileNotFoundException e) {
			throw new Exception("Plik nie istnieje");
		} catch (IOException e) {
			throw new Exception("Wystąpił problem przy zapisie do pliku.");
		} finally {
			try {
				if(out != null){
					out.close();
				}
				out = null;
			} catch (IOException e) {
				//ignore
			}
		}

	}

	public Crossword deserializeCrossword(String path)throws Exception {
		BufferedReader bf = null;
		try {
			FileInputStream in = new FileInputStream(new File(path));
			DataInputStream input = new DataInputStream(in);
			bf = new BufferedReader(new InputStreamReader(input));
			String line;
			CwEntry currentEntry = null;
			long id = -1;
			int width = -1;
			int height = -1;
			FromFileGenerator generator = new FromFileGenerator();
			boolean isEntry = false;
			while ((line = bf.readLine()) != null) {
				if (line.startsWith("id:")) {
					id = new Long(line.replace("id:", ""));
					if (id != -1 && width != -1 && height != -1) {
						generator.initCrossword(width, height, id);
					} else {
						// TODO throw exceptiuon
					}
				} else if (line.startsWith("clue:")) {
					currentEntry.setClue(line.replace("clue:", ""));
				} else if (line.startsWith("width:")) {
					width = new Integer(line.replace("width:", ""));
				} else if (line.startsWith("height:")) {
					height = new Integer(line.replace("height:", ""));
				} else if (line.startsWith("word:")) {
					currentEntry.setWord(line.replace("word:", ""));
				} else if (line.startsWith("x:")) {
					currentEntry.setX(new Integer(line.replace("x:", "")));
				} else if (line.startsWith("y:")) {
					currentEntry.setY(new Integer(line.replace("y:", "")));
				} else if (line.startsWith("direction:")) {
					currentEntry.setDirection(Direction.values()[new Integer(
							line.replace("direction:", ""))]);
				} else if ("{".equals(line)) {
					if (!isEntry) {
						currentEntry = new CwEntry();
						isEntry = true;
					}
				} else if ("}".equals(line) && isEntry) {
					isEntry = false;
					generator.addEntry(currentEntry);
					generator.setWordOnTheBoard(currentEntry);
				}
			}
			return generator.getCrossword();
		} catch (FileNotFoundException e) {
			throw new Exception("Plik nie istnieje");
		} catch (NumberFormatException e) {
			throw new Exception("Zły format pliku");
		} catch (IOException e) {
			throw new Exception("Wystąpił problem przy odczytywaniu pliku.");
		} finally {
			try {
				if(bf != null){
					bf.close();
				}
				bf = null;
			} catch (IOException e) {
				//ignore
			}
		}
	}

	public List<String> getAllFileNames(String path) {
		File dir = new File(path);
		File[] files = dir
				.listFiles(new ExtensionFileFilter("Only crs", "crs"));
		List<String> fileNames = new ArrayList<String>(files.length);
		for (int i = 0; i < files.length; i++) {
			fileNames.add(files[i].getName());
		}
		return fileNames;
	}

}
