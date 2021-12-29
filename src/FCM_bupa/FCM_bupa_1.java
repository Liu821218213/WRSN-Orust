package FCM_bupa;

import java.io.*;
import java.util.*;

/**
 * @author Orust
 * @create 2021/4/21 21:34
 */
// http://blog.sina.com.cn/s/blog_4c1c639101000bcc.html
// 数据集：https://archive.ics.uci.edu/ml/machine-learning-databases/liver-disorders/

public class FCM_bupa_1 {
    public static void main(String[] args) {
        //R0:初始分类矩阵,生成满足条件的随机数
        //n:样本数
        //m:特征值个数
        //c:分类数
        //e:精度
        //q:FCM指标

        //double x[][]={{0.9637,0.2536,0.7415},{0.4659,1,0.5815},{1,0.7893,1},{0.5437,0.9915,0},{0.3062,0.6861,0.2364}};
        double b[][] = {{1, 5, 8}, {5, 10, -3}};
        double[][] VV;
        double[][] RR;
        double[][] CR;
        int i, col, k;
        double[] ClassF;

        int N = 345;
        int M = 6;
        int C = 2;
        double E = 1e-3;
        double Q = 2;
        int D = 2;
        //************读取数据集
        double[][] x = new double[N][M];// 特征指标矩阵
        try {
            FileReader reader = new FileReader("src/FCM_bupa/bupa.data");
            BufferedReader br = new BufferedReader(reader);
            String Line;
            Line = br.readLine();
            System.out.println(Line);
            int row = 0;
            while (Line != null) {
                String[] num = Line.split(",");
                for (col = 0; col < M; col++) {
                    x[row][col] = Double.parseDouble(num[col]);
                }
                Line = br.readLine();//从文件中继续读取一行数据
                row++;
            }
            br.close();
            reader.close();
        } catch (Exception e) {
            System.out.println("读文件发生错误!" + e);
        }

        //************初始化初始分类矩阵R0
        double R0[][] = new double[C][N];
        for (i = 0; i < N; i++) {
            double sum = 0;
            for (col = 0; col < C; col++) {
                if (col == C - 1)
                    R0[col][i] = 1 - sum;
                else {
                    R0[col][i] = (1 - sum) * Math.random();
                    sum = sum + R0[col][i];
                }
            }
        }
        //打印R0[][];
        for (i = 0; i < N; i++) {
            for (col = 0; col < C; col++) {
                //System.out.print(R0[j][i]+" ");
            }
            //System.out.println();
        }

        //由构造函数创建类的实例
        double dis;
        double max;
        FCM_bupa_1 fcm;

        int times = 0;
        System.out.println("           ---------------FCM算法JAVA实现--------------");
        do {
            max = 0;
            VV = FCMCenter(R0, x, N, M, C, Q);//由特征指标矩阵X和初始分类矩阵R0计算聚类中心
            //System.out.println("迭代中的聚类中心");
            //fcm.display(VV);
            RR = modifyR(x, VV, N, M, C, D, Q);//由特征指标矩阵X和聚类中心V修正分类矩阵RR
            //System.out.println("迭代中的模糊分类矩阵");
            //fcm.display(RR);

            //计算RR和R0的精度差
            for (i = 0; i < C; i++)
                for (col = 0; col < N; col++) {
                    if (Math.abs(RR[i][col] - R0[i][col]) > max)
                        max = Math.abs(RR[i][col] - R0[i][col]);
                }
            if (max > E) {
                for (i = 0; i < C; i++)
                    for (col = 0; col < N; col++)
                        R0[i][col] = RR[i][col];
            }
            //max=0.00001;
            times++;
        } while (max > E);

        //输出迭代次数
        System.out.println(" ");
        System.out.println("迭代了  " + times + "   次");
//        //输出特征指标矩阵
//        System.out.println("特征指标矩阵--------------------X");
//        System.out.println();
//        display(x);
        //输出聚类中心
        System.out.println("聚类中心矩阵--------------------V");
        System.out.println();
        display(VV);
        //输出分类矩阵
        System.out.println("模糊分类矩阵--------------------R");
        System.out.println();
        display(RR);
        //输出确定性分类
        System.out.println("确定性分类结果--------------------CR");
        System.out.println();
        CR = displayClass(RR, N, C);//计算确定性分类
        //fcm.display(CR);
        //输出分类系数和平均模糊熵
        ClassF = ClassFactor(RR, N, C);
        System.out.println("分类系数");
        System.out.printf("%10.8f", ClassF[0]);
        System.out.println();
        System.out.println("平均模糊熵");
        System.out.printf("%10.8f", ClassF[1]);
        System.out.println();

        //dis=fcm.compute_dis(a,1,b,1,3,D);
        //System.out.println(dis);

    }

    //计算距离
    public static double compute_dis(double[][] mat1, int k_row, double[][] mat2, int i_row, int n_col, int d) {
        double x, max;
        int j;
        max = 0;
        switch (d) {
            case 1:        //Chebyshev 距离
                //System.out.println("Chebyshev距离");
                max = 0;
                for (j = 0; j < n_col; j++) {
                    x = Math.abs(mat1[k_row][j] - mat2[i_row][j]);
                    if (x > max) max = x;
                }
                return max;
            case 2:       //Euclid距离
                //System.out.println("Euclid距离");
                max = 0;
                for (j = 0; j < n_col; j++) {
                    max += Math.pow((mat1[k_row][j] - mat2[i_row][j]), 2);
                }
                return Math.sqrt(max);
            case 3:       //Hamming距离
                //System.out.println("Hamming距离");
                max = 0;
                for (j = 0; j < n_col; j++) {
                    max += Math.abs(mat1[k_row][j] - mat2[i_row][j]);
                }
                return max;
        }
        return max;
    }

    //计算聚类中心
    public static double[][] FCMCenter(double R[][], double X[][], int N, int M, int C, double Q) {
        double V[][] = new double[C][M];
        int i, j, k;
        double n_sum, m_sum;
        for (i = 0; i < C; i++)
            for (j = 0; j < M; j++) {
                n_sum = 0;
                m_sum = 0;
                for (k = 0; k < N; k++) {
                    n_sum += Math.pow(R[i][k], Q) * X[k][j];
                    m_sum += Math.pow(R[i][k], Q);
                }
                V[i][j] = n_sum / m_sum;
            }
        return V;
    }

    //修正模糊分类矩阵
    public static double[][] modifyR(double[][] X, double[][] V, int N, int M, int C, int D, double q) {
        double kj_sum;
        double R1[][] = new double[C][N];
        int i, k, j;
        for (i = 0; i < C; i++)
            for (k = 0; k < N; k++) {
                kj_sum = 0;
                for (j = 0; j < C; j++) {
                    if (j != i)
                        kj_sum += Math.pow((compute_dis(X, k, V, i, M, D) / compute_dis(X, k, V, j, M, D)), (2 / (q - 1)));
                }
                R1[i][k] = Math.pow((kj_sum + 1), -1);
            }
        return R1;
    }

    //输出矩阵
    public static void display(double[][] Matrix) {
        for (int i = 0; i < Matrix.length; i++) {
            for (int j = 0; j < Matrix[0].length; j++) {
                //Matrix[i][j]=Math.round(Matrix[i][j]);
                System.out.printf("%8.7f", Matrix[i][j]);
                System.out.print(" ");
            }
            System.out.println("");
        }
    }

    //转换为确定性分类
    public static double[][] displayClass(double[][] R1, int N, int C) {
        int i, j;
        double max;
        double CR[][] = new double[C][N];
        for (j = 0; j < N; j++) {
            max = R1[0][j];
            CR[0][j] = 1;
            for (i = 1; i < C; i++) {
                if (R1[i][j] > max) {
                    max = R1[i][j];
                    CR[i][j] = 1;
                    if (i == 1)
                        CR[0][j] = 0;
                } else CR[i][j] = 0;
            }
        }
        return CR;
    }

    //计算分类系数和平均模糊熵
    public static double[] ClassFactor(double[][] R1, int N, int C) {
        int i, j;
        double[] ClassF = {0, 0};
        for (i = 0; i < C; i++)
            for (j = 0; j < N; j++) {
                ClassF[0] += Math.pow(R1[i][j], 2);
                ClassF[1] += R1[i][j] * Math.log(R1[i][j]);  //以e为底的自然对数
            }
        ClassF[0] = ClassF[0] / N;  //分类系数
        ClassF[1] = -ClassF[1] / N; //平均模糊熵
        return ClassF;
    }
}
