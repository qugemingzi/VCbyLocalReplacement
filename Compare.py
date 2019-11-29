# encoding=utf-8
import ILP


def compare(fileLR, fileILP, fileCmp):
    '''
    将LR与ILP结果比对，并存储之
    :param fileLR: LR方法结果储存路径
    :param fileILP: ILP方法结果储存路径
    :param fileCmp: 结果比对存储路径
    :return: null
    '''
    # 打开LR结果文本
    with open(fileLR, "r") as fLR:
        lstLR = fLR.readlines()
    for _ in range(len(lstLR)):
        # 去换行符
        lstLR[_] = lstLR[_].strip("\n")

    # 打开ILP结果文本
    with open(fileILP, "r") as fILP:
        lstILP = fILP.readlines()
    for _ in range(len(lstILP)):
        # 去换行符
        lstILP[_] = lstILP[_].strip("\n")

    # 存储所有的比值
    lst = []
    for _ in range(len(lstILP)):
        if "v" in lstILP[_]:
            strv = str(lstILP[_]).split(" ")
            print("v = ", int(strv[1]))
            ILP.write_in_file(fileCmp, "v", int(strv[1]))
        elif "k" in lstILP[_]:
            strk = str(lstILP[_]).split(" ")
            print("k = ", int(strk[1]))
            ILP.write_in_file(fileCmp, "k", int(strk[1]))
        else:
            strLR = str(lstLR[_]).split(" ")
            strILP = str(lstILP[_]).split(" ")
            for i in range(len(strLR)-1):
                # 结果保留6位小数
                result = float('%.6f' % float(int(strILP[i])/int(strLR[i])))
                lst.append(result)
                print(result, "\t", end=" ")
                ILP.write_in_file(fileCmp, "r", result)
            print()
            ILP.write_in_file(fileCmp, "n", 0)
    lst.sort(reverse=True)
    print("length = ", len(lst))
    print("Max = ", lst[0])
    for _ in range(len(lst)):
        print(lst[_], " ", end=" ")
    print()
    ILP.write_in_file(fileCmp, "r", f'length {len(lst)}')
    ILP.write_in_file(fileCmp, "n", 0)
    ILP.write_in_file(fileCmp, "r", f'Max {lst[0]}')
    ILP.write_in_file(fileCmp, "n", 0)


if __name__ == "__main__":
    fileLR = "LRResult.txt"
    fileILP = "ILPResult.txt"
    fileCmp = "CmpResult.txt"
    compare(fileLR, fileILP, fileCmp)
