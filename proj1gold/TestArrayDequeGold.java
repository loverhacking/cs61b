import static org.junit.Assert.*;
import org.junit.Test;


public class TestArrayDequeGold {
    @Test
    public void testStudentArrayDeque() {
        ArrayDequeSolution<Integer> solArray = new ArrayDequeSolution<>();
        StudentArrayDeque<Integer> testArray = new StudentArrayDeque<>();
        int totalTestNum = 1000;

        String log = "";

        for (int i = 0; i < totalTestNum; i++) {
            if (!solArray.isEmpty()) {

                int flag = StdRandom.uniform(2);
                int num = StdRandom.uniform(totalTestNum);

                if (flag == 0) {
                    log = log + "addFirst(" + num + ")\n";
                    testArray.addFirst(num);
                    solArray.addFirst(num);
                } else {
                    log = log + "addLast(" + num + ")\n";
                    testArray.addLast(num);
                    solArray.addLast(num);
                }
            } else {

                int flag = StdRandom.uniform(4);
                int num = StdRandom.uniform(totalTestNum);

                Integer removeSolNum = 1;
                Integer removeStuNum = 1;

                switch (flag) {
                    case 0:
                        log = log + "addFirst(" + num + ")\n";
                        testArray.addFirst(num);
                        solArray.addFirst(num);
                        break;
                    case 1:
                        log = log + "addLast(" + num + ")\n";
                        testArray.addLast(num);
                        solArray.addLast(num);
                        break;
                    case 2:
                        log = log + "removeFirst()\n";
                        removeSolNum = testArray.removeFirst();
                        removeStuNum = solArray.removeFirst();
                        break;
                    case 3:
                        log = log + "removeLast()\n";
                        removeSolNum = testArray.removeLast();
                        removeStuNum = solArray.removeLast();
                        break;
                    default:
                }
                assertEquals(log, removeSolNum, removeStuNum);
            }
        }
    }


}
