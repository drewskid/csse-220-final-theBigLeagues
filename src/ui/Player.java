package ui;

import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Player extends Sprite{
	private Image playerImage;
	public Player(int xPosition, int yPosition) {
		super(xPosition, yPosition);
		setPlayerImage(new ImageIcon(
	            getClass().getResource("/player_down.png")).getImage());
	}
	
	public Image getPlayerImage() {
		return playerImage;
	}
	public void setPlayerImage(Image image) {
		this.playerImage = image;
	}
}
