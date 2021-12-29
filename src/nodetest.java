/**
 * @author Orust
 * @create 2021/8/21 15:54
 */
import java.util.Random;

public class nodetest {

    //		double r = radius*Math.sqrt(1.0 * random.nextDouble()%(radius*radius));
//		double seta = 2*Math.PI*random.nextDouble();
//		x=radius+r*Math.cos(seta);
//		y=radius+r*Math.sin(seta);
//		x = random.nextDouble()*2*radius;
//		double minY = radius - Math.sqrt(2*radius*x-(x*x));
//		double maxY = radius + Math.sqrt(2*radius*x-(x*x));
//		y = minY + random.nextDouble() * (maxY-minY + 1);

    // 矩阵长、宽、节点数
    public static void getMatrixSites(double length, double width, int count) {
        Random random = new Random();
        double x_center = length / 2.0;
        double y_center = width / 2.0;
        double x, y;
        for (int i = 0; i < count; i++) {
            x = x_center + (1.0 * random.nextDouble() - 0.5) * length;
            y = y_center + (1.0 * random.nextDouble() - 0.5) * width;
            System.out.printf("%.2f\t%.2f%n", x, y);
//            System.out.println(x + " " + y);
        }
    }

    public static void main(String[] args) {

        getMatrixSites(500, 500, 100);
    }
}
