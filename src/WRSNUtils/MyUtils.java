package WRSNUtils;

import WRSNSystem.*;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * @author Orust
 * @create 2021/8/16 12:53
 */

public class MyUtils {
    public static double getEuclidDis(double x1, double y1, double x2, double y2) {
        double temp = Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2);
        return Math.sqrt(temp);
    }

    public void matlabDraw(String filename, StringBuilder sb) throws IOException {
        File file = new File(filename);
        Writer out = new FileWriter(file);
        out.write(sb.toString());
        out.close();
    }

    public void matlabDraw(String filename, StringBuilder sb, boolean flag) throws IOException {
        File file = new File(filename);
        Writer out = new FileWriter(file, flag);
        out.write(sb.toString());
        out.close();
    }

//    // 实现对象数组的深拷贝
//    public List<Node> deepClone(List<Node> nodeList, List<Node> cloneList) {
//        for (Node c : nodeList) {
//            cloneList.add((Node) c.clone());
//        }
//        return cloneList;
//    }
//
//    public List<Mwc> deepClone1(List<Mwc> mwcList, List<Mwc> cloneList) {
//        for (Mwc c : mwcList) {
//            cloneList.add((Mwc) c.clone());
//        }
//        return cloneList;
//    }
//
//    public double getSumDis(double[] a) {
//        double sum = 0;
//        for (int i = 0; i < a.length; i++)
//            sum += a[i];
//        return sum;
//    }
//
//    public double getMin(List<Node> nodeList) {
//        List<Node> cList = new ArrayList<Node>();
//        cList = deepClone(nodeList, cList);
//        cList.sort(Comparator.comparing(Node::getDistanceToBase));
//        double min = cList.get(0).getDistanceToBase();
//        return min;
//    }
//
//    public int getMax(List<Node> nodeList) {
//        List<Node> cList = new ArrayList<Node>();
//        cList = deepClone(nodeList, cList);
//        cList.sort(Comparator.comparing(Node::getHop));
//        int max = cList.get(cList.size() - 1).getHop();
//        return max;
//    }
//
//    public double getMax(List<Node> nodeList, Boolean flag) {
//        List<Node> cList = new ArrayList<Node>();
//        cList = deepClone(nodeList, cList);
//        if (flag) // true，升序
//            cList.sort(Comparator.comparing(Node::getLifeTime));
//        else// false,降序
//            cList.sort(Comparator.comparing(Node::getLifeTime).reversed());
//        double value = cList.get(cList.size() - 1).getLifeTime();
//        return value;
//    }
//
//    public Node getMax(List<Node> nodeList, int flag) {
//        List<Node> cList = new ArrayList<Node>();
//        cList = deepClone(nodeList, cList);
//        if (flag == 1) // true，升序
//            cList.sort(Comparator.comparing(Node::getLifeTime));
//        else if (flag == 0) // false,降序
//            cList.sort(Comparator.comparing(Node::getLifeTime).reversed());
//        Node node = cList.get(0);
//        return node;
//    }
//
//    public double getMinMax(List<Node> nodeList, int flag) {
//        List<Node> cList = new ArrayList<Node>();
//        cList = deepClone(nodeList, cList);
//        if (flag == 1) // true，升序
//            cList.sort(Comparator.comparing(Node::getEnergyRate));
//        else if (flag == 0) // false,降序
//            cList.sort(Comparator.comparing(Node::getEnergyRate).reversed());
//
//        return cList.get(0).getEnergyRate();//升序是最小值，降序是最大值
//    }
//
//    public double getMinMax1(List<Mwc> mwcList, int flag) {
//        List<Mwc> cList = new ArrayList<Mwc>();
//        cList = deepClone1(mwcList, cList);
//        if (flag == 1) // true，升序
//            cList.sort(Comparator.comparing(Mwc::getEmwc));
//        else if (flag == 0) // false,降序
//            cList.sort(Comparator.comparing(Mwc::getEmwc).reversed());
//
//        return cList.get(0).getEmwc();//升序是最小值，降序是最大值
//    }
//
//
//    public int doubleToInt(double n) {
//        int num;
//        // 获取迭代次数
//        if (n - (int) n == 0.0) {
//            num = (int) n;
//        } else {
//            num = (int) n + 1;
//        }
//        return num;
//    }

    public void paintNet(Graphics g, List<Node> nodeList) {
        // 监控区域
        g.setColor(Color.GREEN);
        int netWidth = Constants.NET_WIDTH;
        int netLength = Constants.NET_HEIGHT;
        g.drawRect(0, 0, netLength, netWidth);
        g.setColor(getColor(8));  // 网络区域
        g.fillRect(0, 0, netLength, netWidth);

        Toolkit tool = Toolkit.getDefaultToolkit();
        Image base = tool.getImage("src/images/base.png");
        g.drawImage(base, netWidth - 13, netLength - 19, 26, 26, null);

        // draw nodes
        int i = 1;
        g.setColor(Color.BLUE);
        for (Node node : nodeList) {
            int x = (int) (node.getX());
            int y = (int) (node.getY());
            int r = (int) (Constants.CIRCLE_RADIUS / 5 + 0.5);
            if (node.isDead()) {
                g.setColor(Color.red);
            }
            g.drawOval(x - r, y - r, 2 * r, 2 * r);
            g.setColor(Color.black);
            g.drawString(String.valueOf(i), x, y);
            g.fillOval(x - 2, y - 2, 4, 4);
            i++;
        }
    }

    // 设置颜色，标记各mwc的颜色以及轨迹
    public Color getColor(int i) {
        Color color = null;
        if (i == 0)
            color = Color.BLUE;
        else if (i == 1)
            color = Color.ORANGE;
        else if (i == 2)
            color = new Color(184, 134, 11);
        else if (i == 3)
            color = Color.MAGENTA;
        else if (i == 4)
            color = new Color(255, 127, 80);
        else if (i == 5)
            color = new Color(250, 128, 114);
        else if (i == 6)
            color = new Color(255, 225, 0);
        else if (i == 7)
            color = new Color(238, 130, 238);
        else if (i == 8)
            color = new Color(105, 105, 105);
        else if (i == 9)
            color = new Color(225, 215, 0);
        else if (i == 10)
            color = new Color(165, 42, 42);
        return color;
    }
}