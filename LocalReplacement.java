import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class LocalReplacement {
    private int n; // number of vertex
    private int[][] adjacentMatrix; // adjacent matrix

    /**
     * 默认构造函数
     * 顶点数为6的3正则图
     */
    LocalReplacement(){
        n = 6;
        adjacentMatrix = new int[n+1][n+1];
    }

    /**
     * 构造函数
     * @param n 顶点个数
     * @param adjacentMatrix 邻接矩阵
     */
    LocalReplacement(int n, int [][]adjacentMatrix){
        this.n = n;
        this.adjacentMatrix = adjacentMatrix;
    }

    /**
     * 得到该点单独覆盖边数
     * @param v 顶点编号
     * @param arrayList 当前解集
     * @return 该点在当前解集基础上单独覆盖边数
     */
    int getSingleCover(int v, ArrayList<Integer> arrayList){
        int result = 0;
        for(int i = 1; i <= n; i++){
            if((adjacentMatrix[v][i] == 1) && !arrayList.contains(i))
                result++;
        }
        return result;
    }

    /**
     * 得到该点覆盖边数，单独覆盖记为1，共同覆盖记为0.5
     * @param v 顶点编号
     * @param arrayList 局部替换解集
     * @return 该点覆盖边数
     */
    double getCover(int v, ArrayList<Integer> arrayList){
        double result = 0;
        for(int i = 1; i <= n; i++) {
            if(arrayList.contains(i)){
                result += 0.5*adjacentMatrix[v][i];
            }else {
                result += adjacentMatrix[v][i];
            }
        }
        return result;
    }

    /**
     * 局部替换算法
     * @param k 选择k个顶点
     * @return 局部替换规则下的解，即算法求解出来的k个点覆盖边的数目
     */
    int alg(int k){
        double result = 0;
        ArrayList<Integer> arrayList = new ArrayList<>();
        for(int i = 1; i <= k; i++){
            arrayList.add(i);
        }
        boolean flag = false;
        while(!flag){
            flag = true;
            for(int i = 0; i < arrayList.size(); i++){
                int baseNum = arrayList.get(i);
                arrayList.remove(i);
                int baseRes = getSingleCover(baseNum, arrayList); // 单独覆盖边数
                int compNum = baseNum, compRes;
                for(int j = 1; j <= n; j++){
                    if(!arrayList.contains(j) && j != baseNum){
                        compRes = getSingleCover(j, arrayList);
                        if(compRes > baseRes){
                            flag = false;
                            baseRes = compRes;
                            compNum = j;
                        }
                    }
                }
                arrayList.add(i, compNum);
            }
        }
        System.out.print("arrayList: ");
        for (int temp: arrayList) {
            result += getCover(temp, arrayList); // 覆盖边数
            System.out.print(temp + " ");
        }

        return (int)result;
    }

    /**
     * 读取随机图文件，将图信息保存到map中进行局部替换处理
     * @param file 路径文件
     * @return 返回随机图标识和邻接矩阵的map
     */
    static HashMap<String, int[][]> readFromFile(File file){
        HashMap<String, int[][]> map = new HashMap<>();
        ArrayList<String> arrayList = new ArrayList<>();
        try{
            FileReader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String str;
            // 按行读取字符串
            while((str =bufferedReader.readLine()) != null)
                arrayList.add(str);
            bufferedReader.close();
            reader.close();
        } catch(IOException e){
            e.printStackTrace();
        }
//        System.out.println("arrayList.size() " + arrayList.size());
        for(int i = 0; i < arrayList.size(); ){
            // 随机图标识
            if(arrayList.get(i).contains("+")){
                String nk = arrayList.get(i);
//                System.out.println("nk " + nk);
                String strnk[] = nk.split("\\+");
                // 随机图顶点个数
                int n = Integer.parseInt(strnk[0]);
                // 将随机图矩阵保存
                int [][]adjMtr = new int[n+1][n+1];
                for(int j = i+1; j <= i+n; j++){
                    String []temp = arrayList.get(j).split(" ");
                    for(int k = 0; k < n; k++){
                        adjMtr[j-i][k+1] = Integer.parseInt(temp[k]);
                    }
                }
                // map保存随机图标识和邻接矩阵
                map.put(nk, adjMtr);
                i = i + n + 1;
            }
        }
        arrayList.clear();

        return map;
    }

    /**
     * 将局部替换方法得到的解写入文件中
     * @param file 文件
     * @param s 标识字符，"v"意为顶点数，"k"意为选k个顶点，
     *         "r"意为结果，解集覆盖边数，"n"意为换行， "a"意为平均值
     * @param num 根据s不同而不同，分别为顶点数、k、覆盖边数、null
     * @param avg 根据s不同而不同，分别为平均覆盖边数、平均运行时间
     */
    static void writeInFile(File file, String s, int num, double avg){
        Writer writer = null;
        try{
            writer = new FileWriter(file, true); // true表示追加
            if(s.equals("v")){
                writer.write(s + " " + num + "\n");
            } else if(s.equals("k")){
                writer.write(s + " " + num + "\n");
            } else if(s.equals("r")){
                writer.write(num + " ");
            } else if(s.equals("n")){
                writer.write("\n");
            } else if(s.equals("a")){
                writer.write(avg + " ");
            }
        } catch(IOException e){
            e.printStackTrace();
        } finally {
            try{
                writer.close();
            } catch(IOException e2){
                e2.printStackTrace();
            }
        }
    }

    public static void main(String []args){
        File fileR = new File("D:\\projects\\vertexCover\\src\\Rand3RegGraph.txt");
        if(!fileR.exists()){
            System.out.println("fileR does not exist!");
        }

        // File fileW = new File("D:\\projects\\vertexCover\\src\\LRResult.txt");
        File fileW_p = new File("D:\\projects\\vertexCover\\src\\LRResult_p.txt");
        if(!fileW_p.exists()){
            try{
                fileW_p.createNewFile(); // 如果文件不存在则创建文件
            } catch(IOException e){
                e.printStackTrace();
            }
        }

        // 随机图标识和邻接矩阵的map
        HashMap<String, int[][]> map = readFromFile(fileR);
        LocalReplacement lr;
        int i, k, samNum;

        // for plot
        for(i = 100; i <= 1000;){
            System.out.println("v = " + i);
            writeInFile(fileW_p, "v", i, 0.0);
            for(k = (int)(0.3*i); k <= (int)(0.6*i); k += (int)(0.1*i)){
                System.out.println("k = " + k);
                writeInFile(fileW_p, "k", k, 0.0);
                int avg_r = 0, avg_t = 0;
                for(samNum = 0; samNum <= 9; samNum++){
                    System.out.print("samNum = " + samNum + "\t");
                    long startTime = System.currentTimeMillis(); // 获取开始时间
                    lr = new LocalReplacement(i, map.get(i+"+"+samNum));
                    int result = lr.alg(k);
                    long endTime = System.currentTimeMillis(); // 获取结束时间
                    writeInFile(fileW_p, "r", result, 0.0);
                    avg_r += result;
                    avg_t += (endTime - startTime);
                    System.out.println("result: " + result);
                }

                writeInFile(fileW_p, "a", 0, (double)avg_r/10);
                writeInFile(fileW_p, "a", 0, (double)avg_t/10);
                writeInFile(fileW_p, "n", 0, 0.0);
            }

            i += 100;
        }
        /**
        for(i = 6; i <= 1000;){
            System.out.println("v = " + i);
            writeInFile(fileW, "v", i);
            for(k = (i+6)/4; k < i-2; ){
                System.out.println("k = " + k);
                writeInFile(fileW, "k", k);
                for(samNum = 0; samNum <= 9; samNum++){
                    System.out.print("samNum = " + samNum + "\t");
                    lr = new LocalReplacement(i, map.get(i+"+"+samNum));
                    int result = lr.alg(k);
                    writeInFile(fileW, "r", result);
                    System.out.println("result: " + result);
                }
                writeInFile(fileW, "n", 0);
                if(k < 10){
                    k++;
                } else if(k < 30){
                    k += 2;
                } else if(k < 100){
                    k += 10;
                } else {
                    k += 100;
                }
            }

            if (i < 50) { // 少于50顶点的步长为2
                i += 2;
            } else if(i < 100) { // [50, 100]区间的步长为10
                i += 10;
            } else { // [100, 1000]区间的步长为100
                i += 100;
            }
        }
         **/

    }

}

