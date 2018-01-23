# coding=utf-8
'''
基于邻接矩阵寻找连通分支算法：
如果a(i,j)=0，表示i到j没有通路；
'''

from numpy.random import rand
import numpy as np


# 利用求并集和交集的方法
def cccomplex(adjacencyMat):
    def power(adjacencyMatPower, adjacencyMat):
        adjacencyMatPower *= adjacencyMat
        return adjacencyMatPower

    dimension = np.shape(adjacencyMat)[0]
    eye = np.mat(np.eye(dimension))
    adjacencyMat = np.mat(adjacencyMat)
    adjacencyMatPower = adjacencyMat.copy()
    result = np.logical_or(eye, adjacencyMat)
    for i in range(dimension):
        adjacencyMatPower = power(adjacencyMatPower, adjacencyMat)
        result = np.logical_or(result, adjacencyMatPower)
    final = np.logical_and(result, result.T)
    return final


# 利用求矩阵逆的方法
def connectedConponents(adjacencyMat, alpha=0.5):
    n = np.shape(adjacencyMat)[0]
    E = np.eye(n)
    ccmatrix = np.mat(E - alpha * adjacencyMat)

    return ccmatrix.I


def init(dimension):
    mat = np.ones((dimension, dimension))
    mat[(rand(dimension) * dimension).astype(int), (rand(dimension) * dimension).astype(int)] = 0
    return mat


if __name__ == "__main__":
    dimension = 4
    adjacencyMat = init(dimension)
    adjacencyMat1 = np.array([[0, 1, 0, 0, 0, 0, 0, 0], [0, 0, 1, 0, 1, 1, 0, 0],
                              [0, 0, 0, 1, 0, 0, 1, 0],
                              [0, 0, 1, 0, 0, 0, 0, 1],
                              [1, 0, 0, 0, 0, 1, 0, 0],
                              [0, 0, 0, 0, 0, 0, 1, 0],
                              [0, 0, 0, 0, 0, 1, 0, 1],
                              [0, 0, 0, 0, 0, 0, 0, 1]])
    adjacencyMat2 = np.array([[0, 1, 1, 0], [1, 0, 0, 0], [1, 0, 0, 0], [0, 0, 0, 0]])
    print(cccomplex(adjacencyMat1))  # (A, B, E) (C, D) (F, G) (H)
    print(connectedConponents(adjacencyMat2))
    # [[ True  True False False  True False False False]
    # [ True  True False False  True False False False]
    # [False False  True  True False False False False]
    # [False False  True  True False False False False]
    # [ True  True False False  True False False False]
    # [False False False False False  True  True False]
    # [False False False False False  True  True False]
    # [False False False False False False False  True]]
    # [[ 2.   1.   1.   0. ]
    # [ 1.   1.5  0.5  0. ]
    # [ 1.   0.5  1.5  0. ]
    # [ 0.   0.   0.   1. ]]