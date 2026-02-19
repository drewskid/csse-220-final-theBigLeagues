package model;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.List;

public class Zombie extends Asset {
    private int speed = 2;
    private boolean horizontal; // true = x axis, false = y axis
    private Rectangle collisionArea;

    public Zombie(int h, int w, int x, int y, boolean horizontal) {
        super(h, w, x, y, Color.RED);
        this.horizontal = horizontal;
        setSprite(SpriteStore.load("/assets/zombie.png"));
        collisionArea = new Rectangle(x + 5, y + 5, w - 10, h - 10);
    }

    // Keep collision box aligned to current position
    private void updateCollisionArea() {
        collisionArea.setBounds(getX() + 25/2, getY() + 25/2, getW() - 25, getH() - 25);
    }

    public void update(List<Wall> walls, int worldW, int worldH) {
        int dx = horizontal ? speed : 0;
        int dy = horizontal ? 0 : speed;

        setX(getX() + dx);
        setY(getY() + dy);
        updateCollisionArea();

        // bounce off edges
        if (getX() < 0 || getX() + getW() > worldW || getY() < 0 || getY() + getH() > worldH) {
            setX(getX() - dx);
            setY(getY() - dy);
            speed = -speed;
            updateCollisionArea();
            return;
        }

        // bounce off walls
        for (Wall w : walls) {
            if (getBounds().intersects(w.getBounds())) {
                setX(getX() - dx);
                setY(getY() - dy);
                speed = -speed;
                updateCollisionArea();
                break;
            }
        }
    }

    public Rectangle getCollisionRectangle() {
        return collisionArea;
    }
}
