import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

/**
 * Class created to solve the
 * Mr. Anaga programming problem
 *
 * CS 41540 F21
 *
 * 9/1/21
 *
 * Sam Gera
 * U1173758
 */
public class MrAnaga {

    /**
     * Given a list of n words of k-length, records the amount of unique
     * words contained within that list of words, such that each unique word
     * contains is not an anagram of another word in the list.
     *
     * @param words - List of n words
     * @return the count of unique anagrams
     */
    public static int CountAnagrams(String[] words)
    {
        // keep track of words that have duplicates and those that don't
        HashSet<String> pending = new HashSet<>();
        HashSet<String> duplicates = new HashSet<>();

        int anagrams = 0;
        for(int i = 0; i < words.length; i ++)
        {
            char[] letters = words[i].toCharArray(); // Grab the current word with its characters sorted
            Arrays.sort(letters); // Grab the lexicographic ordering of letters within the current word

            String lexWord = String.valueOf(letters);

            if(pending.remove(lexWord))
            {
                duplicates.add(lexWord);
                anagrams--;
            }
            else if(!duplicates.contains(lexWord))
            {
                pending.add(lexWord);
                anagrams++;
            }
        }
        return anagrams;
    }



    public static void main(String[] args)
    {
        Scanner in = new Scanner(System.in); // Read input from Standard in

        int n = in.nextInt(); // Number of words in list

        int k = in.nextInt(); // Length of words in list

        String[] words = new String[n];

        for(int i = 0; i < n; i ++)
            words[i] = in.next();

        in.close();
        System.out.print(CountAnagrams(words));


    }


}
