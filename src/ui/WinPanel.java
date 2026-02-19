package ui;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class WinPanel extends JPanel {
    private final JLabel title = new JLabel("You Win!", SwingConstants.CENTER);
    private final JLabel scoreLabel = new JLabel("Final Score: 0", SwingConstants.CENTER);

    public final JButton playAgainButton = new JButton("Play Again");
    public final JButton backToStartButton = new JButton("Back to Start");

    public WinPanel() {
        setLayout(new BorderLayout());

        title.setFont(new Font("SansSerif", Font.BOLD, 48));
        scoreLabel.setFont(new Font("SansSerif", Font.BOLD, 24));

        JPanel center = new JPanel(new BorderLayout());
        center.add(title, BorderLayout.CENTER);
        center.add(scoreLabel, BorderLayout.SOUTH);

        JPanel bottom = new JPanel();
        bottom.add(playAgainButton);
        bottom.add(backToStartButton);

        add(center, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    public void setScore(int score) {
        scoreLabel.setText("Final Score: " + score);
    }
}
