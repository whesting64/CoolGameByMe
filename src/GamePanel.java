import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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

    KeyHandler keyHandler = new KeyHandler();
    Thread gameThread;

    //play positons
    int playerX = 100;
    int playerY = 288;
    int playSpeed = 4;
    int dx = 0, dy = 0;

    //game objects
    ArrayList<Rectangle> platforms = new ArrayList<>();

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

            update();

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

    public void update() {

        dx = 0;
        dy = 0;

        if (keyHandler.leftPressed) {
            dx -= playSpeed;
        }
        if (keyHandler.rightPressed) {
            dx += playSpeed;
        }
        if (keyHandler.upPressed) {
            dy -= playSpeed;
        }
        if (keyHandler.downPressed) {
            dy += playSpeed;
        }



        Rectangle nextX = new Rectangle(playerX + dx, playerY, 48, 48);
        boolean collidedX = false;
        for (Rectangle platform : platforms) {
            if (nextX.intersects(platform)) {
                collidedX = true;
                break;
            }
        }
        if (!collidedX) {
            playerX += dx;
        }
        Rectangle nextY = new Rectangle(playerX, playerY + dy, 48, 48);
        boolean collidedY = false;
        for (Rectangle platform : platforms) {
            if (nextY.intersects(platform)) {
                collidedY = true;
                break;
            }
        }
        if (!collidedY) {
            playerY += dy;
        }


    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        g2.setColor(Color.BLACK);
        for (Rectangle platform : platforms) {
            g2.fillRect(platform.x, platform.y, platform.width, platform.height);
        }

        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("img/coolFortniteGuy.png"));
        } catch (IOException e) {
            System.err.println("Error loading coolFortniteGuy.png");
        }
        g2.setColor(Color.black);
        g2.drawImage(image, playerX, playerY, null);
        g2.setColor(Color.blue);
        g2.drawRect(playerX + dx, playerY, 48, 48);
        g2.setColor(Color.red);
        g2.drawRect(playerX, playerY + dy, 48, 48);
        g2.dispose();

    }
}
