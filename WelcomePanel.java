import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.net.URL;
import java.io.File;

public class WelcomePanel extends JPanel {
    private final JPanel cards;
    private final CardLayout cardLayout;
    private final JTextField nameField;
    private BufferedImage bg;

    public WelcomePanel(JPanel cards, CardLayout cardLayout) {
        this.cards = cards;
        this.cardLayout = cardLayout;

        loadBackground();

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        setPreferredSize(new Dimension(800, 600));
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("☆ Space Shooter ☆", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 36));
        title.setForeground(new Color(80, 240, 255)); // cyan-ish
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(title, gbc);

        JLabel subtitle = new JLabel("Pilot your ship — fight aliens & asteroids", SwingConstants.CENTER);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 16));
        subtitle.setForeground(Color.LIGHT_GRAY);
        gbc.gridy = 1;
        add(subtitle, gbc);

        JLabel nameLabel = new JLabel("Commander name:", SwingConstants.LEFT);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        gbc.gridy = 2; gbc.gridwidth = 1; gbc.gridx = 0;
        add(nameLabel, gbc);

        nameField = new JTextField();
        nameField.setFont(new Font("Monospaced", Font.PLAIN, 16));
        nameField.setBackground(Color.BLACK);
        nameField.setForeground(new Color(130, 255, 170)); // greenish text
        nameField.setCaretColor(new Color(80, 240, 255));
        nameField.setBorder(BorderFactory.createLineBorder(new Color(80,240,255), 2));
        nameField.setPreferredSize(new Dimension(260, 36));
        gbc.gridx = 1;
        add(nameField, gbc);

        // small hint
        JLabel hint = new JLabel("e.g. Commander Nova", SwingConstants.LEFT);
        hint.setFont(new Font("SansSerif", Font.ITALIC, 12));
        hint.setForeground(Color.GRAY);
        gbc.gridy = 3; gbc.gridx = 1;
        add(hint, gbc);

        JButton next = new JButton("→ SELECT LEVEL");
        next.setFont(new Font("SansSerif", Font.BOLD, 18));
        next.setBackground(new Color(80,240,255));
        next.setForeground(Color.BLACK);
        next.setFocusPainted(false);
        gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 2;
        add(next, gbc);

        next.addActionListener(e -> {
            if (nameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter your commander name to continue.", "Name required", JOptionPane.WARNING_MESSAGE);
                nameField.requestFocusInWindow();
                return;
            }
            // allow LevelPanel to read name from welcome panel
            cardLayout.show(cards, "LEVEL");
        });
    }

    private void loadBackground() {
        // try classpath then disk fallback
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

    public String getPlayerName() {
        return nameField.getText().trim();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (bg != null) {
            g.drawImage(bg, 0, 0, getWidth(), getHeight(), null);
        } else {
            // fallback gradient
            Graphics2D g2 = (Graphics2D) g;
            g2.setPaint(new GradientPaint(0,0,Color.BLACK,0,getHeight(), Color.DARK_GRAY));
            g2.fillRect(0,0,getWidth(),getHeight());
        }
    }
}













