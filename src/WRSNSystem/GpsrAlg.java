package WRSNSystem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GpsrAlg {
    private List<Node> nodeList;
    private Node sinkNode;
    private List<Node>[] nodeNeighbors;
    private List<Node> nextHops;
    private List<Node>[] preHops;
    private double[][] edge;
    private int maxhop;

    public GpsrAlg(List<Node> nodeList) {
        this.nodeList = nodeList;  // ��ʼ������
        // ��վ��sink����idĬ��ΪnodeList.size()��λ��Ϊ��������
        sinkNode = new Node(nodeList.size(), Constants.NET_WIDTH / 2.0, Constants.NET_HEIGHT / 2.0);
        getNodeNeighbors();  // ��ȡ��ǰ�ڵ�ͨ�Ű뾶NODE_COMMUNICATION_RADIUS�ڵ������ھӽڵ�
        getRNG();  // ɾ��������gpsr�����ı�
    }

    @SuppressWarnings("unchecked")
    public void getNodeNeighbors() {
        nodeNeighbors = new List[nodeList.size()];
        edge = new double[nodeList.size()][nodeList.size()];
        for (int i = 0; i < nodeList.size(); i++) {
            nodeNeighbors[i] = new ArrayList<Node>();
            Node node_i = nodeList.get(i);
            int k = 0;
            for (int j = 0; j < nodeList.size(); j++) {
                Node node_j = nodeList.get(j);

                if (i != j && Math.pow(node_i.getX() - node_j.getX(), 2)
                        + Math.pow(node_i.getY() - node_j.getY(), 2) <= Math.pow(Constants.NODE_COMMUNICATION_RADIUS,
                        2)) {
                    nodeNeighbors[i].add(node_j);
                    edge[i][k] = Math.sqrt(Math.pow((node_i.getX() - node_j.getY()), 2)
                            + Math.pow((node_i.getY() - node_j.getY()), 2));
                    k = k + 1;
                }
            }

        }
    }

    public void getRNG() {
        for (Node u : nodeList) {
            int j = 0;// ��¼v���ھӽڵ��е�λ��
            for (Node v : nodeNeighbors[u.getId()]) {
                for (Node w : nodeNeighbors[u.getId()]) {
                    if (w.getId() != v.getId()) {
                        double Duv = Math.sqrt(Math.pow((u.getX() - v.getX()), 2) + Math.pow((u.getY() - v.getY()), 2));
                        double Duw = Math.sqrt(Math.pow((u.getX() - w.getX()), 2) + Math.pow((u.getY() - w.getY()), 2));
                        double Dvw = Math.sqrt(Math.pow((w.getX() - v.getX()), 2) + Math.pow((w.getY() - v.getY()), 2));
                        if (Duv > getMax(Duw, Dvw)) {
                            edge[u.getId()][j] = 0;// ɾ����
                        }

                    }
                }
                j = j + 1;
            }
        }
    }

    public double getMax(double a, double b) {
        if (a >= b)
            return a;
        else
            return b;
    }

    @SuppressWarnings("unchecked")
    public void getGeo() {
        // ��¼ÿ���ڵ��ǰһ������һ���ڵ�
        nextHops = new ArrayList<Node>();
        preHops = new List[nodeList.size() + 1];
        for (int i = 0; i < preHops.length; i++) {// initial
            preHops[i] = new ArrayList<Node>();
        }

        for (int i = 0; i < nodeList.size(); i++) {
            Node node = nodeList.get(i);
            boolean routerHoleFlag = true;
            double minDisToBase = node.getDistanceToBase();
            Node nextHopNode = null;
            if (minDisToBase > Constants.NODE_COMMUNICATION_RADIUS) {

                // ���ڵ�����ھӽڵ�
                if (nodeNeighbors[i].size() > 0) {

                    for (int j = 0; j < nodeNeighbors[i].size(); j++) {
                        Node neighborNode = nodeNeighbors[i].get(j);
//						if (edge[i][j] > 0) {
                        if (neighborNode.getDistanceToBase() < minDisToBase) {
                            minDisToBase = neighborNode.getDistanceToBase();
                            nextHopNode = neighborNode;
                            routerHoleFlag = false;
                        }
//						} else {
//							// ���ʲô����
//						}
                    }
                    // ����·�ɿն����������ֶ���
                    if (routerHoleFlag) {
                        double pdIn = Math.atan((sinkNode.getY() - node.getY()) / (sinkNode.getX() - node.getX()));
                        pdIn = calAngle(pdIn);
                        double sitaMin = 3 * Math.PI;
                        int k = 0;
                        for (Node neighbor : nodeNeighbors[i]) {
                            if (edge[i][k] > 0) {
                                double pdJ = Math.atan(
                                        (sinkNode.getY() - neighbor.getY()) / (sinkNode.getX() - neighbor.getX()));
                                pdJ = calAngle(pdJ);
                                double sitaB = pdJ - pdIn;
                                sitaB = calAngle(sitaB);
                                if (sitaB < sitaMin) {
                                    sitaMin = sitaB;
                                    nextHopNode = neighbor;
                                }
                            }
                            k = k + 1;
                        }
                        if (nextHopNode != null) {
                            nextHops.add(nextHopNode);
                            preHops[nextHopNode.getId()].add(node);
                            initHop(node);
                            if (node.getHop() <= nextHopNode.getHop())
                                nextHopNode.setHop(node.getHop() + 1);
                        } else {
                            System.out.println("������");
                        }

                    } else {// ����̰���㷨
                        nextHops.add(nextHopNode);
                        preHops[nextHopNode.getId()].add(node);
                        initHop(node);
                        if (node.getHop() >= nextHopNode.getHop())
                            nextHopNode.setHop(node.getHop() + 1);
                    }
                } else {
                    System.out.println("node" + node.getId() + "���ھӽڵ�");
                }
            } else {
                // ����ڵ����վ��һ�����룬����һ��Ϊ��վ
                nextHopNode = sinkNode;
                nextHops.add(nextHopNode);
                preHops[nextHopNode.getId()].add(node);
                initHop(node);
            }
            Node temp = nextHopNode;
            // System.out.println((node.getId() + 1) + "-:-" + (temp.getId() +
            // 1));
            // �������޸����к����ڵ������
            while (nextHops.size() > temp.getId() && nextHops.get(temp.getId()).getId() != nodeList.size()) {
                Node tempnext = nextHops.get(temp.getId());
                // System.out.println((temp.getId() + 1) + "->" +
                // (tempnext.getId() + 1));
                if (temp.getHop() >= tempnext.getHop())
                    tempnext.setHop(temp.getHop() + 1);
                temp = tempnext;
            }
        }
    }

    // ��ʼʱ��������Ĭ��Ϊ1
    private void initHop(Node node) {
        if (node.getHop() == 0)
            node.setHop(1);
    }

    // ��ȡ������������
    public int getMaxHops() {
        maxhop = 0;
        for (Node node : nodeList) {
            maxhop = Math.max(maxhop, node.getHop());
        }
        return maxhop;
    }

    // ���ֶ���
    private double calAngle(double angle) {
        if (angle < 0)
            angle += 2 * Math.PI;
        return angle;
    }

    // �ṩ���ⲿ����
    public List<Node> getNextHops() {
        return nextHops;
    }

    public List<Node>[] getpreHops() {
        return preHops;
    }
}
