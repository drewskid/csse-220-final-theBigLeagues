package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;

import model.GameModel;
import model.Player;

public class GameComponent extends JPanel {

	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;

	private final GameModel model;
	private final GameEvents events;

	private boolean up, down, left, right;

	private final Timer timer;

	public GameComponent(GameModel model, GameEvents events) {
		this.model = model;
		this.events = events;

		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setupKeyBindings();

		timer = new Timer(16, e -> tick());
		timer.start();
	}

	private void tick() {
		// death -> switch card
		if (model.isPlayerDead()) {
			timer.stop();
			if (events != null) events.onPlayerDied();
			return;
		}

		handlePlayerMovement();

		model.update();
		model.collectItem();
		model.hitZombie();

		// level complete -> switch card
		if (model.isLevelComplete()) {
			timer.stop();
			if (events != null) events.onLevelComplete();
			return;
		}

		repaint();
	}

	private void handlePlayerMovement() {
		Player p = model.getPlayer();
		if (p == null) return;

		int speed = p.getSpeed();
		int dx = 0;
		int dy = 0;

		if (up) dy -= speed;
		if (down) dy += speed;
		if (left) dx -= speed;
		if (right) dx += speed;

		if (dx != 0 || dy != 0) {
			model.movePlayer(dx, dy);
		}
	}

	private void setupKeyBindings() {
		int condition = JComponent.WHEN_IN_FOCUSED_WINDOW;
		InputMap input = getInputMap(condition);
		ActionMap actions = getActionMap();

		input.put(KeyStroke.getKeyStroke("pressed UP"), "upPressed");
		input.put(KeyStroke.getKeyStroke("released UP"), "upReleased");
		input.put(KeyStroke.getKeyStroke("pressed DOWN"), "downPressed");
		input.put(KeyStroke.getKeyStroke("released DOWN"), "downReleased");
		input.put(KeyStroke.getKeyStroke("pressed LEFT"), "leftPressed");
		input.put(KeyStroke.getKeyStroke("released LEFT"), "leftReleased");
		input.put(KeyStroke.getKeyStroke("pressed RIGHT"), "rightPressed");
		input.put(KeyStroke.getKeyStroke("released RIGHT"), "rightReleased");

		actions.put("upPressed", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) { up = true; }
		});
		actions.put("upReleased", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) { up = false; }
		});

		actions.put("downPressed", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) { down = true; }
		});
		actions.put("downReleased", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) { down = false; }
		});

		actions.put("leftPressed", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) { left = true; }
		});
		actions.put("leftReleased", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) { left = false; }
		});

		actions.put("rightPressed", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) { right = true; }
		});
		actions.put("rightReleased", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) { right = false; }
		});
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		model.draw(g2);

		// HUD
		g2.setColor(Color.WHITE);
		g2.setFont(new Font("Monospaced", Font.BOLD, 20));
		g2.drawString("Score: " + model.getScore(), 20, 25);

		Player p = model.getPlayer();
		if (p != null) {
			g2.drawString("Lives: " + p.getLives(), 20, 50);
		}
	}
}
