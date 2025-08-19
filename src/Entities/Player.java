package Entities;

import Engine.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Player extends Entity {

    private BufferedImage playerImage = null;

    public Player(int startX, int startY, int speed, int viewRange) {
        super(startX, startY, speed , viewRange);
        try {
            playerImage = ImageIO.read(new File("img/coolFortniteGuy.png"));
        } catch (IOException e) {
            System.err.println("Error loading coolFortniteGuy.png");
        }
    }

    @Override
    public void update() {
        dx = 0;
        dy = 0;
        if (KeyHandler.leftPressed) {
            dx -= entitySpeed;
        }
        if (KeyHandler.rightPressed) {
            dx += entitySpeed;
        }
        if (KeyHandler.upPressed) {
            dy -= entitySpeed;
        }
        if (KeyHandler.downPressed) {
            dy += entitySpeed;
        }

        moveEntity();

    }

    @Override
    public void update(Player player) {

    }

    @Override
    public void draw(Graphics2D g2, boolean debugMode) {

        g2.setColor(Color.black);
        g2.drawImage(playerImage, entityX, entityY, null);

        if (debugMode) {
            g2.setColor(Color.blue);
            g2.drawRect(entityX + dx, entityY, 48, 48);
            g2.setColor(Color.red);
            g2.drawRect(entityX, entityY + dy, 48, 48);
        }

    }
}
