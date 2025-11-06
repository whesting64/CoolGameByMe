package Entities;

import Engine.GamePanel;
import Engine.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Player extends Entity {

    int upHitTimer = 0;
    int downHitTimer = 0;
    int leftHitTimer = 0;
    int rightHitTimer = 0;
    final int hitTimer = 30;

    boolean upHit = false;
    boolean downHit = false;
    boolean leftHit = false;
    boolean rightHit = false;


    private BufferedImage playerImage = null;

    public Player(int startX, int startY, int speed, int viewRange, int lookDirection, int health) {
        super(startX, startY, speed, viewRange, lookDirection, health);
        try {
            playerImage = ImageIO.read(new File("img/Dwayne_The_Rock_Man_Johnson.png"));
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

        changeLookDirection();

        moveEntity();

        if (immunityTimer > 0) immunityTimer--;
    }

    private int primaryLookDirection = 0;

    void changeLookDirection() {

        if (primaryLookDirection == 0) {
            if (KeyHandler.upPressed) primaryLookDirection = 1;
            else if (KeyHandler.leftPressed) primaryLookDirection = 2;
            else if (KeyHandler.downPressed) primaryLookDirection = 3;
            else if (KeyHandler.rightPressed) primaryLookDirection = 4;
        }

        if (primaryLookDirection != 0) {
            lookDirection = primaryLookDirection;
        }

        if (primaryLookDirection == 1 && !KeyHandler.upPressed) primaryLookDirection = 0;
        if (primaryLookDirection == 2 && !KeyHandler.leftPressed) primaryLookDirection = 0;
        if (primaryLookDirection == 3 && !KeyHandler.downPressed) primaryLookDirection = 0;
        if (primaryLookDirection == 4 && !KeyHandler.rightPressed) primaryLookDirection = 0;
    }

    void swordSwing() {

        if (KeyHandler.upArrowPressed && !upHit) {
            upHitTimer = hitTimer;
            upHit = true;
            Rectangle hitBox = new Rectangle(entityX, entityY - GamePanel.getTileSize(), GamePanel.getTileSize(), GamePanel.getTileSize());
            for (Entity other : GamePanel.enemies) {
                if (other == this) continue;
                Rectangle otherHurtBox = new Rectangle(other.entityX, other.entityY, GamePanel.getTileSize(), GamePanel.getTileSize());

                if (hitBox.intersects(otherHurtBox)) {
                    other.getHit();
                }
            }
        }
        if (!KeyHandler.upArrowPressed) upHit = false;

        if (KeyHandler.downArrowPressed && !downHit) {
            downHitTimer = hitTimer;
            downHit = true;
            Rectangle hitBox = new Rectangle(entityX, entityY + GamePanel.getTileSize(), GamePanel.getTileSize(), GamePanel.getTileSize());
            for (Entity other : GamePanel.enemies) {
                if (other == this) continue;
                Rectangle otherHurtBox = new Rectangle(other.entityX, other.entityY, GamePanel.getTileSize(), GamePanel.getTileSize());

                if (hitBox.intersects(otherHurtBox)) {
                    other.getHit();
                }
            }
        }
        if (!KeyHandler.downArrowPressed) downHit = false;

        if (KeyHandler.leftArrowPressed && !leftHit) {
            leftHitTimer = hitTimer;
            leftHit = true;
            Rectangle hitBox = new Rectangle(entityX - GamePanel.getTileSize(), entityY, GamePanel.getTileSize(), GamePanel.getTileSize());
            for (Entity other : GamePanel.enemies) {
                if (other == this) continue;
                Rectangle otherHurtBox = new Rectangle(other.entityX, other.entityY, GamePanel.getTileSize(), GamePanel.getTileSize());

                if (hitBox.intersects(otherHurtBox)) {
                    other.getHit();
                }
            }
        }
        if (!KeyHandler.leftArrowPressed) leftHit = false;

        if (KeyHandler.rightArrowPressed && !rightHit) {
            rightHitTimer = hitTimer;
            rightHit = true;
            Rectangle hitBox = new Rectangle(entityX + GamePanel.getTileSize(), entityY, GamePanel.getTileSize(), GamePanel.getTileSize());
            for (Entity other : GamePanel.enemies) {
                if (other == this) continue;
                Rectangle otherHurtBox = new Rectangle(other.entityX, other.entityY, GamePanel.getTileSize(), GamePanel.getTileSize());

                if (hitBox.intersects(otherHurtBox)) {
                    other.getHit();
                }
            }
        }
        if (!KeyHandler.rightArrowPressed) rightHit = false;

        if (upHitTimer > 0) upHitTimer--;
        if (downHitTimer > 0) downHitTimer--;
        if (leftHitTimer > 0) leftHitTimer--;
        if (rightHitTimer > 0) rightHitTimer--;

    }

    @Override
    public void update(Player player, boolean debugMode) {

    }

    @Override
    public void draw(Graphics2D g2, boolean debugMode) {
        swordSwing();

        g2.setColor(Color.magenta);
        if (upHitTimer > 0) {
            g2.drawRect(entityX, entityY - GamePanel.getTileSize(), GamePanel.getTileSize(), GamePanel.getTileSize());
        } else if (leftHitTimer > 0) {
            g2.drawRect(entityX - GamePanel.getTileSize(), entityY, GamePanel.getTileSize(), GamePanel.getTileSize());
        } else if (downHitTimer > 0) {
            g2.drawRect(entityX, entityY + GamePanel.getTileSize(), GamePanel.getTileSize(), GamePanel.getTileSize());
        } else if (rightHitTimer > 0) {
            g2.drawRect(entityX + GamePanel.getTileSize(), entityY, GamePanel.getTileSize(), GamePanel.getTileSize());
        }
        g2.setColor(Color.black);
        g2.drawImage(playerImage, entityX, entityY, GamePanel.getTileSize(), GamePanel.getTileSize(), null);
        if (health > 0) {
            g2.drawString(String.valueOf(health), entityX, entityY);
        } else {
            g2.drawString("dead", entityX, entityY);
        }

        if (debugMode) {
            g2.setColor(Color.blue);
            g2.drawRect(entityX + dx, entityY, GamePanel.getTileSize(), GamePanel.getTileSize());
            g2.setColor(Color.red);
            g2.drawRect(entityX, entityY + dy, GamePanel.getTileSize(), GamePanel.getTileSize());
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
