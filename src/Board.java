import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Board extends JPanel implements KeyListener, ActionListener, Runnable {
    private final Player player;
    private final List<Alien> aliens;
    private List<Bullet> bullets;
    private int score;
    private int lives;
    private boolean gameOver;
    private final JLabel livesLabel;
    private final JLabel scoreLabel;
    int alienGenTemp = 0;
    private boolean gameWon = false;
    private JButton startButton;
    private boolean gameStarted = false;
    private Image backgroundImage;
    private JButton restartButton;
    private JPanel topLeftPanel;
    private Thread gameThread;
    private boolean paused = false;
    private final Object pauseLock = new Object();
    private String nick;
    private JTextField textField;
    private JLabel printNick;
    private JLabel setNameLabel;

    public Board(SpaceInvaders frame) {
        setFocusable(true);
        addKeyListener(this);
        setBackground(Color.BLACK);
        setLayout(new BorderLayout());

        player = new Player("image/player.png");
        aliens = new ArrayList<>();
        bullets = new ArrayList<>();
        score = 0;
        lives = 3;

        JMenuBar menuBar = new JMenuBar();
        livesLabel = new JLabel();
        scoreLabel = new JLabel();
        printNick = new JLabel();
        printNick.setVisible(false);
        livesLabel.setVisible(false);
        scoreLabel.setVisible(false);


        JMenu shipMenu = new JMenu("Change Ship");
        menuBar.add(shipMenu);

        JMenuItem shipStyle1 = new JMenuItem("Red ship");
        shipStyle1.addActionListener(e -> changeShipStyle("/image/player.png"));
        shipMenu.add(shipStyle1);

        JMenuItem shipStyle2 = new JMenuItem("Green ship");
        shipStyle2.addActionListener(e -> changeShipStyle("/image/player2.png"));
        shipMenu.add(shipStyle2);

        JMenuItem shipStyle3 = new JMenuItem("Orange ship");
        shipStyle3.addActionListener(e -> changeShipStyle("/image/player3.png"));
        shipMenu.add(shipStyle3);

        JMenu helpMenu = new JMenu("Help");
        menuBar.add(helpMenu);

        JMenuItem rules = new JMenuItem("Rules");
        rules.addActionListener(e -> Rules());
        helpMenu.add(rules);


        backgroundImage = new ImageIcon(getClass().getResource("/image/background.jpg")).getImage();


        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(printNick);
        menuBar.add(livesLabel);
        menuBar.add(scoreLabel);
        frame.setJMenuBar(menuBar);
        generateAliens();



        topLeftPanel = new JPanel();
     //   topLeftPanel.add(Box.createHorizontalGlue());
        topLeftPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        startButton = new JButton("Start Game");
   //     startButton.setBounds(350, 400, 100, 50);
        startButton.addActionListener(e -> startGame());
        topLeftPanel.add(startButton);


        textField = new JTextField();
        setNameLabel =  new JLabel("Set your Name: ");
        topLeftPanel.add(setNameLabel);
        textField.setPreferredSize(new Dimension(150, 20));
        topLeftPanel.add(textField);




        restartButton = new JButton("Restart Game");
    //    restartButton.setBounds(350, 500, 100, 50);
        restartButton.addActionListener(e -> restartGame());
        restartButton.setVisible(false);
        topLeftPanel.add(restartButton);


        add(topLeftPanel, BorderLayout.SOUTH);

    }
    private void Rules() {
        JFrame termsFrame = new JFrame("Rules");
        termsFrame.setSize(800, 600);
        termsFrame.setTitle("Space Invaders Rules");
        termsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JEditorPane termsPane = new JEditorPane();
        termsPane.setContentType("text/html");
        termsPane.setText(
                        "<html>" +
                        "<h2>Rules of the Space Invaders game:</h2>" +
                        "<h3>Game Goal:</h3>" +
                        "<p>The goal of the game is to shoot down all the aliens before they reach the main part of the screen.</p>" +
                        "<h3>Control:</h3>" +
                        "<p>Left Arrow key (&larr;): Move the ship to the left<br>" +
                        "Right Arrow key (&rarr;): Move the ship to the right<br>" +
                        "Spacebar: shoot</p>" +
                        "<h3>Rules:</h3>" +
                        "<h4>Starting the Game:</h4>" +
                        "<p>To start the game, click the \"Start Game\" button.</p>" +
                        "<h4>Movement:</h4>" +
                        "<p>Control your spaceship with the left and right arrow keys.</p>" +
                        "<h4>Shooting:</h4>" +
                        "<p>Press SPACEBAR to fire the missile towards the aliens.</p>" +
                        "<h4>Aliens:</h4>" +
                        "<p>The aliens move from one side of the screen to the other, gradually approaching the bottom of the screen.<br>" +
                        "When the aliens reach the edge of the screen, they change direction and move down.</p>" +
                        "<h4>Points:</h4>" +
                        "<p>Each alien you shoot down adds additional points to your score.<br>" +
                        "The result is displayed in the top menu of the game.</p>" +
                        "<h4>Life:</h4>" +
                        "<p>The player starts the game with three lives.<br>" +
                        "If the alien reaches the bottom of the screen or the player is hit by the alien's shot, they lose one life.<br>" +
                        "The number of lives remaining is displayed in the top menu of the game.</p>" +
                        "<h4>End of the game:</h4>" +
                        "<p>The game ends when the player loses all lives (\"GAME OVER\" message) or shoots down all aliens (\"You Won\" message).<br>" +
                        "When you finish the game, you can restart the game by clicking the \"Restart Game\" button.</p>" +
                        "<h4>Ship Change:</h4>" +
                        "<p>In the \"Change Ship\" menu you can choose the appearance of your spaceship.</p>" +
                        "</html>"
        );
        termsPane.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(termsPane);

        termsFrame.add(scrollPane);
        termsFrame.pack();
        termsFrame.setLocationRelativeTo(null);
        termsFrame.setVisible(true);
    }

    private void startGame() {
        nick = textField.getText().trim();
        if (nick.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your Name before starting.");
            return;
        }
        printNick.setText("Player: " + nick + "  ");
        printNick.setVisible(true);
        textField.setVisible(false);
        setNameLabel.setVisible(false);

        gameStarted = true;
        startButton.setVisible(false);
        topLeftPanel.setVisible(false);
        livesLabel.setVisible(true);
        scoreLabel.setVisible(true);
        textField.setVisible(false);
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        while (gameStarted && !gameOver) {

            try {
                Thread.sleep(20);
                if (!paused) {
                    player.move();
                    moveAliens();
                    moveBullets();
                    checkCollisions();
                    checkGameOver();
                    repaint();
                }
            } catch (InterruptedException e) {
                System.out.println("Game interrupted");
            }
        }
    }

    private void checkGameOver() {
        for (Alien alien : aliens) {
            if (alien.getY() + alien.getImage().getHeight(null) >= player.getY()) {
                gameOver = true;
            }
        }
    }
    private void restartGame(){
        gameOver = false;
        gameWon = false;
        gameStarted = false;
        startButton.setVisible(true);
        restartButton.setVisible(false);
        player.setX(400);
        player.setY(650);
        score = 0;
        lives = 3;
        updateLives(lives);
        updateScore(score);
        aliens.clear();
        bullets.clear();
        alienGenTemp = 0;
        generateAliens();
        livesLabel.setVisible(false);
        scoreLabel.setVisible(false);
        printNick.setVisible(false);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

        if (!gameStarted) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 48));
            g.drawString("Space Invaders", getWidth() / 2 - 150, getHeight() / 2 - 50);
            g.setFont(new Font("Arial", Font.PLAIN, 16));
            g.drawString("Press 'Start Game' to begin", getWidth() / 2 - 150, getHeight() / 2);
        } else {
            if (!gameWon) {
                if (!gameOver) {
                    g.drawImage(player.getImage(), player.getX(), player.getY(), this);

                    for (Alien alien : aliens) {
                        g.drawImage(alien.getImage(), alien.getX(), alien.getY(), this);
                    }

                    for (Bullet bullet : bullets) {
                        g.drawImage(bullet.getImage(), bullet.getX(), bullet.getY(), this);
                    }
                    if (paused) {
                        g.setColor(Color.YELLOW);
                        g.setFont(new Font("Arial", Font.BOLD, 48));
                        g.drawString("PAUSED", getWidth() / 2 - 100, getHeight() / 2);
                    }
                } else {
                    g.setColor(Color.RED);
                    g.setFont(new Font("Arial", Font.BOLD, 48));
                    g.drawString("GAME OVER", getWidth() / 2 - 150, getHeight() / 2);
                    restartButton.setVisible(true);
                    topLeftPanel.setVisible(true);
                }
            } else if (gameWon) {
                g.setColor(Color.GREEN);
                g.setFont(new Font("Arial", Font.BOLD, 48));
                g.drawString("GG", getWidth() / 2 - 150, getHeight() / 2);
                g.drawString("You Won", getWidth() / 2 - 150, getHeight() / 2 - 50);
                restartButton.setVisible(true);
                topLeftPanel.setVisible(true);
            }
        }

        Toolkit.getDefaultToolkit().sync();


        Toolkit.getDefaultToolkit().sync();
    }
    public void changeShipStyle(String imagePath) {
        player.setImage(imagePath);
        repaint();

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            player.setDx(-10);
        } else if (key == KeyEvent.VK_RIGHT) {
            player.setDx(10);
        } else if (key == KeyEvent.VK_SPACE) {
            fireBullet();
        } else if (key == KeyEvent.VK_P) {
            pause();

        }
    }

    public void  pause(){
        paused = !paused;

        if (paused){
            synchronized (pauseLock){
                gameThread.interrupt();
            }
        } else {
            synchronized (pauseLock){
                pauseLock.notifyAll();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
            player.setDx(0);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    private void generateAliens() {
        Random random = new Random();
        if (alienGenTemp < AlienRespawnFormation.formations.length) {
            int[][] formation = AlienRespawnFormation.formations[alienGenTemp];

            alienGenTemp++;
            int startX = 50;
            int startY = 30;
            int alienWidth = 80;
            int alienHeight = 80;

            for (int row = 0; row < formation.length; row++) {
                for (int col = 0; col < formation[row].length; col++) {
                    if (formation[row][col] == 1) {
                        aliens.add(new Alien(startX + col * alienWidth, startY + row * alienHeight));
                    }
                }
            }
        } else {
            gameWon = true;
        }
    }

    public void moveAliens() {
        boolean shouldChangeDirection = false;

        for (Alien alien : aliens) {
            alien.move();
            if (alien.getX() <= 0 || alien.getX() + alien.getImage().getWidth(null) >= getWidth()) {
                shouldChangeDirection = true;
            }
        }

        if (shouldChangeDirection) {
            for (Alien alien : aliens) {
                alien.changeDirection();
                alien.moveDown();
            }
        }
    }

    public void checkCollisions() {
        List<Bullet> bulletsToRemove = new ArrayList<>();
        List<Alien> aliensToRemove = new ArrayList<>();

        for (Bullet bullet : bullets) {
            Rectangle bulletBounds = new Rectangle(bullet.getX(), bullet.getY(), bullet.getImage().getWidth(null), bullet.getImage().getHeight(null));

            for (Alien alien : aliens) {
                Rectangle alienBounds = new Rectangle(alien.getX(), alien.getY(), alien.getImage().getWidth(null), alien.getImage().getHeight(null));

                if (bulletBounds.intersects(alienBounds)) {
                    bulletsToRemove.add(bullet);
                    aliensToRemove.add(alien);
                    score += 1;
                    updateScore(score);
                }
            }
        }

        bullets.removeAll(bulletsToRemove);
        aliens.removeAll(aliensToRemove);

        if (aliens.isEmpty()) {
            respawnAliens();
        }
    }
    public void respawnAliens() {
        aliens.clear();
        generateAliens();
        for (Alien alien : aliens) {
            alien.setDx(alien.getDx() + 5);
            alien.setY(alien.getY() + 5);
        }
    }

    public void moveBullets() {
        List<Bullet> bulletsToRemove = new ArrayList<>();
        for (Bullet bullet : bullets) {
            bullet.move();
            if (bullet.getY() < 0) {
                bulletsToRemove.add(bullet);
            }
        }
        bullets.removeAll(bulletsToRemove);
    }

    public void fireBullet() {
        bullets.add(new Bullet(player.getX() + 15, player.getY()));
    }

    public void updateLives(int lives) {
        this.lives = lives;
        livesLabel.setText("Lives: " + lives + " ");
    }

    public void updateScore(int score) {
        this.score = score;
        scoreLabel.setText("Score: " + score + " ");
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
