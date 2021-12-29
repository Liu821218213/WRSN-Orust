package WRSNSystem;

import java.awt.*;

/**
 * @author Orust
 * @create 2021/8/16 12:47
 */
public class Constants {
    //网络
//    public static int NET_RADIUS = 250; // 原本是半径，网络为圆形
    public static int NET_WIDTH = 500;  // 网络宽
    public static int NET_HEIGHT = 500;  // 网络高（长）
    public static int WIDTH = 1000;
    public static int HEIGHT =1000;

    public static double DELTA_TIME = 1;
    final public static int ANIME_DELAY =1000;

    // 小车
    public static double VEHICLE_ENERGY =10000;  // 小车初始能量
    public static double VEHICLE_ENERGY_RATE = 5;  // 小车能耗
    public static double VEHICLE_TRAVEL_ENERGY_RATE = 1;  // 小车行驶能耗
    public static double VEHICLE_VELOCITY = 5;  // 小车速度

    // 节点
    public static double NODE_ENERGY = 150;  //  节点初始能量
    public static int NODE_NUMBER = 100;  // 节点数量
    public static double Node_COMMUNICATION_RADIUS = 150;  // 节点通信半径
    public static double NODE_DATA_RATE = 10;  // 节点数据产生率

    public static double NODE_ENERGY_FACTOR = 1E-2;
    public static double NODE_ENERGY_THRESHOLD = 0.2* NODE_ENERGY;
    //500
//	public static double DisWeight = 1.7171;
//	public static double  RateWeight= 36198.4587;

    //100
    public static double DisWeight = 1.0512;
    public static double  RateWeight=2940.71;

    //100
//	public static double DisWeight = 1;
//	public static double  RateWeight=5648.57631;

    public static double Beta1 =0.0000005;
    public static double Beta2 =0.0000000013;
    public static double p = 0.0000005;
    public static double Alpha = 4;

    public static int RANDOM_SEED = 100;
    public static int RANDOM_NODE_SEED = 100;
    public static int RANDOM_MWC_SEED = 10;
    public static long RANDOM_NODE_ENERGY_RATE = 1;


    final public static double CIRCLE_RADIUS = 3;  // 节点半径
    final public static double EXTEND_FACTOR = 3;

    final public static Color COLOR_GALAXY_WHITE = new Color(255, 255, 255);
    final public static Color COLOR_ALMOND_YELLOW = new Color(250, 249, 222);
    final public static Color COLOR_AKIBA_BROWN = new Color(255, 242, 226);
    final public static Color COLOR_ROUGE_RED = new Color(253, 230, 224);
    final public static Color COLOR_GRASS_GREEN = new Color(227, 237, 205);
    final public static Color COLOR_BEAN_GREEN = new Color(199, 237, 204);
    final public static Color COLOR_OCEAN_BLUE = new Color(220, 226, 241);
    final public static Color COLOR_HEADCLOTH_PURPLE = new Color(233, 235, 254);
    final public static Color COLOR_AURORA_GREY = new Color(234, 234, 239);

}
