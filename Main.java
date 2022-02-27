import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {

        ReadFile reader = new ReadFile();
        Graph graph = reader.createGraph();

        CountMotifs count = new CountMotifs(graph);

        int k = 200;
        double[] estimated = count.three_path_sampler(k);
        int[] actual = count.brute_force_count();

        System.out.println(estimated);
        System.out.println(actual);
    }

}
