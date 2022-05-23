import java.util.Scanner;

/**
 * Class made to solve the TechValley California
 * LP problem
 *
 * Sam Gera
 * u1173758
 * 9.18.21
 * CS 4150 Prof. Travis Martin
 */
public class LP {
    /**
     * find the maximum x and y s.t.:
     *
     * ax + by = R where R is maximized
     * x + y <= m
     * x, y, >= 1
     *
     * when a > b
     *
     * @param a
     * @param b
     * @param m
     * @return
     */
    private static int Solve(int a, int b, int m)
    {
        return a*(m - 1) + b;
    }
    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);
        int a = input.nextInt(); // Price per square foot
        int b = input.nextInt(); // Price per smart bulb
        int m = input.nextInt(); // x + y <= m
        input.close();
        if(a > b)
            System.out.println(Solve(a, b, m));
        else
            System.out.println(Solve(b, a, m));

    }
}
