package ui;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import model.GameModel;

public class GameWindow implements GameEvents {

	private static final String CARD_START = "START";
	private static final String CARD_GAME = "GAME";
	private static final String CARD_DEATH = "DEATH";
	private static final String CARD_WIN = "WIN";

	private final JFrame frame;
	private final GameModel model;

	private final JPanel cards;
	private final CardLayout cl;

	private final StartPanel startPanel;
	private final DeathPanel deathPanel;
	private final WinPanel winPanel;

	private LevelPanel levelPanel; // recreated each level

	private final String[] levels = { "level1.txt", "level2.txt", "level3.txt", "level4.txt", "level5.txt"};
	private int levelIndex = 0;

	public GameWindow(GameModel model) {
		this.model = model;

		frame = new JFrame("CSSE220 Final Project");

		cards = new JPanel();
		cl = new CardLayout();
		cards.setLayout(cl);

		startPanel = new StartPanel();
		deathPanel = new DeathPanel();
		winPanel = new WinPanel();

		cards.add(startPanel, CARD_START);
		cards.add(deathPanel, CARD_DEATH);
		cards.add(winPanel, CARD_WIN);

		frame.setContentPane(cards);

		wireButtons();

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);
		frame.setResizable(false);

		cl.show(cards, CARD_START);
	}

	private void wireButtons() {
		// START -> new run
		startPanel.startButton.addActionListener(e -> startNewRun());

		// DEATH buttons
		deathPanel.restartButton.addActionListener(e -> startNewRun());
		deathPanel.backToStartButton.addActionListener(e -> cl.show(cards, CARD_START));

		// WIN buttons
		winPanel.playAgainButton.addActionListener(e -> startNewRun());
		winPanel.backToStartButton.addActionListener(e -> cl.show(cards, CARD_START));
	}

	private void startNewRun() {
		model.resetGame();
		levelIndex = 0;
		loadAndShowLevel(levelIndex);
	}

	private void loadAndShowLevel(int idx) {
		model.setLevel(levels[idx]);
		model.loadLevel();

		// remake GAME card so the timer restarts cleanly each level
		if (levelPanel != null) {
			cards.remove(levelPanel);
		}
		levelPanel = new LevelPanel(model, this);
		cards.add(levelPanel, CARD_GAME);

		cl.show(cards, CARD_GAME);
		levelPanel.requestFocusForGame();
	}

	@Override
	public void onLevelComplete() {
		levelIndex++;

		if (levelIndex >= levels.length) {
			winPanel.setScore(model.getScore());
			cl.show(cards, CARD_WIN);
			return;
		}

		loadAndShowLevel(levelIndex);
	}

	@Override
	public void onPlayerDied() {
		deathPanel.setScore(model.getScore());
		cl.show(cards, CARD_DEATH);
	}

	public void run() {
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
