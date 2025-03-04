package creatures;

import huglife.*;
import org.junit.Test;

import java.awt.*;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class TestClorus {

    @Test
    public void testBasics() {
        Clorus p = new Clorus(2);
        assertEquals(2, p.energy(), 0.01);
        assertEquals(new Color(34, 0, 231), p.color());
        p.move();
        assertEquals(1.97, p.energy(), 0.01);
        p.move();
        assertEquals(1.94, p.energy(), 0.01);
        p.stay();
        assertEquals(1.93, p.energy(), 0.01);
        p.stay();
        assertEquals(1.92, p.energy(), 0.01);
    }

    @Test
    public void testReplicate() {
        Clorus p = new Clorus(2);
        Clorus pBaby = p.replicate();
        assertNotSame(p, pBaby);
        assertEquals(1, p.energy(), 0.01);
        assertEquals(1, pBaby.energy(), 0.01);
    }

    @Test
    public void testAttack() {
        Clorus p = new Clorus(2);
        p.attack(new Plip());
        assertEquals(3, p.energy(), 0.01);

    }


    @Test
    public void testChoose() {
        Clorus c = new Clorus(1.2);
        HashMap<Direction, Occupant> surrounded = new HashMap<Direction, Occupant>();
        surrounded.put(Direction.TOP, new Impassible());
        surrounded.put(Direction.BOTTOM, new Impassible());
        surrounded.put(Direction.LEFT, new Impassible());
        surrounded.put(Direction.RIGHT, new Impassible());

        // Test no empty squares
        Action actual1 = c.chooseAction(surrounded);
        Action expected1 = new Action(Action.ActionType.STAY);

        assertEquals(expected1, actual1);

        // Test one direction has plip
        surrounded.put(Direction.TOP, new Plip());
        surrounded.put(Direction.BOTTOM, new Empty());
        Action actual2 = c.chooseAction(surrounded);
        Action expected2 = new Action(Action.ActionType.ATTACK, Direction.TOP);

        //assertEquals(expected2, actual2);

        // Test energy >= 1 with one direction empty
        surrounded.put(Direction.TOP, new Empty());
        Action actual3 = c.chooseAction(surrounded);
        Action expected3 = new Action(Action.ActionType.REPLICATE, Direction.TOP);

        // Test energy < 1 with one direction empty
        Clorus newC = new Clorus(0.8);
        Action actual4 = newC.chooseAction(surrounded);
        Action expected4 = new Action(Action.ActionType.MOVE, Direction.TOP);


    }

    public static void main(String[] args) {
        System.exit(jh61b.junit.textui.runClasses(TestPlip.class));
    }
}

