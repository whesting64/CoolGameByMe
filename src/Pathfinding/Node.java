package Pathfinding;

import Engine.GamePanel;
import java.awt.*;

public class Node {

    public Node parent;
    public int col;
    public int row;
    public int fCost;
    public int gCost;
    public int hCost;
    boolean start;
    boolean goal;
    boolean solid;
    boolean open;
    boolean closed;

    public static Node[][] nodeGrid = new Node[GamePanel.getMaxScreenCol()][GamePanel.getMaxScreenRow()];
    Node startNode;
    Node endNode;
    Node currentNode;

    public Node(int col, int row) {
        this.col = col;
        this.row = row;
    }

    public void setAsStart() {
        start = true;
    }
    public void setAsGoal() {
        goal = true;
    }

    // Initialize the grid once
    public static void initializeGrid() {
        for (int col = 0; col < GamePanel.getMaxScreenCol(); col++) {
            for (int row = 0; row < GamePanel.getMaxScreenRow(); row++) {
                nodeGrid[col][row] = new Node(col, row);
            }
        }
    }

    public static void draw(Graphics g, int maxScreenCol, int maxScreenRow, boolean debugMode) {
        if (!debugMode) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.black);

        for (int col = 0; col < maxScreenCol; col++) {
            for (int row = 0; row < maxScreenRow; row++) {
                int x = col * GamePanel.getTileSize();
                int y = row * GamePanel.getTileSize();
                g2.drawRect(x, y, GamePanel.getTileSize(), GamePanel.getTileSize());
            }
        }
    }
}