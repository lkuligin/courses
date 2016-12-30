/**
 * Created by lkuligin on 30/12/2016.
 */

package GraphDecomposition;

import java.util.*;

public class Reachability {

    private static int reach(ArrayList<Integer>[] graph, int verticeFrom, int verticeTo) {
        HashSet<Integer> verticesVisited = new HashSet<>();
        ArrayDeque<Integer> verticesToVisit = new ArrayDeque<>();
        verticesToVisit.add(verticeFrom);
        while (!verticesToVisit.isEmpty()) {
            int vertice = verticesToVisit.poll();
            verticesVisited.add(vertice);
            for (int neighbour : graph[vertice]) {
                if (neighbour == verticeTo) return 1;
                if (!verticesVisited.contains(neighbour)) verticesToVisit.add(neighbour);
            }
        }
        return 0;
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int verticesCount = scanner.nextInt();
        int edgesCount = scanner.nextInt();
        ArrayList<Integer>[] graph = (ArrayList<Integer>[])new ArrayList[verticesCount];
        for (int i = 0; i < verticesCount; i++) {
            graph[i] = new ArrayList<>();
        }
        for (int i = 0; i < edgesCount; i++) {
            int verticeFrom, verticeTo;
            verticeFrom = scanner.nextInt();
            verticeTo = scanner.nextInt();
            graph[verticeFrom - 1].add(verticeTo - 1);
            graph[verticeTo - 1].add(verticeFrom - 1);
        }
        int verticeFrom = scanner.nextInt() - 1;
        int verticeTo = scanner.nextInt() - 1;
        System.out.println(reach(graph, verticeFrom, verticeTo));
    }
}
