import javax.swing.*;

public class SpaceInvaders extends JFrame {


    public SpaceInvaders() {
        JFrame frame = new JFrame("Space Invaders");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);




        Board board = new Board(this);
        add(board);
        setVisible(true);
    }


    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                () -> new SpaceInvaders()
        );
    }
}
