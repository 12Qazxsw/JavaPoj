import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import com.formdev.flatlaf.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    private final int UNIT_SIZE = 25; // 每个单元格大小
    private final int GAME_WIDTH = 900; // 游戏宽度
    private final int GAME_HEIGHT = 600; // 游戏高度
    private final int DELAY = 150; // 定时器延迟（毫秒）
    private final Random random = new Random(); // 用于随机食物的位置
    private Timer timer;
    private final ArrayList<Point> snake = new ArrayList<>(); // 蛇的身体
    private Point food; // 食物位置
    private char direction = 'R'; // 蛇的初始方向（右）
    private boolean running = false; // 游戏是否运行
    private int score = 0; // 分数

    public SnakeGame() {
//        创建游戏窗口
        this.setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
//        设置背景
        this.setBackground(Color.BLACK);
//        通过监听案件来实现控制蛇的移动
        this.setFocusable(true);
//        注册键盘事件的监听器 当有键盘事件发生时，SnakeGame 类中的 keyPressed, keyReleased, 和 keyTyped 方法将会被调用。
        this.addKeyListener(this);
        startGame();
    }

    private void startGame() {
        newSnake(); // 蛇的创建
        spawnFood(); // 食物创建
        running = true; // 运行标记
        timer = new Timer(DELAY, this);  // 创建一个定时器 到时间后this的监听器触发
        timer.start();
    }

//    对于蛇的创建
    private void newSnake() {
//        先清除蛇的元素
        snake.clear();
        snake.add(new Point(4 * UNIT_SIZE, 5 * UNIT_SIZE)); // 头部
        snake.add(new Point(3 * UNIT_SIZE, 5 * UNIT_SIZE)); // 身体
        snake.add(new Point(2 * UNIT_SIZE, 5 * UNIT_SIZE)); // 尾巴
    }

//    生成食物
    private void spawnFood() {
        int x = random.nextInt((GAME_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        int y = random.nextInt((GAME_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
        food = new Point(x, y);
    }

// 用于重绘组件的内容
//    比如食物与蛇
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (running) {
            // 绘制食物
            g.setColor(Color.RED); // 颜色
            g.fillRect(food.x, food.y, UNIT_SIZE, UNIT_SIZE); // 大小 一个单元格

            // 绘制蛇
            for (Point p : snake) {
                g.setColor(Color.GREEN);
                g.fillRect(p.x, p.y, UNIT_SIZE, UNIT_SIZE); // 蛇一段身体的大小
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

//    当死亡事件触发时 调用gameOver
    private void gameOver(Graphics g) {
        g.setColor(Color.RED);  // 添加主题 使窗口好看了一点
        g.setFont(new Font("Arial", Font.BOLD, 50));
        FontMetrics metrics = getFontMetrics(g.getFont());
        String message = "Game Over";
        g.drawString(message, (GAME_WIDTH - metrics.stringWidth(message)) / 2, GAME_HEIGHT / 2);
    }

    private void move() {
        Point head = snake.getFirst(); // 获取蛇头
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
            running = false;  // 死亡标记
            timer.stop();  // 计时停止
        } else {
            snake.addFirst(newHead); // 添加新头部

            // 检测是否吃到食物
            if (newHead.equals(food)) {
                score++;
                spawnFood();
            } else {
                snake.removeLast(); // 移除尾部
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
        }
        repaint();  // 若死亡 重绘画面
    }

//    监听并接受用户键盘输入
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
        FlatDarkLaf.setup();
        JFrame frame = new JFrame("贪吃蛇游戏");
//        使游戏窗口无法被改变
        frame.setResizable(false);
        SnakeGame game = new SnakeGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}



