package model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class ShieldPowerUp extends Collectible {
	
	private int duration = 300; // frames
	
	public ShieldPowerUp(int h, int w, int x, int y) {
		super (h, w, x, y);
		
		
		setSprite(SpriteStore.load("/assets/shield_PowerUp.png"));
	}

	public int getDuration() {
		return duration;
	}
	
	
}
