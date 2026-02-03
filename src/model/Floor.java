package model;

import java.awt.Color;

public class Floor extends Asset {
    public Floor(int h, int w, int x, int y) {
        super(h, w, x, y, Color.GREEN);
        setSprite(SpriteStore.load("/assets/grass.png"));
    }
}
