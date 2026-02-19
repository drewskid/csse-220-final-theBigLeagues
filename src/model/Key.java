package model;

public class Key extends Collectible {
	public Key(int h, int w, int x, int y) {
		super(h, w, x, y);
		setSprite(SpriteStore.load("/assets/key.png"));
	}
}
