import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Bullet {
    int x;
    int y;
    private final int dy;
    int speed;
    private final Image image;

    public Bullet(int x, int y) {
        this.x = x;
        this.y = y;
        this.dy = -10;

        ImageIcon shot = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("image/bullet.png")));
        image = shot.getImage();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void move() {
        this.y += dy;
    }
    public Image getImage() {
        return image;
    }
}
