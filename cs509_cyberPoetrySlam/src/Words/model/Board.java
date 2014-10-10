package Words.model;
import java.util.*;
public class Board implements Iterable<Word>{
    ArrayList<Word> words = new ArrayList<Word>();
	
	//add words to the board
	public void addWords(Word w){
		words.add(w);
	}
	public Word findWord(int x,int y){
		for(Word w:words){
			if(w.intersection(x,y)){
				return w;
			}
		}
		return null;
	}
	public Iterator<Word> iterator() {
		// TODO Auto-generated method stub
		return words.iterator();
	}

}
