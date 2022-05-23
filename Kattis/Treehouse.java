import java.awt.geom.Point2D;
import java.util.*;

/**
 * Solves the Kattis Treehouses MST problem
 *
 * CS 4150 / Samuel Gera / 11.6.21
 */
public class Treehouse
{
    /**
     * Calculates the distance between two Trees
     * @param p1 first treehouse position
     * @param p2 second treehouse position
     * @return distance
     */
    public static double DistanceTo(Point2D.Double p1, Point2D.Double p2)
    {
        return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }

    /**
     * Return the size of some connected component in the
     * intermediate MST
     * @param c
     * @param CCsets
     * @return
     */
    public static int Size(int c, HashMap<Integer, HashSet<Integer>> CCsets)
    {
        if(c == -1)
            return 0;
        return CCsets.get(c).size();
    }

    /**
     * Helper for adding to the set of connected components in CCsets,
     * as to avoid null-pointer exceptions
     *
     * @param t tree
     * @param c component id
     * @param CCsets sets of all connected components
     */
    public static void AddToComponent(int t, int c, HashMap<Integer, HashSet<Integer>> CCsets)
    {
        if(!CCsets.containsKey(c))
            CCsets.put(c, new HashSet<Integer>());
        CCsets.get(c).add(t);
    }

    /**
     * Will union the components of t1, and t2 if possible
     *
     * @param t1 first tree
     * @param t2 second tree
     * @param CCs set of tree->component ids
     * @param CCsets set of all components
     * @return true if t1's and t2's components were joined, false otherwise
     */
    public static boolean Join(int t1, int t2, int[] CCs, HashMap<Integer, HashSet<Integer>> CCsets)
    {

        // Do nothing in the case they are already connected
        if(CCs[t1] == CCs[t2])
            return false;

        //Add to the bigger component all the elements of the smaller component
        else if(Size(CCs[t1], CCsets) > Size(CCs[t2], CCsets))
        {
            int oldComponent = CCs[t2];
            for(int x : CCsets.get(oldComponent))
            {
                CCs[x] = CCs[t1];
                AddToComponent(x, CCs[t1], CCsets);
            }
            CCsets.remove(oldComponent);
        }
        else
        {
            int oldComponent = CCs[t1];
            for(int x : CCsets.get(oldComponent))
            {
                CCs[x] = CCs[t2];
                AddToComponent(x, CCs[t2], CCsets);
            }
            CCsets.remove(oldComponent);
        }
        return true;
    }


    /**
     * Entry point for execution
     * @param args
     */
    public static void main(String[] args)
    {
        // Read input
        Scanner in = new Scanner(System.in);

        int n = in.nextInt(); // Number of treehouse nodes
        int e = in.nextInt(); // Number of grounded treehouse nodes
        int p = in.nextInt(); // Number of treehouse cables already installed
        int[] CCs = new int[n]; // Contains the component information for each node
        HashMap<Integer, HashSet<Integer>> CCsets = new HashMap<>();
        Point2D.Double[] trees = new Point2D.Double[n]; //Contains tree location information
        ArrayList<Edge> cables = new ArrayList<Edge>(); // Keep track of existing and 'imaginary' cables

        //Reading in the first e 'grounded' tree-houses
        for(int i = 0; i < e; i++)
        {
            CCs[i] = 0;
            trees[i] = new Point2D.Double(in.nextDouble(), in.nextDouble());
            AddToComponent(i, 0, CCsets);
        }

        // Reading in the next n-e 'airborne' tree-houses
        for(int i = e; i < n; i++)
        {
            trees[i] = new Point2D.Double(in.nextDouble(), in.nextDouble());
            CCs[i] = i;
            AddToComponent(i, i, CCsets);
        }

        // Connecting the remaining trees
        for(int i = 0; i < p; i++)
        {
            int first = in.nextInt();
            int second = in.nextInt();
            Join(first-1, second-1, CCs, CCsets);
        }

        //Generate all possible edges
        for(int i = 0; i < n; i++)
        {
            for(int j = 0; j < n; j++)
            {
                if (i == j)
                    continue;
                else
                    cables.add(new Edge(i, j, DistanceTo(trees[i], trees[j])));
            }
        }
        cables.sort(null);

        double usedCable = 0; // Tally the amount of cable used as a function of distance

        for(Edge e1 : cables)
        {
            // Attempt to join endpoints of an edge
            // Will return true if e1 is not useless
            if(Join(e1.src, e1.dest, CCs, CCsets))
                usedCable += e1.weight;
        }
        System.out.print(usedCable);
    }
}

/**
 * Edge class.
 * Represents an 'undirected' edge in the MST problem
 * of Tree-houses
 */
class Edge implements Comparable
{
    int src;
    int dest;
    double weight;
    public Edge(int _src, int _dest, double w)
    {
        this.src = _src;
        this.dest = _dest;
        this.weight = w;
    }

    /**
     * Method in order to use Arrays.sort, by implementing comparable.
     * Gives priority to lighter edges
     * @param o
     * @return
     */
    @Override
    public int compareTo(Object o) {
        return this.weight > ((Edge)o).weight ? 1 : -1;
    }
}



