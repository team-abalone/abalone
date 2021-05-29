package com.teamabalone.abalone.view;

import com.teamabalone.abalone.View.SelectionList;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestSelectionList {
    private SelectionList<Integer> list;
    private final int maximum = 2;
    private final int overflow = 2;

    @Before
    public void setup() {
        list = new SelectionList<>(maximum);

        for (int i = 0; i < maximum + overflow; i++) {
            list.select((int) ((Math.random() - 0.5) * 1000));
        }
    }

    @Test
    public void testSelectDuplicate() {
        list.unselectAll();

        boolean returnValue = list.select(2);
        assertTrue(returnValue);

        returnValue = list.select(2);
        assertFalse(returnValue);

        assertEquals(1, list.size());
    }

    @Test
    public void testMaximum() {
        assertEquals(maximum, list.size());
    }

//    @Test
//    public void testUnselect() {
//        list.unselect(list.get(0));
//        assertEquals(maximum - 1, list.size());
//    }

//    @Test
//    public void testCreate() {
//        assertFalse(list.isEmpty());
//        assertEquals("[0, 6]", list.toString());
//        assertEquals(6, (long) list.get(1));updat
//        list.unselectAll();
//        assertTrue(list.isEmpty());
//    }

//    @Test
//    public void testUnselectAll() {
//        list.unselectAll();
//        assertEquals(0, list.size());
//    }

}
