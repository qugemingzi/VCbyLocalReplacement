import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class LocalReplacement {
    private int n; // number of vertex
    private int k; // k-vertex cover
    private int[][] adjacentMatrix; // adjacent matrix

    LocalReplacement(){
        n = 6;
        k = 3;
        adjacentMatrix = new int[n+1][n+1];
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

    public static void main(String []args){
        File file = new File("C:\\Users\\zhoup\\Documents\\project\\java\\src\\Rand3RegGraph.txt");
        if(!file.exists()){
            System.out.println("file does not exist!");
        }

        HashMap<String, int[][]> map = readFromFile(file);

    }

}
