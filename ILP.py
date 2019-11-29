# encoding=utf-8
import numpy as np
import pulp


def read_from_file(file):
    '''
    读取随机图文件，将图信息保存到dict中进行整数线性规划
    :param file: 路径文件
    :return: 返回随机图标识和邻接矩阵的dict
    '''
    with open(file, "r") as f:
        lst = f.readlines()
    for i in range(len(lst)):
        # 去换行符
        lst[i] = lst[i].strip("\n")
    # 字典保存顶点数和邻接矩阵
    dic = {}
    i = 0
    while i < len(lst):
        if "+" in lst[i]:
            # 顶点数+编号
            nk = str(lst[i])
            strnk = nk.split("+")
            # 顶点数
            n = int(strnk[0])
            # 邻接矩阵
            adjMtr = np.zeros((n+1, n+1), dtype=np.int)
            for j in range(i+1, i+n+1):
                temp = lst[j].split(" ")
                for k in range(n):
                    adjMtr[j-i][k+1] = int(temp[k])
            # 图信息存储到字典中
            dic[nk] = adjMtr
            i = i + n + 1
    lst.clear()
    return dic


def write_in_file(file, s, num):
    '''
    将整数线性规划结果存储到文本中
    :param file: 文本路径
    :param s: 标识字符，"v"顶点，"k"k个顶点的顶点覆盖，"r"结果，"n"换行
    :param num: 顶点个数、k、结果、0
    :return: null
    '''
    with open(file, 'a') as f:
        if s == "v":
            f.write(f'{s} {num}\n')
        elif s == "k":
            f.write(f'{s} {num}\n')
        elif s == "r":
            f.write(f'{num} ')
        elif s == "n":
            f.write("\n")


class ILP(object):

    def __init__(self, n, adjacentMatrix):
        '''
        初始化
        :param n: 顶点数
        :param adjacentMatrix: 邻接矩阵
        '''
        self.n = n
        self.adjacentMatrix = adjacentMatrix

    def opt(self, k):
        '''
        调用pulp库求解整数线性规划
        :param k: 限制顶点个数
        :return: 最优解，k个点覆盖最多的边
        '''
        # 目标函数的系数
        z = [0 for _ in range(int(self.n*5/2))]
        for _ in range(int(self.n*3/2)):
            z[_+self.n] = 1
        # 等式约束
        st_eq = [0 for _ in range(int(self.n*5/2))]
        for _ in range(int(self.n*5/2)):
            st_eq[_] = 1-z[_]
        # 大于等于式约束
        st_geq = np.zeros((int(self.n*3/2), int(self.n*5/2)), dtype=np.int).tolist()
        edge = 0
        # 遍历邻接矩阵，生成不等式约束
        for i in range(1, self.n+1):
            for j in range(i, self.n+1):
                if self.adjacentMatrix[i][j] == 1:
                    st_geq[edge][i-1] = 1
                    st_geq[edge][j-1] = 1
                    st_geq[edge][edge+self.n] = -1
                    edge += 1
        b = [0 for _ in range(int(self.n*3/2))]
        # 最大化问题
        problem = pulp.LpProblem(sense=pulp.LpMaximize)
        # 定义变量放到列表中，[0, 1]的整数变量
        x = [pulp.LpVariable(f'x{i}', lowBound=0, upBound=1, cat=pulp.LpInteger) for i in range(1, int(self.n*5/2)+1)]
        # 定义目标函数，lpDot为两个向量点乘
        problem += pulp.lpDot(z, x)

        # 设置等式约束条件
        problem += pulp.lpDot(st_eq, x) == k
        # 设置不等式约束条件
        for i in range(len(st_geq)):
            problem += (pulp.lpDot(st_geq[i], x) >= b[i])
        # 求解问题
        problem.solve()
        # 输出结果
        print(pulp.value(problem.objective))
        print([pulp.value(var) for var in x])
        return int(pulp.value(problem.objective))


if __name__ == "__main__":
        fileR = "D:\\projects\\vertexCover\\src\\Rand3RegGraph.txt"
        fileW = "D:\\projects\\vertexCover\\src\\ILPResult.txt"
        dic = read_from_file(fileR)
        print(len(dic))
        i = 6
        while i <= 1000:
            print("v = ", i)
            write_in_file(fileW, "v", i)
            k = int((i+6)/4)
            while k < i-2:
                print("k = ", k)
                write_in_file(fileW, "k", k)
                samNum = 0
                while samNum <= 9:
                    print("samNum = ", samNum, "\t", end=" ")
                    ilp = ILP(i, dic[f'{i}+{samNum}'])
                    result = ilp.opt(k)
                    print("result = ", result)
                    write_in_file(fileW, "r", result)
                    samNum += 1
                write_in_file(fileW, "n", 0)
                if k < 10:
                    k += 1
                elif k < 30:
                    k += 2
                elif k < 100:
                    k += 10
                else:
                    k += 100
            if i < 50:
                i += 2
            elif i < 100:
                i += 10
            else:
                i += 100


