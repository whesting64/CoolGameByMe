package Entities;

import Engine.GamePanel;

import java.awt.*;


public abstract class Entity {

    // entity stats
    public int entityX;
    public int entityY;
    public int entitySpeed;
    public int viewRange;
    public int lookDirection;
    public int dx = 0, dy = 0;
    public boolean onPath = false;
    public int health;
    int immunity = 120;
    int immunityTimer = 0;

    public Entity(int startX, int startY, int speed, int viewRange, int lookDirection, int health) {

        this.entityX = startX;
        this.entityY = startY;
        this.entitySpeed = speed;
        this.viewRange = viewRange;
        this.lookDirection = lookDirection;
        this.health = health;

    }

    public void moveEntity() {

        entityOnEntityCrime();

        int nextX = entityX + dx;
        int nextY = entityY + dy;

        int leftCol = entityX / GamePanel.getTileSize();
        int rightCol = (entityX + GamePanel.getTileSize() - 1) / GamePanel.getTileSize();
        int topRow = entityY / GamePanel.getTileSize();
        int bottomRow = (entityY + GamePanel.getTileSize() - 1) / GamePanel.getTileSize();
        int nextLeftCol = nextX / GamePanel.getTileSize();
        int nextRightCol = (nextX + GamePanel.getTileSize() - 1) / GamePanel.getTileSize();
        int nextTopRow = nextY / GamePanel.getTileSize();
        int nextBottomRow = (nextY + GamePanel.getTileSize() - 1) / GamePanel.getTileSize();

        if (isTileCollidable(topRow, nextLeftCol) ||
                isTileCollidable(topRow, nextRightCol) ||
                isTileCollidable(bottomRow, nextLeftCol) ||
                isTileCollidable(bottomRow, nextRightCol)) {
            dx = 0;
        }

        if (isTileCollidable(nextTopRow, leftCol) ||
                isTileCollidable(nextTopRow, rightCol) ||
                isTileCollidable(nextBottomRow, leftCol) ||
                isTileCollidable(nextBottomRow, rightCol)) {
            dy = 0;
        }


        boolean collidedX = false;


        if (!collidedX) {
            int newX = entityX + dx;
            if (newX < 0) newX = 0;
            if (newX + GamePanel.getTileSize() > GamePanel.getScreenWidth()) {
                newX = GamePanel.getScreenWidth() - GamePanel.getTileSize();
            }
            entityX = newX;
        }
        boolean collidedY = false;


        if (!collidedY) {
            int newY = entityY + dy;
            if (newY < 0) newY = 0;
            if (newY + (GamePanel.getTileSize()) > GamePanel.getScreenHeight() - GamePanel.getTileSize()) {
                newY = GamePanel.getScreenHeight() - GamePanel.getTileSize() * 2;
            }
            entityY = newY;
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

            if (nextX.intersects(playerHitBox)) {
                dx = 0;
                dy = 0;
                player.getHit();
            }
            if (nextY.intersects(playerHitBox)) {
                dx = 0;
                dy = 0;
                player.getHit();
            }


        }
    }

    private boolean isTileCollidable(int row, int col) {
        if (row < 0 || row >= GamePanel.map.length ||
                col < 0 || col >= GamePanel.map[0].length) {
            return true;
        }

        int tileIndex = GamePanel.map[row][col];
        return GamePanel.tiles[tileIndex].collision;
    }

    void getHit() {
        if (immunityTimer == 0) {
            immunityTimer = immunity;
            health -= 1;
        }
    }


    public void update() {
    }

    public abstract void update(Player player, boolean debugMode);

    public void draw(Graphics2D g2, boolean debugMode) {
    }

    // entity getters
    public int getEntityX() {
        return entityX;
    }

    public int getEntityY() {
        return entityY;
    }

    public int getEntityXGrid() {
        return entityX/GamePanel.getTileSize();
    }

    public int getEntityYGrid() {
        return entityY/GamePanel.getTileSize();
    }

    public int getEntitySpeed() {
        return entitySpeed;
    }

    public int getViewRange() {
        return viewRange;
    }

    public int getLookDirection() {
        return lookDirection;
    }

    public int getHealth() {
        return health;
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

    public void setLookDirection(int lookDirection) {
        this.lookDirection = lookDirection;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}
