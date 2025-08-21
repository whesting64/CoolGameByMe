package Engine;

import Entities.Entity;
import Entities.Player;
import Entities.Enemy;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class GamePanel extends JPanel implements Runnable {

    //screen settings
    static final int originalTileSize = 16;
    public static int scale = 4;
    static final int tileSize = originalTileSize * scale;
    final int maxScreenCol = 10;
    final int maxScreenRow = 9;
    final int screenWidth = maxScreenCol * tileSize;
    final int screenHeight = maxScreenRow * tileSize;
    int FPS = 60;
    public boolean debugMode = false;

    KeyHandler keyHandler = new KeyHandler();
    Thread gameThread;

    //game objects
    public static ArrayList<Rectangle> platforms = new ArrayList<>();
    public static Player player;
    public static ArrayList<Entity> enemies = new ArrayList<>();

    public GamePanel() {


        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.WHITE);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);

        platforms.add(new Rectangle(0, screenHeight - tileSize, screenWidth, tileSize * 2));

        player = new Player(tileSize * 3, tileSize * 3, scale, 150, 1, 10);
        enemies.add(new Enemy(tileSize * 4, tileSize * 4, scale/4, 3 * tileSize, 1, 4));


    }

    public static int getTileSize() {
        return tileSize;
    }

    public void  startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }


    @Override
    public void run() {

        double drawInterval = (double) 1000000000 / FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (gameThread != null && player.health > 0) {


            if (KeyHandler.spacePressed) {
                debugMode = !debugMode;
                KeyHandler.spacePressed = false;
            }

            player.update();
            for (Entity e : enemies) {
                if (e instanceof Enemy) {
                    e.update(player);

                }
            }

            repaint();

            try {
                double remainingDrawTime = nextDrawTime - System.nanoTime();
                remainingDrawTime /= 1000000;

                if (remainingDrawTime < 0) {
                    remainingDrawTime = 0;
                }

                Thread.sleep((long) remainingDrawTime);

                nextDrawTime += drawInterval;

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            enemies.removeIf(e -> e.health <= 0);
        }

    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        g2.setColor(Color.black);
        for (Rectangle platform : GamePanel.platforms) {
            g2.fillRect(platform.x, platform.y, platform.width, platform.height);
        }
        player.draw(g2, debugMode);
        for (Entity enemy : enemies) {
            enemy.draw(g2, debugMode);
        }

        if (player.health == 0) {
            g2.setColor(Color.RED);
            g2.fillRect(0, 0, screenWidth, screenHeight);
            g2.setColor(Color.black);
            g2.setFont(new Font("Arial", Font.BOLD, 200));
            FontMetrics fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth("Oof");
            int textHeight = fm.getAscent();
            int x = (screenWidth - textWidth) / 2;
            int y = (screenHeight + textHeight) / 2;
            g2.drawString("Oof", x, y);
        }

        g2.dispose();

    }
}
