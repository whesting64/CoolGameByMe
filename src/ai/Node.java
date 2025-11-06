package ai;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Node extends JButton implements ActionListener {

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
    boolean checked;

    public Node(int col, int row) {
        this.col = col;
        this.row = row;

        setBackground(new Color(255, 255, 255));
        setForeground(Color.BLACK);
        addActionListener(this);

    }

    public void setAsStart() {
        start = true;
    }
    public void setAsGoal() {
        goal = true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        setBackground(new Color(255, 165, 0, 10));

    }
}