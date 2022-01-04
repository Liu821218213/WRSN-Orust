package WRSNUtils;

import WRSNSystem.Node;
import WRSNSystem.Vehicle;

/**
 * @author Orust
 * @create 2022/1/4 14:15
 */
public class MyTimePair {
    Vehicle vehicle;
    Node node;
    double time;

    public MyTimePair(Vehicle vehicle, Node node, double time) {
        this.vehicle = vehicle;
        this.node = node;
        this.time = time;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }
}
