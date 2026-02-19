package model;

import java.awt.Color;

public class Floor extends Asset {
	boolean isOne;
    public Floor(int h, int w, int x, int y) {
    	
        super(h, w, x, y, Color.GREEN);
        isOne = true;
        setSprite(SpriteStore.load("/assets/floor1.png"));
    }
    
    public void changeFloor() {
    	
    	if(isOne) {
    		setSprite(SpriteStore.load("/assets/floor2.png"));
    		isOne = false;
    	}else {
    		setSprite(SpriteStore.load("/assets/floor1.png"));
    		isOne = true;
    	}
    	
    }
}
