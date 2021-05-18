package com.teamabalone.abalone.view;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.teamabalone.abalone.View.MarbleSet;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Spy;

import java.util.ArrayList;

public class TestMarbleSet {
    ArrayList<Sprite> list = new ArrayList<>();
    MarbleSet marbleSet;

    @Spy
    Sprite sprite1= new Sprite();
    Sprite sprite2= new Sprite();
    Sprite sprite3= new Sprite();
    Sprite sprite4= new Sprite();

    @Before
    public void setup() {
        list.add(sprite1);
        list.add(sprite2);
        list.add(sprite3);
        list.add(sprite4);
        marbleSet = new MarbleSet(list);
    }

    @Test
    public void testConstructorNullArgument() {
        Assert.assertThrows(IllegalArgumentException.class, () -> new MarbleSet(null));
    }

    @Test
    public void testGetMarble() {
        Assert.assertEquals(sprite1, marbleSet.getMarble(0));
    }

    @Test
    public void testGetMarbleOutOfRange() {
        Assert.assertThrows(IllegalArgumentException.class,()-> marbleSet.getMarble(4));
    }

    @Test
    public void testGetMarbleOutOfRangeNegative() {
        Assert.assertThrows(IllegalArgumentException.class,()-> marbleSet.getMarble(-1));
    }

}
