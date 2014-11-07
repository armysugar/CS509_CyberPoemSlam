//package Words.controller;
package Words.controller;
import java.awt.Point;
import java.awt.event.*;
import java.util.*;

import Words.model.*;
import Words.view.*;

public class WordMoveController extends MouseAdapter{
	final Model model;
	final ApplicationCanvas panel;
	final Board b;
	Point anchor;
	int deltaX;
	int deltaY;
	
	int originalx;
	int originaly;
	
	public WordMoveController(Model model,ApplicationCanvas panel){
		this.model = model;
		this.panel = panel;
		this.b = model.getBoard();
	}
	
	/**mouse pressed**/
	public void mousePressed(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		anchor = e.getPoint();
		// no board? no behavior!
		if (model == null) { return;}
		Board board = model.getBoard();
		
		
		if (e.getButton()==MouseEvent.BUTTON3) {
			this.generateNewWord(e.getX(), e.getY());
			return;
		}
		
        if(e.getClickCount() == 2){
        	//must in the protected area
        	this.disconnectWord(x, y);
        }
        
	
		
		// pieces are returned in order of Z coordinate
		Word s = board.findWord(anchor.x, anchor.y);

		if (s != null) {
			setSelectedWord(s);
			return;
		}
	    else{
			Poem p = board.findPoem(anchor.x,anchor.y);
			if(p != null){
				setSelectedPoem(p);
				return;
			}
		}
		
		model.setSelected(null);
		model.setSelectedPoem(null);
	}
	
    /**mouse released**/
	public void mouseReleased(MouseEvent e){
		if(this.release()){
		panel.repaint();}
		else{
			return;
		}
	}
	
	/**mouse dragged**/
	public void mouseDragged(MouseEvent e){
		if(drag(e.getX(), e.getY())){
			panel.repaint();
		}
		else{
			return;
		}
		
	}
	
	/**generate a new word**/
    public boolean generateNewWord(int x, int y){
		if(y>300){
			model.getBoard().addWords(new Word(x, y, 120, 14, "Sample",2));
			panel.repaint();
			}
		return true;
	}
	
    /**set a word as selected**/
	public boolean setSelectedWord(Word s){
		Point relative = new Point (anchor);
		// no longer in the board since we are moving it around...
		//board.remove(s);
		model.setSelected(s);
		originalx = s.getX();
		originaly = s.getY();
			
		// set anchor for smooth moving
		deltaX = relative.x - originalx;
		deltaY = relative.y - originaly;
		return true;
	}
	
	/**set a poem as selected**/
	public boolean setSelectedPoem(Poem p){
		Point relative = new Point(anchor);
		
		model.setSelectedPoem(p);
		originalx = p.getX();
		originaly = p.getY();

		// set anchor for smooth moving
		deltaX = relative.x - originalx;
		deltaY = relative.y - originaly;
		return true;
	}
    
	/**drag mouse**/
	public boolean drag(int x, int y){
		if(model == null){return false;}
		Word selectedWord = model.getSelected();
		Poem selectedPoem = model.getSelectedPoem();
		
		//nothing selected
		if(selectedWord == null&&selectedPoem == null){
			return false;
		}
		
		//word selected
		if(selectedWord != null){
			selectedWord.setLocation(x-deltaX,y-deltaY);
			return true;
		}
		
		//poem selected
		if(selectedPoem != null){
			selectedPoem.setLocation(x-deltaX,y-deltaY);
			return true;
		}
        
		return true;
	}
	
	/**release mouse**/
	public boolean release(){
		if (model == null) { return false; }
		
		Word selectedWord = model.getSelected();
		Poem selectedPoem = model.getSelectedPoem();
		//nothing selected
		if(selectedWord == null&&selectedPoem == null){
			return false;
		}
		
		//move word or poem;
		if(selectedWord != null){
			this.moveWord(selectedWord);
		}
		else{
			this.movePoem(selectedPoem);
		}
		
		//release the mouse and repaint
		model.setSelected(null);
		model.setSelectedPoem(null);
		selectedWord = null;
		selectedPoem = null;
		return true;
	}
	
	/**move a word**/
	public void moveWord(Word w){
		if(this.originaly > 300&&w.getY() > 300){
			this.moveWordinUnprotectedarea(w);
			return;
		}
		
		if(this.originaly > 300&&w.getY() < 300){
			b.protectWords(w);
			return;
		}
		
		if(this.originaly < 300&&w.getY() < 300){
			this.moveWordinProtectedarea(w);
			return;
		}
		
		if(this.originaly < 300&&w.getY() > 300){
			b.releaseWords(w);
			return;
		}
		
	}
	
	/**move a poem**/
	public void movePoem(Poem p){
		if(this.originaly > 300&&p.getY() > 300){
			return;
		}
		
		if(this.originaly > 300&&p.getY() < 300){
			return;
		}
		
		if(this.originaly < 300&&p.getY() < 300){
			this.movePoeminProtectedarea(p);
			return;
		}
		
		if(this.originaly < 300&&p.getY() > 300){
			p.setLocation(originalx,originaly);
			return;
		}
	}
	
	/**move a word within unprotected area**/
	public void moveWordinUnprotectedarea(Word w){
		return;
	}
	
	/**move a word within protected area**/
	public void moveWordinProtectedarea(Word w){
		//no overlap, just return;
		if(b.checkOverlap(w) == null&&b.checkOverlapWord(w) == null){
			return;
		}
		
		//if overlap more than 1 element,set back to original location;
		if(model.getBoard().getOverLapNumber(w)>1){
			w.setLocation(originalx,originaly);
			return;
		}
		
		if(b.checkOverlap(w) != null){
	        this.connectTwoWords(w);
		}
		
		if(b.checkOverlapWord(w) != null){
			this.connectPoemandWord(w);
		}
	}
	
	/**move a poem within protected area**/
	public void movePoeminProtectedarea(Poem p){
		//if overlap, just send back
		if(b.checkOverlapPoem(p)){
			p.setLocation(originalx, originaly);
		}
		return;
	}
	
	/**check potential overlap with a word**/
	public boolean checkWordPotentialOverlap(Word w){
		Word connectWord = b.checkOverlap(w);
		int type = b.getOverlapType(w,connectWord);
		
		//check potential overlap
		if(type == 1||type == 2||type == 6){
			if(model.getBoard().checkPotentialOverlap(w,connectWord,1)){
				return true;
			}
		}
		else if(type == 3||type == 4||type ==5){
			if(model.getBoard().checkPotentialOverlap(w,connectWord,2)){
				return true;
			}
		}
		
		return false;
	}
	
    public void connectTwoWords(Word w){
		//check potential overlap
		boolean potentialOverlap = this.checkWordPotentialOverlap(w);
		
		//connect two words
		if(potentialOverlap == false){
		   WordConnectionController connection = new WordConnectionController(model,panel,b.checkOverlap(w));
		   connection.connect();
		}
		else{
			w.setLocation(originalx, originaly);
		}
    }
    
    public void connectPoemandWord(Word w){
    	Poem connectPoem = model.getBoard().checkOverlapWord(w);
		int type = model.getBoard().getOverlapPoemWord(connectPoem, w);
		boolean potential =  this.checkPotentialOverlapPoem(type, w, connectPoem);
		
		if(type == 7||type == 8||potential == true){
			w.setLocation(originalx,originaly);
		}
		else{
		WordConnectionController connection = new WordConnectionController(model,panel,connectPoem);
		connection.connectPoem(type);}
    }
    
   public boolean checkPotentialOverlapPoem(int type, Word w, Poem p){
	      if(type == 1||type == 4||type == 5){
	        	if(model.getBoard().checkPotentialOverlapPoem(w,p,1)){
	        		return true;
	        	}
	        }
	        else if(type == 2||type == 3||type == 6){
	        	if(model.getBoard().checkPotentialOverlapPoem(w,p,2)){
	        		return true;
	        	}
	        }
	   return false;
   }
   
   public void disconnectWord(int x, int y){
	   if(x<300){
   		Poem p = b.findPoem(x,y);
   		Word disconnectWord = null;
   		Row disconnectRow = null;
   		int type = 0;
   		
   		for(Row r:p.getRows()){
   			for(Word w:r.getWords()){
   				if(w.intersection(x,y)){
   				//must be edge word!
   				if(w.getX() == r.getX()){
   					type = 1;
   					disconnectWord = w;
   					disconnectRow = r;
   				}
   				else if(w.getX() == r.getX()+r.getWidth()-w.getWidth()){
   					type = 2;
   					disconnectWord = w;
   					disconnectRow = r;
   				}
   			}
   			}
   		}
   		
   		model.setSelectedWordinPoem(disconnectWord);
   		WordDisconnectionController disconnect = new WordDisconnectionController(model,panel,p);
   		disconnect.disconnectEdgeWord(type,disconnectRow);
   	}
   	return;
   }
}