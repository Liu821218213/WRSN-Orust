import numpy as np
from sklearn import datasets
from sklearn.decomposition import PCA
import matplotlib.pyplot as plt
# https://blog.csdn.net/a19990412/article/details/89361038

iris = datasets.load_iris()
# print(iris.data)
# print(iris.target)
print(iris.data.shape)


def FCM(X, c_clusters=3, m=2, eps=10):
    membership_mat = np.random.random((len(X), c_clusters))  # 生成随机二维数组shape(150,3)，随机初始化隶属矩阵
    # 这一步的操作是为了使Xi的隶属度总和为1
    membership_mat = np.divide(membership_mat, np.sum(membership_mat, axis=1)[:, np.newaxis])

    while True:
        working_membership_mat = membership_mat ** m  # shape->(150,3)
        # 根据公式计算聚类中心点Centroids.shape->(3,4)
        Centroids = np.divide(np.dot(working_membership_mat.T, X),
                              np.sum(working_membership_mat.T, axis=1)[:, np.newaxis])

        # 该矩阵保存所有实点到每个聚类中心的欧式距离
        n_c_distance_mat = np.zeros((len(X), c_clusters))  # shape->(150,3)
        for i, x in enumerate(X):
            for j, c in enumerate(Centroids):
                n_c_distance_mat[i][j] = np.linalg.norm(x - c, 2)  # 计算l2范数(欧氏距离)

        new_membership_mat = np.zeros((len(X), c_clusters))

        # 根据公式计算模糊矩阵U
        for i, x in enumerate(X):
            for j, c in enumerate(Centroids):
                new_membership_mat[i][j] = 1. / np.sum((n_c_distance_mat[i][j] / n_c_distance_mat[i]) ** (2 / (m - 1)))
        if np.sum(abs(new_membership_mat - membership_mat)) < eps:
            break
        membership_mat = new_membership_mat
    return np.argmax(new_membership_mat, axis=1)


def evaluate(y, t):
    a, b, c, d = [0 for i in range(4)]
    for i in range(len(y)):
        for j in range(i + 1, len(y)):
            if y[i] == y[j] and t[i] == t[j]:
                a += 1
            elif y[i] == y[j] and t[i] != t[j]:
                b += 1
            elif y[i] != y[j] and t[i] == t[j]:
                c += 1
            elif y[i] != y[j] and t[i] != t[j]:
                d += 1
    return a, b, c, d


def external_index(a, b, c, d, m):
    JC = a / (a + b + c)
    FMI = np.sqrt(a ** 2 / ((a + b) * (a + c)))
    RI = 2 * (a + d) / (m * (m + 1))
    return JC, FMI, RI


def evaluate_it(y, t):
    a, b, c, d = evaluate(y, t)
    return external_index(a, b, c, d, len(y))


# 结果数据
test_y = FCM(iris.data)
print(test_y)

# 三个评估指数
print(evaluate_it(iris.target, test_y))

X_reduced = PCA(n_components=2).fit_transform(iris.data)
# 评估结果
# plt.scatter(X_reduced[:, 0], X_reduced[:, 1], c=test_y, cmap=plt.cm.Set1)

# 原图
plt.scatter(X_reduced[:, 0], X_reduced[:, 1], c=iris.target, cmap=plt.cm.Set1)

plt.show()
