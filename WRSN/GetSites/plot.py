# -*- coding:utf-8 -*-
import sys
import numpy as np
import matplotlib.pyplot as plt

# https://www.jianshu.com/p/fa0312f6373c
def getSite_1(x_center, y_center, radius, total):
    count = 0
    x = np.zeros(total)
    y = np.zeros(total)
    while count < total:
        theta = np.random.random() * 2 * np.pi
        r = np.random.random() ** 0.5 * radius

        x[count] = x_center + r * np.cos(theta)
        y[count] = y_center + r * np.sin(theta)
        print(x[count], y[count])
        count += 1

    plt.figure(figsize=(10, 10.1), dpi=125)
    # 画出各个点
    plt.plot(x, y, 'ro')

    # 画出圆形
    _t = np.arange(0, 7, 0.1)
    _x = x_center + np.cos(_t) * radius
    _y = y_center + np.sin(_t) * radius
    plt.plot(_x, _y, 'g-')

    # 对坐标轴进行设置
    plt.xlim(x_center - 1.1 * radius, x_center + 1.1 * radius)
    plt.ylim(y_center - 1.1 * radius, y_center + 1.1 * radius)
    plt.xlabel('x')
    plt.ylabel('y')
    plt.title('Random Scatter')
    plt.grid(True)
    plt.savefig('imag.png')
    plt.show()


if __name__ == '__main__':
    getSite_1(1, 1, 1, 20)