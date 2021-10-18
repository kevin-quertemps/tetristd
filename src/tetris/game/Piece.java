/*
 * Piece.java
 *
 * Created on 19 septembre 2004, 15:51
 */

package tetris.game;



import java.awt.Color;
import java.net.URL;
import java.util.Enumeration;
import java.util.Random;
import java.util.Vector;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import tetris.ihm.Place;
/**
 *
 * @author  de Boussineau
 */
public abstract class Piece {
    
    private static Random random = new Random();
    
    
    public static Piece newPiece(Game game) {
        int n = random.nextInt(7);
        
        if(n==0) return new Tetris(game);
        else if(n==1) return new Triangle(game);
        else if(n==2) return new Cube(game);
        else if(n==3) return new LeftZigzag(game);
        else if(n==4) return new RightZigzag(game);
        else if(n==5) return new EquerreRight(game);
        else return new EquerreLeft(game);
    }
        
    protected static Clip clipDown;
    protected static Clip clipFix;
    protected static Clip clipTurn;
    protected static Clip clipMove;
   
    protected Game game;
    
    private int xCoord, yCoord;
    
    protected Vector carres = new Vector(4);
    
    private Carre center =  new Carre(0, 0);
    
    protected Color color;
    
    private Game.TetrisTimerTask task;
    
    private boolean fixed = false;
    
    public int nextPiecePanelX, nextPiecePanelY;
    

    
    
    
    /** Creates a new instance of Piece */
    public Piece(Game game, int x, int y, int nppX, int nppY, Color color) {
        this.game = game;
        this.xCoord = x;
        this.yCoord = y;
        this.nextPiecePanelX = nppX;
        this.nextPiecePanelY = nppY;
        this.color = color;
        
        this.carres.add(this.center);
    }
    
    
    
    public void setTimerTask(Game.TetrisTimerTask task) {
        this.task = task;
    }
    
    public Game.TetrisTimerTask getTimerTask() {
        return this.task;
    }
    
    
    public void setCenter(Carre center) {
        int x = center.getX();
        int y = center.getY();
        
        for(Enumeration e=this.carres.elements(); e.hasMoreElements();) {
            Carre carre = (Carre) e.nextElement();
            carre.setX(carre.getX() - center.getX());
            carre.setY(carre.getY() - center.getY());
        }
    }
    
    
    
    
    public Color getColor() {
        return this.color;
    }
    
    
    
    public Enumeration getCarres() {
        return this.carres.elements();
    }
    
    
    
    
    
    public synchronized void start() {
        this.markOnScreen();
        if(this.game.getState() != Game.GAME_OVER) this.game.getTimer().schedule(this.task);
    }
    
    
    
    
    
    
    public void markOnScreen() {
        if(Piece.clipDown != null) {
            Piece.clipDown.stop();
            Piece.clipDown.setFramePosition(0);
            Piece.clipDown.start();
        }
        
        Vector invalidPlaces = new Vector(4);
        try {
            for(Enumeration e=this.carres.elements(); e.hasMoreElements();) {
                Carre carre = (Carre) e.nextElement();
                invalidPlaces.add(carre.getPlace());
                this.game.setFullPlace(this.xCoord + carre.getX(), this.yCoord + carre.getY(), this.color);
        
                this.game.getPanel().refreshAfterMouvement(invalidPlaces);
            }
        }
        catch(NoPlaceException npe) {
            try {
                for(Enumeration e=this.carres.elements(); e.hasMoreElements();) {
                    Carre carre = (Carre) e.nextElement();
                    Place place = carre.getPlace();
                    invalidPlaces.add(carre.getPlace());
                    place.setFixed(false);
                    place.setNotFull();
                    this.game.setFullPlace(this.xCoord + carre.getX(), this.yCoord + carre.getY(), this.color);
                }
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            
            this.fix(false);
        
            this.game.getPanel().refreshAfterMouvement(invalidPlaces);

            this.game.gameOver();
        }            
    }

    
    public synchronized void moveLeft() {
        if(this.fixed) return;
        
        boolean displayClip = true;
        boolean hasToRefresh = true;
        Vector invalidPlaces = new Vector();

        for(Enumeration e=this.carres.elements(); e.hasMoreElements();) {
            Place place = ((Carre) e.nextElement()).getPlace();
            place.setNotFull();
            invalidPlaces.add(place);
        }


        Vector fulledPlaces = new Vector(4);
        try {
            this.xCoord --;

            for(Enumeration e=this.carres.elements(); e.hasMoreElements();) {
                Carre carre = (Carre) e.nextElement();
                this.game.setFullPlace(this.xCoord + carre.getX(), this.yCoord + carre.getY(), this.color);
                fulledPlaces.add(carre.getPlace());
                invalidPlaces.add(carre.getPlace());
            }
        }
        catch(NoPlaceException npe) {
            for(Enumeration e=fulledPlaces.elements(); e.hasMoreElements();) {
                ((Place) e.nextElement()).setNotFull();
            }
            this.xCoord ++;
            displayClip = false;
            hasToRefresh = false;
        }            

        if(Piece.clipMove != null && displayClip) {
            Piece.clipMove.stop();
            Piece.clipMove.setFramePosition(0);
            Piece.clipMove.start();
        }

        for(Enumeration e=this.carres.elements(); e.hasMoreElements();) {
            Carre carre = (Carre) e.nextElement();
            try {
                this.game.setFullPlace(this.xCoord + carre.getX(), this.yCoord + carre.getY(), this.color);
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
            invalidPlaces.add(carre.getPlace());
        }

        if(hasToRefresh) this.game.getPanel().refreshAfterMouvement(invalidPlaces);
    }
    
    public synchronized void moveRight() {
        if(this.fixed) return;
        
        boolean displayClip = true;
        boolean hasToRefresh = true;
        Vector invalidPlaces = new Vector();
        
        for(Enumeration e=this.carres.elements(); e.hasMoreElements();) {
            Place place = ((Carre) e.nextElement()).getPlace();
            place.setNotFull();
            invalidPlaces.add(place);
        }


       Vector fulledPlaces = new Vector(4);
        try {            
            this.xCoord ++;

            for(Enumeration e=this.carres.elements(); e.hasMoreElements();) {
                Carre carre = (Carre) e.nextElement();
                this.game.setFullPlace(this.xCoord + carre.getX(), this.yCoord + carre.getY(), this.color);
                fulledPlaces.add(carre.getPlace());
                invalidPlaces.add(carre.getPlace());
            }
        }
        catch(NoPlaceException npe) {
            for(Enumeration e=fulledPlaces.elements(); e.hasMoreElements();) {
                ((Place) e.nextElement()).setNotFull();
            }
            this.xCoord --;
            displayClip = false;
            hasToRefresh = false;
        }            

        if(Piece.clipMove != null && displayClip) {
            Piece.clipMove.stop();
            Piece.clipMove.setFramePosition(0);
            Piece.clipMove.start();
        }

        for(Enumeration e=this.carres.elements(); e.hasMoreElements();) {
            Carre carre = (Carre) e.nextElement();
            try {
                this.game.setFullPlace(this.xCoord + carre.getX(), this.yCoord + carre.getY(), this.color);
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
            invalidPlaces.add(carre.getPlace());
        }

       if(hasToRefresh) this.game.getPanel().refreshAfterMouvement(invalidPlaces);
    }
    
    public synchronized void rotateLeft() {
        for(Enumeration e=this.carres.elements(); e.hasMoreElements();) {
            Carre carre = (Carre) e.nextElement();
            int x = carre.getX();
            int y = carre.getY();
            carre.setX(y);
            carre.setY(-x);
        }
    }
    
    public synchronized void rotateRight() {
        for(Enumeration e=this.carres.elements(); e.hasMoreElements();) {
            Carre carre = (Carre) e.nextElement();
            int x = carre.getX();
            int y = carre.getY();
            carre.setX(-y);
            carre.setY(x);
        }
    }
    
    public synchronized void turnLeft() {
        if(this.fixed) return;
        
        boolean displayClip = true;
        boolean hasToRefresh = true;
        Vector invalidPlaces = new Vector();
        
        for(Enumeration e=this.carres.elements(); e.hasMoreElements();) {
            Place place = ((Carre) e.nextElement()).getPlace();
            place.setNotFull();
            invalidPlaces.add(place);
        }


        Vector fulledPlaces = new Vector(4);
        try {
            this.rotateLeft();

            for(Enumeration e=this.carres.elements(); e.hasMoreElements();) {
                Carre carre = (Carre) e.nextElement();
                this.game.setFullPlace(this.xCoord + carre.getX(), this.yCoord + carre.getY(), this.color);
                fulledPlaces.add(carre.getPlace());
                invalidPlaces.add(carre.getPlace());
            }
        }
        catch(NoPlaceException npe) {
            for(Enumeration e=fulledPlaces.elements(); e.hasMoreElements();) {
                ((Place) e.nextElement()).setNotFull();
            }
            this.rotateRight();
            displayClip = false;
            hasToRefresh = false;
        }            

        if(Piece.clipTurn != null && displayClip) {
            Piece.clipTurn.stop();
            Piece.clipTurn.setFramePosition(0);
            Piece.clipTurn.start();
        }

        for(Enumeration e=this.carres.elements(); e.hasMoreElements();) {
            Carre carre = (Carre) e.nextElement();
            try {
                this.game.setFullPlace(this.xCoord + carre.getX(), this.yCoord + carre.getY(), this.color);
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
            invalidPlaces.add(carre.getPlace());
        }

        if(hasToRefresh) this.game.getPanel().refreshAfterMouvement(invalidPlaces);
    }
    
    public synchronized void turnRight() {
        if(this.fixed) return;
        
        boolean displayClip = true;
        boolean hasToRefresh = true;
        Vector invalidPlaces = new Vector();
        
        for(Enumeration e=this.carres.elements(); e.hasMoreElements();) {
            Place place = ((Carre) e.nextElement()).getPlace();
            place.setNotFull();
            invalidPlaces.add(place);
        }


        Vector fulledPlaces = new Vector(4);
        try {
            this.rotateRight();

            for(Enumeration e=this.carres.elements(); e.hasMoreElements();) {
                Carre carre = (Carre) e.nextElement();
                this.game.setFullPlace(this.xCoord + carre.getX(), this.yCoord + carre.getY(), this.color);
                fulledPlaces.add(carre.getPlace());
                invalidPlaces.add(carre.getPlace());
            }
        }
        catch(NoPlaceException npe) {
            for(Enumeration e=fulledPlaces.elements(); e.hasMoreElements();) {
                ((Place) e.nextElement()).setNotFull();
            }
            this.rotateLeft();
            displayClip = false;
            hasToRefresh = false;
        }            

        if(Piece.clipTurn != null && displayClip) {
            Piece.clipTurn.stop();
            Piece.clipTurn.setFramePosition(0);
            Piece.clipTurn.start();
        }

        for(Enumeration e=this.carres.elements(); e.hasMoreElements();) {
            Carre carre = (Carre) e.nextElement();
            try {
                this.game.setFullPlace(this.xCoord + carre.getX(), this.yCoord + carre.getY(), this.color);
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
            invalidPlaces.add(carre.getPlace());
        }

        if(hasToRefresh) this.game.getPanel().refreshAfterMouvement(invalidPlaces);
    }
    
    public synchronized void moveDown(boolean toFix) {
        if(this.fixed) return;
        
        boolean mustBeFixed = false;
        boolean displayClip = true;
        boolean hasToRefresh = true;
        
        Vector invalidPlaces = new Vector();
        
        for(Enumeration e=this.carres.elements(); e.hasMoreElements();) {
            Place place = ((Carre) e.nextElement()).getPlace();
            place.setNotFull();
            invalidPlaces.add(place);
        }


       Vector fulledPlaces = new Vector(4);
        try {
            this.yCoord ++;

            for(Enumeration e=this.carres.elements(); e.hasMoreElements();) {
                Carre carre = (Carre) e.nextElement();
                this.game.setFullPlace(this.xCoord + carre.getX(), this.yCoord + carre.getY(), this.color);
                fulledPlaces.add(carre.getPlace());
                invalidPlaces.add(carre.getPlace());
            }
        }
        catch(NoPlaceException npe) {
            for(Enumeration e=fulledPlaces.elements(); e.hasMoreElements();) {
                Place place = (Place) e.nextElement();
                place.setNotFull();
            }
            this.yCoord --;
            mustBeFixed = true;
            displayClip = false;
            hasToRefresh = false;
        }            

        if(Piece.clipDown != null && displayClip && ! toFix) {
            Piece.clipDown.stop();
            Piece.clipDown.setFramePosition(0);
            Piece.clipDown.start();
        }
        for(Enumeration e=this.carres.elements(); e.hasMoreElements();) {
            Carre carre = (Carre) e.nextElement();
            invalidPlaces.add(carre.getPlace());
            try {
                this.game.setFullPlace(this.xCoord + carre.getX(), this.yCoord + carre.getY(), this.color);
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
        }

        if(mustBeFixed) this.fix(toFix);

       
        if(hasToRefresh) this.game.getPanel().refreshAfterMouvement(invalidPlaces);
       
        if(mustBeFixed) {
            if(Piece.clipFix != null) {
                Piece.clipFix.stop();
                Piece.clipFix.setFramePosition(0);
                Piece.clipFix.start();
            }
            this.game.checkLines();
            this.game.startNewPiece();
        }
    }
    
    public synchronized void fixAtBottom() {
        if(this.fixed) return;
        
        if(this.task != null) this.task.cancel();
        
        while(! this.fixed) {
        	try {
        		this.wait(5);
        	}
        	catch(InterruptedException ie) {
        		ie.printStackTrace();
        	}
        	this.moveDown(true);
        }
    }

    
    
    
    
    public void fix(boolean quickly) {
        this.fixed = true;

        try {
            this.task.cancel();
        }
        catch(Exception e) {
        }
        for(Enumeration e=this.carres.elements(); e.hasMoreElements();) {
            Place place = ((Carre) e.nextElement()).getPlace();
            place.setFixed(true);
        }
        
        if(quickly) this.game.addPoints(3);
        else this.game.addPoints(1);
    }
    
    
    
    
    
    
    
    public class Carre {
        private int x, y;
        
        public Carre(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
        public void setX(int x) {
            this.x = x;
        }
        
        public void setY(int y) {
            this.y = y;
        }
        
        public int getX() {
            return this.x;
        }
        
        public int getY() {
            return this.y;
        }
        
        public Place getPlace() {
            int xCoord = Piece.this.xCoord + this.x;
            int yCoord = Piece.this.yCoord + this.y;
            return Piece.this.game.getPlace(xCoord, yCoord);
        }
    }
}
