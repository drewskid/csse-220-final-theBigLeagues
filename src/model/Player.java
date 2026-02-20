package model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Player extends Asset {

    public enum Direction { DOWN, UP, LEFT, RIGHT }

    private int lives = 3;
    private int score;
    private int speed = 5;

    private final BufferedImage downSprite;
    private final BufferedImage upSprite;
    private final BufferedImage leftSprite;
    private final BufferedImage rightSprite;
    
    private boolean shieldActive = false;
    private int damageInvincibleTimer = 0;
    

    private Direction dir = Direction.DOWN;

    public Player(int lives, int h, int w, int x, int y) {
        super(h, w - 10, x, y, Color.BLUE);     
        this.lives = lives;

        // load sprites once
        downSprite = SpriteStore.load("/assets/player.png");
        upSprite = SpriteStore.load("/assets/player_up.png");
        leftSprite = SpriteStore.load("/assets/player_left.png");
        rightSprite = SpriteStore.load("/assets/player_right.png");
//        downSprite = SpriteStore.load("/assets/player_new.png");
//        upSprite = SpriteStore.load("/assets/player_up_new.png");
//        leftSprite = SpriteStore.load("/assets/player_left_new.png");
//        rightSprite = SpriteStore.load("/assets/player_right_new.png");
        

        applySprite();
    }
    
    
    

    @Override
	public void draw(Graphics2D g2) {
		// TODO Auto-generated method stub
		super.draw(g2);
		if (shieldActive) {
		    g2.setColor(new Color(0, 150, 255, 100));
		    g2.fillOval(getX() - 5, getY() - 5, getW() + 10, getH() + 10);
		}
	}




	public int getSpeed() { return speed; }
    
    public int getLives() {return lives;}

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
    
    protected void updateLives() {
    	this.lives -= 1;
    }
    
    public boolean hasShield() {
        return shieldActive;
    }

    public void activateShield(int duration) {
        shieldActive = true;
        // optional timer if you want shields to expire automatically
    }

    public void deactivateShield() {
        shieldActive = false;
    }

    public boolean isDamageInvincible() {
        return damageInvincibleTimer > 0;
    }

    public void setDamageInvincible(int duration) {
        damageInvincibleTimer = duration;
    }

    public void updateTimers() {
        if (damageInvincibleTimer > 0) damageInvincibleTimer--;
    }
}
