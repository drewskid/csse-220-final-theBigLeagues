package model;

import java.awt.Color;

public class Collectible extends Asset {
	
	private boolean isCollected;
	
	public Collectible ( int h, int w, int x, int y) {
		super(h,w,x,y, Color.YELLOW);
		
		isCollected = false;
		
		setSprite(SpriteStore.load("/assets/collectible.png"));
		
	}
	
	public void setCollected() {
		isCollected = true;
	}

}
