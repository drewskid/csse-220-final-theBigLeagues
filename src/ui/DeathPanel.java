package ui;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class DeathPanel extends JPanel {
    private final JLabel title = new JLabel("You Died", SwingConstants.CENTER);
    private final JLabel scoreLabel = new JLabel("Score: 0", SwingConstants.CENTER);

    public final JButton restartButton = new JButton("Restart");
    public final JButton backToStartButton = new JButton("Back to Start");

    public DeathPanel() {
        setLayout(new BorderLayout());

        title.setFont(new Font("SansSerif", Font.BOLD, 48));
        scoreLabel.setFont(new Font("SansSerif", Font.BOLD, 24));

        JPanel center = new JPanel(new BorderLayout());
        center.add(title, BorderLayout.CENTER);
        center.add(scoreLabel, BorderLayout.SOUTH);

        JPanel bottom = new JPanel();
        bottom.add(restartButton);
        bottom.add(backToStartButton);

        add(center, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    public void setScore(int score) {
        scoreLabel.setText("Score: " + score);
    }
}
