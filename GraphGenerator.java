import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;

public class GraphGenerator {
    private int n; // number of vertex
    private int[][] adjacentMatrix; // adjacent matrix

    /**
     * 默认构造函数
     * n = 4, 顶点个数为4
     **/
    GraphGenerator(){
        n = 4;
        adjacentMatrix = new int[n + 1][n + 1];
    }

    /**
     * 构造函数
     * @param n 顶点个数
     */
    GraphGenerator(int n){
        this.n = n;
        adjacentMatrix = new int[n + 1][n + 1];
    }

    /**
     * 返回当前节点度数
     * @param i 当前节点
     * @return 该节点当前度数
     */
    int getCurrentDegree(int i){
        int degree = 0;
        for(int j = 1; j <= n; j++)
            degree += adjacentMatrix[i][j];
        return degree;
    }

    /**
     * 返回当前度数为3满足的点集，随机生成时不用考虑这些点
     * @param i 顶点i，只考虑顶点i之后的顶点
     * @return 顶点集合
     */
    HashSet<Integer> getFullDegree(int i){
        HashSet<Integer> set = new HashSet<>();
        for(int j = i + 1; j <= n; j++){
            if(getCurrentDegree(j) >= 3)
                set.add(j);
        }
        return set;
    }

    /**
     * 生成[min, max]区间中number个随机数
     * @param min 区间最小值
     * @param max 区间最大值
     * @param numbers 随机数个数
     * @param set 随机数集合
     * @param full 之后顶点度数已经到达3的顶点集合
     * @return 返回生成的随机数集合
     */
    HashSet<Integer> randomSet(int min, int max, int numbers, HashSet<Integer> set, HashSet<Integer> full){
        if(min > max)
            return set;
        int randNumber;
        while(set.size() < numbers){
            // 生成[min, max]内的随机数
            randNumber = (int)(Math.random() * (max-min+1)) + min;
            if(!full.contains(randNumber)) // 甄别已经满度的顶点
                set.add(randNumber);
        }

        return set;
    }

    /**
     * 根据生成的随机数集合添加相应的边
     * @param i 顶点
     * @param set 随机数集合，需要添加边集合
     */
    void addEdge(int i, HashSet<Integer> set){
        for(int j: set){
            adjacentMatrix[i][j] = 1;
            adjacentMatrix[j][i] = 1;
        }
    }

    /**
     * 随机3-正则图生成函数
     * 随机生成顶点个数为n的3-正则图
     * @return 构造错误，则返回false，否则返回true
     */
    boolean generator(){
        for(int i = 1; i <= n; i++){
            int randomNumbers = 3 - getCurrentDegree(i);
            HashSet<Integer> full = getFullDegree(i);
            if(n-i-full.size() < randomNumbers || randomNumbers < 0){
                // 构造错误
//                System.out.println("generator() WRONG!");
                // 打印邻接矩阵，检查一哈
//                printAdjacentMatrix();
                // 刷新一下
                flush();
                return false;
            }
            HashSet<Integer> set = new HashSet<>();
            set = randomSet(i+1, n, randomNumbers, set, full);
            addEdge(i, set);
        }

        return true;
    }

    /**
     * 打印adjacent matrix
     */
    void printAdjacentMatrix(){
        System.out.println("current adjacent matrix:");
        for(int i = 1; i <= n; i++){
            for(int j = 1; j <= n; j++)
                System.out.print(adjacentMatrix[i][j] + " ");
            System.out.println();
        }
    }

    /**
     * 刷新邻接矩阵
     */
    void flush(){
        for(int i = 1; i <= n; i++)
            for(int j = 1; j <= n; j++)
                adjacentMatrix[i][j] = 0;
    }

    /**
     * 将随机生成的顶点数为v的3-正则图写入指定文件中
     * @param file 文件
     * @param v 顶点个数
     */
    void writeInFile(File file, int v){
        Writer writer = null;
        try{
            writer = new FileWriter(file, true); // true表示追加
            writer.write(v + "\n");
            String str;
            for(int i = 1; i <= n; i++) {
                str = "";
                for (int j = 1; j <= n; j++) {
                    str += adjacentMatrix[i][j] + " ";
                }
                str += "\n";
                writer.write(str);
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

    public static void main(String args[]){
        File file = new File("D:\\projects\\vertexCover\\src\\Rand3RegGraph.txt");
        if(!file.exists()){
            try{
                file.createNewFile(); // 如果文件不存在则创建文件
            } catch(IOException e){
                e.printStackTrace();
            }
        }

        GraphGenerator gg;
        int i, j = 0; // i为顶点个数，j控制每个例子生成sampleNumbers个随机图
        int sampleNumbers = 10; // 每个顶点数生成sampleNumbers个随机图
        for(i = 6; i <= 1000; ) {
            if(j == sampleNumbers){
                if (i < 50) { // 少于50顶点的步长为2
                    i += 2;
                } else if(i < 100) { // [50, 100]区间的步长为10
                    i += 10;
                }else{ // [100, 1000]区间的步长为100
                    i += 100;
                }
            }else {
                gg = new GraphGenerator(i);
                System.out.println("v = " + i);
                boolean flag = true; // 判断随机图是否生成成功
                while (flag) {
                    if (gg.generator()) {
                        gg.writeInFile(file, i);
                        gg.printAdjacentMatrix();
                        flag = false;
                    }
                }
            }
            j = (j+1)%(sampleNumbers+1);
        }
    }
}
