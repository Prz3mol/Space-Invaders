import javax.swing.*;
import java.awt.*;

public class Alien {
    private int x, y, dx;
    private Image image;

    public Alien(int x, int y) {
        this.x = x;
        this.y = y;
        this.dx = 5;
        ImageIcon alienimage = new ImageIcon(getClass().getClassLoader().getResource("image/Alienmid.png"));
        image = alienimage.getImage();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Image getImage() {
        return image;
    }

    public void move() {
        this.x += dx;
    }

    public void changeDirection() {
        this.dx *= -1;
    }

    public void moveDown() {
        this.y += 20;
    }

    public int getDx() {
        return dx;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }
}
