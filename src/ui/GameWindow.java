package ui;

import javax.swing.JFrame;
import javax.swing.Timer;

import model.GameModel;

public class GameWindow {

	public static void show() {
		// Minimal model instance (empty for now, by design)
		GameModel model = new GameModel();
		Timer timer;


		JFrame frame = new JFrame("CSSE220 Final Project");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		frame.add(new GameComponent(model));


		frame.setSize(800, 600);
		frame.setLocationRelativeTo(null); // center on screen (nice UX, still minimal)
		frame.setVisible(true);
//		timer = new Timer(16, null); // ~60 FPS
//        timer.start();
		}

}
