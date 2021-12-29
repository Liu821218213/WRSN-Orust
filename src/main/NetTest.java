package main;

import WRSNSystem.*;
import WRSNUtils.MyUtils;


import javax.swing.*;
import java.awt.*;
import java.util.List;

public class NetTest extends JPanel {
    public void paintComponent(Graphics g) { // 方法名不能改，覆盖了父类的paintComponent方法，重绘时会自动调用该方法
        super.paintComponent(g);

        try {
            NetSystem net = new NetSystem(Constants.NET_HEIGHT, Constants.NET_WIDTH, Constants.NODE_NUMBER);
            List<Node> nodelist = net.getNodeList();

            //绘制基本网络区域、基站以及所有节点
            new MyUtils().paintNet(g, nodelist);

            GpsrAlg gpsr = new GpsrAlg(nodelist);

            gpsr.getGeo();

            List<Node> nexthops = gpsr.getNextHops();
            for (int j = 0; j < nodelist.size(); j++) {
                g.setColor(Color.red); // 设置颜色
                ((Graphics2D) g).setStroke(new BasicStroke(1f));
                g.drawLine((int) nodelist.get(j).getX(), (int) nodelist.get(j).getY(), (int) nexthops.get(j).getX(), (int) nexthops.get(j).getY());
            }
        } catch (Exception e) {
            e.printStackTrace(); // 异常处理？
        }
    }

    public static void main(String[] args) throws Exception {
        JFrame f = new JFrame();
        f.setTitle("GPSR-DataRouter");

        f.setLayout(null);
        int width = Constants.WIDTH;
        int height = Constants.HEIGHT;
        f.setBounds(0, 0, width, height);
        f.getContentPane().setBackground(Constants.COLOR_GRASS_GREEN);

        NetTest nt = new NetTest();
        nt.setBounds(0, 0, width, height);
        nt.setBackground(Constants.COLOR_BEAN_GREEN);
        f.add(nt);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
}
