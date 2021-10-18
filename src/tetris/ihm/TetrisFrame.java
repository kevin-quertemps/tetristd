/*
 * TetrisFrame.java
 *
 * Created on 19 septembre 2004, 15:24
 */

package tetris.ihm;


import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

import client.ClientServlet;

import java.awt.*;
import java.awt.event.*;

import tetris.game.*;
import java.net.URL;
/**
 *
 * @author  de Boussineau
 */
public class TetrisFrame extends JFrame {
    private int gamesWidth, gamesHeigh;
    
    private TetrisPanel tetrisPanel;
    
    private JPanel backgroundNextPiecePanel = new JPanel();
    private NextPiecePanel nextPiecePanel = new NextPiecePanel();
    
    private ScorePanel scorePanel = new ScorePanel();
    
    private JPanel annexPanel = new JPanel();
    
    private JPanel gluePanel = new JPanel();
    private JPanel informationPanel = new JPanel();
    private JLabel startInformation = new JLabel("S : Start");
    private JLabel pauseInformation = new JLabel("P : Pause / Resume");
    private JLabel exitInformation = new JLabel("E : Exit");
    private JLabel emptyLabel = new JLabel();
    private JLabel keysInformation = new JLabel("Play with arrows");
    
    
    private Game game;
    private KeyAdapter startMovementKeyAdapter;
    private KeyAdapter stopMovementKeyAdapter;
    private TimerTask moveRightMovementTask;
    private TimerTask moveLeftMovementTask;
    private TimerTask turnRightMovementTask;
    private TimerTask turnLeftMovementTask;
    

    
    /** Creates a new instance of TetrisFrame */
    public TetrisFrame(int width, int heigh) {       
        this.gamesWidth = width;
        this.gamesHeigh = heigh;
        
		ClientServlet frame = new ClientServlet();
		frame.setVisible(true);
		
        this.addWindowListener(new WindowAdapter() {
        	public void windowClosing(WindowEvent e) {
                try {
                    System.exit(0);
                }
                catch(Exception ex) {
                    TetrisFrame.this.getGame().stop();
                    TetrisFrame.this.dispose();
                }
        	}
        });
        
        
        this.tetrisPanel = new TetrisPanel(this.gamesWidth, this.gamesHeigh);
        
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EtchedBorder(EtchedBorder.LOWERED));

        LineBorder lineBorder = new LineBorder(Color.DARK_GRAY);
        EmptyBorder emptyBorder = new EmptyBorder(2, 2, 2, 2);
        CompoundBorder halfBorder = new CompoundBorder(emptyBorder, lineBorder);
        CompoundBorder border = new CompoundBorder(halfBorder, emptyBorder);
        
        this.setContentPane(contentPane);
        this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.X_AXIS));
        this.getContentPane().add(this.tetrisPanel);
        this.getContentPane().add(this.annexPanel);
        
        Dimension dimension = new Dimension(130, 20*this.gamesHeigh);
        this.annexPanel.setPreferredSize(dimension);
        this.annexPanel.setMaximumSize(dimension);
        this.annexPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        this.annexPanel.setLayout(new BoxLayout(this.annexPanel, BoxLayout.Y_AXIS));
        this.annexPanel.add(this.backgroundNextPiecePanel);
        this.annexPanel.add(this.gluePanel);
        this.annexPanel.add(this.scorePanel);
        
        this.backgroundNextPiecePanel.setBorder(border);
        this.backgroundNextPiecePanel.add(this.nextPiecePanel);
        
        this.gluePanel.setBackground(Color.BLACK);
        this.gluePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        this.gluePanel.setBorder(border);
        this.gluePanel.setLayout(new BoxLayout(this.gluePanel, BoxLayout.X_AXIS));
        this.gluePanel.add(this.informationPanel);
        
        this.informationPanel.setOpaque(false);
        this.informationPanel.setMaximumSize(new Dimension(150, 100));
        this.informationPanel.setLayout(new GridLayout(5, 1));
        this.informationPanel.add(this.startInformation);
        this.informationPanel.add(this.pauseInformation);
        this.informationPanel.add(this.exitInformation);
        this.informationPanel.add(this.emptyLabel);
        this.informationPanel.add(this.keysInformation);
        
        this.startInformation.setForeground(Color.CYAN);
        this.startInformation.setFont(new Font("Comic sans Serif", Font.PLAIN, 12));
        this.pauseInformation.setForeground(Color.CYAN);
        this.pauseInformation.setFont(new Font("Comic sans Serif", Font.PLAIN, 12));
        this.exitInformation.setForeground(Color.CYAN);
        this.exitInformation.setFont(new Font("Comic sans Serif", Font.PLAIN, 12));
        this.keysInformation.setForeground(Color.CYAN);
        this.keysInformation.setFont(new Font("Comic sans Serif", Font.BOLD, 14));
        this.keysInformation.setHorizontalAlignment(SwingConstants.CENTER);
        
        
        this.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent ke) {
                if((ke.getKeyChar() == 's' || ke.getKeyChar() == 'S') && (TetrisFrame.this.getGame().getState() == Game.GAME_INITIALIZED || TetrisFrame.this.getGame().getState() == Game.GAME_OVER)) {
                    new Thread() {
                        public void run() {                            
                            TetrisFrame.this.getGame().stop();
                            TetrisFrame.this.setGame(new Game(TetrisFrame.this.tetrisPanel, TetrisFrame.this.nextPiecePanel, TetrisFrame.this.scorePanel));
                            TetrisFrame.this.getGame().init();
                            TetrisFrame.this.getGame().start();
                        }
                    }.start();
                }
                else if(ke.getKeyChar() == 'p' || ke.getKeyChar() == 'P') {
                    if(TetrisFrame.this.getGame().getState() == Game.GAME_RUNNING) {
                        new Thread() {
                            public void run() {
                            	TetrisFrame.this.getGame().pause();
                            }
                        }.start();
                    }
                    else if(TetrisFrame.this.getGame().getState() == Game.GAME_PAUSED) {
                    	TetrisFrame.this.getGame().resume();
                    }
                        
                }
                else if(ke.getKeyChar() == 'e' || ke.getKeyChar() == 'E') {
                    try {
                        System.exit(0);
                    }
                    catch(Exception e) {
                        TetrisFrame.this.getGame().stop();
                        TetrisFrame.this.dispose();
                    }
                }
            }
        });
            
        this.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
            	TetrisFrame.this.fixPiece(this, ke);
            }
        });

        
        
        
        this.startMovementKeyAdapter = new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
            	TetrisFrame.this.startMovement(this, ke);
             }
        };
        
        this.stopMovementKeyAdapter = new KeyAdapter() {
            public void keyReleased(KeyEvent ke) {
            	TetrisFrame.this.stopMovement(this, ke);
            }
        };
        
        this.addKeyListener(this.startMovementKeyAdapter);
        
        
        this.pack();

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Point center = ge.getCenterPoint();
        this.setLocation(center.x - this.getWidth()/2, center.y - this.getHeight()/2);
        
        this.setResizable(false);
        this.setVisible(true);
        
        this.setGame(new Game(this.tetrisPanel, this.nextPiecePanel, this.scorePanel));
    }
    
    private Game getGame() {
        return this.game;
    }
    
    private void setGame(Game game) {
    	this.game = game;
    }
    
    
    private void initMovementTasks() {
        this.moveLeftMovementTask = new TimerTask() {
            public void run() {
                TetrisFrame.this.getGame().moveLeft();
            }
        };
        
        this.moveRightMovementTask = new TimerTask() {
            public void run() {
                TetrisFrame.this.getGame().moveRight();
            }
        };
        
        this.turnLeftMovementTask = new TimerTask() {
            public void run() {
                TetrisFrame.this.getGame().turnLeft();
            }
        };
        
        this.turnRightMovementTask = new TimerTask() {
            public void run() {
                TetrisFrame.this.getGame().turnRight();
            }
        };
    }
    
    
    private synchronized void startMovement(KeyListener keyListener, KeyEvent ke) {
    	boolean movementStarted = false;
    	
       	this.initMovementTasks();
    	
        if(this.getGame().getState() == Game.GAME_RUNNING && ke.getKeyCode() == KeyEvent.VK_LEFT) {
        	this.getGame().moveLeft();
            this.getGame().getTimer().scheduleAtFixedRate(TetrisFrame.this.moveLeftMovementTask, 150, 50);
            movementStarted = true;
        }
        else if(this.getGame().getState() == Game.GAME_RUNNING && ke.getKeyCode() == KeyEvent.VK_RIGHT) {
        	this.getGame().moveRight();
        	this.getGame().getTimer().scheduleAtFixedRate(TetrisFrame.this.moveRightMovementTask, 150, 50);
            movementStarted = true;
        }
        else if(this.getGame().getState() == Game.GAME_RUNNING && ke.getKeyCode() == KeyEvent.VK_UP) {
            if(! ((ke.getModifiersEx() & InputEvent.SHIFT_DOWN_MASK) == InputEvent.SHIFT_DOWN_MASK)) {
            	this.getGame().turnLeft();
            	this.getGame().getTimer().scheduleAtFixedRate(this.turnLeftMovementTask, 200, 150);
                movementStarted = true;
            }
            else {
            	this.getGame().turnRight();
            	this.getGame().getTimer().scheduleAtFixedRate(this.turnRightMovementTask, 200, 150);
                movementStarted = true;
            }
        }
        
        if(movementStarted) {
        	this.removeKeyListener(keyListener);
        	this.addKeyListener(TetrisFrame.this.stopMovementKeyAdapter);
        }
    }
    
    private synchronized void stopMovement(KeyListener keyListener, KeyEvent ke) {
    	boolean movementStopped = false;
    	
        if(this.getGame().getState() == Game.GAME_RUNNING && ke.getKeyCode() == KeyEvent.VK_LEFT) {
            this.moveLeftMovementTask.cancel();
            this.getGame().getTimer().purge();
            movementStopped = true;
        }
        else if(this.getGame().getState() == Game.GAME_RUNNING && ke.getKeyCode() == KeyEvent.VK_RIGHT) {
        	this.moveRightMovementTask.cancel();
            this.getGame().getTimer().purge();
            movementStopped = true;
       }
        else if(this.getGame().getState() == Game.GAME_RUNNING && ke.getKeyCode() == KeyEvent.VK_UP) {
            if(! ((ke.getModifiersEx() & InputEvent.SHIFT_DOWN_MASK) == InputEvent.SHIFT_DOWN_MASK)) {
            	this.turnLeftMovementTask.cancel();
                this.getGame().getTimer().purge();
                movementStopped = true;
           }
            else {
            	this.turnRightMovementTask.cancel();
                this.getGame().getTimer().purge();
                movementStopped = true;
            }
        }

    	if(movementStopped) {
    		this.removeKeyListener(keyListener);
        	this.addKeyListener(this.startMovementKeyAdapter);            	
    	}
    }
    
    private synchronized void fixPiece(KeyListener keyListener, KeyEvent ke) {
    	if(this.getGame().getState() == Game.GAME_RUNNING && ke.getKeyCode() == KeyEvent.VK_DOWN) {
        	new Thread() {
                public void run() {
                    TetrisFrame.this.getGame().fixAtBottom();
                }
            }.start();
    	}
    }
    
    /**
     * @param args the command line arguments
     * Le main du tetris
     */
    public static void main(String[] args) throws Exception{
    	new TetrisFrame(12,22);
    }
    
}
