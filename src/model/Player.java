package model;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Player extends Asset {

    public enum Direction { DOWN, UP, LEFT, RIGHT }

    private int lives;
    private int score;
    private int speed = 5;

    private final BufferedImage downSprite;
    private final BufferedImage upSprite;
    private final BufferedImage leftSprite;
    private final BufferedImage rightSprite;
    

    private Direction dir = Direction.DOWN;

    public Player(int lives, int h, int w, int x, int y) {
        super(h, w, x, y, Color.BLUE);
        this.lives = lives;

        // load sprites once
        downSprite = SpriteStore.load("/assets/player.png");
        upSprite = SpriteStore.load("/assets/player_up.png");
        leftSprite = SpriteStore.load("/assets/player_left.png");
        rightSprite = SpriteStore.load("/assets/player_right.png");

        applySprite();
    }
    

    public int getSpeed() { return speed; }

    public void setDirection(Direction d) {
        if (d != null && d != dir) {
            dir = d;
            applySprite();
        }
    }

    private void applySprite() {
        switch (dir) {
            case UP -> setSprite(upSprite);
            case LEFT -> setSprite(leftSprite);
            case RIGHT -> setSprite(rightSprite);
            default -> setSprite(downSprite);
        }
    }
}
