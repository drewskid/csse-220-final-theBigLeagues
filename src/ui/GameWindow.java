package ui;

import javax.swing.JFrame;

import model.GameModel;

public class GameWindow {
	private JFrame frame;
	private GameModel model;

	public GameWindow(GameModel model) {
		this.model = model;

		frame = new JFrame("CSSE220 Final Project");
		frame.add(new GameComponent(model));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setResizable(false);
	}

	public void run() {
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
