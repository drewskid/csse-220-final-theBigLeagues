package app;

import javax.swing.SwingUtilities;

import model.GameModel;
import ui.GameWindow;

public class MainApp {
	public static void main(String[] args) {
		
		GameModel model1 = new GameModel();
		
		
		SwingUtilities.invokeLater(() ->new GameWindow(model1).run());
	}
}
