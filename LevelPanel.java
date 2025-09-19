import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.net.URL;
import java.io.File;

public class LevelPanel extends JPanel {
    private final JPanel cards;
    private final CardLayout cardLayout;
    private final WelcomePanel welcome;
    private BufferedImage bg;

    public LevelPanel(JPanel cards, CardLayout cardLayout, WelcomePanel welcome) {
        this.cards = cards;
        this.cardLayout = cardLayout;
        this.welcome = welcome;

        loadBackground();

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12,12,12,12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("☆ Choose Difficulty ☆", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 34));
        title.setForeground(new Color(255, 200, 60));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(title, gbc);

        JLabel subtitle = new JLabel("Select how hot you want the fight to be", SwingConstants.CENTER);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(Color.LIGHT_GRAY);
        gbc.gridy = 1;
        add(subtitle, gbc);

        // Buttons panel
        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        JButton easy = styledButton("EASY", new Color(120, 220, 120));
        JButton medium = styledButton("MEDIUM", new Color(255, 200, 80));
        JButton hard = styledButton("HARD", new Color(240, 120, 120));

        btnPanel.add(easy);
        btnPanel.add(medium);
        btnPanel.add(hard);

        gbc.gridy = 2; add(btnPanel, gbc);

        // Back button
        JButton back = new JButton("← Back");
        back.setBackground(Color.DARK_GRAY);
        back.setForeground(Color.WHITE);
        gbc.gridy = 3; gbc.gridwidth = 1; gbc.gridx = 0;
        add(back, gbc);

        back.addActionListener(e -> cardLayout.show(cards, "WELCOME"));

        // Start buttons
        ActionListener start = ev -> {
            String playerName = welcome.getPlayerName();
            if (playerName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter your name on the welcome screen first.", "Missing name", JOptionPane.WARNING_MESSAGE);
                cardLayout.show(cards, "WELCOME");
                return;
            }
            String difficulty = ((JButton) ev.getSource()).getText();
            // remove any existing GAME panel so we don't accumulate duplicates
            Component[] comps = cards.getComponents();
            java.util.List<Component> toRemove = new java.util.ArrayList<>();
            for (Component c : comps) {
                if (c instanceof GamePanel) toRemove.add(c);
            }
            for (Component c : toRemove) cards.remove(c);

            GamePanel game = new GamePanel(cards, cardLayout, playerName, difficulty);
            // store last used details so GameOver can restart same config
            cards.putClientProperty("lastPlayer", playerName);
            cards.putClientProperty("lastDifficulty", difficulty);

            cards.add(game, "GAME");
            cardLayout.show(cards, "GAME");
            game.requestFocusInWindow();
        };

        easy.addActionListener(start);
        medium.addActionListener(start);
        hard.addActionListener(start);
    }

    private JButton styledButton(String text, Color borderColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 18));
        btn.setForeground(Color.WHITE);
        btn.setBackground(Color.BLACK);
        btn.setBorder(BorderFactory.createLineBorder(borderColor, 3));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(150, 48));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(borderColor.darker()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(Color.BLACK); }
        });
        return btn;
    }

    private void loadBackground() {
        bg = loadImage("/resources/space_bg.png");
    }

    private BufferedImage loadImage(String path) {
        try {
            URL url = getClass().getResource(path);
            if (url != null) return ImageIO.read(url);
        } catch (Exception ignored) {}
        try {
            File f = new File(path.startsWith("/") ? path.substring(1) : path);
            if (f.exists()) return ImageIO.read(f);
        } catch (Exception ignored) {}
        return null;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (bg != null) g.drawImage(bg, 0, 0, getWidth(), getHeight(), null);
        else {
            Graphics2D g2 = (Graphics2D) g;
            g2.setPaint(new GradientPaint(0,0,Color.BLACK,0,getHeight(),Color.DARK_GRAY));
            g2.fillRect(0,0,getWidth(),getHeight());
        }
    }
}















