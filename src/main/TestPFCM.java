package main;

import WRSNSystem.*;
import WRSNUtils.MyUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Orust
 * @create 2021/12/31 17:01
 */
public class TestPFCM extends JPanel{

    public void paintComponent(Graphics g) { // 方法名不能改，覆盖了父类的paintComponent方法，重绘时会自动调用该方法
        super.paintComponent(g);

        try {
            // 初始化网络
            NetSystem net = new NetSystem(Constants.NET_WIDTH, Constants.NET_HEIGHT, Constants.NODE_NUMBER);
            List<Node> nodelist = net.getNodeList();
            // 执行算法

            List<Vehicle> vehicleList = new ArrayList<>();
            for (int i = 0; i < Constants.CLUSTER_NUMBER; i++) {
                vehicleList.add(new Vehicle(i));
            }

            AlgorithmPFCM pfcm = new AlgorithmPFCM(nodelist, vehicleList);
            pfcm.PFCM();

            //绘制基本网络区域、基站以及所有节点
            new MyUtils().paintNetCluster(g, nodelist);

        } catch (Exception e) {
            e.printStackTrace(); // 异常处理？
        }
    }

    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setTitle("PFCM");

        f.setLayout(null);
        int width = Constants.WIDTH;
        int height = Constants.HEIGHT;
        f.setBounds(0, 0, width, height);
        f.getContentPane().setBackground(Constants.COLOR_GRASS_GREEN);

        TestPFCM nt = new TestPFCM(); // 当前类对象
        nt.setBounds(0, 0, width, height);
        nt.setBackground(Constants.COLOR_BEAN_GREEN);
        f.add(nt);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
}
