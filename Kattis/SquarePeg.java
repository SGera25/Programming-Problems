import java.util.Arrays;
import java.util.Scanner;

/**
 * Sam Gera (u1173758)
 *
 * PS4 Programming | Kattis Square Pegs
 *
 * 10.8.21
 */
public class SquarePeg {


    /**
     * Given a squares side, gets the length of it's diagonal
     *
     * In the scope of this problem it is used to translate the square houses side, into a radius
     * that can be used to determine if it can fit in a plot
     * @param side
     * @return length of square's diagonal
     */
    private static double GetDiagonal(double side) {
        return Math.sqrt(2 * Math.pow(side, 2));
    }

    /**
     * This algorithm greedily selects the next smallest house to go in the next
     * smallest plot
     *
     * Given two sorted input arrays in asc order, of plot radii, and normalized house radii
     *
     * @param plots
     * @param houses
     * @return maxinum number of plots that can be filled
     */
    private static int GreedySelect(double[] plots, double[] houses) {
        int max = 0;

        for(int i = 0; i < plots.length; i++)
        {
            for(int j = 0; j < houses.length; j++)
            {
                if(houses[j] < plots[i])
                {
                    max++;
                    houses[j] = Double.POSITIVE_INFINITY; // Prevent this house from being chosen
                    break;
                }
            }
        }
        return max;
    }


    public static void main(String[] args)
    {
        Scanner in = new Scanner(System.in);

        int n = in.nextInt(); // Number of plots
        int m = in.nextInt(); // Number circular houses
        int k = in.nextInt(); // Number of square houses

        double[] plots = new double[n];
        double[] houses = new double[m + k]; // Initializing array to hold both square and circular houses

        for(int i = 0; i < n; i++)
            plots[i] = in.nextDouble();

        for(int i = 0; i < m; i++)
            houses[i] = in.nextDouble();

        for(int i = 0; i < k; i++)
            houses[m+i] = GetDiagonal(in.nextDouble()/2); // Getting the "radius" of the square house

        in.close();

        Arrays.sort(plots);
        Arrays.sort(houses);

        System.out.println(GreedySelect(plots, houses));
    }

}
