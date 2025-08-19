package Entities;

import Engine.GamePanel;

import java.awt.*;


public abstract class Entity{

    // positons
    public int entityX;
    public int entityY;
    public int entitySpeed;
    public int viewRange;
    public int dx = 0, dy = 0;

    public Entity(int startX, int startY, int speed, int viewRange){

        this.entityX = startX;
        this.entityY = startY;
        this.entitySpeed = speed;
        this.viewRange = viewRange;

    }

    public void moveEntity(){

        if (dx != 0 && dy != 0) {
            dy = 0;
        }

        entityOnEntityCrime();

        Rectangle nextX = new Rectangle(entityX + dx, entityY, 48, 48);
        Rectangle nextY = new Rectangle(entityX, entityY + dy, 48, 48);



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
        Rectangle nextX = new Rectangle(entityX + dx, entityY, 48, 48);
        Rectangle nextY = new Rectangle(entityX, entityY + dy, 48, 48);

        for (Entity other : GamePanel.enemies) {
            if (other == this) continue;
            Rectangle otherHitBox = new Rectangle(other.entityX, other.entityY, 48, 48);

            if (nextX.intersects(otherHitBox)) dx = 0;
            if (nextY.intersects(otherHitBox)) dy = 0;
        }

        if (this instanceof Enemy) {
            Player player = GamePanel.player;
            Rectangle playerHitBox = new Rectangle(player.entityX, player.entityY, 48, 48);

            if (nextX.intersects(playerHitBox)) {dx = 0; dy = 0;}
            if (nextY.intersects(playerHitBox)) {dx = 0; dy = 0;}
        }
    }

    public void update(){}

    public abstract void update(Player player);

    public void draw(Graphics2D g2, boolean debugMode) {



    }


}
