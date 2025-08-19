package Engine;

import Entities.Entity;
import Entities.Player;
import Entities.Enemy;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;



public class GamePanel extends JPanel implements Runnable {

    //screen settings
    final int originalTileSize = 16;
    final int scale = 3;
    final int tileSize = originalTileSize * scale;
    final int maxScreenCol = 16;
    final int maxScreenRow = 16;
    final int screenWidth = maxScreenCol * tileSize;
    final int screenHeight = maxScreenRow * tileSize;
    int FPS = 60;
    public boolean debugMode = false;

    KeyHandler keyHandler = new KeyHandler();
    Thread gameThread;

    //game objects
    public static ArrayList<Rectangle> platforms = new ArrayList<>();
    Player player;
    public static ArrayList<Entity> enemies = new ArrayList<>();

    public GamePanel() {


        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.WHITE);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);

        platforms.add(new Rectangle(0, screenHeight - tileSize * 2, screenWidth, tileSize * 2));
        platforms.add(new Rectangle(tileSize * 4, screenHeight - tileSize * 4, tileSize, tileSize));
        platforms.add(new Rectangle(tileSize * 7, screenHeight - tileSize * 6, tileSize, tileSize));
        platforms.add(new Rectangle(tileSize * 12, screenHeight - tileSize * 10, tileSize, tileSize));

        player = new Player(100, 100, 4, 200);
        enemies.add(new Enemy(200, 200, 1, 200));
        enemies.add(new Enemy(300, 300, 3, 100));


    }

    public void  startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }


    @Override
    public void run() {

        double drawInterval = (double) 1000000000 / FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (gameThread != null) {


            if (KeyHandler.spacePressed) {
                debugMode = !debugMode;
                KeyHandler.spacePressed = false;
            }

            player.update();
            for (Entity e : enemies) {
                if (e instanceof Enemy) {
                    e.update(player);

                    if(e.collidesWith(player)) {
                        System.out.println("oof");
                    }

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
        }

    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        g2.setColor(Color.BLACK);
        for (Rectangle platform : GamePanel.platforms) {
            g2.fillRect(platform.x, platform.y, platform.width, platform.height);
        }
        player.draw(g2, debugMode);
        for (Entity enemy : enemies) {
            enemy.draw(g2, debugMode);
        }
        g2.dispose();

    }
}
