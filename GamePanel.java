import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private final JPanel cards;
    private final CardLayout cardLayout;
    private final String playerName;
    private final String difficulty;

    private javax.swing.Timer timer;

    // Player state
    private int spaceshipX, spaceshipY;
    private int spaceshipSpeed = 8;
    private boolean leftPressed, rightPressed;

    // Game state
    private java.util.List<Rectangle> bullets = new ArrayList<>();
    private java.util.List<Rectangle> enemies = new ArrayList<>();
    private java.util.List<Rectangle> asteroids = new ArrayList<>();
    private java.util.List<Point> explosions = new ArrayList<>();

    private int enemySpeed, asteroidSpeed;
    private int score = 0;
    private boolean gameOver = false;

    // Images
    private Image playerImg, enemyImg, asteroidImg, bulletImg, explosionImg, bgImg;

    public GamePanel(JPanel cards, CardLayout cardLayout, String playerName, String difficulty) {
        this.cards = cards;
        this.cardLayout = cardLayout;
        this.playerName = playerName;
        this.difficulty = difficulty;

        setFocusable(true);
        addKeyListener(this);

        spaceshipX = 400;
        spaceshipY = 500;

        switch (difficulty) {
            case "EASY" -> { enemySpeed = 2; asteroidSpeed = 2; }
            case "MEDIUM" -> { enemySpeed = 4; asteroidSpeed = 3; }
            case "HARD" -> { enemySpeed = 6; asteroidSpeed = 4; }
        }

        loadImages();

        timer = new javax.swing.Timer(30, this);
        timer.start();
    }

    private void loadImages() {
        try {
            playerImg = ImageIO.read(getClass().getResource("/resources/player_ship.png"));
            enemyImg = ImageIO.read(getClass().getResource("/resources/enemy_ship.png"));
            asteroidImg = ImageIO.read(getClass().getResource("/resources/asteroid.png"));
            bulletImg = ImageIO.read(getClass().getResource("/resources/bullet.png"));
            explosionImg = ImageIO.read(getClass().getResource("/resources/explosion.png"));
            bgImg = ImageIO.read(getClass().getResource("/resources/space_bg.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            moveSpaceship();
            moveBullets();
            spawnEnemies();
            spawnAsteroids();
            moveEnemies();
            moveAsteroids();
            checkCollisions();
        }
        repaint();
    }

    private void moveSpaceship() {
        if (leftPressed && spaceshipX > 0) spaceshipX -= spaceshipSpeed;
        if (rightPressed && spaceshipX < getWidth() - playerImg.getWidth(null)) spaceshipX += spaceshipSpeed;
    }

    private void moveBullets() {
        Iterator<Rectangle> it = bullets.iterator();
        while (it.hasNext()) {
            Rectangle b = it.next();
            b.y -= 10;
            if (b.y < 0) it.remove();
        }
    }

    private void spawnEnemies() {
        if (new Random().nextInt(40) == 0) {
            enemies.add(new Rectangle(new Random().nextInt(getWidth() - 40), 0, 40, 40));
        }
    }

    private void spawnAsteroids() {
        if (new Random().nextInt(60) == 0) {
            asteroids.add(new Rectangle(new Random().nextInt(getWidth() - 30), 0, 30, 30));
        }
    }

    private void moveEnemies() {
        for (Rectangle e : enemies) {
            e.y += enemySpeed;
        }
    }

    private void moveAsteroids() {
        for (Rectangle a : asteroids) {
            a.y += asteroidSpeed;
        }
    }

    private void checkCollisions() {
        Iterator<Rectangle> bulletIt = bullets.iterator();
        while (bulletIt.hasNext()) {
            Rectangle b = bulletIt.next();

            Iterator<Rectangle> enemyIt = enemies.iterator();
            while (enemyIt.hasNext()) {
                Rectangle e = enemyIt.next();
                if (b.intersects(e)) {
                    score += 10;
                    explosions.add(new Point(e.x, e.y));
                    bulletIt.remove();
                    enemyIt.remove();
                    break;
                }
            }

            Iterator<Rectangle> asteroidIt = asteroids.iterator();
            while (asteroidIt.hasNext()) {
                Rectangle a = asteroidIt.next();
                if (b.intersects(a)) {
                    score += 5;
                    explosions.add(new Point(a.x, a.y));
                    bulletIt.remove();
                    asteroidIt.remove();
                    break;
                }
            }
        }

        // END GAME ONLY on collision with spaceship
        Rectangle shipRect = new Rectangle(spaceshipX, spaceshipY, playerImg.getWidth(null), playerImg.getHeight(null));
        for (Rectangle e : enemies) {
            if (e.intersects(shipRect)) endGame();
        }
        for (Rectangle a : asteroids) {
            if (a.intersects(shipRect)) endGame();
        }
    }

    private void endGame() {
        gameOver = true;
        timer.stop();
        GameOverPanel over = (GameOverPanel) cards.getComponent(2);
        over.setFinalScore(playerName, score);
        cardLayout.show(cards, "GAMEOVER");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Background
        if (bgImg != null) {
            g.drawImage(bgImg, 0, 0, getWidth(), getHeight(), null);
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        // Spaceship
        if (playerImg != null) {
            g.drawImage(playerImg, spaceshipX, spaceshipY, null);
        }

        // Bullets
        for (Rectangle b : bullets) {
            g.drawImage(bulletImg, b.x, b.y, b.width, b.height, null);
        }

        // Enemies
        for (Rectangle e : enemies) {
            g.drawImage(enemyImg, e.x, e.y, e.width, e.height, null);
        }

        // Asteroids
        for (Rectangle a : asteroids) {
            g.drawImage(asteroidImg, a.x, a.y, a.width, a.height, null);
        }

        // Explosions
        for (Point p : explosions) {
            g.drawImage(explosionImg, p.x, p.y, 40, 40, null);
        }
        explosions.clear();

        // HUD
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("Player: " + playerName + "   Score: " + score, 10, 20);

        if (gameOver) {
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.setColor(Color.RED);
            g.drawString("GAME OVER! Press R to Restart", 100, getHeight() / 2);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver && e.getKeyCode() == KeyEvent.VK_R) {
            cardLayout.show(cards, "WELCOME");
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) leftPressed = true;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) rightPressed = true;

        // Fire bullet when UP arrow pressed
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            bullets.add(new Rectangle(
                    spaceshipX + playerImg.getWidth(null) / 2 - 2,
                    spaceshipY, 5, 10
            ));
        }
    }

    @Override public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) leftPressed = false;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) rightPressed = false;
    }
    @Override public void keyTyped(KeyEvent e) {}
}



















