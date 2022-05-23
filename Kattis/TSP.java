import java.util.HashSet;
import java.util.Scanner;

public class TSP {

    /**
     * Entry point for program
     * @param args command line arguments
     */
    public static void main(String[] args) {
        // Read input
        Scanner in = new Scanner(System.in);

        // Number of vertices in directed, weighted graph
        int n = in.nextInt();

        // Read in adjacency array of TSP graph
        int[][] G = new int[n][n];

        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
                G[i][j] = in.nextInt();
        }

        // Memoized data structure
        int[][] mem = new int[n][1 << n];

        for(int i = 0; i < mem.length; i++)
        {
            for(int j = 0; j < mem[0].length; j++)
                mem[i][j] = Integer.MIN_VALUE;
        }

        HashSet<Integer> others = new HashSet<>();

        for(int i = 0; i < n; i++)
        {
            others.add(i);
        }
        // Use Held-Karp in the form of memoized recursion
        System.out.print(HeldKarp(G, 0, others, mem   ));

    }

    /**
     * Held-Karp Memoized recursion algorithm for TSP, that uses
     * bit-masking to encode sub-problems defined by
     * a subset of remaining vertices to visit and current vertex
     *
     * Executes in O(n^2 * 2^n) time
     * @param G directed, completed graph represented by adjacency matrix
     * @return cost of optimal hamiltonian cycle
     */
    public static int HeldKarp(int[][] G, int s, HashSet<Integer> others, int[][] mem)
    {


        int bitmask = SetToBits(others);

        // Hamiltonian cycle is completing
        if(others.size() == 1)
            return G[s][0];


        // Checking if answer is memoized
        if(mem[s][bitmask] > Integer.MIN_VALUE)
            return mem[s][bitmask];

        int best = Integer.MAX_VALUE;

        for(int e : others)
        {

            // Ignore self edges
            if(e == s)
                continue;

            // Make a new set excluding e
            HashSet<Integer> S = new HashSet<>();
            S.addAll(others);
            S.remove(s);

            // Take the best path to cover others
            best = Math.min(best,
                    G[s][e] +
                            HeldKarp(G, e, S, mem)
                    );

            // Memoize path
            mem[s][bitmask] = best;
        }
        return best;
    }

    /**
     * Translates a set to a bitmask representation
     * for lookup inside of a Memoized 2D array
     * @param S set of integers
     * @return bitmask representing the set of integers
     */
    public static int SetToBits(HashSet<Integer> S)
    {
        int bitmask = 0;
        for(int i : S)
        {
            bitmask = bitmask | (1 << i);
        }
        return bitmask;
    }

    /**
     * Finds the lightest cost hamiltonian cycle in the Graph
     * represented by the adjacency matrix in G to solve TSP
     *
     * Using the nearest neighbor algorithm, GREEDILY
     *
     * Executes in O(n^2) time, but does not always return the correct answer
     * @param G adjacency matrix of our graph
     * @return cost of lightest hamiltonian cycle
     */
    public static int NN(int[][] G, int u, HashSet<Integer> visited)
    {
        // Keep track of starting vertex
        int start = u;

        // Current approximate 'best' hamiltonian cycle cost
        int total = 0;

        // Visit all vertices using nearest neighbor
        while(visited.size() < G.length)
        {
            visited.add(u);
            int near = -1;
            int cost = Integer.MAX_VALUE;

            // Calculate the nearest neighbor
            for(int i = 0; i < G.length; i++)
            {
                if(cost > G[u][i])
                {
                    near = i;
                    cost = G[u][i];
                }
            }
            total+= cost;
            u = near;
        }
        return total + G[u][start];
    }
}
