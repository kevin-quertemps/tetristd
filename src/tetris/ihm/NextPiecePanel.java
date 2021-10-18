/*
 * NextPiecePanel.java
 *
 * Created on 19 septembre 2004, 15:53
 */

package tetris.ihm;



import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JPanel;

import tetris.game.Piece;
/**
 *
 * @author  de Boussineau
 */
public class NextPiecePanel extends JPanel {
    
    private Piece piece;
    
    private Vector places = new Vector(4);
    
    private Graphics graphics;
    
    
    /** Creates a new instance of NextPiecePanel */
    public NextPiecePanel() {
        this.setBackground(Color.BLACK);
        
        Dimension dimension = new Dimension(60, 100);
        this.setPreferredSize(dimension);
        this.setMaximumSize(dimension);
        this.setMinimumSize(dimension);
        
        for(int i=0; i<4; i++) {
            Vector line = new Vector(2);
            line.add(new NPPlace(1, i+1));
            line.add(new NPPlace(2, i+1));
            this.places.add(line);
        }
        
    }
    
    
    
    public void setPiece(Piece piece) {
        this.piece = piece;
    }
    
    
    
    protected NPPlace getNPPlace(int i, int j) {
        return (NPPlace) ((Vector) this.places.elementAt(j-1)).elementAt(i-1);
    }
    
    

    
    
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(this.piece != null) {
            for(Enumeration e=this.piece.getCarres(); e.hasMoreElements();) {
                Piece.Carre carre = (Piece.Carre) e.nextElement();
                NPPlace place = this.getNPPlace(this.piece.nextPiecePanelX + carre.getX(), this.piece.nextPiecePanelY + carre.getY());
                place.setColor(piece.getColor());
                place.draw(g);
            }

        }
    }
    
    
    
    
    private class NPPlace extends Place {
        public NPPlace(int x, int y) {
            super(x, y);
        }
        
        public void draw(Graphics g) {
            g.setColor(this.getColor());
            g.fillRoundRect(10+20*(this.x-1)+1, 10+20*(this.y-1)+1, 18, 18, 4, 4);

            int r = this.getColor().getRed(); r = r-70 < 0 ? 0 : r-70;
            int gr = this.getColor().getGreen(); gr = gr-70 < 0 ? 0 : gr-70;
            int b = this.getColor().getBlue(); b = b-70 < 0 ? 0 : b-70;
            Color color2 = new Color(r, gr, b);
            g.setColor(color2);
            g.fillRoundRect(10+20*(this.x-1)+3, 10+20*(this.y-1)+3, 14, 14, 4, 4);
        }
    }
    
}
