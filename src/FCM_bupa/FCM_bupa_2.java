package FCM_bupa;

import java.io.*;
import java.util.*;

/**
 * @author Orust
 * @create 2021/4/21 21:37
 */
// https://blog.csdn.net/xiaoxik/article/details/72485835

public class FCM_bupa_2 {
    public static void main(String[] args) {
        double[][] VV;//由特征指标矩阵X和初始分类矩阵R0和FCM公式，得到的聚类中心矩阵
        double[][] RR;//由特征指标矩阵X和聚类中心V修正得到的隶属矩阵RR
        double[][] CertainClass = new double[0][];//确定性矩阵
        int i, j, k;
        double[] ClassF;
        int numOfData = 345; //numOfData--N:样本数!!!修改
        int numOfFeatures = 6; //numOfFeatures--M:特征值个数!!!修改
        int numOfClasses = 2; //numOfClasses--C:分类数!!!修改
        double E = 1e-3; //e:精度
        double Q = 2;//q:FCM指标
        int D = 2; //选择距离计算公式//1.Chebyshev 距离 //2. Euclid距离 3. //Hamming距离

        //************读取数据集.txt
        double[][] x = new double[numOfData][numOfFeatures];// 特征指标矩阵
        try {
            FileReader fr1 = new FileReader("src/FCM_bupa/bupa.data");//建立FileReader对象，并实例化为fr
            BufferedReader br = new BufferedReader(fr1);//建立BufferedReader对象，并实例化为br
            String line = null;//从文件读取一行字符串    //已修改！！！！！！！
            //判断读取到的字符串是否不为空
            int ii = 0;
            //标题列
            while ((line = br.readLine()) != null) {//已修改！！！！！！！

                String[] num = line.split(",");// String[] num = Line.split("\u0009");!!!修改
                ii++;
                for (j = 0; j < numOfFeatures; j++) {
                    x[ii][j] = Double.parseDouble(num[j]);
//                    sop(x[ii][j]);
                }
            }
            br.close();//关闭BufferedReader对象
            fr1.close();//关闭文件
        } catch (Exception e) {
            System.out.println("读文件发生错误!");
        }
        //************初始化初始分类矩阵R0****************对于所有的点n，n对于所有的类别的隶属度函数之和为1。
        double R0[][] = new double[numOfClasses][numOfData];//R0:初始分类矩阵,生成满足条件的随机数。
        for (i = 0; i < numOfData; i++) {
            double sum = 0;
            for (j = 0; j < numOfClasses; j++) {
                if (j == numOfClasses - 1)
                    R0[j][i] = 1 - sum;//
                else {
                    R0[j][i] = (1 - sum) * Math.random();
                    sum = sum + R0[j][i];
                }
            }
        }

        //由构造函数创建类的实例对象
        double dis;
        double max;
        FCM_bupa_2 fcm = new FCM_bupa_2();
        int times = 0;
        System.out.println(" ---------------FCM算法JAVA语言版--------------");
        do {
            max = 0;
            VV = fcm.FCMCenter(R0, x, numOfData, numOfFeatures, numOfClasses, Q);//由特征指标矩阵X和初始分类矩阵R0和FCM公式，计算聚类中心的坐标
            RR = fcm.modifyR(x, VV, numOfData, numOfFeatures, numOfClasses, D, Q);//由特征指标矩阵X和聚类中心V修正分类矩阵RR
            //System.out.println("迭代中的模糊分类矩阵");
            //fcm.display(RR);
            //计算RR和R0的精度差
            for (i = 0; i < numOfClasses; i++)
                for (j = 0; j < numOfData; j++) {
                    if (Math.abs(RR[i][j] - R0[i][j]) > max)
                        max = Math.abs(RR[i][j] - R0[i][j]);
                }
            if (max > E) {
                for (i = 0; i < numOfClasses; i++)
                    for (j = 0; j < numOfData; j++)
                        R0[i][j] = RR[i][j];
            }
            //max=0.00001;
            times++;
        } while (max > E);  //循环终止条件
        //---------------------------------显示实验结果---------------------------------
        //输出迭代次数
        System.out.println(" ");
        System.out.println("迭代了  " + times + "  次");
        //输出聚类中心
        System.out.println("-------------------聚类中心矩阵--------------------V");
        System.out.println();
        fcm.display(VV);
        //输出分类矩阵
        // System.out.println("---------------------隶属矩阵--------------------R");
        //System.out.println();
        //fcm.display(RR);
        //输出确定性分类
        //System.out.println("------------------确定性分类矩阵--------------------CR");
        //System.out.println();
        //CertainClass=fcm.certainClass(RR,numOfData,numOfClasses);
        //fcm.display( CertainClass);//计算确定性分类
        //------------------------------存储实验结果------------------------------------
        try {
            FileWriter fw = new FileWriter("src/FCM_bupa/Iris Results.txt");
            BufferedWriter bufw = new BufferedWriter(fw);
            for (int a = 0; a < numOfData; a++) {
                for (int b = 0; b < numOfClasses; b++) {
                    bufw.write(CertainClass[b][a] + "  ");
                    bufw.flush();
                }
                bufw.newLine();
                bufw.flush();
            }
        } catch (Exception e) {
            System.out.println("写文件发生错误!");
        }


        //fcm.display(CR);
        //输出分类系数和平均模糊熵
        ClassF = fcm.ClassFactor(RR, numOfData, numOfClasses);
        System.out.println("分类系数");
        System.out.printf("%10.8f", ClassF[0]);
        System.out.println();
        System.out.println("平均模糊熵");
        System.out.printf("%10.8f", ClassF[1]);
        System.out.println();
    }


    //---------------------------计算距离---------------------------
    public double compute_dis(double mat1[][], int k_row, double mat2[][], int i_row, int n_col, int d)//d:距离公式选择
    {
        double x, max;
        int j;
        max = 0;
        switch (d) {
            case 1:     //Chebyshev 距离
                //System.out.println("Chebyshev距离");
                max = 0;
                for (j = 0; j < n_col; j++) {
                    x = Math.abs(mat1[k_row][j] - mat2[i_row][j]);
                    if (x > max) max = x;
                }
                return max;
            case 2:    //Euclid距离
                //System.out.println("Euclid距离");
                max = 0;
                for (j = 0; j < n_col; j++) {
                    max += Math.pow((mat1[k_row][j] - mat2[i_row][j]), 2);
                }
                return Math.sqrt(max);
            case 3:    //Hamming距离
                //System.out.println("Hamming距离");
                max = 0;
                for (j = 0; j < n_col; j++) {
                    max += Math.abs(mat1[k_row][j] - mat2[i_row][j]);
                }
                return max;
        }
        return max;
    }

    //------------------------计算聚类中心--------------------------
    public double[][] FCMCenter(double R[][], double X[][], int numOfData, int numOfFeatures, int numOfClasses, double Q)//参考FCM计算聚类中心的公式。
    {
        double V[][] = new double[numOfClasses][numOfFeatures];
        double n_sum, m_sum;
        for (int i = 0; i < numOfClasses; i++)
            for (int j = 0; j < numOfFeatures; j++) {
                n_sum = 0;
                m_sum = 0;
                for (int k = 0; k < numOfData; k++) {
                    n_sum += Math.pow(R[i][k], Q) * X[k][j];
                    m_sum += Math.pow(R[i][k], Q);
                }
                V[i][j] = n_sum / m_sum;
            }
        return V;
    }

    //---------------------------------------修正模糊分类矩阵---------------------------------------
    public double[][] modifyR(double[][] X, double[][] V, int numOfData, int numOfFeatures, int numOfClasses, int D, double q) {
        double kj_sum;
        double[][] R1 = new double[numOfClasses][numOfData];
        int i, k, j;
        for (i = 0; i < numOfClasses; i++)
            for (k = 0; k < numOfData; k++) {
                kj_sum = 0;
                for (j = 0; j < numOfClasses; j++) {
                    if (j != i)
                        kj_sum += Math.pow((compute_dis(X, k, V, i, numOfFeatures, D) / compute_dis(X, k, V, j, numOfFeatures, D)), (2 / (q - 1)));
                }
                R1[i][k] = Math.pow((kj_sum + 1), -1);//加1是指当j=i时，kj_sum的其中缺的值是1.
            }
        return R1;
    }

    //-----------------------------------输出矩阵------------------------------------------
    public void display(double[][] Matrix) {
        for (double[] matrix : Matrix) {
            for (int j = 0; j < Matrix[0].length; j++) {
                System.out.printf("%8.7f", matrix[j]);
                System.out.print(" ");
            }
            System.out.println("");
        }
    }

    //------------------------------转换为确定性分类----------------------------------------2222222-如何转化的???????
    public double[][] certainClass(double RR[][], int numOfData, int numOfClasses) {
        double max;
        double[][] CertainClass = new double[numOfClasses][numOfData];
        for (int j = 0; j < numOfData; j++) {
            max = RR[0][j];
            CertainClass[0][j] = 1;
            for (int i = 1; i < numOfClasses; i++) {
                if (RR[i][j] > max) {
                    max = RR[i][j];
                    CertainClass[i][j] = 1;
                    if (i == 1)
                        CertainClass[0][j] = 0;
                } else
                    CertainClass[i][j] = 0;
            }
        }
        return CertainClass;
    }

    //计算分类系数和平均模糊熵
    public double[] ClassFactor(double R1[][], int numOfData, int numOfClasses) {
        int i, j;
        double[] ClassF = {0, 0};
        for (i = 0; i < numOfClasses; i++)
            for (j = 0; j < numOfData; j++) {
                ClassF[0] += Math.pow(R1[i][j], 2);
                ClassF[1] += R1[i][j] * Math.log(R1[i][j]);
            }
        ClassF[0] = ClassF[0] / numOfData;  //分类系数
        ClassF[1] = -ClassF[1] / numOfData; //平均模糊熵
        return ClassF;
    }

    public static void sop(Object obj) {
        System.out.println(obj);
    }
}
