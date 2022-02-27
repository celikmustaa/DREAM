import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

public class CountMotifs {
    public Graph graph;
    public int[] Te_array;
    public long[] cumulative_Te_array;
    public double[] Pe_array;
    public int W;

    public CountMotifs(Graph graph){
        this.graph = graph;
        this.Te_array= new int[graph.edge_list.size()];
        this.cumulative_Te_array = new long[graph.edge_list.size()];
        this.Pe_array = new double[graph.edge_list.size()];
        this.W = 0;

        for(int i = 0; i < graph.edge_list.size(); i++){
            ArrayList<UndirectedNode> edge = this.graph.edge_list.get(i);
            this.Te_array[i] = (this.graph.map.get(edge.get(0).id).adjacency_list.size() - 1) * (this.graph.map.get(edge.get(1).id).adjacency_list.size() - 1);
            this.cumulative_Te_array[i] = (i == 0) ? Te_array[i] : Te_array[i-1] + Te_array[i];
            this.W += this.Te_array[i];
        }

        for(int i = 0; i < graph.edge_list.size(); i++){
            ArrayList<UndirectedNode> edge = graph.edge_list.get(i);
            this.Pe_array[i] = this.Te_array[i]/this.W;
        }
    }

    public ArrayList<ArrayList<UndirectedNode>> sample(){
        UndirectedNode u1, u0, v0, v1; // u=u0, v=v0, u'=u1, v'=v1

        // select a random edge and set u and v
        int randomNumber = ThreadLocalRandom.current().nextInt(0, this.W);
        ArrayList<UndirectedNode> random_edge = graph.edge_list.get(binarySearch(randomNumber, 0, this.graph.edge_list.size()));
        u0 = random_edge.get(0); v0 = random_edge.get(1);

        // set u'
        do {randomNumber = ThreadLocalRandom.current().nextInt(0, u0.adjacency_list.size());}
        while (u0.adjacency_list.get(randomNumber) == v0.id);
        u1 = graph.map.get(randomNumber);

        // set v'
        do {randomNumber = ThreadLocalRandom.current().nextInt(0, v0.adjacency_list.size());}
        while (v0.adjacency_list.get(randomNumber) == u0.id);
        v1 = graph.map.get(randomNumber);

        // return the three edges
        ArrayList<ArrayList<UndirectedNode>> output = new ArrayList<ArrayList<UndirectedNode>>();
        ArrayList<UndirectedNode> edge1 = new ArrayList<UndirectedNode>();
        edge1.add(u1);
        edge1.add(u0);
        output.add(edge1);
        ArrayList<UndirectedNode> edge2 = new ArrayList<UndirectedNode>();
        edge2.add(u0);
        edge2.add(v0);
        output.add(edge2);
        ArrayList<UndirectedNode> edge3 = new ArrayList<UndirectedNode>();
        edge3.add(v0);
        edge3.add(v1);
        output.add(edge3);
        return output;
    }


    // returns an index so that randomNumber is either smaller than or equal to the number in
    // the cumulative array at that index but also greater than the number before that index
    public int binarySearch(int randomNumber, int left, int right){
        while (true){
            if (randomNumber <= this.cumulative_Te_array[left]){
                return left;
            }

            int middle = left + (right-left)/2;

            if (randomNumber <= this.cumulative_Te_array[middle] && randomNumber > this.cumulative_Te_array[middle-1]){
                return middle;
            }

            if (randomNumber <= this.cumulative_Te_array[middle]){
                right = middle - 1;
            }
            else{
                left = middle + 1;
            }
        }
    }

    public double[] three_path_sampler(int k){
        // ArrayList<ArrayList<ArrayList<UndirectedNode>>> set_of_edge_sets = new ArrayList<ArrayList<ArrayList<UndirectedNode>>>();
        int[] count_i = {0,0,0,0,0,0};
        double[] C_i = {0,0,0,0,0,0}; // sapkali C
        int[] A2_i = {0, 1, 2, 4, 6, 12};

        for (int i = 0; i < k; i++){
            ArrayList<ArrayList<UndirectedNode>> edge_set = sample();
            // set_of_edge_sets.add(edge_set);

            switch (determine_induced_motif(edge_set.get(0).get(0).id, edge_set.get(0).get(1).id, edge_set.get(3).get(0).id, edge_set.get(3).get(1).id)){
                case 0:             // triangle or not connected
                    break;
                case 1:
                    count_i[0]++;   // 3-star
                    break;
                case 2:
                    count_i[1]++;   // 3-path
                    break;
                case 3:
                    count_i[2]++;   // tailed-triangle
                    break;
                case 4:
                    count_i[3]++;   // 4-cycle
                    break;
                case 5:
                    count_i[4]++;   // chordal-4-cycle
                    break;
                case 6:
                    count_i[5]++;   // 4-clique
                    break;
            }
        }

        for (int i = 1; i < 6; i++){
            C_i[i] = (count_i[i] / k) * (this.W / A2_i[i]);
        }

        int N_1 = 0;
        for(int key: graph.map.keySet()){
            int degree = graph.map.get(key).degree;
            N_1 += (degree)*(degree-1)*(degree-2) / 6;  // degree combination three
        }

        C_i[0] = N_1 - C_i[2] - 2*C_i[4] - 4*C_i[5];

        return C_i;
    }

    public int[] brute_force_count(){
        int[] C_i = {0,0,0,0,0,0};
        ArrayList<Integer> keyList = new ArrayList<Integer>(graph.map.keySet());

        for(int i = 0; i < keyList.size()-3; i++){
            for(int j = i+1; j < keyList.size()-2; j++){
                for(int k = j+1; k < keyList.size()-1; k++){
                    for(int l = k+1; l < keyList.size(); l++){

                        switch (determine_induced_motif(keyList.get(i), keyList.get(j), keyList.get(k), keyList.get(l))){
                            case 0:         // triangle or not connected
                                break;
                            case 1:
                                C_i[0]++;   // 3-star
                                break;
                            case 2:
                                C_i[1]++;   // 3-path
                                break;
                            case 3:
                                C_i[2]++;   // tailed-triangle
                                break;
                            case 4:
                                C_i[3]++;   // 4-cycle
                                break;
                            case 5:
                                C_i[4]++;   // chordal-4-cycle
                                break;
                            case 6:
                                C_i[5]++;   // 4-clique
                                break;
                        }

                    }
                }
            }
        }
        return C_i;

    }

    public int determine_induced_motif(int u1, int u0, int v0,int v1){ //even though it seems that they are in order, they won't be in brute force queries
        // to do

        return 0;
    }

}
