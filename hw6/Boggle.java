import java.util.*;

public class Boggle {


    /** change board to char[][] array with size M * N */
    private static char[][] boardArray;


    /** the length of boardArray */
    private static int M;

    /** the width of boardArray */
    private static int N;




    private static LinkedList<Direction> list;

    private static Trie t;

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


        for (int i = 0; i < M; i++) {
            boardArray[i] = strings[i].toCharArray();
        }

        t = new Trie();
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

        list = new LinkedList<>();
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                String s = String.valueOf(boardArray[i][j]);
                if (t.startsWith(s)) {
                    Direction d = new Direction(i, j);
                    d.setVisited(1 << (i * N + j));
                    d.setValue(s);

                    list.add(d);

                    solveHelper(matchWordSet);
                }


            }
        }

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

    private static void solveHelper(PriorityQueue<String> matchWordSet) {


        while (!list.isEmpty()) {
            Direction d = list.removeLast();

            int dx = d.getX();
            int dy = d.getY();
            String value = d.getValue();
            int visited = d.getVisited();

            LinkedList<Direction> neighbors = getDirections(dx, dy);


            if (value.length() >= 3 && t.contains(value)
                    && !matchWordSet.contains(value)) {
                matchWordSet.add(value);
            }

            while (!neighbors.isEmpty()) {
                Direction dd = neighbors.removeLast();

                int ddx = dd.getX();
                int ddy = dd.getY();

                int pos = ddx * N + ddy;


                if ((visited & (1 << pos)) != 0) {
                    continue;
                }



                String temp = addString(value, boardArray[ddx][ddy]);

                if (temp.length() >= 3 && !t.startsWith(temp)) {
                    continue;
                }
                dd.setVisited(visited | (1 << pos));
                dd.setValue(temp);

                list.addLast(dd);



            }

        }

    }

    private static String addString(String word, char c) {

        StringBuilder sb = new StringBuilder();

        sb.append(word);
        sb.append(c);
        return sb.toString();
    }

    private static LinkedList<Direction> getDirections(int starti, int startj) {
        LinkedList<Direction> directions = new LinkedList<>();

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                if (checkInArray(starti + i, startj + j)) {
                    Direction d = new Direction(starti + i, startj + j);
                    directions.add(d);
                }
            }
        }

        return directions;
    }


    private static boolean checkInArray(int i, int j) {
        return i >= 0 && i < M && j >= 0 && j < N;
    }




}



