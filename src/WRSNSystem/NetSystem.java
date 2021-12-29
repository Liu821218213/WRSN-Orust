package WRSNSystem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
        String filename = "src/datas/node.txt";
        Scanner sc = new Scanner(new File(filename));
        StringBuilder tmp = new StringBuilder();
        while (sc.hasNextLine()) {
            tmp.append(sc.nextLine().trim()).append(";");
        }
        sc.close();
        String[] arr = tmp.toString().split(";");

        // 初始化节点
        for (int i = 0; i < nodeNum; i++) {
            String[] as = arr[i + 1].split("\t");
            Node node = new Node(i, Double.parseDouble(as[0]), Double.parseDouble(as[1]), Constants.NODE_DATA_RATE);
            // 实时能量,初始时为最大值最大能量
            node.setEnergy(Constants.NODE_ENERGY);
//            node.setStartEnergy(Constants.NODE_ENERGY);
            nodeList.add(node);
        }

    }

    public List<Node> getNodeList() {
        return nodeList;
    }
}
