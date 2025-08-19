package Entities;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Enemy extends Entity {

    private BufferedImage enemyImage = null;

    public Enemy(int startX, int startY, int speed, int viewRange) {
        super(startX, startY, speed, viewRange);
        try {
            enemyImage = ImageIO.read(new File("img/zombie.png"));
        } catch (IOException e) {
            System.err.println("Error loading zombie.png");
        }

    }

    @Override
    public void update(Player player) {
        dx = 0;
        dy = 0;
        double diffx = player.entityX - this.entityX;
        double diffy = player.entityY - this.entityY;
        double distance = Math.sqrt(diffx * diffx + diffy * diffy);
        if (distance <= this.viewRange + 24) {
            if (player.entityX < this.entityX) {
                dx = -entitySpeed;
            } else if (player.entityX > this.entityX) {
                dx = entitySpeed;
            }

            if (player.entityY < this.entityY) {
                dy = -entitySpeed;
            }
            if (player.entityY > this.entityY) {
                dy = entitySpeed;
            }
        }

        moveEntity();

    }

    @Override
    public void draw(Graphics2D g2, boolean debugMode) {

        g2.setColor(Color.black);
        g2.drawImage(enemyImage, entityX, entityY, null);

        if (debugMode) {
            g2.setColor(Color.blue);
            g2.drawRect(entityX + dx, entityY, 48, 48);
            g2.setColor(Color.red);
            g2.drawRect(entityX, entityY + dy, 48, 48);
            g2.setColor(Color.green);
            g2.drawOval(entityX + 24 - viewRange, entityY + 24 - viewRange, viewRange * 2, viewRange * 2);
        }
    }

}

