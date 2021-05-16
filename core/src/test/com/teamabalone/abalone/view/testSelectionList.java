package com.teamabalone.abalone.view;

import com.teamabalone.abalone.View.SelectionList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class testSelectionList {
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
        Assert.assertTrue(returnValue);

        returnValue = list.select(2);
        Assert.assertFalse(returnValue);

        Assert.assertEquals(1, list.size());
    }

    @Test
    public void testMaximum() {
        Assert.assertEquals(maximum, list.size());
    }
}
