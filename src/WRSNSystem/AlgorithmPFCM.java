package WRSNSystem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author Orust
 * @create 2021/12/31 16:08
 */
public class AlgorithmPFCM {

    private List<Node> nodeList;  // 节点链表
    private int clusterNum; // 簇（小车）数量
    private double[][] uik;
    private double[][] umk;  // 簇k的聚类中心节点相对于簇k的隶属度值

    private List<Cluster> clusterList; // 簇链表

    public AlgorithmPFCM(List<Node> nodeList, List<Cluster> clusterList) throws IOException {
        this.nodeList = nodeList;
        this.clusterList = clusterList;
        this.clusterNum = clusterList.size();

        this.uik = getUikFromPython();
    }

    private double[][] getUikFromPython() throws IOException {
        List<Node> nodeList = new ArrayList<>();
        String filename = "WRSN/PFCM/node_U_ik.txt";
        Scanner sc = new Scanner(new File(filename));
        StringBuilder tmp = new StringBuilder();
        while (sc.hasNextLine()) {
            tmp.append(sc.nextLine().trim()).append(";");
        }
        sc.close();
        String[] arr = tmp.toString().split(";");

        double[][] uij = new double[Constants.NODE_NUMBER][Constants.CLUSTER_NUMBER];
        // 初始化隶属度矩阵
        for (int i = 0; i < Constants.NODE_NUMBER; i++) {
            String[] as = arr[i].split("\t");
            for (int j = 0; j < Constants.CLUSTER_NUMBER; j++) {
                uij[i][j] = Double.parseDouble(as[j]);
            }
        }
        return uij;
    }

    public void PFCM() {
        for (int i = 0; i < nodeList.size(); i++) {
            int maxIdx = -1;
            double maxUik = 0;
            for (int j = 0; j < clusterNum; j++) {
                if (uik[i][j] >= maxUik) {
                    maxUik = uik[i][j];
                    maxIdx = j;
                }
            }
            nodeList.get(i).setClusterId(maxIdx);  // 设置节点所属簇
        }

        for (int i = 0; i < clusterNum; i++) {
            double maxUik = 0;
            for (int j = 0; j < nodeList.size(); j++) {
                maxUik = Math.max(maxUik, uik[j][i]);
            }
            clusterList.get(i).setUmk(maxUik);  // 设置簇的umk
//            System.out.println(clusterList.get(i).getUmk());
        }
    }

}
