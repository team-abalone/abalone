package com.teamabalone.abalone.view;

import com.teamabalone.abalone.View.SelectionList;

import org.junit.Assert;
import org.junit.Test;

public class testSelectionList {

    @Test
    public void createSelectionList() {
        SelectionList<Integer> list = new SelectionList<>(2);
        list.select(2);
        list.select(6);
        list.select(-2);
        Assert.assertFalse(list.isEmpty());
        Assert.assertEquals("[2, 6]", list.toString());
        Assert.assertEquals(6, (long) list.get(1));
        list.unselectAll();
        Assert.assertTrue(list.isEmpty());
    }

    @Test
    public void test(){
        Assert.assertEquals(true,true);
    }
}
