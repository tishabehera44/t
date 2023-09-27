import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    private static final int WIDTH = 300;
    private static final int HEIGHT = 300;
    private static final int UNIT_SIZE = 10;
    private static final int DELAY = 100;

    private ArrayList<Point> snake;
    private Point food;
    private int direction;
    private boolean isMoving;
    private boolean gameOver;

    public SnakeGame() {
        snake = new ArrayList<>();
        addKeyListener(this);
        setBackground(Color.black);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        startGame();
    }

    public void startGame() {
        snake.clear();
        snake.add(new Point(5, 5));
        food = createFood();
        direction = KeyEvent.VK_RIGHT;
        isMoving = true;
        gameOver = false;
        Timer timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (!gameOver) {
            g.setColor(Color.red);
            g.fillRect(food.x * UNIT_SIZE, food.y * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);

            for (Point point : snake) {
                g.setColor(Color.green);
                g.fillRect(point.x * UNIT_SIZE, point.y * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
            }
        } else {
            gameOver(g);
        }
    }

    public void move() {
        Point head = snake.get(0);
        Point newHead = new Point(head);

        switch (direction) {
            case KeyEvent.VK_UP:
                newHead.y--;
                break;
            case KeyEvent.VK_DOWN:
                newHead.y++;
                break;
            case KeyEvent.VK_LEFT:
                newHead.x--;
                break;
            case KeyEvent.VK_RIGHT:
                newHead.x++;
                break;
        }

        if (newHead.equals(food)) {
            snake.add(0, newHead);
            food = createFood();
        } else {
            snake.add(0, newHead);
            snake.remove(snake.size() - 1);
        }
    }

    public Point createFood() {
        Random rand = new Random();
        int x, y;
        do {
            x = rand.nextInt(WIDTH / UNIT_SIZE);
            y = rand.nextInt(HEIGHT / UNIT_SIZE);
        } while (snake.contains(new Point(x, y)));
        return new Point(x, y);
    }

    public void gameOver(Graphics g) {
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        g.drawString("Game Over", WIDTH / 6, HEIGHT / 2);
        g.drawString("Score: " + (snake.size() - 1), WIDTH / 4, HEIGHT / 2 + 40);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isMoving && !gameOver) {
            move();
            checkCollision();
        }
        repaint();
    }

    public void checkCollision() {
        Point head = snake.get(0);
        if (head.x < 0 || head.x >= WIDTH / UNIT_SIZE || head.y < 0 || head.y >= HEIGHT / UNIT_SIZE) {
            gameOver = true;
            isMoving = false;
        }
        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                gameOver = true;
                isMoving = false;
                break;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_Q && gameOver) {
            startGame();
        } else if (key == KeyEvent.VK_UP && direction != KeyEvent.VK_DOWN) {
            direction = KeyEvent.VK_UP;
        } else if (key == KeyEvent.VK_DOWN && direction != KeyEvent.VK_UP) {
            direction = KeyEvent.VK_DOWN;
        } else if (key == KeyEvent.VK_LEFT && direction != KeyEvent.VK_RIGHT) {
            direction = KeyEvent.VK_LEFT;
        } else if (key == KeyEvent.VK_RIGHT && direction != KeyEvent.VK_LEFT) {
            direction = KeyEvent.VK_RIGHT;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame snakeGame = new SnakeGame();
        frame.add(snakeGame);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}