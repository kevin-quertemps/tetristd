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
public class RightZigzag extends Piece {
    
    /** Creates a new instance of RightZigzag */
    public RightZigzag(Game game) {
        super(game, game.getWidth()/2, 2, 1, 3, Color.YELLOW);
        
        this.carres.add(new Carre(1,  -1));
        this.carres.add(new Carre(1,  0));
        this.carres.add(new Carre(0,  1));
    }
    
}
