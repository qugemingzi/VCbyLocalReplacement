# encoding=utf-8
import numpy as np


def read_from_file(file):
    '''
    读取随机图文件，将图信息保存到dict中进行整数线性规划
    :param file: 路径文件
    :return: 返回随机图标识和邻接矩阵的dict
    '''
    with open(file, "r") as f:
        lst = f.readlines()
    for i in range(len(lst)):
        lst[i] = lst[i].strip("\n")
    dic = {}
    i = 0
    while i < len(lst):
        if "+" in lst[i]:
            nk = lst[i]
            strnk = nk.split("+")
            n = int(strnk[0])
            adjMtr = np.zeros((n+1, n+1), dtype=np.int)
            for j in range(i+1, i+n+1):
                temp = lst[j].split(" ")
                for k in range(n):
                    adjMtr[j-i][k+1] = int(temp[k])
            dic[nk] = adjMtr
            i = i + n + 1
    lst.clear()
    return dic


if __name__ == "__main__":
    fileR = "D:\\projects\\vertexCover\\src\\Rand3RegGraph.txt"
    dic = read_from_file(fileR)
    # print(len(dic))
