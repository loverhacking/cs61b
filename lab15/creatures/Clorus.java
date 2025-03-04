package creatures;

import huglife.*;

import java.awt.*;
import java.util.List;
import java.util.Map;

public class Clorus extends Creature {

    /** red color. */
    private int r;
    /** green color. */
    private int g;
    /** blue color. */
    private int b;

    private static final double moveEnergyLose = 0.03;

    private static final double statyEnergyLose = 0.01;

    private static final double repEnergyRetained = 0.5;

    private static final double moveProbability = 0.5;

    /** creates plip with energy equal to E. */
    public Clorus(double e) {
        super("clorus");
        r = 0;
        g = 0;
        b = 0;
        energy = e;
    }

    /** creates a plip with energy equal to 1. */
    public Clorus() {this(1);}

    public Color color() {
        r = 34;
        g = 0;
        b = 231;
        return color(r, g, b);
    }

    public void attack(Creature c) {
        energy = energy + c.energy();
    }

    public void move() {
        energy = energy - moveEnergyLose;
    }

    public void stay() {
        energy = energy - statyEnergyLose;
    }

    public Clorus replicate() {
        double babyEnergy = energy * (1 - repEnergyRetained);
        energy = energy * repEnergyRetained;
        return new Clorus(babyEnergy);

    }

    public Action chooseAction(Map<Direction, Occupant> neighbors) {
        java.util.List<Direction> empties = getNeighborsOfType(neighbors, "empty");

        List<Direction> plipSet = getNeighborsOfType(neighbors, "plip");

        if (empties.isEmpty()) {
            return new Action(Action.ActionType.STAY);
        }
        if (!plipSet.isEmpty()) {
            Direction moveDir = HugLifeUtils.randomEntry(plipSet);
            return new Action(Action.ActionType.ATTACK, moveDir);
        }
        if (energy >= 1) {
            Direction moveDir = HugLifeUtils.randomEntry(empties);
            return new Action(Action.ActionType.REPLICATE, moveDir);
        }
        Direction moveDir = HugLifeUtils.randomEntry(empties);
        return new Action(Action.ActionType.MOVE, moveDir);
    }

}
