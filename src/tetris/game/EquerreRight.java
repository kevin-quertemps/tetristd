/*
 * EquerreRight.java
 *
 * Created on 20 septembre 2004, 22:35
 */

package tetris.game;




import java.awt.Color;
/**
 *
 * @author  de Boussineau
 */
public class EquerreRight extends Piece {
    
    /** Creates a new instance of EquerreRight */
    public EquerreRight(Game game) {
        super(game, game.getWidth()/2, 2, 1, 3, Color.MAGENTA);
        
        this.carres.add(new Carre(1,  -1));
        this.carres.add(new Carre(0,  -1));
        this.carres.add(new Carre(0,  1));
    }
    
}
