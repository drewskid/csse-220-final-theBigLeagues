package model;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public final class SpriteStore {
    private static final Map<String, BufferedImage> cache = new HashMap<>();

    private SpriteStore() {}

    public static BufferedImage load(String resourcePath) {
        // resourcePath example: "/assets/player.png"
        if (cache.containsKey(resourcePath)) return cache.get(resourcePath);

        try (InputStream in = SpriteStore.class.getResourceAsStream(resourcePath)) {
            if (in == null) {
                System.err.println("Sprite not found on classpath: " + resourcePath);
                cache.put(resourcePath, null);
                return null;
            }
            BufferedImage img = ImageIO.read(in);
            cache.put(resourcePath, img);
            return img;
        } catch (IOException e) {
            System.err.println("Failed to load sprite: " + resourcePath);
            e.printStackTrace();
            cache.put(resourcePath, null);
            return null;
        }
    }
}
