package WRSNUtils;

import WRSNSystem.Constants;
import WRSNSystem.Node;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOError;
import java.io.IOException;
import java.util.Scanner;

/**
 * @author Orust
 * @create 2021/12/30 16:29
 */
public class scanner {

    public static void main(String[] args) throws IOException {
        solve();
    }

    private static void solve() throws IOException {
        String filename = "src/datas/node.txt";
        Scanner sc = new Scanner(new File(filename));
        StringBuilder tmp = new StringBuilder();

        while (sc.hasNextLine()) {
//            System.out.println(i++);
//            System.out.println(sc.nextLine().trim());
            tmp.append(sc.nextLine().trim()).append(";");
        }
        sc.close();
//        System.out.println(tmp);
        String[] arr = tmp.toString().split(";");
        for (int i = 0; i < arr.length; i++) {
            String[] as = arr[i].split("\t");
            System.out.println(Double.parseDouble(as[0]) + " " + Double.parseDouble(as[1]));

        }
    }
}
