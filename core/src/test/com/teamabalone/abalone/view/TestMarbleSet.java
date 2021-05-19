package com.teamabalone.abalone.view;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.teamabalone.abalone.View.MarbleSet;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Spy;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

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
        assertThrows(IllegalArgumentException.class, () -> new MarbleSet(null));
    }

    @Test
    public void testGetMarble() {
        assertEquals(sprite1, marbleSet.getMarble(0));
    }

    @Test
    public void testGetMarbleOutOfRange() {
        assertThrows(IllegalArgumentException.class,()-> marbleSet.getMarble(4));
    }

    @Test
    public void testGetMarbleOutOfRangeNegative() {
        assertThrows(IllegalArgumentException.class,()-> marbleSet.getMarble(-1));
    }

}
