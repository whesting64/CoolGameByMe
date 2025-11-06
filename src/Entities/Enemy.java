package Entities;

import Engine.GamePanel;
import Engine.KeyHandler;
import ai.Pathfinding;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Enemy extends Entity {

    private BufferedImage enemyImage = null;
    private GamePanel gp;

    public Enemy(int startX, int startY, int speed, int viewRange, int lookDirection, int health, GamePanel gp) {
        super(startX, startY, speed, viewRange, lookDirection, health);
        this.gp = gp;
        try {
            enemyImage = ImageIO.read(new File("img/zombie.png"));
        } catch (IOException e) {
            System.err.println("Error loading zombie.png");
        }

    }

    @Override
    public void update(Player player, boolean debugMode) {
        double diffx = player.entityX - this.entityX;
        double diffy = player.entityY - this.entityY;
        dx = 0;
        dy = 0;
        double distance = Math.sqrt(diffx * diffx + diffy * diffy);
        if (distance <= this.viewRange + ((double) GamePanel.getTileSize() / 2)) {
            onPath = true;
            gp.pathfinder.setNodes(this.getEntityXGrid(), this.getEntityYGrid(), player.getEntityXGrid(), player.getEntityYGrid());

            if(gp.pathfinder.search()) {

            };

        }
        if (Math.abs(diffx) > Math.abs(diffy)) {
            lookDirection = (diffx < 0) ? 2 : 4; // Left or Right
        } else {
            lookDirection = (diffy < 0) ? 1 : 3; // Up or Down
        }

        if (!debugMode || KeyHandler.nextPressed) {
            moveEntity();
        }

        if (immunityTimer > 0) immunityTimer--;

    }

    @Override
    public void draw(Graphics2D g2, boolean debugMode) {

        g2.setColor(Color.black);
        g2.drawImage(enemyImage, entityX, entityY, GamePanel.getTileSize(), GamePanel.getTileSize(), null);
        if (health > 0) {
            g2.drawString(String.valueOf(health), entityX, entityY);
        }

        if (debugMode) {
            g2.setColor(Color.blue);
            g2.drawRect(entityX + dx, entityY, GamePanel.getTileSize(), GamePanel.getTileSize());
            g2.setColor(Color.red);
            g2.drawRect(entityX, entityY + dy, GamePanel.getTileSize(), GamePanel.getTileSize());
            g2.setColor(Color.green);
            g2.drawOval(entityX + (GamePanel.getTileSize() / 2) - viewRange, entityY + (GamePanel.getTileSize() / 2) - viewRange, viewRange * 2, viewRange * 2);
            g2.setColor(Color.cyan);
            if (lookDirection == 1) {
                g2.drawRect(entityX + (GamePanel.getTileSize() / 2), entityY + (GamePanel.getTileSize() / 2), 1, -GamePanel.getTileSize());
            } else if (lookDirection == 2) {
                g2.drawRect(entityX + (GamePanel.getTileSize() / 2), entityY + (GamePanel.getTileSize() / 2), -GamePanel.getTileSize(), 1);
            } else if (lookDirection == 3) {
                g2.drawRect(entityX + (GamePanel.getTileSize() / 2), entityY + (GamePanel.getTileSize() / 2), 1, GamePanel.getTileSize());
            } else if (lookDirection == 4) {
                g2.drawRect(entityX + (GamePanel.getTileSize() / 2), entityY + (GamePanel.getTileSize() / 2), GamePanel.getTileSize(), 1);

            }
        }
    }

}

