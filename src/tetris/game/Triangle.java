/*
 * Trinagle.java
 *
 * Created on 20 septembre 2004, 18:57
 */

package tetris.game;



import java.awt.Color;
/**
 *
 * @author  de Boussineau
 */
public class Triangle extends Piece {
    
    /** Creates a new instance of Trinagle */
    public Triangle(Game game) {
        super(game, game.getWidth()/2, 2, 1, 3, Color.GREEN);
        
        this.carres.add(new Carre(0, -1));
        this.carres.add(new Carre(1, 0));
        this.carres.add(new Carre(0, 1));
    }
    
}
