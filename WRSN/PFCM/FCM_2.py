import numpy as np
import matplotlib.pyplot as plt
from sklearn.cluster import KMeans
from skfuzzy.cluster import cmeans
from scipy.spatial.distance import cdist

# https://blog.csdn.net/FrankieHello/article/details/79581315

# [low,high) + size，产生[1,100)个规格为100*2的数据
cp = np.random.uniform(0, 500, (100, 2))
# np.savetxt("data.txt", cp)
# train = cp[:]
train = np.loadtxt("data.txt")  # 将文件中数据加载到data数组里


# test = cp[50:]
# print(train)


# data = [(35.0456, -85.2672),
#         (35.1174, -89.9711),
#         (35.9728, -83.9422),
#         (36.1667, -86.7833)]
#
# x1 = np.array([(1, 3), (2, 4), (5, 6)])
# x2 = [(3, 7), (4, 8), (6, 9)]

# [[0.         4.70444794 1.6171966  1.88558331]
#  [4.70444794 0.         6.0892811  3.35605413]
#  [1.6171966  6.0892811  0.         2.84770898]
#  [1.88558331 3.35605413 2.84770898 0.        ]]

# [[4.47213595 3.16227766 2.23606798]
#  [5.83095189 4.47213595 2.23606798]
#  [7.81024968 6.40312424 3.16227766]]

# print(np.sqrt(np.sum((data - data)**2)))
# def _distance(data, centers, metric='euclidean'):
#     m, n = len(data), len(data[0])
#     d = len(centers)
#     cdist = np.zeros((m, m))
#
#     for k in range(d):
#         for i in range(m):
#             cur = 0
#             for j in range(n):
#                 cur += np.power(data[i][j] - centers[k][j], 2)
#             cur = np.power(cur, 1 / 2)
#             cdist[k][i] = cur
#     return cdist
# print(_distance(data, data))

def _distance(data, centers, metric='euclidean'):
    dis = cdist(data, centers, metric=metric).T
    m = dis.shape[0]
    n = dis.shape[1]
    c = np.ones((m, n)) * 0.7 + 1
    return dis * c


# 计算指数值并规范化数据
def normalize_power_columns(x, exponent):
    assert np.all(x >= 0.0)
    x = x.astype(np.float64)
    x = x / np.max(x, axis=0, keepdims=True)
    x = np.fmax(x, np.finfo(x.dtype).eps)
    if exponent < 0:
        x /= np.min(x, axis=0, keepdims=True)
        x = x ** exponent
    else:
        x = x ** exponent
    result = normalize_columns(x)
    return result


def _cmeans0(data, u_old, c, m, metric):
    # Normalizing, then eliminating any potential zero values.
    u_old = normalize_columns(u_old)
    u_old = np.fmax(u_old, np.finfo(np.float64).eps)
    um = u_old ** m

    # Calculate cluster centers
    data = data.T
    cntr = um.dot(data) / np.atleast_2d(um.sum(axis=1)).T

    d = _distance(data, cntr, metric)
    d = np.fmax(d, np.finfo(np.float64).eps)
    jm = (um * d ** 2).sum()
    u = normalize_power_columns(d, - 2. / (m - 1))
    return cntr, u, jm, d


# 归一化数据
def normalize_columns(columns):
    normalized_columns = columns / np.sum(columns, axis=0, keepdims=1)
    return normalized_columns


# 分类指标
def _fp_coeff(u):
    n = u.shape[1]
    return np.trace(u.dot(u.T)) / float(n)


# FCM
def cmeans(data, c, m, error, maxiter, metric='euclidean', init=None, seed=None):
    # Setup u0
    if init is None:
        if seed is not None:
            np.random.seed(seed=seed)
        n = data.shape[1]  # 样本数
        u0 = np.random.rand(c, n)  # 随机产生初始u0
        u0 = normalize_columns(u0)  # 归一化u0
        init = u0.copy()
    u0 = init

    # numpy.fmax()计算数组元素的逐元素最大值。此函数比较两个数组，并返回一个新的包含元素级最大值的数组。
    # 如果要比较的元素之一是NaN，则返回non-nan元素。如果两个元素均为NaN，则返回第一个。
    # finfo函数是根据括号中的类型来获得信息，获得符合这个类型的数型，eps是取非负的最小值
    u = np.fmax(u0, np.finfo(np.float64).eps)

    # Initialize loop parameters
    jm = np.zeros(0)  # 目标值J
    p = 0  # 迭代次数

    # Main cmeans loop
    while p < maxiter - 1:
        u2 = u.copy()
        [cntr, u, Jjm, d] = _cmeans0(data, u2, c, m, metric)
        jm = np.hstack((jm, Jjm))
        p += 1

        # Stopping rule
        if np.linalg.norm(u - u2) < error:
            break

    # Final calculations
    error = np.linalg.norm(u - u2)
    fpc = _fp_coeff(u)

    return cntr, u, u0, d, jm, p, fpc


# FCM 注意data的数据格式，shape是类似（特征数目，数据个数）与很多训练数据的shape正好是相反的
train = train.T
center, u, u0, d, jm, p, fpc = cmeans(train, m=2, c=5, error=0.005, maxiter=1000)
print(fpc)
for i in u:
    # np.argmax()表示最大值的索引axis=0表示每一行为一组，得到的label即为最后模糊分簇的确定性结果
    label = np.argmax(u, axis=0)
print(label)

# 颜色映射
cmap = {0: 'r', 1: 'g', 2: 'b', 3: 'k', 4: 'm', 5: 'y', 6: 'c'}
for i in range(len(train[0])):
    plt.scatter(train[0][i], train[1][i], c=cmap[label[i]])
plt.show()

# 0.7019734655242942 0.6195384227929872 0.587218713463331
# 0.7238128118947551 0.6286535730794628 0.6473437168926698 0.6334867548376991 0.595992245694624