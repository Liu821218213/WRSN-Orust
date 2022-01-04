package WRSNUtils;

import WRSNSystem.Node;

/**
 * @author Orust
 * @create 2022/1/3 21:03
 */
public class MyCPPair {
    Node node;
    double CP;

    public MyCPPair(Node node, double CP) {
        this.node = node;
        this.CP = CP;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public double getCP() {
        return CP;
    }

    public void setCP(double CP) {
        this.CP = CP;
    }
}
