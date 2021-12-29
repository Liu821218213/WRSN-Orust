import java.io.*;
import java.util.*;

/**
 * @author Orust
 * @create 2021/4/21 15:44
 */


public class FCM_iris {
    private static final String FILE_PAR = "src/initParam.properties";//参数配置
    private static final String FILE_PAR2 = "src/initParam2.properties";//参数配置

    private static final String FILE_DATA_IN = "src/FCM_bupa/bupa.data";//输入的样本数据
    private static final String FILE_DATA_IN2 = "src/seeds_dataset.txt";//输入的样本数据

    private static final String FILE_CENTER = "src/vCenter.txt";//聚类中心
    private static final String FILE_MATRIX = "src/uMatrix.txt";//隶属度矩阵

    public int nCount;//样本数
    public int dimension;//每个样本点的维数
    public int cCount;//要聚类的类别数
    public int maxCycle;//最大的迭代次数
    public double m;//参数m
    public double limit;//算法结束迭代的阈值1e-6

    public FCM_iris() {
        super();
    }

    /*通过构造函数的方式配置参数*/
    public FCM_iris(int nCount, int dimension, int cCount, int maxCycle, double m, double limit) {
        this.nCount = nCount;
        this.dimension = dimension;
        this.cCount = cCount;
        this.maxCycle = maxCycle;
        this.m = m;
        this.limit = limit;
    }

    /*读取配置文件配置参数
    将配置文件读取到InputStream对象中*/
    public void getPar(String string) {
        try {
            Properties prop = new Properties();
            BufferedReader in = new BufferedReader(new FileReader(string));
            prop.load(in);
            nCount = Integer.parseInt(prop.getProperty("nCount"));
            dimension = Integer.parseInt(prop.getProperty("dimension"));
            cCount = Integer.parseInt(prop.getProperty("cCount"));
            m = Integer.parseInt(prop.getProperty("m"));
            maxCycle = Integer.parseInt(prop.getProperty("maxCycle"));
            limit = Double.parseDouble(prop.getProperty("limit"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*读取样本
    样本保存在二维数组中*/
    public void getPattern(double[][] pattern, String string) throws IOException {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(string));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //读取样本文件
        String line = null;
        /*
         * 正则表达式中\s匹配任何空白字符，包括空格、制表符、换页符等等, 等价于[ \f\n\r\t\v]
         * 而“\s+”则表示匹配任意多个上面的字符。另因为反斜杠在Java里是转义字符，所以在Java里，我们要这么用“\\s+”
         * */

//        String regex = "\\s+";
        String regex = ",";  //字符串以','拆分
        int row = 0;
        while (true) {
            line = br.readLine();
            if (line == null)
                break;
            String[] split = line.split(regex);//字符串拆分
            for (int i = 0; i < split.length - 1; i++) {
                pattern[row][i] = Double.parseDouble(split[i]);
            }
            row++;
        }
        br.close();
    }

    /*求数组最大值最小值*/
    public double[] getMinMax(double[] a) {
        double[] minmax = new double[2];
        double minValue = a[0];
        double maxValue = a[0];
        for (double v : a) {
            minValue = Math.min(minValue, v);
            maxValue = Math.max(maxValue, v);
        }
        minmax[0] = minValue;
        minmax[1] = maxValue;
        return minmax;
    }

    /*标准化样本，这里采用极大极小标准化，标准化后每个指标都在区间[0, 1]之间*/
    public void Normalized(double[][] pattern, int nCount, int dimension) {
        double[] a = new double[pattern.length];//提取列
        //先复制pattern到copypattern
        double[][] copypattern = new double[nCount][dimension];
        for (int i = 0; i < pattern.length; i++) {
            for (int j = 0; j < pattern[i].length; j++) {
                copypattern[i][j] = pattern[i][j];
            }
        }

        for (int j = 0; j < pattern[0].length; j++) {
            for (int k = 0; k < pattern.length; k++)
                a[k] = pattern[k][j];
            for (int i = 0; i < pattern.length; i++) {
                double[] min_max = getMinMax(a);
                double minValue = min_max[0];
                double maxValue = min_max[1];
                //pattern[i][j]=(pattern[i][j]-minValue)/(maxValue-minValue)是错误写法
                pattern[i][j] = (copypattern[i][j] - minValue) / (maxValue - minValue);
            }
        }

    }

    /*求矩阵第j列之和操作--用于规范化样本*/
    public double sumArray(double[][] array, int j) {
        double sum = 0;
        for (int i = 0; i < array.length; i++) {
            sum += array[i][j];
        }
        return sum;
    }

    /**
     * 本函数完成FCM聚类算法
     *
     * @param pattern   为样本点向量  指标为：样本指标*维数=维数指标
     * @param dimension 每个样本的维数
     * @param nCount    样本的个数
     * @param cCount    分类的数目
     * @param m         FCM的重要模糊控制参数m
     * @param maxCycle  最大循环次数
     * @param limit     算法结束迭代的阈值1e-6
     * @param uMatrix   输出的划分矩阵   c*nCount
     * @param vCenter   输出的样本的聚类中心矩阵    cCount*dimension
     */
    public void FCM(double[][] pattern, int dimension, int nCount, int cCount, double m,
                    int maxCycle, double limit, double[][] uMatrix, double[][] vCenter) {
        //验证输入参数的有效性
        if (cCount >= nCount || m <= 1)
            return;
        //数据标准化
        Normalized(pattern, nCount, dimension);

        int iterCnt = 0;//迭代次数
        boolean iterFlag = true;//迭代标志

        double[][] temp = new double[cCount][nCount];  //隶属度矩阵副本

        double[] sum = new double[nCount];  //记录每一列的和
        for (int i = 0; i < cCount; i++) {
            for (int j = 0; j < nCount; j++) {
                uMatrix[i][j] = Math.random(); //随机初始化隶属度
                temp[i][j] = uMatrix[i][j];
                sum[j] += temp[i][j];
            }
        }

        /* 隶属度归一化，保证隶属度矩阵每一列和为1*/
        for (int i = 0; i < cCount; i++) {
            for (int j = 0; j < nCount; j++) {
                uMatrix[i][j] = temp[i][j] / sum[j];
            }
        }


        while (iterFlag) {
            //每次保存更新前的隶属度
            for (int i = 0; i < cCount; i++) {
                for (int j = 0; j < nCount; j++) {
                    temp[i][j] = uMatrix[i][j];
                }
            }

            //更新聚类中心V
            for (int i = 0; i < cCount; i++) {
                for (int dimen = 0; dimen < dimension; dimen++) {
                    double a = 0, b = 0;
                    for (int k = 0; k < nCount; k++) {
                        a += Math.pow(uMatrix[i][k], m) * pattern[k][dimen];
                        //	System.out.println(a);
                        b += Math.pow(uMatrix[i][k], m);
                    }
                    vCenter[i][dimen] = a / b;  // d代表维度
			/*		if(t==0&&i==2){
						System.out.println(a);
						System.out.println(b);
						System.out.println(vCenter[t][i]);
					}
					*/
                }
            }

            //更新隶属度U
            for (int i = 0; i < cCount; i++) {
                for (int k = 0; k < nCount; k++) {
                    double e = 0;
                    for (int j = 0; j < cCount; j++) {
                        double c = 0, d = 0;
                        double res = 0;
                        for (int dimen = 0; dimen < dimension; dimen++) {
//                            c += Math.pow(pattern[k][dimen] - vCenter[i][dimen], 2 / (m - 1));
//                            d += Math.pow(pattern[k][dimen] - vCenter[j][dimen], 2 / (m - 1));
                            double a = pattern[k][dimen] - vCenter[i][dimen];
                            double b = pattern[k][dimen] - vCenter[j][dimen];
                            res = Math.pow(a / b, 2 / (m - 1));
//                            System.out.println(vCenter[i][dimen]);
//                            System.out.println(vCenter[j][dimen]);
                        }
//                        e += c / d;
                        e += res;
                    }
                    uMatrix[i][k] = 1 / e;
                }
            }

            //判断u是否收敛，或是否达到最大迭代次数
            double[][] var = new double[cCount][nCount];  //var为u矩阵变化量
            double f = 0;  //u变化量最大值
            for (int i = 0; i < cCount; i++)
                for (int j = 0; j < nCount; j++) {
                    var[i][j] = Math.abs(temp[i][j] - uMatrix[i][j]);
                    f = Math.max(f, var[i][j]);
                }

            if (f <= limit || iterCnt > maxCycle)
                iterFlag = false;
            iterCnt++;

            // 目标函数J的值
            double result = getObjectValue(uMatrix, vCenter, pattern, cCount, nCount, dimension, m);
            System.out.println("第" + iterCnt + "次迭代J：" + result);
        }
    }


    /**
     * 计算目标函数值J
     *
     * @param u         隶属度矩阵
     * @param v         聚类中心矩阵
     * @param pattern   数据集
     * @param cCount    聚类数
     * @param nCount    节点数
     * @param dimension 维度
     * @param m         模糊因子
     * @return 目标函数J的值
     */
    public double getObjectValue(double[][] u, double[][] v, double[][] pattern,
                                 int cCount, int nCount, int dimension, double m) {

        double[][] temp = new double[nCount][cCount];
        for (int i = 0; i < cCount; i++)
            for (int j = 0; j < nCount; j++) {
                temp[j][i] = 0;
            }

        for (int i = 0; i < cCount; i++) {
            for (int k = 0; k < nCount; k++) {
                double t = 0;
                for (int j = 0; j < dimension; j++) {
                    t += Math.pow(pattern[k][j] - v[i][j], 2);
                }
                temp[k][i] += Math.pow(u[i][k], m) * t;
            }
        }

        double objectValue = 0;
        for (int i = 0; i < cCount; i++) {
            for (int j = 0; j < nCount; j++) {
                objectValue += temp[j][i];
            }
        }

        return objectValue;
    }

    /*运行FCM算法*/
    public void runFCM(String string) throws IOException {
        double[][] pattern = new double[nCount][dimension];
        double[][] uMatrix = new double[cCount][nCount];
        double[][] vCenter = new double[cCount][dimension];

        //获取样本，保存在pattern数组中
        getPattern(pattern, string);
        //执行FCM
        FCM(pattern, dimension, nCount, cCount, m, maxCycle, limit, uMatrix, vCenter);
        //输出结果，u v
        printUV(uMatrix, vCenter);
    }

    /*输出隶属度矩阵和聚类的中心*/
    public void printUV(double[][] uMatrix, double[][] vCenter) {
        StringBuilder str;
        String tab = "  ";
        //矩阵转置，便于在txt中显示
        double[][] new_uMatrix = new double[nCount][cCount];
        for (int i = 0; i < new_uMatrix.length; i++) {
            for (int j = 0; j < new_uMatrix[i].length; j++) {
                new_uMatrix[i][j] = uMatrix[j][i];
            }
        }
        //输出隶属度矩阵
        try {
            FileWriter matrixFileWriter = new FileWriter(FILE_MATRIX);
            for (int i = 0; i < nCount; i++) {
                str = new StringBuilder();
                for (int j = 0; j < cCount; j++) {
                    str.append(new_uMatrix[i][j]).append(tab);
                }
                str.append("\n");
                matrixFileWriter.write(str.toString());
            }
            matrixFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //输出聚类的中心
        try {
            FileWriter centerFileWriter = new FileWriter(FILE_CENTER);

            for (int i = 0; i < cCount; i++) {
                str = new StringBuilder();
                for (int j = 0; j < dimension; j++) {
                    str.append(vCenter[i][j]).append(tab);
                }
                str.append("\n");
                centerFileWriter.write(str.toString());
            }
            centerFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*主函数*/
    public static void main(String[] args) throws IOException {
        FCM_iris FCM_iris = new FCM_iris();
        FCM_iris.getPar(FILE_PAR);
        FCM_iris.runFCM(FILE_DATA_IN);
    }
}