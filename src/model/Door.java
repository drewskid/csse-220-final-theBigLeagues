package model;

import java.awt.Color;

public class Door extends Asset {
	private boolean locked = true;
	public Door(int h, int w, int x, int y) {
		super(h, w, x, y, Color.ORANGE);
		setSprite(SpriteStore.load("/assets/door.png"));
	}
	public boolean isLocked() {
		return locked;
	}
	public void unlock() {
		locked = false;
	}
}
