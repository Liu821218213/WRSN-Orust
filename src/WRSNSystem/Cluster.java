package WRSNSystem;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Orust
 * @create 2021/12/29 14:42
 */
public class Cluster {
    private int id;
    private List<Node> nodeList;
    private List<Node> aliveList;

    public Cluster(int id) {
        this.id = id;
        this.aliveList = new ArrayList<Node>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Node> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<Node> nodeList) {
        this.nodeList = nodeList;
    }

    public List<Node> getAliveList() {
        return aliveList;
    }

    public void setAliveList(List<Node> aliveList) {
        this.aliveList = aliveList;
    }
}
