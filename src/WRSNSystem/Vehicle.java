package WRSNSystem;

import WRSNUtils.MyQueue;

import java.util.List;

/**
 * @author Orust
 * @create 2021/12/28 19:12
 */
public class Vehicle {

    private int id;
    private double x;
    private double y;
    private double velocity; // 速度
    private double energy;
    private double chargeRate;
    private double travelEnergyRate;
    private double Emwc; // ?

    private boolean isMoving;
    private boolean istoBase;
    private boolean isCharging;
    private boolean isBreaking;
    private MyQueue<Vehicle> clusterTestQueue;
    private MyQueue<Node> nodeQueue;

    private List<Node> clusterNodeList;

    private double umk;  // 簇k的聚类中心节点相对于簇k的隶属度值

    public Vehicle(int id) {
        this.id = id;
        this.x = Constants.NET_WIDTH / 2.0;  // 小车初始位置设为基站
        this.y = Constants.NET_HEIGHT - Constants.NET_HEIGHT / 2.0;

        this.velocity = Constants.VEHICLE_VELOCITY;
        this.energy = Constants.VEHICLE_ENERGY;
        this.chargeRate = Constants.VEHICLE_ENERGY_RATE;
        this.travelEnergyRate = Constants.VEHICLE_TRAVEL_ENERGY_RATE;
        Emwc = 0;

        this.isMoving = false;
        this.istoBase = false;
        this.isCharging = false;
        this.isBreaking = true;

        this.clusterTestQueue = new MyQueue<>();
        this.nodeQueue = new MyQueue<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public double getEnergy() {
        return energy;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }

    public double getChargeRate() {
        return chargeRate;
    }

    public void setChargeRate(double chargeRate) {
        this.chargeRate = chargeRate;
    }

    public double getTravelEnergyRate() {
        return travelEnergyRate;
    }

    public void setTravelEnergyRate(double travelEnergyRate) {
        this.travelEnergyRate = travelEnergyRate;
    }

    public double getEmwc() {
        return Emwc;
    }

    public void setEmwc(double emwc) {
        Emwc = emwc;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    public boolean isIstoBase() {
        return istoBase;
    }

    public void setIstoBase(boolean istoBase) {
        this.istoBase = istoBase;
    }

    public boolean isCharging() {
        return isCharging;
    }

    public void setCharging(boolean charging) {
        isCharging = charging;
    }

    public boolean isBreaking() {
        return isBreaking;
    }

    public void setBreaking(boolean breaking) {
        isBreaking = breaking;
    }

    public MyQueue<Vehicle> getClusterTestQueue() {
        return clusterTestQueue;
    }

    public void setClusterTestQueue(MyQueue<Vehicle> clusterTestQueue) {
        this.clusterTestQueue = clusterTestQueue;
    }

    public MyQueue<Node> getNodeQueue() {
        return nodeQueue;
    }

    public void setNodeQueue(MyQueue<Node> nodeQueue) {
        this.nodeQueue = nodeQueue;
    }

    public List<Node> getClusterNodeList() {
        return clusterNodeList;
    }

    public void setClusterNodeList(List<Node> clusterNodeList) {
        this.clusterNodeList = clusterNodeList;
    }

    public double getUmk() {
        return umk;
    }

    public void setUmk(double umk) {
        this.umk = umk;
    }
}
