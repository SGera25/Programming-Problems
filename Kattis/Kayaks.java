import java.util.Scanner;

/**
 * Class made to solve the kayaking problem:
 *
 * You are the organizer of a kayaking competition. Unfortunately strong winds have damaged a few kayaks, and the race starts in 5 minutes! Fortunately, some teams have brought reserve kayaks. Since kayaks are bulky and hard to carry, teams are willing to lend kayaks to opposing teams if and only if they are starting immediately next to them. For example, team with the starting number 4 will lend its reserve kayak only to teams 3 and 5. Of course if some team did bring a reserve and its kayak was damaged, they will use it themselves and not lend it to anyone.
 *
 * You as the organizer now need to know, what is the minimal number of teams that cannot start the race, not even in borrowed kayaks.
 *
 * Input
 * The first line of input contains one integer, 𝑁 (2≤𝑁≤10), the total number of teams.
 *
 * The second line contains exactly N numbers, one for each of the N teams (in order). Number i is 1 if and only if team i has a damaged kayak.
 *
 * The third line contains exactly N numbers, one for each of the N teams (in order). Number i is 1 if and only if team i has a reserve kayak.
 */

/**
 * Sam Gera (u1173758)
 * 9/14/21
 * CS 4150 F21 (Prof. Travis Martin)
 */
public class Kayaks {
    /**
     * Solves the kayaking problem given the above description
     *
     * @param d array of damaged kayaks where d[i] = 0 if kayak is non-damaged and 1 o.w.
     * @param r array of reserve kayaks where r[i] = 0 if no reserve kayak exists and 1 o.w.
     * @param index the current team the algorithm is looking at
     * @param state describes whether the previous and current reserve kayak has been taken (0 = none, 1 = previous taken, 2 = current taken, 3 = both)
     * @return the maximum number of teams that can partake
     */
    public static int Solve(int[] d, int[] r, int index, int state)
    {
        int minTeams = d.length;
        // Continuing forward until we have reached the end of the array
        if(index != d.length-1)
        {
            //Current kayak is damaged
            if(d[index] == 1)
            {
                // No previous or current kayak taken
                if(state == 0)
                {
                    // Taking Kayak from previous
                    if (index != 0 && r[index - 1] == 1)
                        minTeams = Math.min(Solve(d, r, index + 1, 0), minTeams);
                    // Taking Kayak from self
                    if (r[index] == 1)
                        minTeams = Math.min(Solve(d, r, index + 1, 1), minTeams);
                    //Taking kayak from forward team
                    if (r[index + 1] == 1)
                        minTeams = Math.min(Solve(d, r, index + 1, 2), minTeams);
                    //Taking none
                    minTeams = Math.min(1 + Solve(d, r, index + 1, 0), minTeams);
                }
                // Previous kayak taken
                else if(state == 1)
                {
                    // Taking Kayak from self
                    if (r[index] == 1)
                        minTeams = Math.min(Solve(d, r, index + 1, 1), minTeams);
                    //Taking kayak from forward team
                    if (r[index + 1] == 1)
                        minTeams = Math.min(Solve(d, r, index + 1, 2), minTeams);
                    //Taking none
                    minTeams = Math.min(1 + Solve(d, r, index + 1, 0), minTeams);
                }
                // Current kayak taken
                else if (state == 2)
                {
                    //Take kayak from previous
                    if (index != 0 && r[index - 1] == 1)
                        minTeams = Math.min(Solve(d, r, index + 1, 1), minTeams);
                    //Taking kayak from forward team
                    if (r[index + 1] == 1)
                        minTeams = Math.min(Solve(d, r, index + 1, 3), minTeams);
                    //Taking none
                    minTeams = Math.min(1 + Solve(d, r, index + 1, 1), minTeams);
                }
                else // State = 3
                {
                    //Taking kayak from forward team
                    if (r[index + 1] == 1)
                        minTeams = Math.min(Solve(d, r, index + 1, 3), minTeams);
                    //Taking none
                    minTeams = Math.min(1 + Solve(d, r, index + 1, 1), minTeams);
                }
            }
            else
            {
                // No kayaks taken, but state will be 0
                if(state == 0 || state == 1)
                    minTeams = Math.min(Solve(d,r,index+1, 0), minTeams);
                // No kayaks taken, bust state will be 1
                else
                    minTeams = Math.min(Solve(d,r, index+1, 1), minTeams);
            }
        }
        // Base cases where we have reached the end of the array of kayaks
        else
        {
            // last kayak is damaged
            if(d[index] == 1)
            {
                if(state == 0)
                {
                    //Attempt to retrieve own or previous reserve
                    if(r[index] == 1)
                        return 0;
                    else if (r.length > 1 && r[index-1] == 1)
                        return 0;
                    return 1;
                }
                else if(state == 1)
                {
                    // Attempt to retrieve own kayak if it exists
                    if(r[index] == 1)
                        return 0;
                    return 1;
                }
                else if(state == 2)
                {
                    // Attempt to retrieve previous reserve if it exists
                    if(r[index-1] == 1)
                        return 0;
                    return 1;
                }
                return 1; // State = 3
            }
            // last kayak is not damaged
            else
                return 0;
        }
        return minTeams;
    }

    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);

        // number of kayaking teams
        int n = input.nextInt();

        // Forming some useful bit arrays (of integer type)
        int[] damaged = new int[n];
        int[] reserves = new int[n];
        for(int i = 0; i < n; i++)
            damaged[i] = input.nextInt();
        for(int i = 0; i < n; i++)
            reserves[i] = input.nextInt();
        input.close();

        System.out.println(Solve(damaged, reserves, 0, 0));


    }
}
