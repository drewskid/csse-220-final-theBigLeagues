package ui;

/**
 * UI callbacks from the game loop so GameWindow can switch CardLayout screens.
 */
public interface GameEvents {
    void onLevelComplete();
    void onPlayerDied();
}