package crossword.generator;

import java.util.ArrayList;
import java.util.List;

import crossword.crossword.Crossword;
import crossword.model.FileService;

public class CwsBrowser implements Reader, Writer {

	private List<Crossword> crosswords = new ArrayList<Crossword>();
	private int currentIndex = -1;
	
	

	@Override
	public void write(Crossword crossword, String path)throws Exception {
		if(crossword.isSaved()){
			return;
		}
		String serializedCrossword = crossword.toString();
		FileService ef = new FileService();
		ef.saveCrosswordToFile(serializedCrossword, path + "/" + crossword.getId() + ".crs");
		crossword.setSaved(true);
	}

	@Override
	public List<Crossword> getAllCws(String path)throws Exception {
		FileService ef = new FileService();
		List<String> fileNames = ef.getAllFileNames(path);
		List<Crossword> crosswords= new ArrayList<Crossword>(fileNames.size());
		for(String fileName : fileNames){
			final Crossword cws = ef.deserializeCrossword(path + "/" + fileName);
			crosswords.add(cws);
		}
		return crosswords;
	}
	
	
	public void readAllCws(String path)throws Exception{
		for(Crossword crossword : getAllCws(path))
			addCrossword(crossword);
	}
	
	public Crossword openCrossword(String path)throws Exception{
		FileService ef = new FileService();
		Crossword cws = ef.deserializeCrossword(path);
		addCrossword(cws);
		return cws;
	}
	
	public Crossword next(){
		if(crosswords.size() == 0){
			return null;
		}
		if(currentIndex<crosswords.size()-1){
			return crosswords.get(++currentIndex);
		}else{
			currentIndex = 0;
			return crosswords.get(currentIndex);
		}
	}
	
	public Crossword prev(){
		if(crosswords.size() == 0){
			return null;
		}
		if(currentIndex >= 1){
			return crosswords.get(--currentIndex);
		}else{
			currentIndex = crosswords.size()-1;
			return crosswords.get(currentIndex);
		}
	}
	
	public void addCrossword(Crossword crossword){
		crosswords.add(crossword);
	}

	
	public void saveAllUnsavedCrosswords(String path)throws Exception{
		for(Crossword crossword:crosswords){
			if(!crossword.isSaved()){
				write(crossword, path);
			}
		}
	}
}
