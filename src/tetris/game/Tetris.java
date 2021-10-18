/*
 * Tetris.java
 *
 * Created on 19 septembre 2004, 17:24
 */

package tetris.game;




import java.awt.*;
/**
 *
 * @author  de Boussineau
 */
public class Tetris extends Piece {
    
    
    /** Creates a new instance of Tetris */
    public Tetris(Game game) {
        super(game, game.getWidth()/2, 2, 1, 2, Color.RED);
        this.carres.add(new Carre(0, -1));
        this.carres.add(new Carre(0, 1));
        this.carres.add(new Carre(0, 2));
    }
    
}
