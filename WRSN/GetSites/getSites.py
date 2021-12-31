# test.py
import sys
import random
import numpy as np
import math
import matplotlib.pyplot as plt


def getMatrixSites(length, width, total):
    x = np.zeros(total)
    y = np.zeros(total)
    x_center = length / 2
    y_center = width / 2
    count = 0
    while count < total:
        x[count] = x_center + (random.random() - 0.5) * length
        y[count] = y_center + (random.random() - 0.5) * width
        print(x[count], y[count])
        count += 1
    plt.figure(figsize=(10, 10.1), dpi=125)
    # 画出各个点
    plt.plot(x, y, 'ro')

    plt.show()

def getCircularSites_1(x_center, y_center, radius):
    count = 0
    while count < 10:
        count += 1
        x = x_center + (random.random() - 0.5) * radius * 2
        y = y_center + (random.random() - 0.5) * radius * 2
        if (x - x_center) ** 2 + (y - y_center) ** 2 <= radius ** 2:
            print(x, y)


def getCircularSites_2(x_center, y_center, radius):
    count = 0
    while count < 10:
        count += 1
        theta = random.random() * 2 * math.pi
        r = random.random() ** 0.5 * radius   # 由于平面随机采样，所以需要开方，但是值域还是[0, 1]

        x = x_center + r * math.cos(theta)
        y = y_center + r * math.sin(theta)
        print(x, y)

if __name__ == "__main__":
    getMatrixSites(300, 300, 100)
    # getCircularSites_1(5, 5, 10)
    # getCircularSites_2(5, 5, 10)