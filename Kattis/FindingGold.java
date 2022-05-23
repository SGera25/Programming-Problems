import java.util.Scanner;

/**
 * CS 4150, Prof. Travis Martin F21
 * PS5 Problem: Gold
 *
 * Sam Gera (u1173758)
 * 10.24.21
 */
public class FindingGold {


    /**
     * Representing the problem structures graph as the map itself, with edges
     * being adjacent tiles, this function performs DFS under the rules of finding gold.
     *
     * Will tally every Gold tile reached, when properly backing off due to a 'breeze'
     * i.e. an adjacent trap tile
     *
     * @param map graph data structure as a 2D array of tiles
     * @param visited record visitation in a 2D array
     * @param col current tile's column index
     * @param row current tile's row index
     * @return
     */
    private static int goldDFS(char[][] map, boolean[][] visited, int col, int row)
    {
        int gold = 0;

        visited[col][row] = true; // Update visitation

        if(map[col][row] == 'G') // Check for position on gold tile
            gold++;

        // Check to see if we 'feel a breeze' due to a trap tile
        if((col-1 > -1 && map[col-1][row] == 'T') ||
                (col + 1 < map.length  && map[col+1][row] == 'T') ||
                (row-1 > -1 && map[col][row-1] == 'T') ||
                (row+1 < map[0].length && map[col][row+1] == 'T'))
            return gold;

        // Attempting to visit tile to the left
        if(col-1 > -1 && map[col-1][row] != '#' && !visited[col-1][row])
            gold += goldDFS(map, visited, col-1, row);

        // Attempting to visit tile to the right
        if(col + 1 < map.length  && map[col+1][row] != '#' && !visited[col+1][row])
            gold += goldDFS(map, visited, col+1, row);

        // Attempting to visit tile below
        if(row-1 > -1 && map[col][row-1] != '#' && !visited[col][row-1])
            gold += goldDFS(map, visited, col, row-1);

        // Attempting to visit tile above
        if(row+1 < map[0].length && map[col][row+1] != '#' && !visited[col][row+1])
            gold += goldDFS(map, visited, col, row+1);

        return gold;
    }


    /**
     * Entry point of program. Takes input specified by
     * the finding gold problem
     *
     * @param args
     */
    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);

        int c = input.nextInt(); // number of columns in game map
        int r = input.nextInt(); // number of rows in game map
        char[][] map = new char[c][r]; // this is our graph data structure, adjacent tiles have edges
        boolean[][] visited = new boolean[c][r]; // keep track of tiles that have been stepped on
        int startCol = 0;
        int startRow = 0;

        input.nextLine();


        for(int i = 0; i < r; i++)
        {
            String row = input.nextLine();
            for(int j = 0; j < row.length(); j++){
                char tile = row.charAt(j);
                if(tile == 'P') //Record starting position
                {
                    startCol = j;
                    startRow = i;
                }
                map[j][i] = tile; // Get and store each character in the current line representing a row on the map
            }
        }

        input.close();

        // Initialize visitation to false
        for(int i = 0; i < c; i++)
            for(int j = 0; j < r; j++)
                visited[i][j] = false;

        System.out.println(goldDFS(map, visited, startCol, startRow));
    }



}
