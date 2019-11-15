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
        for(int i = 0; i < arrayList.size(); ){
            if(arrayList.get(i).contains("+")){
                String nk = arrayList.get(i);
                int n = Integer.parseInt(nk.charAt(0)+"");
                int [][]adjMtr = new int[n+1][n+1];
                for(int j = i+1; j <= i+n; j++){
                    String []temp = arrayList.get(j).split(" ");
                    for(int k = 0; k < n; k++){
                        adjMtr[j-i][k+1] = Integer.parseInt(temp[k]);
                    }
                }
                map.put(nk, adjMtr);
                i = i + n + 1;
            }
        }
        arrayList.clear();

        return map;
    }

    public static void main(String []args){
        File file = new File("D:\\projects\\vertexCover\\src\\Rand3RegGraph.txt");
        if(!file.exists()){
            System.out.println("file does not exist!");
        }

        HashMap<String, int[][]> map = readFromFile(file);
        System.out.println(map.size());
        int n = 6;
        int adj[][] = map.get("6+0");
        for(int i = 1; i <= n; i++){
            for(int j = 1; j <= n; j++){
                System.out.print(adj[i][j] + " ");
            }System.out.println();
        }
        n = 50;
        adj = map.get("50+1");
        for(int i = 1; i <= n; i++){
            for(int j = 1; j <= n; j++){
                System.out.print(adj[i][j] + " ");
            }System.out.println();
        }
        n = 1000;
        adj = map.get("1000+9");
        for(int i = 1; i <= n; i++){
            for(int j = 1; j <= n; j++){
                System.out.print(adj[i][j] + " ");
            }System.out.println();
        }
    }

}
