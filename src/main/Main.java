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
    public static void main(String[] args) throws IOException {
        NetSystem net = new NetSystem(Constants.NET_WIDTH, Constants.NET_HEIGHT, Constants.NODE_NUMBER);
        net.buildDataRouter();

        for (Node node : net.getNodeList()) {
            System.out.println(node.getLifeTime());
        }

    }

}
