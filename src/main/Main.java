package main;

import WRSNSystem.*;

import javax.swing.*;
import java.io.IOException;

/**
 * @author Orust
 * @create 2021/12/30 11:08
 */
public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // 初始化网络
//        NetSystem net = new NetSystem(Constants.NET_WIDTH, Constants.NET_HEIGHT, Constants.NODE_NUMBER);
        NetSystem net = new NetSystem(500, 100, "src/datas/node500_100.txt");
//        net.buildDataRouter();  // 计算各节点能耗、生命时间
        net.buildDataRandom();  // 计算各节点能耗、生命时间
        net.executeChargeMethod();

//        for (Node node : net.getNodeList()) {
//            System.out.println("lifetime：" + node.getLifeTime() + "\t节点能耗：" + node.getEnergyRate());
//            for (int i = 0; i < node.getUk().size(); i++) {
//                System.out.print(i + ":" + node.getUk().get(i) + " ");
//            }
//            System.out.println( );
//        }

        for (Vehicle vehicle : net.getVehicleList()) {
            System.out.printf("%d:\t", vehicle.getId());
            for (Node node : vehicle.getServeNodeList()) {
                System.out.printf("%d\t", node.getId());
            }
            System.out.println();
        }

        paintNet(net);

    }

    public static void paintNet(NetSystem net) {
        JFrame f = new JFrame();
        f.setTitle("MyNet");
        f.setLayout(null);
        int width = Constants.WIDTH;
        int height = Constants.HEIGHT;
        f.setBounds(0, 0, width, height);
        PaintNet nt = new PaintNet(net); // 当前类对象
        nt.setBounds(0, 0, width, height);
        f.add(nt);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
}
