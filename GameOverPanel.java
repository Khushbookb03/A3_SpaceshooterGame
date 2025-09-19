import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameOverPanel extends JPanel implements KeyListener {
    private final JPanel cards;
    private final CardLayout cardLayout;
    private JLabel scoreLabel;

    public GameOverPanel(JPanel cards, CardLayout cardLayout) {
        this.cards = cards;
        this.cardLayout = cardLayout;

        setLayout(new GridBagLayout());
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12,12,12,12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("💀 GAME OVER 💀", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 36));
        title.setForeground(Color.RED);
        gbc.gridy = 0; add(title, gbc);

        scoreLabel = new JLabel("Score: 0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
        scoreLabel.setForeground(Color.WHITE);
        gbc.gridy = 1; add(scoreLabel, gbc);

        JPanel btns = new JPanel();
        btns.setOpaque(false);
        JButton restart = new JButton("Restart");
        JButton exit = new JButton("Exit");
        restart.setBackground(new Color(120,220,120));
        exit.setBackground(new Color(240,120,120));
        btns.add(restart);
        btns.add(exit);
        gbc.gridy = 2; add(btns, gbc);

        restart.addActionListener(e -> {
            String lastPlayer = (String) cards.getClientProperty("lastPlayer");
            String lastDiff = (String) cards.getClientProperty("lastDifficulty");
            if (lastPlayer != null && lastDiff != null) {
                GamePanel newGame = new GamePanel(cards, cardLayout, lastPlayer, lastDiff);
                java.util.List<Component> toRemove = new java.util.ArrayList<>();
                for (Component c : cards.getComponents()) if (c instanceof GamePanel) toRemove.add(c);
                for (Component c : toRemove) cards.remove(c);
                cards.add(newGame, "GAME");
                cardLayout.show(cards, "GAME");
                newGame.requestFocusInWindow();
            } else {
                cardLayout.show(cards, "LEVEL");
            }
        });

        exit.addActionListener(e -> System.exit(0));
    }

    public void setFinalScore(String playerName, int score) {
        scoreLabel.setText("Player: " + playerName + "  |  Score: " + score);
        requestFocusInWindow(); // allow R to be captured
    }

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_R) {
            String lastPlayer = (String) cards.getClientProperty("lastPlayer");
            String lastDiff = (String) cards.getClientProperty("lastDifficulty");
            if (lastPlayer != null && lastDiff != null) {
                GamePanel newGame = new GamePanel(cards, cardLayout, lastPlayer, lastDiff);
                java.util.List<Component> toRemove = new java.util.ArrayList<>();
                for (Component c : cards.getComponents()) if (c instanceof GamePanel) toRemove.add(c);
                for (Component c : toRemove) cards.remove(c);
                cards.add(newGame, "GAME");
                cardLayout.show(cards, "GAME");
                newGame.requestFocusInWindow();
            } else {
                cardLayout.show(cards, "LEVEL");
            }
        }
    }
    @Override public void keyReleased(KeyEvent e) {}
}










