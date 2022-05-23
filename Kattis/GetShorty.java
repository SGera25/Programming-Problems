import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * Kattis GetShorty problem.
 * Computes the shortest 'scaling' path algorithm
 * by use of a variant of dijkstra's algorithm that uses
 * edge 'factors' instead of weights
 *
 * CS-4150 11/10/21 Samuel Gera (u1173758)
 */
public class GetShorty {

    private static double DijkstraFactor(HashMap<Integer, Double>[] G, int s)
    {
        // Initializing Dijkstra
        PriorityQueue<Vertex> p = new PriorityQueue<>();
        double[] dist = new double[G.length];
        InitSSP(dist, s);
        p.add(new Vertex(s, 1));
        HashSet<Integer> visited = new HashSet<>();
        // Run Dijkstra's v-1 times
        for(int i = 0; i < G.length; i++)
        {
            if(p.isEmpty())
                break;
            Vertex u = p.poll(); // Get the vertex with highest "factor"

            if(visited.contains(u.id))
            {
                i--;
                continue;
            }

            visited.add(u.id);

            for(int v : G[u.id].keySet())
            {
                // dist(u) + uv
                double t = u.dist * G[u.id].get(v);

                // If uv is tense relax it, else do nothing
                if(t > dist[v])
                {
                    dist[v] = t;
                    p.add(new Vertex(v, t));
                }
            }
        }

        // Return the distance from source to node with id n-1
        return dist[dist.length-1];
    }

    /**
     * Initializing the dist array for Dijkstra's
     * dependent upon the source vertex for SSP
     * Will use -1 to represent infinity in the context
     * of the problem. This is because 0 <= f <= 1
     * @param dist dist array
     * @param s source vertex
     */
    private static void InitSSP(double[] dist, int s)
    {
        for(int i = 0; i < dist.length; i++)
            dist[i] = i == s ? 1 : -1;
    }

    /**
     * Entry point for our program
     * @param args
     */
    public static void main(String[] args)
    {
        Scanner in = new Scanner(System.in); // Read input

        int n = in.nextInt(); // Number of intersections
        int m = in.nextInt(); // Number of corridors

        HashMap<Integer, Double>[] G = new HashMap[n]; // "Adjacency array" representing our graph

        // Initialize our graph with empty Hashmaps
        for(int i = 0; i < n; i++)
            G[i] = new HashMap<>();

        // Construct edges of G
        for(int i = 0; i < m; i++ )
        {
            int src = in.nextInt();
            int dest = in.nextInt();
            double w = in.hasNextDouble() ? in.nextDouble() : (double)in.nextInt();
            G[src].put(dest, w);
            G[dest].put(src, w);
        }

        // Stop reading from input
        in.close();


        // Use a variant of Dijkstra's algorithm in order to calculate shortest 'scaling' path
        System.out.format("%.4f", DijkstraFactor(G, 0));
    }
}

class Vertex implements Comparable
{
    int id;
    double dist;

    public Vertex(int _id, double _dist)
    {
        this.id = _id;
        this.dist = _dist;
    }

    @Override
    public int compareTo(Object o) {
        return this.dist < ((Vertex)o).dist ? 1 : -1;
    }
}
