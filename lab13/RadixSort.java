import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeMap;

/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 *
 */
public class RadixSort {
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     */
    public static String[] sort(String[] asciis) {

        int maxLength = Integer.MIN_VALUE;
        for (String s : asciis) {
            maxLength = Math.max(maxLength, s.length());
        }

        String[] copy = Arrays.copyOf(asciis, asciis.length);

        for (int i = maxLength - 1; i >= 0; i--) {
            sortHelperLSD(copy, i);
        }
        return copy;
    }

    /**
     * LSD helper method that performs a destructive counting sort the array of
     * Strings based off characters at a specific index.
     * @param asciis Input array of Strings
     * @param index The position to sort the Strings on.
     */
    private static void sortHelperLSD(String[] asciis, int index) {
        // Optional LSD helper method for required LSD radix sort
        int R = 256;
//        int[] counts = new int[R + 1];
//        for (String s : asciis) {
//            int num = placeHolder(s, index);
//            counts[num]++;
//        }

//        int[] starts = new int[R + 1];
//        int pos = 0;
//        for (int i = 0; i < starts.length; i++) {
//            starts[i] = pos;
//            pos += counts[i];
//        }
//
//        String[] sorted = new String[asciis.length];
//        for (int i = 0; i < asciis.length; i++) {
//            String item = asciis[i];
//            int num = placeHolder(item, index);
//            int place = starts[num];
//            sorted[place] = item;
//            starts[num]++;
//        }
//
//        for (int i = 0; i < asciis.length; i++) {
//            asciis[i] = sorted[i];
//        }

//        TreeMap<Integer, LinkedList<String>> map = new TreeMap<>();
//        for (String ascii : asciis) {
//            int num = placeHolder(ascii, index);
//            if (!map.containsKey(num)) {
//                map.put(num, new LinkedList<>());
//            }
//            map.get(num).add(ascii);
//        }
//
//
//
//        int count = 0;
//
//        for (int i = 0; i <= R; i++) {
//            if (map.containsKey(i)) {
//                LinkedList<String> list = map.get(i);
//                while (!list.isEmpty()){
//                    asciis[count] = list.removeFirst();
//                    count++;
//                }
//            }
//        }

        int[] count = new int[256];
        int[] starts = new int[256];

        int[] lettersAtIndex = new int[asciis.length];

        for (int i = 0; i < asciis.length; i++) {
            int letterAscii;
            try {
                // System.out.println(asciis[i].charAt(index));
                letterAscii = (int) asciis[i].charAt(index);
            } catch (StringIndexOutOfBoundsException e) {
                letterAscii = 0;
            }

            lettersAtIndex[i] = letterAscii;
            count[letterAscii]++;
        }

        for (int i = 0; i < starts.length - 1; i++) {
            starts[i + 1] = starts[i] + count[i];
        }

        String[] sortedAsciis = new String[asciis.length];
        for (int i = 0; i < asciis.length; i++) {
            int item = lettersAtIndex[i];
            int place = starts[item];
            sortedAsciis[place] = asciis[i];
            starts[item]++;
        }

        for (int i = 0; i < sortedAsciis.length; i++) {
            asciis[i] = sortedAsciis[i];
        }



    }

    public static int placeHolder(String ascii, int index) {
        if (index >= ascii.length()) {
            return 0;
        }
        return (int) ascii.charAt(index) + 1;
    }



    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        // Optional MSD helper method for optional MSD radix sort
        return;
    }

    public static void main(String[] args) {
        String[] strings = new String[]{"jeg", "jobber", "med", "dette", "i", "100", "arhundrer"};
        // String[] strings = new String[]{"jeg", "liker", "luken", "mat"};
        strings = sort(strings);
        for (String string : strings) {
            System.out.println(string);
        }
        //[100, arhundrer, dette, i, jeg, jobber, med]
    }

  
}
