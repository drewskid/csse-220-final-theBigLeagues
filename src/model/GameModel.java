package model;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

public class GameModel {

	public static final int WORLD_W = 800;
	public static final int WORLD_H = 600;

	public static final int TILE = 50;

	public static final int WALL_THICKNESS = TILE;
	public static final int PLAYER_SIZE = TILE * 3 / 4;
	public static final int ZOMBIE_SIZE = TILE * 3 / 4;

	private ArrayList<Floor> floor = new ArrayList<>();
	private ArrayList<Wall> walls = new ArrayList<>();
	private ArrayList<Door> doors = new ArrayList<>();
	private Exit exit;
	private Player player;
	private ArrayList<Zombie> zombies = new ArrayList<>();
	private ArrayList<Collectible> collectibles = new ArrayList<>();
	private boolean hasKey = false;

	// YOUR STATE (kept)
	private int score = 0;
	private int oldLives = 3;

	private int playerStartX;
	private int playerStartY;

	private boolean levelComplete = false;

	private String level;

	public GameModel(String level) {
		this.level = level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public boolean isLevelComplete() {
		return levelComplete;
	}

	public boolean isPlayerDead() {
		return player != null && player.getLives() <= 0;
	}

	public Player getPlayer() {
		return player;
	}

	public int getScore() {
		return score;
	}

	public void draw(Graphics2D g2) {
		for (Floor f : floor) f.draw(g2);
		for (Wall w : walls) w.draw(g2);
		for(Door d : doors) d.draw(g2);
		if (exit != null) exit.draw(g2);
		for (Collectible c : collectibles) c.draw(g2);
		if (player != null) player.draw(g2);
		for (Zombie z : zombies) z.draw(g2);
	}

	/**
	 * YOUR BEHAVIOR:
	 * - Zombies move
	 * - Player timers (Andrew feature) update each frame
	 * - LevelComplete only becomes true when player reaches exit (you handle changing levels elsewhere)
	 * - Zombie contact processed here
	 */
	public void update() {
		for (Zombie z : zombies) {
			z.update(walls, doors, WORLD_W, WORLD_H);
		}

		// Andrew feature: timer updates (invincibility/shield duration, etc)
		if (player != null) {
			player.updateTimers();
		}

		// YOUR level completion logic
		if (player != null && exit != null && player.getBounds().intersects(exit.getBounds())) {
			oldLives = player.getLives();
			levelComplete = true;
		}

		hitZombie();
	}

	/**
	 * YOUR MOVEMENT: step-by-step collision so you never "tunnel" through walls.
	 */
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
				if (outOfBounds(player) || hitsObstacle(player)) player.setX(oldX);
			}

			if (stepY != 0) {
				int oldY = player.getY();
				player.setY(oldY + stepY);
				if (outOfBounds(player) || hitsObstacle(player)) player.setY(oldY);
			}
		}

		if (dy < 0) player.setDirection(Player.Direction.UP);
		else if (dy > 0) player.setDirection(Player.Direction.DOWN);
		else if (dx < 0) player.setDirection(Player.Direction.LEFT);
		else if (dx > 0) player.setDirection(Player.Direction.RIGHT);
	}

	/**
	 * YOUR INPUT BEHAVIOR:
	 * Collect happens when the user presses space (or whatever triggers this).
	 *
	 * UPDATED to support Andrew's ShieldPowerUp.
	 */
	private boolean hitsObstacle(Asset a) {
		for(Wall w : walls) {
			if(a.getBounds().intersects(w.getBounds()))
				return true;
		}
		Iterator<Door> it =doors.iterator();
		while(it.hasNext()) {
			Door d = it.next();
			if(a.getBounds().intersects(d.getBounds())) {
				if(hasKey) {
					it.remove();
					hasKey = false;
					return false;
				}
				return true;
			}
		}
		return false;
	}
	public void collectItem() {
		if (player == null) return;

		collectibles.removeIf(c -> {
			if (!player.getBounds().intersects(c.getBounds())) return false;
			// Code that implements the key
			if(c instanceof Key) {
				hasKey = true;
				return true;
			}
			// If Andrew's code introduced ShieldPowerUp, support it.
			if (c instanceof ShieldPowerUp) {
				ShieldPowerUp sp = (ShieldPowerUp) c;
				player.activateShield(sp.getDuration());
			} else {
				score += 50;
			}
			return true;
		});
	}

	/**
	 * UPDATED Zombie hit logic:
	 * - If shield is active, absorb hit, knock zombie back, and give short i-frames
	 * - Else take damage if not invincible, respawn player (YOUR behavior), give i-frames
	 *
	 * If your Zombie uses getCollisionRectangle() instead of getBounds(), we can keep that too.
	 * Here we use getBounds() (Andrew style). If you need collision rectangle, tell me and I’ll swap it.
	 */
	public void hitZombie() {
		if (player == null) return;

		for (Zombie z : zombies) {
			if (player.getBounds().intersects(z.getBounds())) {

				// Case 1: shield absorbs
				if (player.hasShield()) {
					player.deactivateShield();

					int dx = Integer.signum(z.getX() - player.getX());
					int dy = Integer.signum(z.getY() - player.getY());
					if (z.isHorizontal()) dy = 0;
					else dx = 0;

					z.knockBack(dx, dy);
					player.setDamageInvincible(20);
					return;
				}

				// Case 2: normal damage
				if (!player.isDamageInvincible()) {
					player.updateLives();
					player.setDamageInvincible(20);

					// YOUR behavior: respawn player after taking damage
					respawnPlayer();

					int dx = Integer.signum(z.getX() - player.getX());
					int dy = Integer.signum(z.getY() - player.getY());
					if (z.isHorizontal()) dy = 0;
					else dx = 0;

					z.knockBack(dx, dy);
					return;
				}

				return; // only process one zombie per frame
			}
		}
	}

	public void respawnPlayer() {
		if (player == null) return;
		player.setX(playerStartX);
		player.setY(playerStartY);
	}

//	Replaced by Obstacle
//	private boolean hitsWall(Asset a) {
//		for (Wall w : walls) {
//			if (a.getBounds().intersects(w.getBounds())) return true;
//		}
//		return false;
//	}

	private boolean outOfBounds(Asset a) {
		return a.getX() < 0 || a.getY() < 0
				|| a.getX() + a.getW() > WORLD_W
				|| a.getY() + a.getH() > WORLD_H;
	}

	/**
	 * YOUR LOADER ENTRYPOINT (kept)
	 */
	public void loadLevel() {
		levelComplete = false;
		hasKey = false;

		String filename = this.level;

		floor.clear();
		walls.clear();
		doors.clear();
		zombies.clear();
		collectibles.clear();
		player = null;
		exit = null;

		ArrayList<String> lines = readAllLines(filename);
		if (lines.isEmpty()) {
			playerStartX = TILE;
			playerStartY = TILE;
			player = new Player(oldLives, PLAYER_SIZE, PLAYER_SIZE, TILE, TILE);
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

				floor.add(new Floor(TILE, TILE, x, y));

				switch (ch) {
				case '*':
				case '#':
					walls.add(new Wall(TILE, TILE, x, y));
					break;
				
				case 'D':
					doors.add(new Door(TILE, TILE, x, y));
					break;
				
				case 'K':
					int kw = TILE * 2 / 3;
					int kh = TILE * 2 / 3;
					collectibles.add(new Key(kw, kh, x + (TILE - kw) / 2, y + (TILE - kh) / 2));
					break;

				case 'P':
					playerStartX = x + (TILE - PLAYER_SIZE) / 2;
					playerStartY = y + (TILE - PLAYER_SIZE) / 2;
					player = new Player(oldLives, PLAYER_SIZE, PLAYER_SIZE, playerStartX, playerStartY);
					break;

				case 'Z':
					zombies.add(new Zombie(ZOMBIE_SIZE, ZOMBIE_SIZE,
							x + (TILE - ZOMBIE_SIZE) / 2,
							y + (TILE - ZOMBIE_SIZE) / 2,
							true));
					break;

				case 'z':
					zombies.add(new Zombie(ZOMBIE_SIZE, ZOMBIE_SIZE,
							x + (TILE - ZOMBIE_SIZE) / 2,
							y + (TILE - ZOMBIE_SIZE) / 2,
							false));
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

				case 'S': // Andrew feature: ShieldPowerUp tile
					int sw = TILE / 3;
					int sh = TILE / 3;
					collectibles.add(new ShieldPowerUp(
							sw, sh,
							x + (TILE - sw) / 2,
							y + (TILE - sh) / 2
					));
					break;

				case 'X':
					exit = new Exit(TILE, TILE, x, y);
					break;

				default:
					break;
				}
			}
		}

		if (player == null) {
			playerStartX = TILE;
			playerStartY = TILE;
			player = new Player(oldLives, PLAYER_SIZE, PLAYER_SIZE, TILE, TILE);
		}
	}

	private ArrayList<String> readAllLines(String filename) {
		ArrayList<String> lines = new ArrayList<>();

		// try loading from resources
		try (InputStream in = GameModel.class.getClassLoader().getResourceAsStream(filename)) {
			if (in != null) {
				try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
					String line;
					while ((line = br.readLine()) != null) lines.add(line);
				}
				return lines;
			}
		} catch (Exception ignored) { }

		// fallback: try loading from disk
		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(new java.io.FileInputStream(new File(filename))))) {
			String line;
			while ((line = br.readLine()) != null) lines.add(line);
		} catch (Exception e) {
			System.out.println(filename + " not found or could not be read: " + e.getMessage());
		}

		return lines;
	}

	public void resetGame() {
		score = 0;
		oldLives = 3;
		levelComplete = false;
	}
}
