import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Player {
    private int x, y, dx;
    private Image image;

    public Player(String imagePath) {
        setImage(imagePath);
        this.x = 400;
        this.y = 650;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public void move() {
        x += dx;
        if (x < 0) {
            x = 0;
        }
        if (x > 800 - image.getWidth(null)) {
            x = 800 - image.getWidth(null);
        }
    }

    public Image getImage() {
        return image;
    }

    public void setImage(String imagePath) {
        ImageIcon ship = new ImageIcon(getClass().getResource(imagePath));
        image = ship.getImage();
    }
}
