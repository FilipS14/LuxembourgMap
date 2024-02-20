import java.awt.*;

public class Node {
    private final int id;
    private int longitude;
    private int latitude;

    public Node(int id, int longitude, int latitude) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public int getId() {
        return id;
    }

    public int getLongitude() {
        return longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public int getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public void drawNode(Graphics g){
        final int raza = 1;
        g.setColor(Color.BLACK);
        g.fillOval(latitude - raza, longitude - raza, 2*raza, 2*raza);
    }
}
