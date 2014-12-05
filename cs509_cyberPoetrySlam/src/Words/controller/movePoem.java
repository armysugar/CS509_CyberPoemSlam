package Words.controller;
import Words.model.*;

public class movePoem extends Move{
	final Poem poem;
	int oldx;
	int oldy;
	int newx;
	int newy;
	
	public movePoem(Poem poem,int oldx,int oldy,int newx,int newy){
		this.poem = poem;
		this.oldx = oldx;
		this.oldy = oldy;
		this.newx = newx;
		this.newy = newy;
	}
	
	@Override
	public boolean execute(){
		poem.setLocation(newx, newy, newx, newy);
		return true;
	}
	
	@Override
	public boolean undo(){
		poem.setLocation(oldx,oldy,newx,newy);
		return true;
	}
}