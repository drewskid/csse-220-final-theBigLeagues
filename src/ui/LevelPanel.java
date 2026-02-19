package ui;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import model.GameModel;

/**
 * Wrapper around GameComponent so CardLayout can swap levels cleanly.
 */
public class LevelPanel extends JPanel {
    private final GameComponent gameComponent;

    public LevelPanel(GameModel model, GameEvents events) {
        setLayout(new BorderLayout());
        gameComponent = new GameComponent(model, events);
        add(gameComponent, BorderLayout.CENTER);
    }

    public void requestFocusForGame() {
        gameComponent.requestFocusInWindow();
    }
}
