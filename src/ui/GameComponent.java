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

	private final int WIDTH = 800;
	private final int HEIGHT = 600;
	private GameModel model;
	private boolean up, down, left, right, space, R;
	private boolean gameOver = false;

	public GameComponent(GameModel model) {
		this.model = model;
		space = false;
		R = false;
		setPreferredSize(new Dimension(WIDTH, HEIGHT));

		setupKeyBindings();
		
		Timer timer = new Timer(16, e -> {
			if(model.getPlayer().getLives() <= 0) {
				if (R) model.resetGame();
				repaint();
				return;
			}
			handlePlayerMovement();
			model.update();   
			model.collectItem();
			model.hitZombie();
			repaint();
		});
		timer.start();
		
		
		
	}


	private void handlePlayerMovement() {
		int speed = model.getPlayer().getSpeed();
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
		input.put(KeyStroke.getKeyStroke("pressed SPACE"), "spacePressed");
		input.put(KeyStroke.getKeyStroke("released SPACE"), "spaceReleased");
		input.put(KeyStroke.getKeyStroke("pressed R"), "rPressed");
		input.put(KeyStroke.getKeyStroke("released R"), "rReleased");

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
		actions.put("spacePressed", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {space = true;}
		});
		actions.put("spaceReleased", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {space = false;}
		});
		actions.put("rPressed", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {R = true;}
		});
		actions.put("rReleased", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {R = false;}
		});
		
		
		

	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		model.draw(g2);
		g2.setColor(Color.WHITE);
		g2.setFont(new Font("Monospaced", Font.BOLD, 20));
		g2.drawString("Score: " + model.getScore(), 20, 25);
		g2.drawString("Lives: " + model.getPlayer().getLives(), 40, 50);
		if(model.getPlayer().getLives() <= 0) {
			g2.setFont(new Font("Monospaced", Font.BOLD, 48));
			g2.setColor(Color.RED);
			String msg = "GAME OVER";
			g2.drawString(msg, (WIDTH/2)-100, HEIGHT/2);
			g2.setFont(new Font("Monospaced", Font.BOLD, 20));
			g2.drawString("Press R to Restart", (WIDTH/2)-75, HEIGHT/2+40);
		}
	}
}
