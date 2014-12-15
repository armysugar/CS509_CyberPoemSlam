package Words.model;
import java.util.*;

public class Board implements Iterable<Word>, java.io.Serializable {
    ArrayList<Word> words = new ArrayList<Word>();
    ArrayList<Word> protectedWords = new ArrayList<Word>();
    ArrayList<Word> unProtectedWords = new ArrayList<Word>();
    ArrayList<Poem> poems = new ArrayList<Poem>();
 
    /**Add words into board**/
	public void addWords(Word w){
		words.add(w);
		unProtectedWords.add(w);
	}
	
	/**Add poems into board**/
	public void addPoems(Poem p){
		poems.add(p);
		
	}
	
	/**To get the word list of board**/
	public ArrayList<Word> getWords(){
		return this.words;
	}
	
	/**To get the list of protected words**/
	public ArrayList<Word> getProtectedWords(){
		return this.protectedWords;
	}
	
	/**To get the list of unprotected words**/
	public ArrayList<Word> getunprotectedWords(){
		return this.unProtectedWords;
	}
	
    /**To get the poem list in board**/
    public ArrayList<Poem> getPoems() {
    	return this.poems;
    }
	
    /**check if there is overlap between two poems**/
    public Poem getOverlapPoem(Poem p){
    	for(Poem poem:poems){
    		if(!poem.equals(p)){
    		for(Row r:poem.getRows()){
    			for(Word w:r.getWords()){
    				for(Row row:p.getRows()){
    					if(w.overlapRow(row)){
    						return poem;
    					}
    				}
    			  }
    		    }
    		}
    	}
    	return null;
    }
    
    /**select a row by selecting area**/
    public Row getSelectedRow(Area a){
    	for(Poem p:poems){
    		for(Row r:p.rows){
    			if(a.getX() < r.getX()&&a.getY()<r.getY()&&a.getX()+a.getWidth()>r.getX()+r.getWidth()&&a.getY()+a.getHeight()>r.getY()+r.getHeight()){
    				return r;
    			}
    		}
    	}
    	return null;
    }
    
    /**protect a unprotected word**/
	public void protectWords(Word w){
		unProtectedWords.remove(w);
		protectedWords.add(w);
	}
	
	/**release a protected word**/
	public void releaseWords(Word w){
		protectedWords.remove(w);
		unProtectedWords.add(w);
	}
	
	/**find a mouse clicked row**/
	public boolean findRow(int x,int y,Row r){
		if(r == null){
			System.out.println("null");
			return false;
		}
		if(r.intersection(x, y)){
			return true;
		}
		return false;
	}
	
	/**find a word by x,y**/
	public Word findWord(int x,int y){
		for(Word w:words){
			if(w.intersection(x,y)){
				return w;
			}
		}
		return null;
	}
	
	/**find a poem by x,y**/
	public Poem findPoem(int x,int y){
		for(Poem p:poems){
			if(p.intersection(x,y)){
				return p;
			}
		}
		return null;
	}
	
	/**Get a poem by a given row**/
	public Poem getPoemByRow(Row r){
		for(Poem p:poems){
			for(Row row:p.getRows()){
				if(row.equals(r)){
					return p;
				}
			}
		}
		return null;
	}
	
	/**check if there is overlap between two words**/
	public Word checkOverlap(Word w){
		for(Word s:words){
			if(s.equals(w)){
				continue;
			}
			if(s.overlap(w)){
				return s;
			}
		}
		return null;
	}
	
	/**check if any overlap on a poem**/
	public boolean checkOverlapPoem(Poem p){
		for(Word s:words){
			for(Row r:p.getRows()){
				if(s.overlapRow(r)){
					return true;
				}
			}
		}
		return false;
	}
	
	/**get a overlap poem of a word**/
	public Poem checkOverlapWord(Word w){
		for(Poem p:poems){
			for(Row r:p.getRows()){
				if(w.overlapRow(r)){
					return p;
				}
			}
		}
		return null;
	}
	
	/**get overlap element number of a word**/
    public int getOverLapNumber(Word w){
    	int number = 0;
    	for(Word s:words){
    		if(w.overlap(s)){
    			number++;
    		}
    	}
    	for(Poem p:poems){
    		for(Row r:p.getRows()){
    			if(w.overlapRow(r)){
    				number++;
    			}
    		}
    	}
    	return number;
    }
    
    public Poem getSubmittedPoemByArea(Area a){
    	for(Poem p:poems){
    		if(a.getX()<p.getMin_x()&&a.getY()<p.getMin_y()&&a.getX()+a.getWidth()>p.getMax_x()&&a.getY()+a.getHeight()>p.getMax_y()){
    			return p;
    		}
    	}
    	return null;
    }
    /**check potential overlap after connection**/
    public boolean checkPotentialOverlapPoem(Word selectedWord,Poem connectPoem,int type){
    	Row r = connectPoem.getOverlapRow(selectedWord);
    	if(type == 1){
    		int testx = r.getX()-selectedWord.getWidth();
    		int testy = r.getY();
    		Word test = new Word(testx,testy,selectedWord.getWidth(),selectedWord.getHeight(),"test",2);
    		for(Word w:words){
    			  if(test.overlap(w)){
    				  if(!w.equals(selectedWord))
    				     return true;
    			   }
    		}
    		for(Poem p:poems){
    			for(Row row:p.getRows()){
    				if(test.overlapRow(row)){
    					System.out.println("error!");
    					return true;
    				}
    			}
    		}
    	}
    	else if(type ==2){
    		int testx = r.getX()+r.getWidth()+selectedWord.getWidth();
    		int testy = r.getY();
    		Word test = new Word(testx,testy,selectedWord.getWidth(),selectedWord.getHeight(),"test",2);
    		for(Word w:words){
    			if(test.overlap(w)){
    				return true;
    			}
    		}
    		for(Poem p:poems){
    			for(Row row:p.getRows()){
    				if(test.overlapRow(row)){
    					return true;
    				}
    			}
    		}
    	}
    	return false;
    }
    
    /**check potential overlap with word after connection**/
    public boolean checkPotentialOverlap(Word selectedWord,Word connectWord,int type){
    	if(type == 1){
    		int testx = connectWord.getX()-selectedWord.getWidth();
    		int testy = connectWord.getY();
    		Word test = new Word(testx,testy,selectedWord.getWidth(),selectedWord.getHeight(),"test",2);
    		for(Word w:words){
    			if(!w.equals(selectedWord)){
    			if(test.overlap(w)){
    				return true;
    			}
    			}
    		}
    		for(Poem p:poems){
    			for(Row r:p.getRows()){
    				if(test.overlapRow(r)){
    					return true;
    				}
    			}
    		}
    	}
    	else if(type == 2){
    		int testx = connectWord.getX()+connectWord.getWidth();
    		int testy = connectWord.getY();
    		Word test = new Word(testx,testy,selectedWord.getWidth(),selectedWord.getHeight(),"test",2);
    		for(Word w:words){
    			if(test.overlap(w)){
    				if(!w.equals(selectedWord)){
    				System.out.println("wo kao");
    				return true;}
    			}
    		}
    		for(Poem p:poems){
    			for(Row r:p.getRows()){
    				if(test.overlapRow(r)){
    					return true;
    				}
    			}
    		}
    	}
    	return false;
    }
    
    public boolean checkDisconnectionAvailability(Poem p,Word w){
    	Row r = p.getRowByWord(w);
    	int rightLimit,leftLimit;
    	//not edge word, can not disconnect
    	if(r.getFirstWord() != w&&r.getLastWord()!=w){
    		return false;
    	}
    	
    	if(r.getNextRow()==null&&r.getFormerRow()==null){
    		return true;
    	}
		
    	if(r.getNextRow() == null){
    		rightLimit = r.getFormerRow().getX()+r.getFormerRow().getWidth();
    		leftLimit = r.getFormerRow().getX();
    	}
    	else if(r.getFormerRow() == null){
    		rightLimit = r.getNextRow().getX()+r.getNextRow().getWidth();
    		leftLimit = r.getNextRow().getX();
    	}
    	else{
    		int temp1 = r.getFormerRow().getX()+r.getFormerRow().getWidth();
    		int temp2 = r.getNextRow().getX()+r.getNextRow().getWidth();
    		
    		rightLimit = Math.max(temp1, temp2);
    		
    		int temp3 = r.getFormerRow().getX();
    		int temp4 = r.getNextRow().getX();
    		
    		leftLimit = Math.min(temp3, temp4);
    	}
		
    	if(r.getFirstWord() == w){
    		if(r.getNextWord(w).getX() > rightLimit){
    			System.out.println("why?");
    			return false;
    		}
    	}
    	
    	if(r.getLastWord() == w){
    		if(r.getFormerWord(w).getX()+r.getFormerWord(w).getWidth() < leftLimit){
    			return false;
    		}
    	}
    	return true;
    }
    
    /**check the type of overlap**/
	public int getOverlapType(Word w1,Word w2){
	if((w1.x+w1.width)>w2.getX()&&(w1.x+w1.width)<(w2.getX()+w2.width)&&(w2.getY()+w2.getHeight())>w1.y&&(w2.getY()+w2.getHeight())<(w1.y+w1.height)){
   		 return 1;
   	 }
   	 if((w1.x+w1.width)>w2.getX()&&(w1.x+w1.width)<(w2.getX()+w2.width)&&(w1.y+w1.height)>w2.getY()&&(w1.y+w1.height)<(w2.getY()+w2.height)){
   		 return 2; 
   	 }
   	 if((w2.getX()+w2.getWidth())>w1.x&&(w2.getX()+w2.getWidth())<(w1.x+w1.width)&&(w1.y+w1.height)>w2.getY()&&(w1.y+w1.height)<(w2.getY()+w2.height)){
   		 return 3;
   	 }
   	 if((w2.getX()+w2.getWidth()>w1.x)&&(w2.getX()+w2.getWidth())<(w1.x+w1.width)&&(w2.getY()+w2.getHeight()>w1.y)&&(w2.getY()+w2.getHeight())<(w1.y+w1.height)){
   		 return 4; 
   	 }
   	 if((w2.getX()+w2.getWidth()>w1.x)&&(w2.getX()+w2.getWidth()<w1.x+w1.width)&&w2.getY()==w1.y){
   		 return 5;
   	 }
   	 if((w1.x+w1.width>w2.getX())&&(w1.x+w1.width<w2.getX()+w2.getWidth())&&w1.y==w2.getY()){
   		 return 6;
   	 }
		return 0;
	}
	
	public int getOverlapTypeRowWord(Word w, Row r){
		if(w.getX()+w.getWidth()>r.getX()&&w.getX()+w.getWidth()<r.getX()+r.getWidth()&&
				w.getX()<r.getX()&&w.getY()+w.getHeight()>r.getY()&&w.getY()+w.getHeight()<r.getY()+r.getHeight()){
			return 1;
		}
		if(w.getX()<r.getX()+r.getWidth()&&w.getX()+w.getWidth()>r.getX()+r.getWidth()&&r.getX()<w.getX()
				&&w.getY()+w.getHeight()>r.getY()&&w.getY()+w.getHeight()<r.getY()+r.getHeight()){
			return 2;
		}
		if(w.getX()+w.getWidth()>r.getX()+r.getWidth()&&w.getX()<r.getX()+r.getWidth()&&r.getX()<w.getX()
				&&w.getY()+w.getHeight()>r.getY()+r.getHeight()&&w.getY()<r.getY()+r.getHeight()){
			return 3;
		}
		if(w.getX()+w.getWidth()>r.getX()&&w.getX()+w.getWidth()<r.getX()+r.getWidth()&&w.getX()<r.getX()
				&&w.getY()+w.getHeight()>r.getY()+r.getHeight()&&w.getY()<r.getY()+r.getHeight()){
			return 4;
		}
		if(w.getX()+w.getWidth()>r.getX()&&w.getX()+w.getWidth()<r.getX()+r.getWidth()&&
				w.getX()<r.getX()&&w.getY()==r.getY()){
			return 5;
		}
		if(w.getX()<r.getX()+r.getWidth()&&w.getX()+w.getWidth()>r.getX()+r.getWidth()&&r.getX()<w.getX()
				&&w.getY()==r.getY()){
			return 6;
		}
		if(w.getX()>r.getX()&&w.getX()+w.getWidth()<r.getX()+r.getWidth()&&w.getY()+w.getHeight()>r.getY()&&w.getY()+w.getHeight()<r.getY()+r.getHeight()){
			return 7;
		}
		if(w.getX()>r.getX()&&w.getX()+w.getWidth()<r.getX()+r.getWidth()&&w.getY()+w.getHeight()>r.getY()+r.getHeight()&&w.getY()<r.getY()+r.getHeight()){
			return 8;
		}
		return 0;
	}
	
	public int getOverlapPoemWord(Poem p, Word w){
		for(Row r:p.getRows()){
			if(w.overlapRow(r)){
				return getOverlapTypeRowWord(w,r);
			}
		}
		return 0;
	}
	
	public boolean getWord(int x,int y){
		for(Word w:words){
			if(w.getX()==x&&w.getY()==y)
				return true;
		}
		return false;
	}
	
	public void setPoemList(ArrayList<Poem> poems){
		this.poems = poems;
	}
	
	public void setProtectedWords(ArrayList<Word> protectedW){
		this.protectedWords = protectedW;
	}
	
	public void setunProtectedWords(ArrayList<Word> words){
		this.unProtectedWords = words;
	}
	
	public void removePoem(Poem p){
		poems.remove(p);
	}
	
	public boolean findPoem(Poem p){
		for(Poem poem:poems){
			if(poem.equals(p)){
				return true;
			}
		}
		return false;
	}
	
	public Iterator<Poem> poemIterator(){
		return poems.iterator();
	}
	
	public Iterator<Word> iterator() {
		// TODO Auto-generated method stub
		return words.iterator();
	}
	
	public Iterator<Word> protectedWordsIterator(){
		return protectedWords.iterator();
	}
	
	public Iterator<Word> unprotectedWordsIterator(){
		return unProtectedWords.iterator();
	}


    public String toString(){
        String board = new String();
        for(Word w : words){
//            board.concat(w.toString() + " , ");
            board = board + w.toString() + " , ";
        }
        return board;
    }
    
    public int size() {
        return unProtectedWords.size();
    }

    /** Return the given shape by index position. */
    public Word get(int rowIndex) {
        return unProtectedWords.get(rowIndex);
    }

    /** Sort shapes using given comparator. */
    public void sort(Comparator<Word> comparator) {
        Collections.sort(unProtectedWords, comparator);
    }

    public HashMap<Integer, Integer> countType() {
        HashMap<Integer, Integer> wordTypes = new HashMap<Integer, Integer>();
        //wordTypes.put(0, unProtectedWords.size());

        for(Word word: unProtectedWords){
            Integer count = wordTypes.get(word.wordType);
            if(count == null){
                wordTypes.put(word.wordType, 1);
            }
            else{
                wordTypes.put(word.wordType, count + 1);
            }
        }
        return wordTypes;
    }
}
//>>>>>>> refs/remotes/origin/Kuan_Jun
