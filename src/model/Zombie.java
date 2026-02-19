package model;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.List;

public class Zombie extends Asset {
    private int speed = 2;
    private boolean horizontal; // true = x axis, false = y axis

    // YOUR feature: tighter collision box
    private Rectangle collisionArea;

    // ANDREW feature: knockback state
    private int knockbackX = 0;
    private int knockbackY = 0;
    private int knockbackTimer = 0;

    public Zombie(int h, int w, int x, int y, boolean horizontal) {
        super(h, w, x, y, Color.RED);
        this.horizontal = horizontal;
        setSprite(SpriteStore.load("/assets/zombie.png"));

        // initialize collision box and sync it
        collisionArea = new Rectangle(x + 5, y + 5, w - 10, h - 10);
        updateCollisionArea();
    }

    // Keep collision box aligned to current position
    private void updateCollisionArea() {
        // Keep your idea of "smaller than sprite" collision
        int inset = 12; // tweak if needed
        collisionArea.setBounds(getX() + inset, getY() + inset, getW() - 2 * inset, getH() - 2 * inset);
    }

    public void update(List<Wall> walls, int worldW, int worldH) {

        // ANDREW behavior: apply knockback first, then return
        if (knockbackTimer > 0) {

            int oldX = getX();
            int oldY = getY();

            setX(getX() + knockbackX);
            setY(getY() + knockbackY);
            updateCollisionArea();

            // Prevent going out of bounds
            if (getX() < 0 || getX() + getW() > worldW ||
                getY() < 0 || getY() + getH() > worldH) {

                setX(oldX);
                setY(oldY);
                updateCollisionArea();
                knockbackTimer = 0;
                return;
            }

            // Prevent entering walls
            for (Wall w : walls) {
                if (getBounds().intersects(w.getBounds())) {
                    setX(oldX);
                    setY(oldY);
                    updateCollisionArea();
                    knockbackTimer = 0;
                    return;
                }
            }

            knockbackTimer--;
            return;
        }

        // Normal movement
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

    // YOUR method: GameModel (older) might still call this
    public Rectangle getCollisionRectangle() {
        return collisionArea;
    }

    // ANDREW method: GameModel (newer) uses this for shield knockback logic
    public void knockBack(int dx, int dy) {
        int force = 6;      // speed of knockback
        int duration = 10;  // frames of knockback

        knockbackX = dx * force;
        knockbackY = dy * force;
        knockbackTimer = duration;
    }

    // ANDREW method: needed by newer hit logic to decide knockback axis
    public boolean isHorizontal() {
        return horizontal;
    }
}
