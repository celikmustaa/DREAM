import java.util.*;

public class Graph {
    public HashMap<Integer, UndirectedNode> map; // node_id: node
    public ArrayList<ArrayList<UndirectedNode>> edge_list; // [[node1, node2],[n3, n4]]

    public Graph(){
        this.map = new HashMap<Integer, UndirectedNode>();
        this.edge_list = new ArrayList<ArrayList<UndirectedNode>>();
    }

    public void connect(int node1_id, int node2_id){
        UndirectedNode node1 = this.map.get(node1_id);
        UndirectedNode node2 = this.map.get(node2_id);

        node1.adjacency_list.add(node2_id);
        node2.adjacency_list.add(node1_id);

        node1.degree++; node2.degree++;

        ArrayList<UndirectedNode> edge = new ArrayList<UndirectedNode>();
        edge.add(node1); edge.add(node2);
        this.edge_list.add(edge);

        Collections.sort(node1.adjacency_list);
        Collections.sort(node2.adjacency_list);

    }

    public void disconnect(int node1_id, int node2_id){
        UndirectedNode node1 = this.map.get(node1_id);
        UndirectedNode node2 = this.map.get(node2_id);

        node1.adjacency_list.remove(node2_id);
        node2.adjacency_list.remove(node1_id);

        node1.degree--; node2.degree--;

        for (ArrayList<UndirectedNode> edge: edge_list){
            if (edge.contains(node1) && edge.contains(node2)){
                this.edge_list.remove(edge);
                break;
            }
        }
    }
}

