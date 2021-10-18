/*
 * RightZigzag.java
 *
 * Created on 20 septembre 2004, 18:53
 */

package tetris.game;




import java.awt.Color;
/**
 *
 * @author  de Boussineau
 */
public class LeftZigzag extends Piece {
    
    /** Creates a new instance of RightZigzag */
    public LeftZigzag(Game game) {
        super(game, game.getWidth()/2+1, 2, 2, 3, Color.CYAN);
        
        this.carres.add(new Carre(-1,  -1));
        this.carres.add(new Carre(-1,  0));
        this.carres.add(new Carre(0,  1));
    }
    
}
