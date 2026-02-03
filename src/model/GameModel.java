package model;

import java.awt.Graphics2D;
import java.util.ArrayList;

public class GameModel {

	public static final int WORLD_W = 800;
	public static final int WORLD_H = 600;
	
	public static final int TILE = 48;
	public static final int WALL_THICKNESS = TILE; 
	public static final int PLAYER_SIZE = TILE;
	public static final int ZOMBIE_SIZE = TILE;


	private Floor floor;
	private ArrayList<Wall> walls = new ArrayList<>();
	private Player player;
	private ArrayList<Zombie> zombies = new ArrayList<>();

	public GameModel() {
		this.floor = new Floor(WORLD_H, WORLD_W, 0, 0);
		addWalls();
		this.player = new Player(3, PLAYER_SIZE, PLAYER_SIZE, TILE, TILE);
		addZombies();
	}

	public void draw(Graphics2D g2) {
		floor.draw(g2);

		for (Wall w : walls) w.draw(g2);

		player.draw(g2);

		for (Zombie z : zombies) z.draw(g2);
	}

	public void update() {
		for (Zombie z : zombies) {
			z.update(walls, WORLD_W, WORLD_H);
		}
	}

	public Player getPlayer() {
		return player;
	}

	// Player movement with wall collision + bounds check
	public void movePlayer(int dx, int dy) {
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



	private void addWalls() {
		// Border walls (your originals but corrected sizes)
		walls.add(new Wall(WORLD_H, WALL_THICKNESS, 0, 0));                         // left
		walls.add(new Wall(WORLD_H, WALL_THICKNESS, WORLD_W - WALL_THICKNESS, 0));  // right
		walls.add(new Wall(WALL_THICKNESS, WORLD_W, 0, 0));                         // top
		walls.add(new Wall(WALL_THICKNESS, WORLD_W, 0, WORLD_H - WALL_THICKNESS));  //bottom
		
		int t = TILE;
		
		String[] maze = {
				"####################",
		        "#P..##....##....##..#",
		        "#..##..##....##..##.#",
		        "#......##..##......#",
		        "####..####..####..###",
		        "#......##....##......#",
		        "#..######..##..######.",
		        "#..##......##......##.",
		        "#..##..########..##..#",
		        "#......##....##......#",
		        "###..####..##..####..#",
		        "#..##....##....##..Z.#",
		        "#..##..##..##..##..#.#",
		        "#......##....##......#",
		        "####################"
		    };

		
		for (int row = 0; row < maze.length; row++) {
	        String line = maze[row];

	        for (int col = 0; col < line.length(); col++) {
	            char c = line.charAt(col);

	            if (c == '#') {
	                int x = col * t;
	                int y = row * t;
	                boolean isBorderTile =
	                        row == 0 || row == maze.length - 1 ||
	                        col == 0 || col == line.length() - 1;

	                if (!isBorderTile) {
	                    walls.add(new Wall(t, t, x, y));
	                }
	            }
	        }
	    }


		
	}

	private void addZombies() {
		zombies.add(new Zombie(ZOMBIE_SIZE, ZOMBIE_SIZE, 5 * TILE, 5 * TILE, true)); 
	}
}
