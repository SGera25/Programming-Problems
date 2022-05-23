import java.awt.*;
import java.util.HashSet;
import java.util.Scanner;

/**
 * Solution to the Kattis Transportation Planning
 * problem (APSP) : PS9 Programming CS-4150
 *
 * Sam Gera (u1173758)
 * 11.23.21
 */
public class TransportationPlanning {


    /**
     * Entry point for program
     * @param args
     */
    public static void main(String[] args)
    {
        // Read from standard input
        Scanner in = new Scanner(System.in);

        // Number of "intersections"
        int n = in.nextInt();

        Point[] nodes = new Point[n];

        for(int i = 0; i < n; i++)
            nodes[i] = new Point(in.nextInt(), in.nextInt());

        // Number of "roads'
        int m = in.nextInt();

        // Adjacency array to represent our Graph of intersections and roads
        HashSet<Integer>[] G = new HashSet[n];

        for(int i = 0; i < n; i++)
            G[i] = new HashSet<Integer>();

        // Graph is undirected so we add both edges
        for(int i = 0; i < m; i++)
        {
            int u = in.nextInt();
            int v = in.nextInt();
            G[u].add(v);
            G[v].add(u);
        }

        // The weight of an edge between two intersections (if it exists) is the distance between them
        double[][] w = new double[n][n];

        // Precomputing all possible edge weights w[u][v] = w(uv)
        for(int i = 0; i < n; i++)
            for(int j = 0; j < n; j++)
                w[i][j] = DistTo(nodes[i], nodes[j]);

        System.out.print(FindBestRoad(G, w));
    }

    private static double FindBestRoad(HashSet<Integer>[] G, double[][] w )
    {
        // Use APSP Floyd-Warshall to compute our preliminary-distance array
        double[][] dist = FloydWarshall(G, w);

        double bestSum = CommuneTime(dist);
        // check every new possible edge uv, to see if it improves communeTime(dist)
        for(int u = 0; u < dist.length; u++)
        {
            for(int v = 0; v < dist.length; v++)
            {
                double[][] newDist = TryEdge(dist, w, u, v);
                double newCommuteSum = CommuneTime(newDist);
                if(newCommuteSum < bestSum)
                    bestSum = newCommuteSum;
            }
        }
        return bestSum;
    }

    /**
     * Returns the new APSP 2D array by adding the edge
     * uv, by updating paths to be shorter that benefit from
     * taking the uv edge
     * @param dist existing APSP distance array
     * @param w precomputed edge weights
     * @param u source
     * @param v destination
     * @return new APSP distance array
     */
    private static double[][] TryEdge(double[][] dist, double[][] w, int u, int v)
    {
        // Make sure not to change the underlying dist array
        double[][] newDist = new double[dist.length][dist.length];
        for(int i = 0; i < newDist.length; i++)
            for(int j = 0; j < newDist.length; j++)
                newDist[i][j] = dist[i][j];

        // Straight line distance should be better than existing shortest path (if the edge didn't already exist)
        newDist[u][v] = w[u][v];
        newDist[v][u] = w[v][u];

        // Recompute the new distances for shortest paths that benefit by traversing through u or v
        for(int i = 0; i < newDist.length; i++)
        {
            for(int j = 0; j < newDist.length; j++)
            {
                newDist[i][j] = Math.min(Math.min(newDist[i][j],
                        newDist[i][u] + w[u][v] + newDist[v][j]),
                        newDist[i][v] + w[v][u] + newDist[u][j]
                        );
            }
        }
        return newDist;
    }


    /**
     * Finds APSP for an undirected graph G, with an array of edge weights w.
     *
     * Uses Floyd-Warshall's DP algorithm to compute APSP and return it in a
     * 2D array in O(n^3) time
     * @param G Graph as an adjacency array
     * @param w edge weights
     * @return distance array where dist[u][v] denotes the shortest path from u->v
     */
    private static double[][] FloydWarshall(HashSet<Integer>[] G, double[][] w)
    {
        // Initialize Floyd-Warshall
        double[][] dist = new double[G.length][G.length];

        for(int i = 0; i < dist.length; i++){
            for(int j = 0; j < dist.length; j++)
            {
                if(G[i].contains(j) || i == j)
                    dist[i][j] = w[i][j];
                else
                    dist[i][j] = Double.POSITIVE_INFINITY;
            }
        }
        // Running Floyd-Warshall
        for(int r = 0; r < dist.length; r++)
            for(int u = 0; u < dist.length; u++)
                for(int v = 0; v < dist.length; v++)
                    if(dist[u][r] + dist[r][v] < dist[u][v])
                        dist[u][v] = dist[u][r] + dist[r][v];
        return dist;
    }


    /**
     * Helper method to determine the distance between two
     * intersections, which is just the 2d distance between their
     * location
     * @param p1
     * @param p2
     * @return 2D-distance between p1 and p2
     */
    private static double DistTo(Point p1, Point p2)
    {
        return Math.sqrt(Math.pow((p1.x - p2.x), 2) + Math.pow((p1.y - p2.y), 2));
    }

    /**
     * Returns the total commune time given the input
     * distance array
     * @param dist distance array
     * @return total commune time given all the shortest paths
     */
    private static double CommuneTime(double[][] dist)
    {
        double sum = 0;
        for(int i = 0; i < dist.length; i++)
            for(int j = i; j < dist.length; j++)
                sum += dist[i][j];
        return sum;
    }

}
