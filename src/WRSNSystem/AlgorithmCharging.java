package WRSNSystem;

import WRSNUtils.MyPair;

import java.util.*;

/**
 * @author Orust
 * @create 2022/1/3 12:31
 */
public class AlgorithmCharging {
    List<Node> nodeList;
    List<Vehicle> vehicleList;

    double curTime;
    Set<Node> waitQueue;  // 整个网络维护一个等待序列，共c种排序方式，c为簇的数量
    List<Node> curServingList;  // 当前被服务的节点（包括正在被充电和即将被充电的节点）

    private double bPow;
    private double qPow;

    Comparator<MyPair> comparator = new Comparator<MyPair>() {
        @Override
        public int compare(MyPair o1, MyPair o2) {
            return Double.compare(o2.getCP(), o1.getCP());
        }
    };


    public AlgorithmCharging(List<Node> nodeList, List<Vehicle> vehicleList) {
        this.nodeList = nodeList;
        this.vehicleList = vehicleList;
        this.curTime = 0;
        this.waitQueue = new HashSet<>();
        this.curServingList = new ArrayList<>(vehicleList.size());
    }

    public void initQueue() {
        for (Vehicle vehicle : vehicleList) {
            for (Node node : nodeList) {
                node.setEndTime(node.getLifeTime());  // 初始时，curTime为0，endTime就等于节点lifetime
                // 若公式(X1)成立，则节点i可以在生命期结束前被MCV充电
                boolean x1 = (node.getEndTime() >= (node.getDistanceToBase() / Constants.VEHICLE_VELOCITY));
                // 若公式(X2)成立，则MCV为节点i充完电之后可以返回基站
                boolean x2 = (vehicle.getEnergy() >= Constants.VEHICLE_TRAVEL_ENERGY_RATE *
                        ((node.getDistanceToBase() * 2) / Constants.VEHICLE_VELOCITY));
                if (x1 && x2) {
                    bPow = vehicle.getUmk() / (vehicle.getUmk() - node.getUk().get(vehicle.getId()));
                    double cp = Math.pow(Constants.CP_b, bPow) * 1;
                    vehicle.getChargingQueue().add(new MyPair(node, cp));  // 加入充电序列
                } else {
                    waitQueue.add(node);
                }
            }
            vehicle.getChargingQueue().sort(comparator);
            // 插入每个list的首个节点，确保无重复节点
            for (int k = 0; k < nodeList.size(); k++) {
                Node node = vehicle.getChargingQueue().get(k).getNode();
                if (!curServingList.contains(node)) {
                    curServingList.add(node);
                    break;
                }
            }

            for (int i = 0; i < vehicle.getChargingQueue().size(); i++) {
                System.out.printf("%d %d:\t%.4f\n", i, vehicle.getChargingQueue().
                        get(i).getNode().getId(), vehicle.getChargingQueue().get(i).getCP());
            }
            System.out.println("----------------------");
        }
        for (Node node : curServingList) {
            System.out.printf("待充电节点%d:(%.2f, %.2f)\n", node.getId(), node.getX(), node.getY());
        }

    }


    public void getNextNode() {

    }


    //匿名Comparator实现
//    public static Comparator<Node> comparator = (a, b) -> {
//        return b.getCP().get(0).compareTo(a.getCP().get(0));
//            if (a < b)
//                return 1;
//            else if (a > b)
//                return -1;
//            else
//                return 0;
//    };


}
