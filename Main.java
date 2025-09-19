import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("🚀 Space Shooter");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setResizable(false);

            CardLayout layout = new CardLayout();
            JPanel cards = new JPanel(layout);

            WelcomePanel welcome = new WelcomePanel(cards, layout);
            LevelPanel level = new LevelPanel(cards, layout, welcome);
            GameOverPanel gameOver = new GameOverPanel(cards, layout);

            cards.add(welcome, "WELCOME");
            cards.add(level, "LEVEL");
            cards.add(gameOver, "GAMEOVER");

            frame.add(cards);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            layout.show(cards, "WELCOME");
        });
    }
}












