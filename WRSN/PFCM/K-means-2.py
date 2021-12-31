# 导入库
import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
from sklearn.cluster import KMeans
from sklearn import datasets
from sklearn.cluster import AgglomerativeClustering
from sklearn.metrics import confusion_matrix
from skfuzzy.cluster import cmeans

# 导入数据集
iris = datasets.load_iris()
# 取特征空间中的4个维度
X = iris.data[:, :4]

#############################k-means##########################
###不同初始值###
# 构造聚类器,分两类
estimator = KMeans(n_clusters=2, init=np.array([[5.1, 3.5, 1.4, 0.2], [4.9, 3.0, 1.4, 0.2]]))
# 聚类
estimator.fit(X)
# 获取聚类标签
label_pred = estimator.labels_
# 绘制k-means结果
plt.figure()
x0 = X[label_pred == 0]
x1 = X[label_pred == 1]
center = estimator.cluster_centers_
plt.scatter(x0[:, 0], x0[:, 1], c="red", marker='o', label='label0')
plt.scatter(x1[:, 0], x1[:, 1], c="green", marker='*', label='label1')
plt.scatter(center[:, 0], center[:, 1], c="blue", marker='+')
txt = ['A', 'B']
for i in range(len(center[:, 0])):
    plt.annotate(txt[i], xy=(center[:, 0][i], center[:, 1][i]), xytext=(center[:, 0][i] + 0.01, center[:, 1][i] + 0.01))
plt.xlabel('sepal length')
plt.ylabel('sepal width')
plt.legend(loc=2)

# 构造聚类器,分两类
estimator = KMeans(n_clusters=2, init=np.array([[4.7, 3.2, 1.3, 0.2], [4.6, 3.1, 1.5, 0.2]]))
# 聚类
estimator.fit(X)
# 获取聚类标签
label_pred = estimator.labels_
# 绘制k-means结果
plt.figure()
x0 = X[label_pred == 0]
x1 = X[label_pred == 1]
center = estimator.cluster_centers_
plt.scatter(x0[:, 0], x0[:, 1], c="red", marker='o', label='label0')
plt.scatter(x1[:, 0], x1[:, 1], c="green", marker='*', label='label1')
plt.scatter(center[:, 0], center[:, 1], c="blue", marker='+')
txt = ['A', 'B']
for i in range(len(center[:, 0])):
    plt.annotate(txt[i], xy=(center[:, 0][i], center[:, 1][i]), xytext=(center[:, 0][i] + 0.01, center[:, 1][i] + 0.01))
plt.xlabel('sepal length')
plt.ylabel('sepal width')
plt.legend(loc=2)

# 构造聚类器,分两类
estimator = KMeans(n_clusters=2, init=np.array([[5.0, 3.6, 1.4, 0.2], [5.4, 3.9, 1.7, 0.4]]))
# 聚类
estimator.fit(X)
# 获取聚类标签
label_pred = estimator.labels_
# 绘制k-means结果
plt.figure()
x0 = X[label_pred == 0]
x1 = X[label_pred == 1]
center = estimator.cluster_centers_
plt.scatter(x0[:, 0], x0[:, 1], c="red", marker='o', label='label0')
plt.scatter(x1[:, 0], x1[:, 1], c="green", marker='*', label='label1')
plt.scatter(center[:, 0], center[:, 1], c="blue", marker='+')

txt = ['A', 'B']
for i in range(len(center[:, 0])):
    plt.annotate(txt[i], xy=(center[:, 0][i], center[:, 1][i]), xytext=(center[:, 0][i] + 0.01, center[:, 1][i] + 0.01))
plt.xlabel('sepal length')
plt.ylabel('sepal width')
plt.legend(loc=2)

plt.show()
###不同初始值###


##不同聚类数####
n = 3
estimator = KMeans(n_clusters=3)
# 聚类
estimator.fit(X)
# 获取聚类标签
label_pred = estimator.labels_
# 绘制k-means结果
plt.figure()
x0 = X[label_pred == 0]
x1 = X[label_pred == 1]
x2 = X[label_pred == 2]
center = estimator.cluster_centers_
plt.scatter(x0[:, 0], x0[:, 1], c="red", marker='o', label='label0')
plt.scatter(x1[:, 0], x1[:, 1], c="green", marker='*', label='label1')
plt.scatter(x2[:, 0], x2[:, 1], c="yellow", marker='^', label='label2')
plt.scatter(center[:, 0], center[:, 1], center[:, 1], c="blue", marker='+')

txt = ['A', 'B', 'C']
for i in range(len(center[:, 0])):
    plt.annotate(txt[i], xy=(center[:, 0][i], center[:, 1][i]), xytext=(center[:, 0][i] + 0.01, center[:, 1][i] + 0.01))
plt.xlabel('sepal length')
plt.ylabel('sepal width')
plt.legend(loc=2)

n = 4
estimator = KMeans(n_clusters=4)
# 聚类
estimator.fit(X)
# 获取聚类标签
label_pred = estimator.labels_
# 绘制k-means结果
plt.figure()
x0 = X[label_pred == 0]
x1 = X[label_pred == 1]
x2 = X[label_pred == 2]
x3 = X[label_pred == 3]
center = estimator.cluster_centers_
plt.scatter(x0[:, 0], x0[:, 1], c="red", marker='o', label='label0')
plt.scatter(x1[:, 0], x1[:, 1], c="green", marker='*', label='label1')
plt.scatter(x2[:, 0], x2[:, 1], c="yellow", marker='^', label='label2')
plt.scatter(x3[:, 0], x3[:, 1], c="pink", marker='>', label='label3')
plt.scatter(center[:, 0], center[:, 1], center[:, 1], c="blue", marker='+')

txt = ['A', 'B', 'C', 'D']
for i in range(len(center[:, 0])):
    plt.annotate(txt[i], xy=(center[:, 0][i], center[:, 1][i]), xytext=(center[:, 0][i] + 0.01, center[:, 1][i] + 0.01))
plt.xlabel('sepal length')
plt.ylabel('sepal width')
plt.legend(loc=2)

n = 5
estimator = KMeans(n_clusters=5)
# 聚类
estimator.fit(X)
# 获取聚类标签
label_pred = estimator.labels_
# 绘制k-means结果
plt.figure()
x0 = X[label_pred == 0]
x1 = X[label_pred == 1]
x2 = X[label_pred == 2]
x3 = X[label_pred == 3]
x4 = X[label_pred == 4]
center = estimator.cluster_centers_
plt.scatter(x0[:, 0], x0[:, 1], c="red", marker='o', label='label0')
plt.scatter(x1[:, 0], x1[:, 1], c="green", marker='*', label='label1')
plt.scatter(x2[:, 0], x2[:, 1], c="yellow", marker='^', label='label2')
plt.scatter(x3[:, 0], x3[:, 1], c="pink", marker='>', label='label3')
plt.scatter(x4[:, 0], x4[:, 1], c="black", marker='<', label='label4')
plt.scatter(center[:, 0], center[:, 1], center[:, 1], c="blue", marker='+')

txt = ['A', 'B', 'C', 'D', 'E']
for i in range(len(center[:, 0])):
    plt.annotate(txt[i], xy=(center[:, 0][i], center[:, 1][i]), xytext=(center[:, 0][i] + 0.01, center[:, 1][i] + 0.01))
plt.xlabel('sepal length')
plt.ylabel('sepal width')
plt.legend(loc=2)

plt.show()
###不同聚类数####


# 簇的数量
distance = []
k = []
for n_clusters in range(1, 19):
    cls = KMeans(n_clusters).fit(X)


    # 曼哈顿距离
    def manhattan_distance(x, y):
        return np.sum(abs(x - y))


    distance_sum = 0
    for i in range(n_clusters):
        group = cls.labels_ == i
        members = X[group, :]
        for v in members:
            distance_sum += manhattan_distance(np.array(v), cls.cluster_centers_[i])
    distance.append(distance_sum)
    k.append(n_clusters)
plt.scatter(k, distance)
plt.plot(k, distance)
plt.xlabel("k")
plt.ylabel("distance")
plt.show()
##############################k-means##########################


##############################凝聚层次聚类##########################
clustering = AgglomerativeClustering(linkage='ward', n_clusters=2)

res = clustering.fit(X)

print("各个簇的样本数目：")
print(pd.Series(clustering.labels_).value_counts())
print("聚类结果：")
print(confusion_matrix(iris.target, clustering.labels_))

plt.figure()
d0 = X[clustering.labels_ == 0]
plt.plot(d0[:, 0], d0[:, 1], 'r.')
d1 = X[clustering.labels_ == 1]
plt.plot(d1[:, 0], d1[:, 1], 'go')
plt.xlabel("Sepal.Length")
plt.ylabel("Sepal.Width")
plt.title("AGNES Clustering")
plt.show()
##############################凝聚层次聚类##########################


############################FCM##########################
# cntr：聚类中心
# u：最后的隶属度矩阵，
# u0：初始化的隶属度矩阵
# d：是一个矩阵记录每一个点到聚类中心的欧式距离
# jm：是目标函数的优化历史
# p：p是迭代的次数
# fpc：全称是fuzzy
# partition
# coefficient， 是一个评价分类好坏的指标，它的范围是0到1, 1
# 表示效果最好
train_data = X.T
center, u, u0, d, jm, p, fpc = cmeans(train_data, m=2, c=5, error=0.005, maxiter=1000)

plt.figure()
for i in u:
    label = np.argmax(u, axis=0)
for i in range(150):
    if label[i] == 0:
        plt.scatter(train_data[0][i], train_data[1][i], c='red', marker='o', label='label0')
    elif label[i] == 1:
        plt.scatter(train_data[0][i], train_data[1][i], c='green', marker='*', label='label1')
    elif label[i] == 2:
        plt.scatter(train_data[0][i], train_data[1][i], c='pink', marker='^', label='label2')
    elif label[i] == 3:
        plt.scatter(train_data[0][i], train_data[1][i], c='black', marker='>', label='label2')
    elif label[i] == 4:
        plt.scatter(train_data[0][i], train_data[1][i], c='purple', marker='>', label='label2')
plt.scatter(center[:, 0], center[:, 1], center[:, 1], c="blue", marker='+')
txt = ['A', 'B', 'C', 'D', 'E']
for i in range(len(center[:, 0])):
    plt.annotate(txt[i], xy=(center[:, 0][i], center[:, 1][i]), xytext=(center[:, 0][i] + 0.01, center[:, 1][i] + 0.01))
plt.show()
############################FCM##########################
