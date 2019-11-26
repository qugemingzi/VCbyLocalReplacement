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
            adjMtr = np.zeros((n+1, n+1), dtype=np.int)
            for j in range(i+1, i+n+1):
                temp = lst[j].split(" ")
                for k in range(n):
                    adjMtr[j-i][k+1] = int(temp[k])
            dic[nk] = adjMtr
            i = i + n + 1
    lst.clear()
    return dic


class ILP(object):

    def __init__(self, n, adjacentMatrix):
        self.n = n
        self.adjacentMatrix = adjacentMatrix

    def opt(self, k):
        result = 0
        z = [0 for _ in range(int(self.n*5/2))]
        for _ in range(int(self.n*3/2)):
            z[_+self.n] = 1
        st_eq = [0 for _ in range(int(self.n*5/2))]
        for _ in range(int(self.n * 5 / 2)):
            st_eq[_] = 1-z[_]
        st_geq = np.zeros((int(self.n*3/2), int(self.n*5/2)), dtype=np.int).tolist()
        edge = 0
        for i in range(1, self.n+1):
            for j in range(i, self.n+1):
                if self.adjacentMatrix[i][j] == 1:
                    st_geq[edge][i-1] = 1
                    st_geq[edge][j-1] = 1
                    st_geq[edge][edge+self.n] = -1
                    edge += 1
        b = [0 for _ in range(int(self.n*3/2))]
        problem = pulp.LpProblem(sense=pulp.LpMaximize)
        x = [pulp.LpVariable(f'x{i}', lowBound=0, upBound=1, cat=pulp.LpInteger) for i in range(1, int(self.n*5/2)+1)]
        problem += pulp.lpDot(z, x)

        problem += pulp.lpDot(st_eq, x) == k
        for i in range(len(st_geq)):
            problem += (pulp.lpDot(st_geq[i], x) >= b[i])
        problem.solve()
        result = pulp.value(problem.objective)
        print(result)
        print([pulp.value(var) for var in x])
        return result


if __name__ == "__main__":
        fileR = "D:\\projects\\vertexCover\\src\\test.txt"
        dic = read_from_file(fileR)
        print(len(dic))
        i = 6
        while i <= 10:
            print("v = ", i)
            k = int((i+6)/4)
            while k < i-2:
                print("k = ", k)
                samNum = 0
                while samNum <= 9:
                    print("samNum = ", samNum, "\t", end=" ")
                    ilp = ILP(i, dic[f'{i}+{samNum}'])
                    result = ilp.opt(k)
                    print("result = ", result)
                    samNum += 1
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


