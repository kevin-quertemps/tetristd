/*
 * ScorePanel.java
 *
 * Created on 19 septembre 2004, 16:05
 */

package tetris.ihm;



import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
/**
 *
 * @author  de Boussineau
 */
public class ScorePanel extends JPanel {
    
    private JPanel scorePanel = new JPanel();
    private JLabel scoreTitleLabel = new JLabel("Score :");
    private JLabel scoreLabel = new JLabel("0");
    
    private JPanel levelPanel = new JPanel();
    private JLabel levelTitleLabel = new JLabel("Level :");
    private JLabel levelLabel = new JLabel("1");
    
    private JPanel linesPanel = new JPanel();
    private JLabel linesTitleLabel = new JLabel("Lines :");
    private JLabel linesLabel = new JLabel("0");
    
    private JPanel tetrisPanel = new JPanel();
    private JLabel tetrisTitleLabel = new JLabel("Tetris :");
    private JLabel tetrisLabel = new JLabel("0");

    
    
    /** Creates a new instance of ScorePanel */
    public ScorePanel() {
        Dimension dimension = new Dimension(150, 160);
        this.setPreferredSize(dimension);
        this.setMinimumSize(dimension);
        this.setMaximumSize(dimension);

        LineBorder lineBorder = new LineBorder(Color.DARK_GRAY);
        EmptyBorder emptyBorder = new EmptyBorder(2, 2, 2, 2);
        CompoundBorder halfBorder = new CompoundBorder(emptyBorder, lineBorder);
        CompoundBorder border = new CompoundBorder(halfBorder, emptyBorder);
        this.setBorder(border);

        this.setLayout(new GridLayout(4, 1, 5, 5));
        
        this.add(this.levelPanel);
        this.add(this.linesPanel);
        this.add(this.tetrisPanel);
        this.add(this.scorePanel);
        
        this.levelPanel.setBackground(Color.BLACK);
        this.levelPanel.setLayout(new FlowLayout());
        this.levelPanel.add(this.levelTitleLabel);
        this.levelPanel.add(this.levelLabel);
        
        this.levelTitleLabel.setFont(new Font("Comic Sans Serif", Font.PLAIN, 15));
        this.levelTitleLabel.setBackground(Color.BLACK);
        this.levelTitleLabel.setForeground(Color.WHITE);
        
        this.levelLabel.setFont(new Font("Comic Sans Serif", Font.PLAIN, 15));
        this.levelLabel.setBackground(Color.BLACK);
        this.levelLabel.setForeground(Color.WHITE);
        this.levelLabel.setHorizontalAlignment(SwingConstants.CENTER);

        this.scorePanel.setBackground(Color.BLACK);
        this.scorePanel.setLayout(new FlowLayout());
        this.scorePanel.add(this.scoreTitleLabel);
        this.scorePanel.add(this.scoreLabel);
        
        this.scoreTitleLabel.setFont(new Font("Comic Sans Serif", Font.BOLD, 15));
        this.scoreTitleLabel.setBackground(Color.BLACK);
        this.scoreTitleLabel.setForeground(Color.RED);
        
        this.scoreLabel.setFont(new Font("Comic Sans Serif", Font.BOLD, 15));
        this.scoreLabel.setBackground(Color.BLACK);
        this.scoreLabel.setForeground(Color.RED);
        this.scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);

        this.linesPanel.setBackground(Color.BLACK);
        this.linesPanel.setLayout(new FlowLayout());
        this.linesPanel.add(this.linesTitleLabel);
        this.linesPanel.add(this.linesLabel);
        
        this.linesTitleLabel.setFont(new Font("Comic Sans Serif", Font.PLAIN, 15));
        this.linesTitleLabel.setBackground(Color.BLACK);
        this.linesTitleLabel.setForeground(Color.YELLOW);
        
        this.linesLabel.setFont(new Font("Comic Sans Serif", Font.PLAIN, 15));
        this.linesLabel.setBackground(Color.BLACK);
        this.linesLabel.setForeground(Color.YELLOW);
        this.linesLabel.setHorizontalAlignment(SwingConstants.CENTER);

        this.tetrisPanel.setBackground(Color.BLACK);
        this.tetrisPanel.setLayout(new FlowLayout());
        this.tetrisPanel.add(this.tetrisTitleLabel);
        this.tetrisPanel.add(this.tetrisLabel);
        
        this.tetrisTitleLabel.setFont(new Font("Comic Sans Serif", Font.BOLD, 15));
        this.tetrisTitleLabel.setBackground(Color.BLACK);
        this.tetrisTitleLabel.setForeground(Color.ORANGE);
        
        this.tetrisLabel.setFont(new Font("Comic Sans Serif", Font.BOLD, 15));
        this.tetrisLabel.setBackground(Color.BLACK);
        this.tetrisLabel.setForeground(Color.ORANGE);
        this.tetrisLabel.setHorizontalAlignment(SwingConstants.CENTER);
    }
    
    
    public void setScore(int score) {
        this.scoreLabel.setText(String.valueOf(score));
    }
    
    public void setLevel(int level) {
        this.levelLabel.setText(String.valueOf(level));
    }
    
    public void setLines(int lines) {
        this.linesLabel.setText(String.valueOf(lines));
    }
    
    public void setTetris(int tetris) {
        this.tetrisLabel.setText(String.valueOf(tetris));
    }
}
