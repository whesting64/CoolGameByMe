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

    public boolean collidesWith(Entity other){

        Rectangle myHitBox = new Rectangle(entityX, entityY, 48, 48);
        Rectangle otherHitBox = new Rectangle(other.entityX, other.entityY, 48, 48);
        return myHitBox.intersects(otherHitBox);

    }

    public void update(){}

    public abstract void update(Player player);

    public void draw(Graphics2D g2, boolean debugMode) {



    }


}
