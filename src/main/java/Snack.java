import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Snack {
    public static void main(String[] args) {
        // 创建一个 JFrame 窗口
        JFrame frame = new JFrame("贪吃蛇 示例");
        frame.setSize(500, 400); // 设置窗口大小
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 设置关闭操作

        frame.setVisible(true);

//        新建一个组件
        JPanel panel = new JPanel();

        panel.add(new JButton("按钮"));
        panel.add(new JLabel("标签"));








//        添加组件
        frame.add(panel);


    }
}
