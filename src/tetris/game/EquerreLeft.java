/*
 * EquerreLeft.java
 *
 * Created on 20 septembre 2004, 22:38
 */

package tetris.game;




import java.awt.Color;
/**
 *
 * @author  de Boussineau
 */
public class EquerreLeft extends Piece {
    
    /** Creates a new instance of EquerreLeft */
    public EquerreLeft(Game game) {
        super(game, game.getWidth()/2+1, 2, 2, 3, Color.PINK);
        
        this.carres.add(new Carre(-1, -1));
        this.carres.add(new Carre(0, -1));
        this.carres.add(new Carre(0, 1));        
    }
    
}
