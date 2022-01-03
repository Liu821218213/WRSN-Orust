package main;

import WRSNSystem.Constants;
import WRSNSystem.NetSystem;
import WRSNSystem.Node;

import java.io.IOException;

/**
 * @author Orust
 * @create 2021/12/30 11:08
 */
public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // 初始化网络
        NetSystem net = new NetSystem(Constants.NET_WIDTH, Constants.NET_HEIGHT, Constants.NODE_NUMBER);
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

    }

}
