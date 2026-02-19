package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class StartPanel extends JPanel {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    public JButton startButton;

    public StartPanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setOpaque(true);

        // ===== Center content =====
        JPanel center = new JPanel();
        center.setBackground(Color.WHITE);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBorder(BorderFactory.createEmptyBorder(70, 80, 40, 80));

        JLabel title = new JLabel("MAZE RUNNER", SwingConstants.CENTER);
        title.setAlignmentX(CENTER_ALIGNMENT);
        title.setForeground(Color.BLACK);
        title.setFont(new Font("SansSerif", Font.BOLD, 48));

        JLabel subtitle = new JLabel("Collect everything • Avoid zombies • Find the exit", SwingConstants.CENTER);
        subtitle.setAlignmentX(CENTER_ALIGNMENT);
        subtitle.setForeground(Color.BLACK);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 16));

        JPanel infoCard = makeInfoCard();

        center.add(title);
        center.add(Box.createRigidArea(new Dimension(0, 10)));
        center.add(subtitle);
        center.add(Box.createRigidArea(new Dimension(0, 30)));
        center.add(infoCard);

        // ===== Bottom button row =====
        JPanel bottom = new JPanel();
        bottom.setBackground(Color.WHITE);
        bottom.setBorder(BorderFactory.createEmptyBorder(0, 0, 60, 0));

        startButton = new JButton("START GAME");
        styleButton(startButton);

        bottom.add(startButton);

        add(center, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    private JPanel makeInfoCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel howTo = new JLabel("How to Play");
        howTo.setForeground(Color.BLACK);
        howTo.setFont(new Font("SansSerif", Font.BOLD, 20));
        howTo.setAlignmentX(LEFT_ALIGNMENT);

        card.add(howTo);
        card.add(Box.createRigidArea(new Dimension(0, 10)));

        card.add(makeBullet("Move with Arrow Keys"));
        card.add(makeBullet("Collect the gems to increase your score"));
        card.add(makeBullet("Avoid zombies (touching one costs a life)"));
        card.add(makeBullet("Reach the exit after collecting everything"));

        card.setAlignmentX(CENTER_ALIGNMENT);
        return card;
    }

    private JLabel makeBullet(String text) {
        JLabel l = new JLabel("• " + text);
        l.setForeground(Color.BLACK);
        l.setFont(new Font("SansSerif", Font.PLAIN, 15));
        l.setAlignmentX(LEFT_ALIGNMENT);
        return l;
    }

    private void styleButton(JButton b) {
        b.setFont(new Font("SansSerif", Font.BOLD, 18));
        b.setForeground(Color.BLACK);
        b.setBackground(Color.WHITE);
        b.setOpaque(true);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
    }
}
