import java.util.Random;
import java.util.Scanner;

public class AssemblyLine {
    /*
    DP parameters:
    current step on the line: i,
    current line: l
     */

    public static String randomInput(int n, int s, int seed)
    {
        Random rand = new Random(seed);
        String output = n + " " + s + "\n";

        // Generate random assembly lines
        for(int i = 0; i < n; i++)
        {
            for(int j = 0; j < s; j++)
            {
                output += rand.nextInt(600) + " ";
            }
            output += "\n";
        }
        // Generate random swap costs
        for(int i = 0; i < s-1; i++)
            output += rand.nextInt(300) + " ";
        return output;
    }

    public static void main(String[] args)
    {
        // System.out.print(randomInput(2, 15, 36));
        Scanner input = new Scanner(System.in);

        // Read n (# of assembly lines), s (# of steps)
        int n = input.nextInt();
        int s = input.nextInt();

        // Read in values of n production lines, with s steps
        int[][] lines = new int[n][s];
        for(int i = 0; i < n; i++)
            for(int j = 0; j < s; j++)
                lines[i][j] = input.nextInt();

        // Read in swap costs
        int[] swaps = new int[s];
        swaps[0] = 0; // No cost in terms of which assembly line to start at
        for(int i = 1; i < s; i++)
            swaps[i] = input.nextInt();

        input.close();

        // Create our solution table, optimal value is the max among [0..n-1][0]
        int[][] DPval = new int[n][s+1];

        // Keep track of our solutions as a string of the steps taken
        String[][] DPsol = new String[n][s+1];
        for(int i = 0; i < n; i++ )
            DPsol[i][s] = ""; // Fill null values with empty strings for DP building, so we can build strings from bottom to top

        // Evaluation order s-1 -> 0, n-1->0
        for(int i = s-1; i >= 0; i--)
        {
            for(int j = n-1; j >= 0; j--)
            {
                DPval[j][i] = Integer.MAX_VALUE;
                // Consider each possible swap
                for(int k = 0; k < n; k++)
                {
                    // Stay in current line
                    if(k == j && DPval[j][i] > DPval[j][i+1] + lines[j][i])
                    {
                        DPval[j][i] = DPval[j][i+1] + lines[j][i];
                        DPsol[j][i] = " " + (j+1) + DPsol[j][i+1];
                    }

                    // Swap to line k
                    else if(DPval[j][i] > DPval[k][i+1] + lines[k][i] + swaps[i])
                    {
                        DPval[j][i] = DPval[k][i+1] + lines[k][i] + swaps[i];
                        DPsol[j][i] = " " + (k+1) + DPsol[k][i+1];
                    }
                }
            }
        }

        // Get our max value index
        int optimal = 0;
        for(int i = 0; i < n; i++)
            if(DPval[optimal][0] > DPval[i][0])
                optimal = i;

        // Print our values from the top of the table
        System.out.println( "\n" + DPval[optimal][0]);
        System.out.println(DPsol[optimal][0].substring(1));

    }



}
