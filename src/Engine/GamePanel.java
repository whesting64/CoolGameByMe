package Engine;

import Entities.Entity;
import Entities.Player;
import Entities.Enemy;
import ai.Pathfinding;
import map.Map;
import map.Tiles;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class GamePanel extends JPanel implements Runnable {

    //screen settings
    static final int originalTileSize = 16;
    final static int scale = 4;
    static final int tileSize = originalTileSize * scale;
    static final int maxScreenCol = 15;
    static final int maxScreenRow = 15;
    static final int screenWidth = maxScreenCol * tileSize;
    static final int screenHeight = maxScreenRow * tileSize;
    int FPS = 60;
    public static boolean debugMode = true;
    public final Rectangle bottomBar;
    public static int[][] map;
    public static Tiles[] tiles = Tiles.loadTilesImg();

    public int playerStartX = 3, playerStartY = 3;
    public int enemyStartX = 5, enemyStartY = 5;

    KeyHandler keyHandler = new KeyHandler();
    Thread gameThread;

    //game objects
    public static ArrayList<Rectangle> platforms = new ArrayList<>();
    public static Player player;
    public static ArrayList<Entity> enemies = new ArrayList<>();
    public Pathfinding pathfinder = new Pathfinding(this);

    public GamePanel() {

        map = Map.loadMap("Maps/Map01.txt", maxScreenRow, maxScreenCol);
        pathfinder.instantaiteNodes();

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.WHITE);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);

        bottomBar = new Rectangle(0, screenHeight - tileSize, screenWidth, tileSize * 2);
        player = new Player(tileSize * playerStartX, tileSize * playerStartY, scale, 150, 1, 10);
        enemies.add(new Enemy(tileSize * enemyStartX, tileSize * enemyStartY, scale / 4, 3 * tileSize, 1, 4, this));


    }

    public static int getTileSize() {
        return tileSize;
    }

    public static int getScreenWidth() {
        return screenWidth;
    }

    public static int getScreenHeight() {
        return screenHeight;
    }

    public static int getMaxScreenCol() {return maxScreenCol;}

    public static int getMaxScreenRow() {return maxScreenRow;}

    public void startGameThread() {
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
                    e.update(player, debugMode);

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
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.black);
        drawMap(g2, map);
        g2.fill(bottomBar);

        if (debugMode && pathfinder.pathList != null) {
            g2.setColor(new Color(255, 165, 0, 120)); // semi-transparent green
            int tileSize = getTileSize();
            for (ai.Node n : pathfinder.pathList) {
                int x = n.col * tileSize;
                int y = n.row * tileSize;
                g2.fillRect(x, y, tileSize, tileSize);
            }
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

    public void drawMap(Graphics2D g2, int[][] map) {

        int tileSize = GamePanel.getTileSize();

        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {

                int tileNum = map[row][col];
                Tiles tile = tiles[tileNum];
                g2.drawImage(tile.tileImg, col * tileSize, row * tileSize, tileSize, tileSize, null);
            }
        }
    }

}
