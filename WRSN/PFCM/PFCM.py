import numpy as np
import matplotlib.pyplot as plt
from sklearn.cluster import KMeans
from skfuzzy.cluster import cmeans
from scipy.spatial.distance import cdist

netSize = 500  # 网络大小
nodeCnt = 100  # 节点数量

# [0, netSize) 产生nodeCnt个节点数据
cp = np.random.uniform(0, netSize, (nodeCnt, 2))

# 文件名
name = 'node_' + str(netSize) + '_' + str(nodeCnt) + '.txt'
# name_1 = 'node_' + str(netSize) + '_' + str(nodeCnt) + '_1.txt'
# 是否保存节点数据
# np.savetxt(name, cp, fmt="%.2f", delimiter="\t")
# train = cp[:]
train = np.loadtxt(name)  # 将文件中数据加载到data数组里


# 距离因子（带能耗影响因子）
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
print(center)
# print(u)

file_u = 'node_U_ik.txt'
np.savetxt(file_u, u.T, fmt="%.3f", delimiter="\t")

for i in u:
    # np.argmax()表示最大值的索引axis=0表示每一行为一组，得到的label即为最后模糊分簇的确定性结果
    label = np.argmax(u, axis=0)
print(label)

# 颜色映射
cmap = {0: 'r', 1: 'g', 2: 'b', 3: 'k', 4: 'm', 5: 'y', 6: 'c'}
for i in range(nodeCnt):
    plt.scatter(train[0][i], train[1][i], c=cmap[label[i]])
plt.show()
