import java.awt.*;
import java.awt.geom.Line2D;

public class Edge implements Comparable{
    private final int idStart;
    private int idEnd;
    private final int cost;
    private Color color;

    public Edge(int idStart, int idEnd, int cost, Color color) {
        this.idStart = idStart;
        this.idEnd = idEnd;
        this.cost = cost;
        this.color = color;
    }

    public int getIdStart() {
        return idStart;
    }

    public int getIdEnd() {
        return idEnd;
    }

    public void setIdEnd(int idEnd) {
        this.idEnd = idEnd;
    }

    public int getCost() {
        return cost;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int compareTo(Object o){
        Edge other = (Edge) o;
        return Integer.compare(this.cost, other.cost);
    }

    public void drawEdge(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        float strokeWidth = (color == Color.black) ? 1.0f : 3.0f;
        g2d.setStroke(new BasicStroke(strokeWidth));

        g.setColor(color);
        g2d.draw(new Line2D.Float(
                Graph.nodeArray.get(idStart).getLatitude(),
                Graph.nodeArray.get(idStart).getLongitude(),
                Graph.nodeArray.get(idEnd).getLatitude(),
                Graph.nodeArray.get(idEnd).getLongitude())
        );
    }
}
