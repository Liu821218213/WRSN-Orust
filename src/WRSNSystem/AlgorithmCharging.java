package WRSNSystem;

import WRSNUtils.MyCPPair;
import WRSNUtils.MyTimePair;
import WRSNUtils.MyUtils;

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

    Comparator<MyCPPair> myCPPairComparator = new Comparator<MyCPPair>() {
        @Override
        public int compare(MyCPPair o1, MyCPPair o2) {
            return Double.compare(o2.getCP(), o1.getCP());  // 按照CP从大到小排序
        }
    };

    Comparator<MyTimePair> myTimePairComparator = new Comparator<MyTimePair>() {
        @Override
        public int compare(MyTimePair o1, MyTimePair o2) {
            return Double.compare(o1.getTime(), o2.getTime());  // 按照time从小到大排序
        }
    };

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

    // PriorityQueue类实现了优先队列，默认是一个小根堆的形式，
    // 如果要定义大根堆，需要在初始化的时候加入一个自定义的比较器。
    // 小根堆是return o1 - o2，大根堆是return o2 - o1

    // Constants.CLUSTER_NUMBER为堆初始容量，myTimePairComparator为比较器
    PriorityQueue<MyTimePair> nextDeltaTimeMinHeap = new PriorityQueue<>(Constants.CLUSTER_NUMBER, myTimePairComparator);

    MyUtils myUtils = new MyUtils();


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
                node.setLifeEndTime(node.getLifeTime());  // 初始时，curTime为0，endTime就等于节点lifetime
                // 若公式(X1)成立，则节点i可以在生命期结束前被MCV充电
                boolean x1 = (node.getLifeEndTime() >= (node.getDistanceToBase() / Constants.VEHICLE_VELOCITY));
                // 若公式(X2)成立，则MCV为节点i充完电之后可以返回基站
                boolean x2 = (vehicle.getEnergy() >= Constants.VEHICLE_TRAVEL_ENERGY_RATE * ((node.getDistanceToBase() * 2) / Constants.VEHICLE_VELOCITY));
                if (x1 && x2) {
//                    bPow = vehicle.getUmk() / (vehicle.getUmk() - node.getUk().get(vehicle.getId()));
                    bPow = node.getUk().get(vehicle.getId()) / vehicle.getUmk();
                    qPow = 0;
                    double cp = Math.pow(Constants.CP_b, bPow) * 1;
                    vehicle.getChargingQueue().add(new MyCPPair(node, cp));  // 加入充电序列
                } else {
                    waitQueue.add(node);
                }
            }
            vehicle.getChargingQueue().sort(myCPPairComparator);  // TODO 这里也可以用堆实现，降低复杂度
            // 插入每个list的首个节点，确保无重复节点，即保证了不会多个车同时为一个节点充电
            for (int k = 0; k < nodeList.size(); k++) {
                Node node = vehicle.getChargingQueue().get(k).getNode();
                if (!curServingList.contains(node)) {  // TODO curServingList可用HashSet实现，降低复杂度
                    curServingList.add(node);

                    double moveDis = myUtils.getEuclidDis(vehicle.getX(), vehicle.getY(), node.getX(), node.getY());
                    double movePeriod = moveDis / Constants.VEHICLE_VELOCITY;
                    double moveEnergy = movePeriod * Constants.VEHICLE_TRAVEL_ENERGY_RATE;
                    double nodeLossEnergy = movePeriod * node.getEnergyRate();  // 初始时节点损耗为车移动的时间段内的损耗
                    double chargeEnergy = nodeLossEnergy / Constants.VEHICLE_CHARGING_EFFICIENCY;
                    double chargePeriod = chargeEnergy / Constants.VEHICLE_CHARGE_ENERGY_RATE;
                    nextDeltaTimeMinHeap.offer(new MyTimePair(vehicle, node, movePeriod + chargePeriod));
                    System.out.printf("move:%.2fs\t%.2fj\tcharge:%.2fs\t%.2fj\n", movePeriod, moveEnergy, chargePeriod, chargeEnergy);

                    break;
                }
            }

//            for (int i = 0; i < vehicle.getChargingQueue().size(); i++) {
//                System.out.printf("%d %d:\t%.4f\n", i, vehicle.getChargingQueue().
//                        get(i).getNode().getId(), vehicle.getChargingQueue().get(i).getCP());
//            }
//            System.out.println("----------------------");
        }
//        for (Node node : curServingList) {
//            System.out.printf("待充电节点%d:(%.2f, %.2f)\n", node.getId(), node.getX(), node.getY());
//        }

    }


    public void chargeNextNode() {
        MyTimePair pair = nextDeltaTimeMinHeap.poll();
        Vehicle vehicle = pair.getVehicle();
        Node node = pair.getNode();
        double curTime = pair.getTime();
        System.out.printf("车%d为节点%d充电:\tcurTime:%.2f\n", vehicle.getId(), node.getId(), curTime);
        double moveDis = myUtils.getEuclidDis(vehicle.getX(), vehicle.getY(), node.getX(), node.getY());
        double movePeriod = moveDis / Constants.VEHICLE_VELOCITY;
        double moveEnergy = movePeriod * Constants.VEHICLE_TRAVEL_ENERGY_RATE;

        double deltaTime = curTime - node.getLastEndChargeTime();
        double nodeLossEnergy = deltaTime * node.getEnergyRate();
        double chargeEnergy = nodeLossEnergy / Constants.VEHICLE_CHARGING_EFFICIENCY;

        node.setEnergy(Constants.NODE_ENERGY);  // 设定每次为节点充电都会充满电量
        node.setLastEndChargeTime(curTime);
        node.setLifeEndTime(curTime + node.getLifeTime());
        vehicle.setX(node.getX());
        vehicle.setY(node.getY());
        vehicle.setEnergy(vehicle.getEnergy() - moveEnergy - chargeEnergy);

        vehicle.getServeNodeList().add(node);
        updateQueue(vehicle, node, curTime);
    }

    private void updateQueue(Vehicle vehicle, Node haveChargedNode, double curTime) {
        vehicle.getChargingQueue().clear();  // 清空当前车的等待序列
        for (Node node : nodeList) {
            double dis = myUtils.getEuclidDis(vehicle.getX(), vehicle.getY(), node.getX(), node.getY());
            boolean x1 = (node.getLifeEndTime() >= (dis / Constants.VEHICLE_VELOCITY) + curTime);
            double deltaTime = curTime - node.getLastEndChargeTime();
            double nodeLossEnergy = deltaTime * node.getEnergyRate();
            double chargeEnergy = nodeLossEnergy / Constants.VEHICLE_CHARGING_EFFICIENCY;
            double chargePeriod = chargeEnergy / Constants.VEHICLE_CHARGE_ENERGY_RATE;
            boolean x2 = (vehicle.getEnergy() >= nodeLossEnergy / Constants.VEHICLE_CHARGING_EFFICIENCY +
                    Constants.VEHICLE_TRAVEL_ENERGY_RATE * ((dis + node.getDistanceToBase()) / Constants.VEHICLE_VELOCITY));

            if (x1 && x2) {
//            bPow = vehicle.getUmk() / (vehicle.getUmk() - node.getUk().get(vehicle.getId()));
                bPow = node.getUk().get(vehicle.getId()) / vehicle.getUmk();
                qPow = chargePeriod / (node.getLifeEndTime() - curTime + chargePeriod);
                double cp = Math.pow(Constants.CP_b, bPow) * Math.pow(Constants.CP_q, qPow);
//                cp = Constants.CP_b * bPow * Math.pow(Constants.CP_q, qPow);
                vehicle.getChargingQueue().add(new MyCPPair(node, cp));  // 加入充电序列
            } else {
                waitQueue.add(node);  // TODO 等待序列
            }
        }
        vehicle.getChargingQueue().sort(myCPPairComparator);
        for (MyCPPair myCPPair : vehicle.getChargingQueue()) {
            Node toBeChargedNode = myCPPair.getNode();
            if (!curServingList.contains(toBeChargedNode)) {  // 保证不会多个车同时为一个节点充电
                curServingList.remove(haveChargedNode);
                curServingList.add(toBeChargedNode);
                updateNextDeltaTimeMinHeap(vehicle, myCPPair.getNode(), curTime);
                break;
            }
        }
        // 当小车能量不够时，会出现nextDeltaTimeMinHeap.size()不等于curServingList.size()的情况
        System.out.println("************" + vehicle.getChargingQueue().size());
//        System.out.println("************" + curServingList.size());
//        System.out.println("****************" + nextDeltaTimeMinHeap.size());
    }

    public void updateNextDeltaTimeMinHeap(Vehicle vehicle, Node targetNode, double curTime) {
        // 假如在time时刻，为node充电，则可以计算以下信息
        // 小车到目标节点的移动距离、移动时长、移动花费能量
        double moveDis = myUtils.getEuclidDis(vehicle.getX(), vehicle.getY(), targetNode.getX(), targetNode.getY());
        double movePeriod = moveDis / Constants.VEHICLE_VELOCITY;
        double moveEnergy = movePeriod * Constants.VEHICLE_TRAVEL_ENERGY_RATE;
        // 当前节点上次充完电到当前时间的时间间隔，并更新lastEndChargeTime
        double deltaTime = curTime - targetNode.getLastEndChargeTime();
        // 在此期间节点的能量损耗、小车为节点充电花费的能量、此次充电花费时间
        double nodeLossEnergy = deltaTime * targetNode.getEnergyRate();
        double chargeEnergy = nodeLossEnergy / Constants.VEHICLE_CHARGING_EFFICIENCY;
        double chargePeriod = chargeEnergy / Constants.VEHICLE_CHARGE_ENERGY_RATE;
        nextDeltaTimeMinHeap.offer(new MyTimePair(vehicle, targetNode, curTime + movePeriod + chargePeriod));
        System.out.printf("move:%.2fs\t%.2fj\tcharge:%.2fs\t%.2fj\n", movePeriod, moveEnergy, chargePeriod, chargeEnergy);
    }

}
