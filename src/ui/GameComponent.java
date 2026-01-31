package ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

import model.GameModel;

public class GameComponent extends JComponent {
//	try {
//		BufferedImage background= ImageIO.read(getClass().getResource("/floor.png"));
//    } catch (IOException | IllegalArgumentException e) {
//        System.out.println("Error loading sprites!");
//        e.printStackTrace();
//    }	
	private GameModel model;
    private Image background;


	public GameComponent(GameModel model) {
	this.model = model;
	background = new ImageIcon(
            getClass().getResource("/floor.png")).getImage();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g.drawImage(background, 0, 0, getWidth(), getHeight(),this);

	


	// TODO: draw based on model state
	}
}
