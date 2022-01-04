package WRSNSystem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * @author Orust
 * @create 2021/12/28 19:07
 */
public class NetSystem {
    private int width;
    private int height;
    private int nodeNum;
    private List<Node> nodeList;
    private List<Vehicle> vehicleList;
    private int vehicleNum;

    private int n;
    private int m;

    public NetSystem(int width, int height, int nodeCnt) throws IOException {
        this.width = width;
        this.height = height;
        this.nodeNum = nodeCnt;
        nodeList = new ArrayList<>();
        String filename = "src/datas/node5.txt";
        Scanner sc = new Scanner(new File(filename));
        StringBuilder tmp = new StringBuilder();
        while (sc.hasNextLine()) {
            tmp.append(sc.nextLine().trim()).append(";");
        }
        sc.close();
        String[] arr = tmp.toString().split(";");

        // 初始化节点
        for (int i = 0; i < nodeNum; i++) {
            String[] as = arr[i].split("\t");
            Node node = new Node(i, Double.parseDouble(as[0]), Double.parseDouble(as[1]), Constants.NODE_DATA_RATE);
            // 实时能量,初始时为最大值最大能量
            node.setEnergy(Constants.NODE_ENERGY);
//            node.setStartEnergy(Constants.NODE_ENERGY);
            nodeList.add(node);
        }

        // 初始化充电车
        this.vehicleNum = Constants.CLUSTER_NUMBER;
        vehicleList = new ArrayList<>();
        for (int i = 0; i < this.vehicleNum; i++) {
            vehicleList.add(new Vehicle(i));
        }
    }

    // gpsr计算各节点能耗与总生命时间
    public void buildDataRouter() {
        // data router
        AlgorithmGpsr gpsr = new AlgorithmGpsr(nodeList);
        gpsr.getGeo();
        // next-hop node,previous-hop node
        List<Node> nexthops = gpsr.getNextHops();
        List<Node>[] prehops = gpsr.getpreHops();
        int maxHop = gpsr.getMaxHops();  // 当前网络最大转发跳数

        // calculate sendrate
        for (int i = 1; i <= maxHop; i++) {
            int j = 0;
            Random rnd = new Random(Constants.RANDOM_NODE_ENERGY_RATE);
            while (j < nodeList.size()) {
                if (nodeList.get(j).getHop() == i) {
                    Node node = nodeList.get(j);
                    double dis2 = Math.pow(node.getX() - nexthops.get(j).getX(), 2) +
                            Math.pow(node.getY() - nexthops.get(j).getY(), 2);
//                    double dis2 = 1800 + (rnd.nextDouble() * 2 - 1);
                    double c = Constants.Beta1 + Constants.Beta2 * Math.pow(dis2, 2);
                    double sendRate = node.getDataRate();
                    double receiveRate = 0;
                    if (i > 1) {
                        for (Node n : prehops[j]) {
                            receiveRate += n.getSendRate();
                        }
                        sendRate += receiveRate;
                        node.setSendRate(sendRate);
                        node.setEnergyRate(Constants.e2 * receiveRate + c * sendRate);
                    } else {
                        node.setSendRate(sendRate);
                        node.setEnergyRate(Constants.e2 * receiveRate + c * sendRate);
                    }
                    node.setLifeTime(Constants.NODE_ENERGY / node.getEnergyRate());
                }
                j++;
            }
        }
    }

    public void buildDataRandom() {
        Random rnd = new Random(1);
        for (Node node : nodeList) {
            double dis = 1800 + (rnd.nextDouble() * 2 - 1);
            double e0e1 = Constants.Beta1 + Constants.Beta2 * Math.pow(dis, 2);
            double sendRate = 60 + (rnd.nextDouble() * 2 - 1) * node.getDataRate() * 2;
            double receiveRate = 50 + (rnd.nextDouble() * 2 - 1) * node.getDataRate() * 2;
            node.setEnergyRate(e0e1 * sendRate + Constants.e2 * receiveRate);
            node.setLifeTime(Constants.NODE_ENERGY / node.getEnergyRate());
//            System.out.printf("c:%.10f 传输率:%f 感知率:%f %f %f\n", e0e1, sendRate, receiveRate, node.getEnergyRate(), node.getLifeTime());
        }
//        System.out.printf("c:%f 传输率:%f 感知率:%f\n", c, sendRate, receiveRate);
    }



    // 执行充电方法
    public void executeChargeMethod() throws ClassNotFoundException, IOException {
        AlgorithmPFCM pfcm = new AlgorithmPFCM(nodeList, vehicleList);
        pfcm.PFCM();  // 分簇，初始化各节点的隶属度属性

        AlgorithmCharging charging = new AlgorithmCharging(nodeList, vehicleList);
        charging.initQueue();
        for (int i = 0; i < 50; i++) {
            charging.chargeNextNode();
        }

    }


    public List<Node> getNodeList() {
        return nodeList;
    }

}
