package app;

import java.awt.Color;

import javax.swing.SwingUtilities;

import model.GameModel;
import ui.GameWindow;

public class MainApp {
	public static void main(String[] args) {

		GameModel model = new GameModel("level1.txt");
		System.out.println("Game is running");

		SwingUtilities.invokeLater(() -> new GameWindow(model).run());
	}
}
