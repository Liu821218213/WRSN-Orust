package WRSNUtils;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * @author Orust
 * @create 2021/12/29 14:30
 */

public class MyQueue<T> extends LinkedList<T> {
    private static final long serialVersionUID = 1L;
    private Set<T> containSet = new HashSet<T>();

    public void push(T element) {
        if (!containSet.contains(element)) {
            add(element);
//            super.add(element);  // super可以省略，如果子类没有重写那么默认调用父类方法
            containSet.add(element);
        }
    }

    public T watch() {
        if (size() > 0) {
            return super.get(0);
        } else {
            return null;
        }
    }

    public T pop() {
        if (size() > 0) {
            T first = super.get(0);
            super.removeFirst();
            containSet.remove(first);
            return first;
        } else {
            return null;
        }
    }

    public static void main(String[] args) {
        MyQueue<Integer> queue = new MyQueue<Integer>();

        queue.push(2);
        queue.push(3);
        queue.push(4);
        queue.push(6);
        queue.push(3);

        System.out.println(queue);

        System.out.println(queue.watch());
        queue.pop();
        System.out.println(queue);
    }
}
