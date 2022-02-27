import java.util.*;

public class UndirectedNode {
    public int id;
    public int value;
    public int degree;
    public ArrayList<Integer> adjacency_list;

    //Constructors
    public UndirectedNode(int id){
        this.id = id;
        this.value = 0;
        this.degree = 0;
        this.adjacency_list = new ArrayList<Integer>();
    }

    public UndirectedNode(int id, int value){
        this.id = id;
        this.value = value;
        this.degree = 0;
        this.adjacency_list = new ArrayList<Integer>();
    }




}
