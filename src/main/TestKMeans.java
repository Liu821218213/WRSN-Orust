package main;

import WRSNSystem.*;
import WRSNUtils.MyUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * @author Orust
 * @create 2021/12/31 13:56
 */
public class TestKMeans extends JPanel {

    public void paintComponent(Graphics g) { // 方法名不能改，覆盖了父类的paintComponent方法，重绘时会自动调用该方法
        super.paintComponent(g);

        try {
            // 初始化网络
            NetSystem net = new NetSystem(Constants.NET_WIDTH, Constants.NET_HEIGHT, Constants.NODE_NUMBER);
            List<Node> nodelist = net.getNodeList();
            // 执行算法
            AlgorithmKMeans kMeans = new AlgorithmKMeans(nodelist, Constants.CLUSTER_NUMBER);
            kMeans.kmeans();

            //绘制基本网络区域、基站以及所有节点
            new MyUtils().paintNetCluster(g, nodelist);

        } catch (Exception e) {
            e.printStackTrace(); // 异常处理？
        }
    }

    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setTitle("KMeans");

        f.setLayout(null);
        int width = Constants.WIDTH;
        int height = Constants.HEIGHT;
        f.setBounds(0, 0, width, height);
        f.getContentPane().setBackground(Constants.COLOR_GRASS_GREEN);

        TestKMeans nt = new TestKMeans(); // 当前类对象
        nt.setBounds(0, 0, width, height);
        nt.setBackground(Constants.COLOR_BEAN_GREEN);
        f.add(nt);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
}
