package model;

import java.awt.Color;
import java.util.List;

public class Zombie extends Asset {
    private int speed = 2;
    private boolean horizontal; // true = x axis, false = y axis

    public Zombie(int h, int w, int x, int y, boolean horizontal) {
        super(h, w, x, y, Color.RED);
        this.horizontal = horizontal;
        setSprite(SpriteStore.load("/assets/zombie.png"));
    }

    public void update(List<Wall> walls, int worldW, int worldH) {
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
}
