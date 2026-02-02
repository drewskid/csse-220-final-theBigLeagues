package ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.Timer;

import model.GameModel;

public class GameComponent extends JComponent implements KeyListener{
//	try {
//		BufferedImage background= ImageIO.read(getClass().getResource("/floor.png"));
//    } catch (IOException | IllegalArgumentException e) {
//        System.out.println("Error loading sprites!");
//        e.printStackTrace();
//    }	
	private GameModel model;
    private Image background;
    private Player player;
    private Zombie zombie;


	public GameComponent(GameModel model) {
		this.model = model;
		background = new ImageIcon(
            getClass().getResource("/floor.png")).getImage();
		player = new Player(200,200);
		zombie = new Zombie(300,100);
		setFocusable(true);
		addKeyListener(this);
		requestFocusInWindow();
		 // ~60 FPS game loop
        Timer timer = new Timer(16, e -> {
            player.update();
            zombie.updateEnemy();
            repaint();
        });
        timer.start();
    }

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g.drawImage(background, 0, 0, getWidth(), getHeight(),this);
		
		g.drawImage(player.getPlayerImage(), player.getX(), player.getY(), 100, 100,this);
	
		g.drawImage(zombie.getZombieImage(), zombie.getX(), zombie.getY(), 100, 100,this);

	// TODO: draw based on model state
	}


	@Override
	public void keyPressed(KeyEvent e) {
		player.keyPressed(e);
		repaint();
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		player.keyReleased(e);
	}
	@Override 
	public void keyTyped(KeyEvent e) {}
}
