package WRSNSystem;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * @author Orust
 * @create 2021/12/31 11:50
 */
public class AlgorithmKMeans {

    private List<Node> nodeList;// 节点链表
    private int clusterNum; // 簇（小车）数量
    private List<Vehicle> vehicleList;// 充电车链表
//    private List<Node>[] clusterNodeList;// 聚类链表

    private ArrayList<double[]> dataSet; // 数据集链表
    private ArrayList<double[]> center; // 中心点链表
    private ArrayList<ArrayList<double[]>> cluster; // 聚类链表
    private int m; // 迭代次数
    private ArrayList<Double> deviation; // 每次迭代的误差链表

    public AlgorithmKMeans(List<Node> nodeList, int clusterNum) {
        this.nodeList = nodeList;
        this.clusterNum = clusterNum;
        this.vehicleList = new ArrayList<>();
        for (int i = 0; i < this.clusterNum; i++) {
            Vehicle vehicle = new Vehicle(i);
            vehicleList.add(vehicle);
        }


        dataSet = new ArrayList<>();
        center = new ArrayList<>();
        cluster = new ArrayList<>();

        m = 0;
        deviation = new ArrayList<>();
    }

    // 随机初始化clusterNum个不同的簇中心，前提是数据集中不存在相同位置的点
    private ArrayList<double[]> initCenter() {
        int[] randoms = new int[clusterNum];
        boolean flag;
        Random random = new Random();
        int temp = random.nextInt(nodeList.size());
        for (int i = 0; i < clusterNum; i++) {
            flag = true;
            while (flag) {
                temp = random.nextInt(nodeList.size());
                int j = 0;
                while (j < i) {
                    if (temp == randoms[j])  // 防止找到数据集中的同一个点
                        break;
                    j++;
                }
                if (j == i)
                    flag = false;
            }
            randoms[i] = temp;
        }

        ArrayList<double[]> center = new ArrayList<>();
        for (int i = 0; i < clusterNum; i++) {
            center.add(dataSet.get(randoms[i]));  // 生成初始化中心链表
        }
        return center;

    }


    // 初始化空的聚类链表
    private ArrayList<ArrayList<double[]>> initCluster() {
        cluster.clear();
        for (int i = 0; i < clusterNum; i++) {
            cluster.add(new ArrayList<>());
        }
        return cluster;
    }

    private void init() { // 初始化
        if (dataSet == null || dataSet.size() == 0) {
            double[][] dataSetArray = new double[nodeList.size()][2];
            for (int i = 0; i < nodeList.size(); i++) {
                dataSetArray[i][0] = nodeList.get(i).getX();
                dataSetArray[i][1] = nodeList.get(i).getY();
                dataSet.add(dataSetArray[i]);
            }
        }
        center = initCenter();
        cluster = initCluster();
    }


    // 重新设置距离每个点最近的簇，更新各list
    private void setCluster() {
        double[] dist = new double[clusterNum];
        for (int i = 0; i < nodeList.size(); i++) {
            for (int j = 0; j < clusterNum; j++) {
                dist[j] = distance(dataSet.get(i), center.get(j));  // 存储到各簇的距离
            }
            cluster.get(minDistance(dist)).add(dataSet.get(i));
            nodeList.get(i).setClusterId(minDistance(dist));
        }
    }

    private void errorSquare() {  // 计算每次迭代后，所有点与其对应中心点的距离误差值
        double errorValue = 0;
        for (int i = 0; i < clusterNum; i++) {
            for (int j = 0; j < cluster.get(i).size(); j++) {
                errorValue += distance(cluster.get(i).get(j), center.get(i));
            }
        }
        deviation.add(errorValue);
    }

    // 更新中心点
    private ArrayList<double[]> updateCenter() {
        for (int i = 0; i < clusterNum; i++) {
            double[] newCenter = new double[2];
            int n = cluster.get(i).size();
            if (n != 0) {
                for (int j = 0; j < n; j++) {
                    newCenter[0] += cluster.get(i).get(j)[0];
                    newCenter[1] += cluster.get(i).get(j)[1];
                }
                newCenter[0] = newCenter[0] / n;
                newCenter[1] = newCenter[1] / n;
                center.set(i, newCenter);
            }
        }
        return center;
    }

    public void kmeans() {
        init();
        while (true) {
            setCluster();
            errorSquare();  // 计算误差的平方
            if (m > 0 && m < 15) {
                double error = deviation.get(m) - deviation.get(m - 1);
//                System.out.println(error);
                if (error == 0) {
                    System.out.println("KMeans Iterate Count:" + m);
                    break;
                }
            }
            m++;
            center = updateCenter();
            cluster = initCluster();
        }
    }


    public ArrayList<ArrayList<double[]>> getCluster() { // 获取聚类链表
        return cluster;
    }

    public void printDataArray(ArrayList<double[]> dataArray, String dataArrayName) {
        for (int i = 0; i < dataArray.size(); i++) {
            System.out.println(
                    "print:" + dataArrayName + "[" + i + "]={" + dataArray.get(i)[0] + "," + dataArray.get(i)[1] + "}");
        }
        System.out.println("===================================");
    }

    private double distance(double[] point, double[] center) {
        double x = point[0] - center[0];
        double y = point[1] - center[1];
        return x * x + y * y;
    }

    private int minDistance(double[] distance) {
        double minDistance = distance[0];
        int minLocation = 0;
        for (int i = 1; i < clusterNum; i++) {
            if (minDistance > distance[i]) {
                minDistance = distance[i];
                minLocation = i;
            } else if (distance[i] == minDistance) {  // 如果相等，随机返回其中一个位置
                Random random = new Random();
                if (random.nextInt(10) < 5) {
                    minLocation = i;
                }
            }
        }
        return minLocation;
    }


    public static void main(String[] args) throws FileNotFoundException {
        List<Node> nodeList = new ArrayList<>();
        String filename = "src/datas/node.txt";
        Scanner sc = new Scanner(new File(filename));
        StringBuilder tmp = new StringBuilder();
        while (sc.hasNextLine()) {
            tmp.append(sc.nextLine().trim()).append(";");
        }
        sc.close();
        String[] arr = tmp.toString().split(";");

        // 初始化节点
        for (int i = 0; i < 100; i++) {
            String[] as = arr[i].split("\t");
            Node node = new Node(i, Double.parseDouble(as[0]), Double.parseDouble(as[1]), Constants.NODE_DATA_RATE);
            // 实时能量,初始时为最大值最大能量
            node.setEnergy(Constants.NODE_ENERGY);
//            node.setStartEnergy(Constants.NODE_ENERGY);
            nodeList.add(node);
        }

        AlgorithmKMeans algorithmKMeans = new AlgorithmKMeans(nodeList, Constants.CLUSTER_NUMBER);
        algorithmKMeans.kmeans();

    }
}
