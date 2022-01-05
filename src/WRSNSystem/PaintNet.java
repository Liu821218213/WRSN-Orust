package WRSNSystem;

import WRSNUtils.MyUtils;
import main.TestPFCM;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Orust
 * @create 2022/1/5 10:02
 */
public class PaintNet extends JPanel {
    private NetSystem net;

    public PaintNet(NetSystem net) {
        this.net = net;
    }

    public void paintComponent(Graphics g) { // 方法名不能改，覆盖了父类的paintComponent方法，重绘时会自动调用该方法
        super.paintComponent(g);
        MyUtils myUtils = new MyUtils();

        try {
            List<Node> nodelist = net.getNodeList();
            List<Vehicle> vehicleList = net.getVehicleList();
            // 绘制基本网络区域、基站以及所有节点
            myUtils.paintNetCluster(g, nodelist);

            // 绘制小车行驶路径
//            for (Vehicle vehicle : vehicleList) {
//                Color color = myUtils.getColor(vehicle.getId());
//                myUtils.paintNetTravelPath(g, vehicle.getServeNodeList(), color);
//            }
            Vehicle vehicle = vehicleList.get(4);
            Color color = myUtils.getColor(vehicle.getId());
            myUtils.paintNetTravelPath(g, vehicle.getServeNodeList(), color);


        } catch (Exception e) {
            e.printStackTrace(); // 异常处理？
        }
    }
}
