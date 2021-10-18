/*
 * Place.java
 *
 * Created on 19 septembre 2004, 15:40
 */

package tetris.ihm;



import java.awt.*;
import tetris.game.NoPlaceException;
/**
 *
 * @author  de Boussineau
 */
public abstract class Place {
    
    protected int x;    
    protected int y;
    
    protected boolean full = false;
    protected boolean fixed = false;
    
    protected Color color = Color.BLACK;
    protected boolean highlight = false;
    
    protected TetrisPanel tetrisPanel;
    
    
    
    /** Creates a new instance of Place */
    public Place(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    
    
    public void setY(int y) {
        this.y = y;
    }
    
    public int getY() {
        return this.y;
    }
    
    
    
    
    public void setColor(Color color) {
        this.color = color;
    }
    
    public void setHighlight(boolean b) {
        this.highlight = b;
    }
    
    public Color getColor() {
        return this.color;
    }
    
    
    
    public void setFull(Color color) throws NoPlaceException {
        if(this.isFixed()) throw new NoPlaceException();
        this.full = true;
        this.setColor(color);
    }
    
    public void setNotFull() {
        this.full = false;
    }
    
    
    
    public void setFixed(boolean b) {
        this.fixed = b;
    }
    
    public boolean isFixed() {
        return this.fixed;
    }
    
    
    public abstract void draw(Graphics g);
}
