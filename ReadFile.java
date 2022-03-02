import java.io.*;
import java.util.*;

public class ReadFile {
    public static String line = "";
    public static String file_location = "C:\\Users\\musta\\Desktop\\Hacettepe\\DREAM\\lastfm_asia_edges.csv";
    public static ArrayList<String> lines = new ArrayList<String>();

    public ReadFile() throws IOException {
        BufferedReader br = new BufferedReader((new FileReader(file_location)));

        while ((line = br.readLine()) != null){
            lines.add(line);
        }
    }

    public Graph createGraph(){
        Graph graph = new Graph();

        for (String line: lines) {
            String[] splitted = line.split(",");

            if (!graph.map.containsKey(Integer.parseInt(splitted[0]))){
                UndirectedNode node1 = new UndirectedNode(Integer.parseInt(splitted[0]));
                graph.map.put(node1.id, node1);
            }

            if (!graph.map.containsKey(Integer.parseInt(splitted[1]))){
                UndirectedNode node2 = new UndirectedNode(Integer.parseInt(splitted[1]));
                graph.map.put(node2.id, node2);
            }

            graph.connect(Integer.parseInt(splitted[0]), Integer.parseInt(splitted[1]));

        }

        for(int id: graph.map.keySet()){
            Collections.sort(graph.map.get(id).adjacency_list);
        }

        return graph;
    }
}
