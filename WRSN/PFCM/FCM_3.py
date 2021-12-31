import numpy as np
import matplotlib.pyplot as plt
from sklearn.cluster import KMeans
from skfuzzy.cluster import cmeans

# https://blog.csdn.net/FrankieHello/article/details/79581315

# [low,high) + size，产生[1,100)个规格为100*2的数据
# cp = np.random.uniform(0, 500, (100, 2))
#
# train = cp[:]
# test = cp[50:]
# print(train)

netSize = 500  # 网络大小
nodeCnt = 100  # 节点数量

# [0, netSize) 产生nodeCnt个节点数据
cp = np.random.uniform(0, netSize, (nodeCnt, 2))

# 文件名
name = 'node_' + str(netSize) + '_' + str(nodeCnt) + '.txt'
# 是否保存节点数据
# np.savetxt(name, cp)
# train = cp[:]
train = np.loadtxt(name)  # 将文件中数据加载到data数组里

# skfuzzy.cmeans
train = train.T
center, u, u0, d, jm, p, fpc = cmeans(train, m=2, c=3, error=0.005, maxiter=1000)

for i in u:
    label = np.argmax(u, axis=0)
print(label)

for i in range(50):
    if label[i] == 0:
        plt.scatter(train[0][i], train[1][i], c='r')
    elif label[i] == 1:
        plt.scatter(train[0][i], train[1][i], c='g')
    elif label[i] == 2:
        plt.scatter(train[0][i], train[1][i], c='b')

plt.show()
