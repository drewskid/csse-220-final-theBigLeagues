package model;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.imageio.plugins.tiff.ExifGPSTagSet;

public class GameModel {

    public static final int WORLD_W = 800;
    public static final int WORLD_H = 600;

    // Each character in the text file = one TILE x TILE cell.
    public static final int TILE = 100;

    public static final int WALL_THICKNESS = TILE;
    public static final int PLAYER_SIZE = TILE / 2;
    public static final int ZOMBIE_SIZE = TILE / 2;

    private Floor floor;
    private ArrayList<Wall> walls = new ArrayList<>();
    private Exit exit;
    private Player player;
    private ArrayList<Zombie> zombies = new ArrayList<>();
    private ArrayList<Collectible> collectibles = new ArrayList<>();
    private int score = 0;

    public GameModel() {
        this.floor = new Floor(WORLD_H, WORLD_W, 0, 0);
        loadLevel("level1.txt");
    }

    public void draw(Graphics2D g2) {
        floor.draw(g2);
        exit.draw(g2);

        for (Wall w : walls) w.draw(g2);

        // If your Collectible has draw(), this will render them.
        for (Collectible c : collectibles) c.draw(g2);

        if (player != null) player.draw(g2);
        
        for (Zombie z : zombies) z.draw(g2);
    }

    public void update() {
        for (Zombie z : zombies) {
            z.update(walls, WORLD_W, WORLD_H);
        }
        if (player.getBounds().intersects(exit.getBounds())) {
        	loadLevel("level2.txt");
        }
        
        

        // Optional: basic collectible pickup (only if Collectible extends Asset and you want it)
        // collectibles.removeIf(c -> player != null && player.getBounds().intersects(c.getBounds()));
    }

    public Player getPlayer() {
        return player;
    }
    
    public int getScore() {
    	return score;
    }

    // Player movement with wall collision + bounds check
    public void movePlayer(int dx, int dy) {
        if (player == null) return;

        int steps = Math.max(Math.abs(dx), Math.abs(dy));
        if (steps == 0) return;

        int stepX = Integer.signum(dx);
        int stepY = Integer.signum(dy);

        for (int i = 0; i < steps; i++) {
            if (stepX != 0) {
                int oldX = player.getX();
                player.setX(oldX + stepX);

                if (outOfBounds(player) || hitsWall(player)) {
                    player.setX(oldX);
                }
            }

            if (stepY != 0) {
                int oldY = player.getY();
                player.setY(oldY + stepY);

                if (outOfBounds(player) || hitsWall(player)) {
                    player.setY(oldY);
                }
            }
        }

        if (dy < 0) player.setDirection(Player.Direction.UP);
        else if (dy > 0) player.setDirection(Player.Direction.DOWN);
        else if (dx < 0) player.setDirection(Player.Direction.LEFT);
        else if (dx > 0) player.setDirection(Player.Direction.RIGHT);
    }
    
    public void collectItem() {

        collectibles.removeIf(c -> {
        	if(player.getBounds().intersects(c.getBounds())) {
        		score += 50; // each coin is worth 50, can be changed
        		return true;
        	}
        	return false;
        });
    }
    
    public void hitZombie() {
    	for (Zombie z : zombies) {
    		if (player.getBounds().intersects(z.getBounds())) {
    			player.updateLives();
    			movePlayer(player.getX(), player.getY());
    		}
    	}
    }

    private boolean hitsWall(Asset a) {
        for (Wall w : walls) {
            if (a.getBounds().intersects(w.getBounds())) return true;
        }
        return false;
    }

    private boolean outOfBounds(Asset a) {
        return a.getX() < 0 || a.getY() < 0 ||
               a.getX() + a.getW() > WORLD_W ||
               a.getY() + a.getH() > WORLD_H;
    }

    /**
     * Loads a level from a text file where each character is a tile.
     *
     * Legend (customize as you like):
     *  '*' or '#' = wall
     *  'P'        = player start
     *  'Z'        = zombie start
     *  'C' or 'G' = collectible
     *  '.' ' ' 'E'= open floor (E treated as open tile for now)
     *  'X'        = exit tile
     */
    private void loadLevel(String filename) {
        walls.clear();
        zombies.clear();
        collectibles.clear();
        player = null;

        ArrayList<String> lines = readAllLines(filename);
        if (lines.isEmpty()) {
           
            player = new Player(3, PLAYER_SIZE, PLAYER_SIZE, TILE, TILE);
            zombies.add(new Zombie(ZOMBIE_SIZE, ZOMBIE_SIZE, 5 * TILE, 5 * TILE, true));
            return;
        }

        
        int rows = lines.size();
        int cols = 0;
        for (String s : lines) cols = Math.max(cols, s.length());

        int mapW = cols * TILE;
        int mapH = rows * TILE;
        int offsetX = Math.max(0, (WORLD_W - mapW) / 2);
        int offsetY = Math.max(0, (WORLD_H - mapH) / 2);

        for (int row = 0; row < rows; row++) {
            String line = lines.get(row);

            for (int col = 0; col < line.length(); col++) {
                char ch = line.charAt(col);
                int x = offsetX + col * TILE;
                int y = offsetY + row * TILE;

                switch (ch) {
                    case '*':
                    case '#':
                        walls.add(new Wall(TILE, TILE, x, y));
                        break;

                    case 'P':
                        // Put the player roughly centered in the tile
                        player = new Player(
                                3,
                                PLAYER_SIZE, PLAYER_SIZE,
                                x + (TILE - PLAYER_SIZE) / 2,
                                y + (TILE - PLAYER_SIZE) / 2
                        );
                        break;

                    case 'Z':
                        zombies.add(new Zombie(
                                ZOMBIE_SIZE, ZOMBIE_SIZE,
                                x + (TILE - ZOMBIE_SIZE) / 2,
                                y + (TILE - ZOMBIE_SIZE) / 2,
                                true
                        ));
                        break;

                    case 'C':
                    case 'G':
                        
                        int cw = TILE / 3;
                        int chh = TILE / 3;
                        collectibles.add(new Collectible(
                                cw, chh,
                                x + (TILE - cw) / 2,
                                y + (TILE - chh) / 2
                        ));
                        break;
                    
                   case 'X':
                	   exit = new Exit(TILE, TILE, x, y);
                       break;

                    // '.', ' ', 'E' etc. are just open floor tiles; do nothing
                    default:
                        break;
                }
            }
        }

        // Safety: if no player was found in the file
        if (player == null) {
            player = new Player(3, PLAYER_SIZE, PLAYER_SIZE, TILE, TILE);
        }
    }

    
    private ArrayList<String> readAllLines(String filename) {
        ArrayList<String> lines = new ArrayList<>();

        
        try (InputStream in = GameModel.class.getClassLoader().getResourceAsStream(filename)) {
            if (in != null) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
                    String line;
                    while ((line = br.readLine()) != null) lines.add(line);
                }
                return lines;
            }
        } catch (Exception ignored) { }

        // Fallback to file system (project root / working directory)
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new java.io.FileInputStream(new File(filename))))) {
            String line;
            while ((line = br.readLine()) != null) lines.add(line);
        } catch (Exception e) {
            System.out.println(filename + " not found or could not be read: " + e.getMessage());
        }

        return lines;
    }
    public void resetGame() {
    	score = 0;
    	loadLevel("level1.txt");
    }
}
