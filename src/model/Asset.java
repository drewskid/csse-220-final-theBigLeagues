package model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Asset {
    private int height;
    private int width;
    private int x;
    private int y;

    // optional sprite
    private BufferedImage sprite;

    // optional fallback color
    private Color color;

    public Asset(int h, int w, int x, int y, Color c) {
        this.height = h;
        this.width = w;
        this.x = x;
        this.y = y;
        this.color = c;
        this.sprite = null;
    }

    public void setSprite(BufferedImage sprite) {
        this.sprite = sprite;
    }
    protected java.awt.image.BufferedImage getSprite() {
        return sprite;
    }


    public void draw(Graphics2D g2) {
        if (sprite != null) {
            // Draw scaled to width/height
            g2.drawImage(sprite, x, y, width, height, null);
        } else {
            // Fallback if sprite fails to load
            g2.setColor(color == null ? Color.MAGENTA : color);
            g2.fillRect(x, y, width, height);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getW() { return width; }
    public int getH() { return height; }

    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
}
