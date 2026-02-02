package ui;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public abstract class Sprite {
	private static BufferedImage spriteImage;
	protected int xPosition;
	protected int yPosition;
	protected static int dx = 5;
	protected static int dy = 5;
	private boolean up, down, left, right;
	
	public Sprite(int xPosition, int yPosition) {
		this.xPosition = xPosition;
		this.yPosition = yPosition;
	}
	
	public int getX() { return xPosition; }
    public int getY() { return yPosition; }
	
    // movement logic
    public void update() {
        if (up)    yPosition -= dy;
        if (down)  yPosition += dy;
        if (left)  xPosition -= dx;
        if (right) xPosition += dx;
    }
	
	
	 // Key Input
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W -> up = true;
                case KeyEvent.VK_S -> down = true;
                case KeyEvent.VK_A -> left = true;
                case KeyEvent.VK_D -> right = true;
            }
        }

        public void keyReleased(KeyEvent e) {   // ✅ Must be at class level
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W -> up = false;
                case KeyEvent.VK_S -> down = false;
                case KeyEvent.VK_A -> left = false;
                case KeyEvent.VK_D -> right = false;
            }
        }  	
    }

	 



/* 
 * Sprites will be an abstract class and serve 
 * as a blueprint for when the players and zombies
 *  are created. It will contain shared data such as 
 *  position and a move method. Player will contain logic for
 *   adding gems, moving through tiles, and pushing zombies.
 *    Zombie will have AI logic.
*/