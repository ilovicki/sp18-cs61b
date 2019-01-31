package creatures;

import huglife.*;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class TestClorus {
    @Test
    public void testBasics() {
        Clorus c = new Clorus(2);
        Clorus d = new Clorus(3);
        c.move();
        assertEquals(1.97, c.energy(), 0.01);
        c.stay();
        assertEquals(1.96, c.energy(), 0.01);
        c.attack(d);
        assertEquals(4.96, c.energy(), 0.01);
        c.replicate();
        assertEquals(2.48, c.energy(), 0.01);
    }

    @Test
    public void testReplicate() {
        Clorus p = new Clorus(3);
        Clorus q = p.replicate();
        assertEquals(1.5, p.energy(), 0.01);
        assertEquals(1.5, q.energy(), 0.01);
        assertNotSame(p, q);
    }


    @Test
    public void testChoose() {
        Clorus c = new Clorus(1.2);
        HashMap<Direction, Occupant> surrounded = new HashMap<Direction, Occupant>();
        surrounded.put(Direction.TOP, new Impassible());
        surrounded.put(Direction.BOTTOM, new Impassible());
        surrounded.put(Direction.LEFT, new Impassible());
        surrounded.put(Direction.RIGHT, new Impassible());

        Action actual1 = c.chooseAction(surrounded);
        Action expected1 = new Action(Action.ActionType.STAY);
        assertEquals(expected1, actual1);

        surrounded.put(Direction.TOP, new Empty());
        surrounded.put(Direction.BOTTOM, new Plip());
        Action actual2 = c.chooseAction(surrounded);
        Action expected2 = new Action(Action.ActionType.ATTACK, Direction.BOTTOM);
        assertEquals(expected2, actual2);

        surrounded.put(Direction.BOTTOM, new Impassible());
        Action actual3 = c.chooseAction(surrounded);
        Action expected3 = new Action(Action.ActionType.REPLICATE, Direction.TOP);
        assertEquals(expected3, actual3);

        c = new Clorus(0.6);
        Action actual4 = c.chooseAction(surrounded);
        Action expected4 = new Action(Action.ActionType.MOVE, Direction.TOP);
        assertEquals(expected4, actual4);

    }
}
