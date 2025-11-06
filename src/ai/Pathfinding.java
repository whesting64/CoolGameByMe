package ai;

import Engine.GamePanel;

import java.util.ArrayList;

public class Pathfinding {

    GamePanel gp;
    Node [][] node;
    public ArrayList<Node> openList = new ArrayList<>();
    public ArrayList<Node> pathList = new ArrayList<>();
    Node startNode, goalNode, currentNode;
    boolean goalReached;
    int step = 0;

    public Pathfinding(GamePanel gamePanel) {
        this.gp = gamePanel;;
    }

    public void instantaiteNodes() {

        node = new Node[GamePanel.getMaxScreenCol()][GamePanel.getMaxScreenRow()];

        int col = 0;
        int row = 0;

        while(col < GamePanel.getMaxScreenCol() && row < GamePanel.getMaxScreenRow()) {

            node[col][row] = new Node(col, row);
            col++;
            if(col == GamePanel.getMaxScreenCol()) {
                col = 0;
                row++;
            }
        }
    }

    public void resetNodes() {

        int col = 0;
        int row = 0;

        while(col < GamePanel.getMaxScreenCol() && row < GamePanel.getMaxScreenRow()) {
            node[col][row].open = false;
            node[col][row].checked = false;
            node[col][row].solid = false;

            col++;
            if(col == GamePanel.getMaxScreenCol()) {
                col = 0;
                row++;
            }

        }

        openList.clear();
        pathList.clear();
        goalReached = false;
        step = 0;
    }

    public void setNodes(int startCol, int startRow, int goalCol, int goalRow) {

        resetNodes();

        startNode = node[startCol][startRow];
        currentNode = startNode;
        goalNode = node[goalCol][goalRow];
        openList.add(currentNode);

        int col = 0;
        int row = 0;

        while(col < GamePanel.getMaxScreenCol() && row < GamePanel.getMaxScreenRow()) {

            int tileNum = GamePanel.map[row][col];
            if(GamePanel.tiles[tileNum].collision) {
                node[col][row].solid = true;
            }

            getCost(node[col][row]);

            col++;
            if(col == GamePanel.getMaxScreenCol()) {
                col = 0;
                row++;
            }
        }
    }

    public void getCost(Node node) {

        int xDistence = Math.abs(node.col - startNode.col);
        int yDistence = Math.abs(node.row - startNode.row);
        node.gCost = xDistence + yDistence;

        xDistence = Math.abs(node.col - goalNode.col);
        yDistence = Math.abs(node.row - goalNode.row);
        node.hCost = xDistence + yDistence;

        node.fCost = node.gCost + node.hCost;
    }

    public boolean search() {

        while(!goalReached && step < 500) {

            int col = currentNode.col;
            int row = currentNode.row;

            currentNode.checked = true;
            openList.remove(currentNode);

            if (row - 1 >= 0) {
                openNode(node[col][row - 1]); // Up
            }
            if (col - 1 >= 0) {
                openNode(node[col - 1][row]); // Left
            }
            if (row + 1 < GamePanel.getMaxScreenRow()) {
                openNode(node[col][row + 1]); // Down
            }
            if (col + 1 < GamePanel.getMaxScreenCol()) {
                openNode(node[col + 1][row]); // Right
            }

            int bestNodeIndex = 0;
            int bestNodeFCost = 999;

            for(int i = 0; i < openList.size(); i++) {

                if(openList.get(i).fCost < bestNodeFCost) {
                    bestNodeIndex = i;
                    bestNodeFCost = openList.get(i).fCost;
                } else if(openList.get(i).fCost == bestNodeFCost) {
                    if(openList.get(i).gCost < openList.get(bestNodeIndex).gCost) {
                        bestNodeIndex = i;
                        bestNodeFCost = openList.get(i).fCost;
                    }
                }
            }
            if(openList.size() == 0) {
                break;
            }

            currentNode = openList.get(bestNodeIndex);

            if(currentNode == goalNode) {
                goalReached = true;
                trackThePath();
            }
            step++;
        }

        return goalReached;
    }

    public void openNode(Node node) {

        if (!node.open && !node.checked && !node.solid) {

            node.open = true;
            node.parent = currentNode;
            openList.add(node);
        }
    }

    public void trackThePath() {

        Node current = goalNode;

        while(current != startNode) {

            pathList.add(0,current);
            current  = current.parent;
        }
    }
}
