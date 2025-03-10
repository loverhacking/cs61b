import edu.princeton.cs.algs4.TrieSET;

import java.util.*;

public class Boggle {


    /** change board to char[][] array with size M * N */
    private static char[][] boardArray;


    /** the length of boardArray */
    private static int M;

    /** the width of boardArray */
    private static int N;

    /** record whether the char in boardArray has been visited */
    private static boolean[][] visited;

    private static TrieSET t;

    // File path of dictionary file
    static String dictPath = "words.txt";







    /**
     * Solves a Boggle puzzle.
     * 
     * @param k The maximum number of words to return.
     * @param boardFilePath The file path to Boggle board file.
     * @return a list of words found in given Boggle board.
     *         The Strings are sorted in descending order of length.
     *         If multiple words have the same length,
     *         have them in ascending alphabetical order.
     */
    public static List<String> solve(int k, String boardFilePath) {
        // YOUR CODE HERE

        In inputFile = new In(boardFilePath);
        if (k < 0 || !inputFile.exists()) {
            throw new IllegalArgumentException();
        }

        // change every line in txt to a element in String[]
        String[] strings = inputFile.readAllStrings();
        checkIsRectangular(strings);

        M = strings.length;
        N = strings[0].length();

        // change board to char[][] array
        boardArray = new char[M][N];
        visited = new boolean[M][N];



        for (int i = 0; i < M; i++) {
            boardArray[i] = strings[i].toCharArray();
        }

        t = new TrieSET();
        In wordFile = new In(dictPath);
        while (wordFile.hasNextLine()) {
            String a = wordFile.readLine();
            t.add(a);
        }



        PriorityQueue<String> matchWordSet = new PriorityQueue<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if (o1.length() != o2.length()) {
                    return -(o1.length() - o2.length());
                }
                return o1.compareTo(o2);
            }
        });


        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                setFalse();
                solveHelper(i, j, "", matchWordSet);
            }
        }



        //System.out.println(matchWordSet);

        LinkedList<String> ans = new LinkedList<>();
        for (int i = 0; i < k; i++) {
            ans.add(matchWordSet.poll());
            if (matchWordSet.isEmpty()) {
                break;
            }
        }

        return ans;
    }

    private static void checkIsRectangular(String[] strings) {
        if (strings.length < 2) {
            return;
        }
        int length = strings[0].length();
        for (int i = 1; i < strings.length; i++) {
            if (length != strings[i].length()) {
                throw new IllegalArgumentException();
            }
        }
        return;
    }

    private static void solveHelper(int starti, int startj, String word, PriorityQueue<String> matchWordSet) {
        visited[starti][startj] = true;

        StringBuilder sb = new StringBuilder();
        sb.append(word);
        sb.append(boardArray[starti][startj]);
        String newWord = sb.toString();


        if (newWord.length() >= 3 && t.contains(newWord) && !matchWordSet.contains(newWord)) {
            matchWordSet.add(newWord);
        }

        if (newWord.length() >= 3 && !t.keysWithPrefix(newWord).iterator().hasNext()) {
            visited[starti][startj] = false;
            return;
        }

        LinkedList<Direction> neighbors = getDirections(starti, startj);


        for (Direction d : neighbors) {
            int x = d.getX();
            int y = d.getY();
            solveHelper(x, y, newWord, matchWordSet);
        }
        visited[starti][startj] = false;

    }

    /** set all element in visited[][] to false */
    private static void setFalse() {
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                visited[i][j] = false;
            }
        }
    }




    private static LinkedList<Direction> getDirections(int starti, int startj) {
        LinkedList<Direction> directions = new LinkedList<>();

        directions.addLast(new Direction(starti, startj - 1));
        directions.addLast(new Direction(starti - 1, startj - 1));
        directions.addLast(new Direction(starti - 1, startj));
        directions.addLast(new Direction(starti - 1, startj + 1));
        directions.addLast(new Direction(starti, startj + 1));
        directions.addLast(new Direction(starti + 1, startj + 1));
        directions.addLast(new Direction(starti + 1, startj));
        directions.addLast(new Direction(starti + 1, startj - 1));

        LinkedList<Direction> validDirections = new LinkedList<>();
        for (Direction d : directions) {
            int x = d.getX();
            int y = d.getY();
            if (checkInArray(x, y)) {
                validDirections.add(d);
            }
        }
        return validDirections;
    }

    private static boolean checkInArray(int i, int j) {
        return i >= 0 && i < M && j >= 0 && j < N && !visited[i][j];
    }

    

}
