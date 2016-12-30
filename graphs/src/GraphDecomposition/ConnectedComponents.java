/**
 * Created by lkuligin on 30/12/2016.
 */

package GraphDecomposition;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class ConnectedComponents {

    private static int numberOfComponents(ArrayList<Integer>[] graph) {
        if (graph.length == 1) return 1;
        int result = 0;
        HashSet<Integer> verticesVisited = new HashSet<>();
        for (int vertice = 0; vertice<graph.length; vertice++) {
            if (!verticesVisited.contains(vertice)) {
                result += 1;
                ArrayDeque<Integer> verticesToVisit = new ArrayDeque<>();
                verticesToVisit.add(vertice);
                while (!verticesToVisit.isEmpty()) {
                    int v = verticesToVisit.poll();
                    verticesVisited.add(v);
                    for (int neighbour : graph[v]) {
                        if (!verticesVisited.contains(neighbour)
                                && !verticesToVisit.contains(v)) verticesToVisit.add(neighbour);
                    }

                }
            }

        }
        return result;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int verticesCount = scanner.nextInt();
        int edgesCount = scanner.nextInt();
        ArrayList<Integer>[] graph = (ArrayList<Integer>[])new ArrayList[verticesCount];
        for (int i=0; i<verticesCount; i++ ) {
            graph[i] = new ArrayList<>();
        }
        for (int i = 0; i < edgesCount; i++) {
            int verticeFrom, verticeTo;
            verticeFrom = scanner.nextInt();
            verticeTo = scanner.nextInt();
            graph[verticeFrom - 1].add(verticeTo - 1);
            graph[verticeTo - 1].add(verticeFrom - 1);
        }
        System.out.println(numberOfComponents(graph));
    }
}
