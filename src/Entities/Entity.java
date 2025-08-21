package Entities;

import Engine.GamePanel;

import java.awt.*;

import static java.lang.Math.round;
import static java.lang.Math.sqrt;


public abstract class Entity{

    // positons
    public int entityX;
    public int entityY;
    public int entitySpeed;
    public int viewRange;
    public int lookDirection;
    public int dx = 0, dy = 0;

    public Entity(int startX, int startY, int speed, int viewRange, int lookDirection){

        this.entityX = startX;
        this.entityY = startY;
        this.entitySpeed = speed;
        this.viewRange = viewRange;
        this.lookDirection = lookDirection;

    }

    public void moveEntity(){

        entityOnEntityCrime();


        Rectangle nextX = new Rectangle(entityX + dx, entityY, GamePanel.getTileSize(), GamePanel.getTileSize());
        Rectangle nextY = new Rectangle(entityX, entityY + dy, GamePanel.getTileSize(), GamePanel.getTileSize());



        boolean collidedX = false;
        for (Rectangle platform : GamePanel.platforms) {
            if (nextX.intersects(platform)) {
                collidedX = true;
                break;
            }
        }
        if (!collidedX) {
            entityX += dx;
        }
        boolean collidedY = false;
        for (Rectangle platform : GamePanel.platforms) {
            if (nextY.intersects(platform)) {
                collidedY = true;
                break;
            }
        }
        if (!collidedY) {
            entityY += dy;
        }

    }


    public void entityOnEntityCrime() {
        Rectangle nextX = new Rectangle(entityX + dx, entityY, GamePanel.getTileSize(), GamePanel.getTileSize());
        Rectangle nextY = new Rectangle(entityX, entityY + dy, GamePanel.getTileSize(), GamePanel.getTileSize());

        for (Entity other : GamePanel.enemies) {
            if (other == this) continue;
            Rectangle otherHitBox = new Rectangle(other.entityX, other.entityY, GamePanel.getTileSize(), GamePanel.getTileSize());

            if (nextX.intersects(otherHitBox)) dx = 0;
            if (nextY.intersects(otherHitBox)) dy = 0;
        }

        if (this instanceof Enemy) {
            Player player = GamePanel.player;
            Rectangle playerHitBox = new Rectangle(player.entityX, player.entityY, GamePanel.getTileSize(), GamePanel.getTileSize());

            if (nextX.intersects(playerHitBox)) {dx = 0; dy = 0; player.getHit();}
            if (nextY.intersects(playerHitBox)) {dx = 0; dy = 0; player.getHit();}


        }
    }


    public void update(){}

    public abstract void update(Player player);

    public void draw(Graphics2D g2, boolean debugMode) {}

    // entity getters
    public int getEntityX() {
        return entityX;
    }
    public int getEntityY() {
        return entityY;
    }
    public int getEntitySpeed() {
        return entitySpeed;
    }
    public int getViewRange() {
        return viewRange;
    }

    // entity setters
    public void setEntityX(int entityX) {
        this.entityX = entityX;
    }
    public void setEntityY(int entityY) {
        this.entityY = entityY;
    }
    public void setEntitySpeed(int entitySpeed) {
        this.entitySpeed = entitySpeed;
    }
    public void setViewRange(int viewRange) {
        this.viewRange = viewRange;
    }
}
