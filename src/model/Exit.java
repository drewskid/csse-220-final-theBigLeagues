package model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Exit extends Asset {

    public Exit(int h, int w, int x, int y) {
        super(h, w, x, y, Color.GRAY);
        setSprite(SpriteStore.load("/assets/exit.png"));
    }

    @Override
    public void draw(Graphics2D g2) {
        BufferedImage img = getSprite();
        if (img == null) {
            super.draw(g2);
            return;
        }

        int tileW = img.getWidth();
        int tileH = img.getHeight();

        // repeat the texture (no stretching)
        for (int yy = getY(); yy < getY() + getH(); yy += tileH) {
            for (int xx = getX(); xx < getX() + getW(); xx += tileW) {
                int drawW = Math.min(tileW, getX() + getW() - xx);
                int drawH = Math.min(tileH, getY() + getH() - yy);

//                g2.drawImage(img,xx, yy, xx + drawW, yy + drawH,0, 0, drawW, drawH, null);
                g2.drawImage(img, xx, yy, 100, 100, null);
            }
        }
    }
}
