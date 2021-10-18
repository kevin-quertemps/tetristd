/*
 * Cube.java
 *
 * Created on 20 septembre 2004, 18:50
 */

package tetris.game;



import java.awt.Color;
/**
 *
 * @author  de Boussineau
 */
public class Cube extends Piece {
    
    /** Creates a new instance of Cube */
    public Cube(Game game) {
        super(game, game.getWidth()/2, 1, 1, 2, Color.BLUE);
        
        this.carres.add(new Carre(0, 1));
        this.carres.add(new Carre(1, 1));
        this.carres.add(new Carre(1, 0));
    }
    
    public void turnLeft() {
    }
    
    public void turnRight() {
    }
}
