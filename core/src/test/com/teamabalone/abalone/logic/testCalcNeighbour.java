package com.teamabalone.abalone.logic;

import com.teamabalone.abalone.Gamelogic.Directions;
import com.teamabalone.abalone.Gamelogic.Field;
import com.teamabalone.abalone.Gamelogic.HexCoordinate;

import org.junit.Assert;
import org.junit.Test;

import javax.jws.Oneway;

public class testCalcNeighbour {
    HexCoordinate should;
    HexCoordinate standard =  new HexCoordinate(0,0,0);
    Field field = new Field(5);

    @Test
    public void defaultTest(){
        should =  new HexCoordinate(0,0,0);
        HexCoordinate object = new HexCoordinate(0,0,0);
        Assert.assertEquals(should, object);
    }

    @Test
    public void calcLeft(){
        should = new HexCoordinate(-1,+1,0);
        HexCoordinate object = field.calcNeighbour(standard, Directions.LEFT);
        Assert.assertEquals(should, object);
    }

    @Test
    public void calcRight(){
        should = new HexCoordinate(+1,-1,0);
        HexCoordinate object = field.calcNeighbour(standard, Directions.RIGHT);
        Assert.assertEquals(should, object);
    }

    @Test
    public void calcRightUp(){
        should = new HexCoordinate(+1,0,-1);
        HexCoordinate object = field.calcNeighbour(standard, Directions.RIGHTUP);
        Assert.assertEquals(should, object);
    }

    @Test
    public void calcRightDown(){
        should = new HexCoordinate(0,-1,+1);
        HexCoordinate object = field.calcNeighbour(standard, Directions.RIGHTDOWN);
        Assert.assertEquals(should, object);
    }

    @Test
    public void calcLeftUp(){
        should = new HexCoordinate(0,+1,-1);
        HexCoordinate object = field.calcNeighbour(standard, Directions.LEFTUP);
        Assert.assertEquals(should, object);
    }

    @Test
    public void calcLeftDown(){
        should = new HexCoordinate(-1,0,+1);
        HexCoordinate object = field.calcNeighbour(standard, Directions.LEFTDOWN);
        Assert.assertEquals(should, object);
    }

    @Test (expected = IllegalStateException.class)
    public void calcFail(){
        field.calcNeighbour(standard, Directions.NOTSET);
    }


}
