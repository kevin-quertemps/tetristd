/*
 * TetrisPanel.java
 *
 * Created on 19 septembre 2004, 15:27
 */

package tetris.ihm;





import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import tetris.game.Game;

/**
 *
 * @author  de Boussineau
 */
public class TetrisPanel extends javax.swing.JPanel {

    
    private Game game;

    private int gamesWidth, gamesHeight;
    
    private Vector places;
    private Vector placesToRepaint = new Vector();
    private Vector fixedPlaces = new Vector();
    
    private Image image;
    
    private boolean refreshAfterMouvement = false;
    private boolean refreshLines = false;
    
    private Rectangle invalidRectangle;
    
    
    /** Creates a new instance of TetrisPanel */
    public TetrisPanel(int width, int height) {
        this.image = Game.image1;
        
        this.gamesWidth = width;
        this.gamesHeight = height;
        this.places = new Vector(this.gamesHeight);
        for(int i=1; i<=this.gamesHeight; i++) {
            Vector line = new Vector(this.gamesWidth);
            for(int j=1; j<=this.gamesWidth; j++){
                    line.add(new TPPlace(j, i));
            }
            this.places.add(line);
        }

        this.setBackground(Color.BLACK);
        
        Dimension dimension = new Dimension(20*this.gamesWidth, 20*this.gamesHeight);
        this.setPreferredSize(dimension);
        this.setMaximumSize(dimension);
        this.setMinimumSize(dimension);
    }
    
    public TetrisPanel() {
        this(12, 25);
    }
    
    public void setGame(Game game) {
        this.game = game;
    }
    
    public void init() {
        this.fixedPlaces = new Vector();
    }
    
    
    
    
    public int getGamesWidth() {
        return this.gamesWidth;
    }
    
    public int getGamesHeight() {
        return this.gamesHeight;
    }
    
    
    
    
    
    public Place getPlace(int x, int y) {
        return (Place) ((Vector) this.places.elementAt(y-1)).elementAt(x-1);
    }
    
    public Vector placesInRectangle(Rectangle r) {
        int x1 = (int) (r.x/20); int x2 = (int) ((r.x + r.width)/20);
        int y1 = (int) (r.y/20); int y2 = (int) ((r.y + r.height)/20);
        
        Vector result = new Vector();
        for(int i=x1+1; i<=x2; i++) {
            for(int j=y1+1; j<=y2; j++) {
                result.add(this.getPlace(i, j));
            }
        }
        
        return result;
    }
    
    public void fixPlace(Place p) {
        this.fixedPlaces.add(p);
    }
    
    public void unFixPlace(Place p) {
        this.fixedPlaces.remove(p);
    }
    
    
    public void removeLine(int lineNumber) {
        for(Enumeration e=((Vector) this.places.elementAt(lineNumber -1)).elements(); e.hasMoreElements();) {
            this.fixedPlaces.remove(e.nextElement());
        }
        this.places.removeElementAt(lineNumber - 1);
    }
    
    public void createFirstLine() {
        Vector newLine = new Vector(this.gamesWidth);
        for(int j=1; j<=this.gamesWidth; j++) {
            newLine.add(new TPPlace(j, 1));
        }
        this.places.insertElementAt(newLine, 0);
    }        
    
    
    
    
    public void refreshAfterMouvement(final Vector invalidPlaces) {
        try {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    TetrisPanel.this.refreshAfterMouvement = true;
                    TetrisPanel.this.placesToRepaint = invalidPlaces;
           
                    Enumeration e = TetrisPanel.this.placesToRepaint.elements();
                    Rectangle invalidRectangle = ((TPPlace) e.nextElement()).getRectangle();
                    while(e.hasMoreElements()) {
                        invalidRectangle = invalidRectangle.union(((TPPlace) e.nextElement()).getRectangle());
                    }
                    
                    TetrisPanel.this.invalidRectangle = invalidRectangle;
                    TetrisPanel.this.paintImmediately(invalidRectangle);
                    TetrisPanel.this.refreshAfterMouvement = false;
                }
            });
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public void refreshAfterLines() {
        try {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    TetrisPanel.this.refreshLines = true;
                    TetrisPanel.this.paintImmediately(0, 0, TetrisPanel.this.getWidth(), TetrisPanel.this.getHeight());
                    TetrisPanel.this.refreshLines = false;
                }
            });
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(this.refreshAfterMouvement) {

            g.setClip(this.invalidRectangle);
            g.drawImage(this.image, 0, 0, this.getWidth(), this.getHeight(), this);

            Vector invalidPlaces = this.placesInRectangle(this.invalidRectangle);
            for(Enumeration en=invalidPlaces.elements(); en.hasMoreElements();) {
                ((Place) en.nextElement()).draw(g);
            }
        }

        else if(this.refreshLines) {
            g.drawImage(this.image, 0, 0, this.getWidth(), this.getHeight(), this);

            for(Enumeration e=this.fixedPlaces.elements(); e.hasMoreElements();) {
                ((Place) e.nextElement()).draw(g);
            }
        }

        else {
            g.drawImage(this.image, 0, 0, this.getWidth(), this.getHeight(), this);

            for(Enumeration e1=this.places.elements(); e1.hasMoreElements();) {
                for(Enumeration e2 = ((Vector) e1.nextElement()).elements(); e2.hasMoreElements();) {
                    ((Place) e2.nextElement()).draw(g);
                }
            }
        }
        
        if(this.game != null && this.game.getState() == Game.GAME_OVER) {
            g.setClip(0,  0, this.getWidth(), this.getHeight());
            g.setColor(Color.WHITE);
            g.setFont(new Font("Comic Sans Serif", Font.BOLD, 30));
            g.drawString("GAME OVER", 10, 210);
        }
    }
    
    
    
    public void refreshImage() {
        int n = this.game.getLevel();
        switch(n) {
            case 1: {
                this.image = Game.image1;
                break;
            }
            case 2: {
                this.image = Game.image2;
                break;
            }
            case 3: {
                this.image = Game.image3;
                break;
            }
            case 4: {
                this.image = Game.image4;
                break;
            }
            case 5: {
                this.image = Game.image5;
                break;
            }
        }
    }
    
    
    
    private class TPPlace extends Place {
        
        public TPPlace(int x, int y) {
            super(x, y);
        }
        
        
        public void setFixed(boolean b) {
            super.setFixed(b);
            if(b) TetrisPanel.this.fixPlace(this);
        }
        
        
        public Rectangle getRectangle() {
            return new Rectangle(20*(this.x-1), 20* (this.y-1), 20, 20);
        }
    
    
    
        public void draw(Graphics g) {
            if(this.highlight) {
                g.setColor(Color.WHITE);
                g.fillRoundRect(20*(this.x-1)+1, 20*(this.y-1)+1, 18, 18, 4, 4);
            }
            else {
                if(this.full) {
                    g.setColor(Color.BLACK);
                    g.fillRect(20*(this.x-1), 20*(this.y-1), 20, 20);
                    
                    g.setColor(this.color);
                    g.fillRoundRect(20*(this.x-1)+1, 20*(this.y-1)+1, 18, 18, 4, 4);

                    int r = this.color.getRed(); r = r-70 < 0 ? 0 : r-70;
                    int gr = this.color.getGreen(); gr = gr-70 < 0 ? 0 : gr-70;
                    int b = this.color.getBlue(); b = b-70 < 0 ? 0 : b-70;
                    Color color2 = new Color(r, gr, b);
                    g.setColor(color2);
                    g.fillRoundRect(20*(this.x-1)+3, 20*(this.y-1)+3, 14, 14, 4, 4);
                }
            }
        }
    }

}
