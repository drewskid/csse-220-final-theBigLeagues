package model;

import java.awt.Color;
import java.util.List;

public class Zombie extends Asset {
    private int speed = 2;
    private boolean horizontal; // true = x axis, false = y axis
    private int knockbackX = 0;
    private int knockbackY = 0;
    private int knockbackTimer = 0;
    

    public Zombie(int h, int w, int x, int y, boolean horizontal) {
        super(h, w, x, y, Color.RED);
        this.horizontal = horizontal;
        setSprite(SpriteStore.load("/assets/zombie.png"));
    }

    public void update(List<Wall> walls, int worldW, int worldH) {
    	if (knockbackTimer > 0) {

    	    int newX = getX() + knockbackX;
    	    int newY = getY() + knockbackY;

    	    int oldX = getX();
    	    int oldY = getY();

    	    setX(newX);
    	    setY(newY);

    	    // Prevent going out of bounds
    	    if (getX() < 0 || getX() + getW() > worldW ||
    	        getY() < 0 || getY() + getH() > worldH) {

    	        setX(oldX);
    	        setY(oldY);
    	        knockbackTimer = 0;
    	        return;
    	    }

    	    // Prevent entering walls
    	    for (Wall w : walls) {
    	        if (getBounds().intersects(w.getBounds())) {
    	            setX(oldX);
    	            setY(oldY);
    	            knockbackTimer = 0;
    	            return;
    	        }
    	    }

    	    knockbackTimer--;
    	    return;
    	}

        int dx = horizontal ? speed : 0;
        int dy = horizontal ? 0 : speed;

        setX(getX() + dx);
        setY(getY() + dy);

        // bounce off edges
        if (getX() < 0 || getX() + getW() > worldW || getY() < 0 || getY() + getH() > worldH) {
            setX(getX() - dx);
            setY(getY() - dy);
            speed = -speed;
            return;
        }

        // bounce off walls
        for (Wall w : walls) {
            if (getBounds().intersects(w.getBounds())) {
                setX(getX() - dx);
                setY(getY() - dy);
                speed = -speed;
                break;
            }
        }
    }
    
    public void knockBack(int dx, int dy) {

        int force = 6;     // speed of knockback
        int duration = 10; // frames of knockback

        knockbackX = dx * force;
        knockbackY = dy * force;
        knockbackTimer = duration;
        
        
    }
    
    public boolean isHorizontal() {
        return horizontal;
    }
}
