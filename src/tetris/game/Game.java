package tetris.game;

/*
 * Game.java
 *
 * Created on 19 septembre 2004, 15:26
 */



import java.awt.Color;
import java.awt.Image;
import java.net.URL;
import java.util.Enumeration;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;

import tetris.ihm.NextPiecePanel;
import tetris.ihm.Place;
import tetris.ihm.ScorePanel;
import tetris.ihm.TetrisPanel;
/**
 *
 * @author  de Boussineau
 */
public class Game {
        
    public static int GAME_INITIALIZED = 2;
    public static int GAME_RUNNING = 0;
    public static int GAME_PAUSED = 1;
    public static int GAME_OVER = 3;
        
    private static Clip lineClip;
    private static Clip tetrisClip;
       
    public static Image image1, image2, image3, image4, image5;
    
    public static void initializeImages(String source) {
        try{
            image1 = new ImageIcon(new URL(source + "/images/image1.jpg")).getImage();
            image2 = new ImageIcon(new URL(source + "/images/image2.jpg")).getImage();
            image3 = new ImageIcon(new URL(source + "/images/image3.jpg")).getImage();
            image4 = new ImageIcon(new URL(source + "/images/image4.jpg")).getImage();
            image5 = new ImageIcon(new URL(source + "/images/image5.jpg")).getImage();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
        
    private int score = 0, level = 1, nbLines = 0, nbTetris = 0;
    
    private int width, heigh;
    
    private TetrisPanel tetrisPanel;
    private NextPiecePanel nextPiecePanel;
    private ScorePanel scorePanel;
    
    private Piece currentPiece;
    private Piece nextPiece;
    
    private TetrisTimer timer = new Game.TetrisTimer();
    
    
    private int gameState = Game.GAME_INITIALIZED;
    
    /** Creates a new instance of Game */
    public Game(TetrisPanel tp, NextPiecePanel npp, ScorePanel sp) {
        this.tetrisPanel = tp;
        this.nextPiecePanel = npp;
        this.scorePanel = sp;
        
        this.width = this.tetrisPanel.getGamesWidth();
        this.heigh = this.tetrisPanel.getGamesHeight();
        
        this.tetrisPanel.setGame(this);
    }
    
    public int getState() {
        return this.gameState;
    }
    
    
    public int getWidth() {
        return this.width;
    }
    
    
    public int getLevel() {
        return this.level;
    }
    
    public TetrisPanel getPanel() {
        return this.tetrisPanel;
    }
      
    public Game.TetrisTimer getTimer() {
        return this.timer;
    }
    
    public Piece getCurrentPiece() {
        return this.currentPiece;
    }
   
    public void init() {
        this.tetrisPanel.init();
        this.score = 0;
        this.scorePanel.setScore(0);
        this.level = 1;
        this.scorePanel.setLevel(1);
        this.nbLines = 0;
        this.scorePanel.setLines(0);
        this.nbTetris = 0;
        this.scorePanel.setTetris(0);
        Vector invalidPlaces = new Vector();
        for(int i=1; i<=this.width; i++) {
            for(int j=1; j<=this.heigh; j++) {
                Place place = this.getPlace(i, j);
                place.setHighlight(false);
                place.setFixed(false);
                place.setNotFull();
                invalidPlaces.add(place);
            }
        }
        this.tetrisPanel.refreshImage();
        this.tetrisPanel.repaint();
    }
    
    public synchronized void start() {
        if(! (this.gameState == Game.GAME_OVER || this.gameState == Game.GAME_INITIALIZED)) return;
        this.gameState = Game.GAME_RUNNING;
        this.newPiece();
        this.startNewPiece();
    }
    
    public synchronized void stop() {
        try {
            this.timer.cancel();
        }
        catch(Exception e) {
        }
    }
    
    public synchronized void pause() {
        this.gameState = Game.GAME_PAUSED;
        this.currentPiece.getTimerTask().cancel();
    }
    
    public void resume() {
        this.gameState = Game.GAME_RUNNING;
        this.timer.schedule(new Game.TetrisTimerTask());
    }
    
    public void gameOver() {
        this.gameState = Game.GAME_OVER;
        this.timer.cancel();
        this.tetrisPanel.repaint();
    }    
    
    public void newPiece() {
        this.currentPiece = this.nextPiece;
        this.nextPiece = Piece.newPiece(this);
        this.nextPiecePanel.setPiece(this.nextPiece);
        this.nextPiecePanel.repaint();
    }
    
    public synchronized void startNewPiece() {
        this.newPiece();
        if(this.gameState != Game.GAME_OVER) new Game.TetrisTimerTask();
        if(this.gameState != Game.GAME_OVER) this.currentPiece.start();
    }
    
    public void moveLeft() {
        this.currentPiece.moveLeft();
    }
    
    public void moveRight() {
        this.currentPiece.moveRight();
    }

    public void turnLeft() {
        this.currentPiece.turnLeft();
    }
    
    public void turnRight() {
        this.currentPiece.turnRight();
    }
    
    public synchronized void moveDown() {
        this.currentPiece.moveDown(false);
    }

    public synchronized void fixAtBottom() {
        this.currentPiece.fixAtBottom();
    }
    
    
    
    
    public void addPoints(int points) {
        this.score += points;
        this.scorePanel.setScore(this.score);
    }
    
    public void addLines(int nbLines) {
        this.nbLines += nbLines;        
        if(nbLines == 4) this.nbTetris++;
        
        if(this.nbLines > 70) {
            this.level = 5;
            this.tetrisPanel.refreshImage();
        }
        else if(this.nbLines > 45) {
            this.level = 4;
            this.tetrisPanel.refreshImage();
        }
        else if(this.nbLines > 25) {
            this.level = 3;
            this.tetrisPanel.refreshImage();
        }
        else if(this.nbLines > 10) {
            this.level = 2;
            this.tetrisPanel.refreshImage();
        }
        
        this.scorePanel.setLevel(this.level);
        
        this.scorePanel.setLines(this.nbLines);
        this.scorePanel.setTetris(this.nbTetris);
        
        if(nbLines == 1) this.addPoints(20);
        else if(nbLines == 2) this.addPoints(75);
        else if(nbLines == 3) this.addPoints(200);
        else if(nbLines == 4) this.addPoints(500);
    }
    
    
    
    

    
    
    
    public Place getPlace(int x, int y) {
        return this.tetrisPanel.getPlace(x, y);
    }
    
    public void setFullPlace(int xCoord, int yCoord, Color color) throws NoPlaceException {
        if(xCoord < 1 || xCoord > this.width) throw new NoPlaceException();
        if(yCoord < 1 || yCoord > this.heigh) throw new NoPlaceException();
        
        this.getPlace(xCoord, yCoord).setFull(color);
    }
    
    
    
    
    public void checkLines() {
        Vector linesNumbers = new Vector(4);
        
        for(int i=1; i<=this.heigh; i++) {
            boolean lineFull = true;
            for(int j=1; j<=this.width; j++) {
                if(! this.getPlace(j, i).isFixed()) {
                    lineFull = false;
                    break;
                }
            }
            if(lineFull) {
                linesNumbers.add(new Integer(i));
            }
        }
        
        int size = linesNumbers.size();
        if(size != 0) {
            this.addLines(linesNumbers.size());

            this.highlightLines(linesNumbers);
            this.removeLines(linesNumbers);

            if(size < 4 && Game.lineClip != null) {
                Game.lineClip.stop();
                Game.lineClip.setFramePosition(0);
                Game.lineClip.start();
            }
            else if(Game.tetrisClip != null ) {
                Game.tetrisClip.stop();
                Game.tetrisClip.setFramePosition(0);
                Game.tetrisClip.start();
            }
        }
        
        this.tetrisPanel.refreshAfterLines();        
    }
    
    public void highlightLines(Vector linesNumbers) {
        Vector invalidPlaces = new Vector();
        
        for(Enumeration e=linesNumbers.elements(); e.hasMoreElements();) {
            int i = ((Integer) e.nextElement()).intValue();
            for(int j=1; j<=this.width; j++) {
                Place place = this.getPlace(j, i);
                place.setHighlight(true);
                invalidPlaces.add(place);
            }
        }
        
        this.tetrisPanel.refreshAfterMouvement(invalidPlaces);
        
        Object waiter = new Object();
        synchronized(waiter) {
            try {
                waiter.wait(200);
            }
            catch(Exception ex) {
            }
        }
    }
    
    public void removeLines(Vector linesNumbers) {
        int lineNumber = 0;
        for(Enumeration e=linesNumbers.elements(); e.hasMoreElements();) {
            lineNumber = ((Integer) e.nextElement()).intValue();
            for(int j=lineNumber-1; j>=1; j--) {
                for(int i=1; i<=this.width; i++) {
                    Place place = this.getPlace(i, j);
                    place.setY(place.getY()+1);
                }
            }
            this.tetrisPanel.removeLine(lineNumber);
            this.tetrisPanel.createFirstLine();
        }
    }
    
        
    public class TetrisTimer extends Timer {
        public void schedule(TetrisTimerTask task) {
            long period = (long) (2000/((double) 1+2.5d*Game.this.level));
            super.scheduleAtFixedRate(task, period, period);
        }
    }
    
    public class TetrisTimerTask extends TimerTask {
        public TetrisTimerTask() {
            Game.this.currentPiece.setTimerTask(this);
        }
        
        public void run() {
            Game.this.moveDown();
        }
    }
}
