package model;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class GameModel {

	public static final int WORLD_W = 800;
	public static final int WORLD_H = 600;

	public static final int TILE = 50;

	public static final int WALL_THICKNESS = TILE;
	public static final int PLAYER_SIZE = TILE * 3 / 4;
	public static final int ZOMBIE_SIZE = TILE * 3 / 4;

	private ArrayList<Floor> floor = new ArrayList<>();
	private ArrayList<Wall> walls = new ArrayList<>();
	private Exit exit;
	private Player player;
	private ArrayList<Zombie> zombies = new ArrayList<>();
	private ArrayList<Collectible> collectibles = new ArrayList<>();

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
		if (exit != null) exit.draw(g2);
		for (Collectible c : collectibles) c.draw(g2);
		if (player != null) player.draw(g2);
		for (Zombie z : zombies) z.draw(g2);
	}

	public void update() {
		for (Zombie z : zombies) {
			z.update(walls, WORLD_W, WORLD_H);
		}

		// LEVEL COMPLETE ONLY when exit is reached AND all collectibles are collected
		if (player != null && exit != null
				&& player.getBounds().intersects(exit.getBounds())) {
			oldLives = player.getLives();
			levelComplete = true;
		}
	}

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
				if (outOfBounds(player) || hitsWall(player)) player.setX(oldX);
			}

			if (stepY != 0) {
				int oldY = player.getY();
				player.setY(oldY + stepY);
				if (outOfBounds(player) || hitsWall(player)) player.setY(oldY);
			}
		}

		if (dy < 0) player.setDirection(Player.Direction.UP);
		else if (dy > 0) player.setDirection(Player.Direction.DOWN);
		else if (dx < 0) player.setDirection(Player.Direction.LEFT);
		else if (dx > 0) player.setDirection(Player.Direction.RIGHT);
	}

	public void collectItem() {
		if (player == null) return;

		collectibles.removeIf(c -> {
			if (player.getBounds().intersects(c.getBounds())) {
				score += 50;
				return true;
			}
			return false;
		});
	}

	public void hitZombie() {
		if (player == null) return;

		for (Zombie z : zombies) {
			if (player.getBounds().intersects(z.getCollisionRectangle())) {
				player.updateLives();
				respawnPlayer();
				return;
			}
		}
	}

	public void respawnPlayer() {
		if (player == null) return;
		player.setX(playerStartX);
		player.setY(playerStartY);
	}

	private boolean hitsWall(Asset a) {
		for (Wall w : walls) {
			if (a.getBounds().intersects(w.getBounds())) return true;
		}
		return false;
	}

	private boolean outOfBounds(Asset a) {
		return a.getX() < 0 || a.getY() < 0
				|| a.getX() + a.getW() > WORLD_W
				|| a.getY() + a.getH() > WORLD_H;
	}

	public void loadLevel() {
		levelComplete = false;

		String filename = this.level;

		floor.clear();
		walls.clear();
		zombies.clear();
		collectibles.clear();
		player = null;
		exit = null;

		ArrayList<String> lines = readAllLines(filename);
		if (lines.isEmpty()) {
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
					collectibles.add(new Collectible(cw, chh,
							x + (TILE - cw) / 2,
							y + (TILE - chh) / 2));
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
