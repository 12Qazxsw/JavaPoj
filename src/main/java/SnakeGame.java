import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    private final int UNIT_SIZE = 25; // 每个单元格大小
    private final int GAME_WIDTH = 600; // 游戏宽度
    private final int GAME_HEIGHT = 600; // 游戏高度
    private final int DELAY = 150; // 定时器延迟（毫秒）
    private final Random random = new Random();
    private Timer timer;
    private ArrayList<Point> snake = new ArrayList<>(); // 蛇的身体
    private Point food; // 食物位置
    private char direction = 'R'; // 蛇的初始方向（右）
    private boolean running = false; // 游戏是否运行
    private int score = 0; // 分数

    public SnakeGame() {
        this.setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(this);
        startGame();
    }

    private void startGame() {
        newSnake();
        spawnFood();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    private void newSnake() {
        snake.clear();
        snake.add(new Point(4 * UNIT_SIZE, 5 * UNIT_SIZE)); // 头部
        snake.add(new Point(3 * UNIT_SIZE, 5 * UNIT_SIZE)); // 身体
        snake.add(new Point(2 * UNIT_SIZE, 5 * UNIT_SIZE)); // 尾巴
    }

    private void spawnFood() {
        int x = random.nextInt((GAME_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        int y = random.nextInt((GAME_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
        food = new Point(x, y);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (running) {
            // 绘制食物
            g.setColor(Color.RED);
            g.fillRect(food.x, food.y, UNIT_SIZE, UNIT_SIZE);

            // 绘制蛇
            for (Point p : snake) {
                g.setColor(Color.GREEN);
                g.fillRect(p.x, p.y, UNIT_SIZE, UNIT_SIZE);
            }

            // 绘制分数
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + score, (GAME_WIDTH - metrics.stringWidth("Score: " + score)) / 2, g.getFont().getSize());
        } else {
            gameOver(g);
        }
    }

    private void gameOver(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        FontMetrics metrics = getFontMetrics(g.getFont());
        String message = "Game Over";
        g.drawString(message, (GAME_WIDTH - metrics.stringWidth(message)) / 2, GAME_HEIGHT / 2);
    }

    private void move() {
        Point head = snake.get(0); // 获取蛇头
        Point newHead = new Point(head);

        // 根据方向移动蛇头
        switch (direction) {
            case 'U' -> newHead.y -= UNIT_SIZE;
            case 'D' -> newHead.y += UNIT_SIZE;
            case 'L' -> newHead.x -= UNIT_SIZE;
            case 'R' -> newHead.x += UNIT_SIZE;
        }

        // 检测碰撞
        if (newHead.x < 0 || newHead.y < 0 || newHead.x >= GAME_WIDTH || newHead.y >= GAME_HEIGHT || snake.contains(newHead)) {
            running = false;
            timer.stop();
        } else {
            snake.add(0, newHead); // 添加新头部

            // 检测是否吃到食物
            if (newHead.equals(food)) {
                score++;
                spawnFood();
            } else {
                snake.remove(snake.size() - 1); // 移除尾部
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if ((key == KeyEvent.VK_UP || key == KeyEvent.VK_W) && direction != 'D') {
            direction = 'U';
        } else if ((key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) && direction != 'U') {
            direction = 'D';
        } else if ((key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) && direction != 'R') {
            direction = 'L';
        } else if ((key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) && direction != 'L') {
            direction = 'R';
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("贪吃蛇游戏");
        SnakeGame game = new SnakeGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}



