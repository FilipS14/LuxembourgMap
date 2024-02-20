import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MyPanel extends JPanel {
    private int nodeStart;
    private int nodeEnd;
    private Graph map;
    private boolean selection = false;

    public MyPanel() {
        JButton resetButton = new JButton("restMap");
        resetButton.addActionListener(e -> resetMap());
        resetButton.setBackground(Color.BLUE);
        resetButton.setForeground(Color.WHITE);
        add(resetButton);
        JButton button1 = new JButton("Dijkstra");
        button1.setBackground(Color.BLUE);
        button1.setForeground(Color.WHITE);
        add(button1);
        map = new Graph();
        repaint();

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (nodeStart != -1 && nodeEnd != -1) {
                    map.Dijkstra(nodeStart, nodeEnd);
                    repaint();
                }
            }
        });

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!selection) {
                    selection = true;
                    Point pointStart = e.getPoint();
                    nodeStart = Graph.findNodeByApproximateCoordinates(pointStart.x, pointStart.y);
                    System.out.println("A fost selectat prmiul nod");
                } else {
                    selection = false;
                    Point pointEnd = e.getPoint();
                    nodeEnd = Graph.findNodeByApproximateCoordinates(pointEnd.x, pointEnd.y);
                    System.out.println("A fost selectat al doilea nod");
                }
            }
        });
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Node node : Graph.nodeArray) {
            node.drawNode(g);
        }
        for (Edge edge : Graph.edgeArray) {
            edge.drawEdge(g);
        }
    }

    private void resetMap() {

        for (Edge edge : Graph.edgeArray) {
            edge.setColor(Color.BLACK);
        }
        repaint();
    }
}
