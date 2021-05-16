package com.teamabalone.abalone.logic.test;

import com.teamabalone.abalone.Gamelogic.HexCoordinate;

import org.junit.Assert;
import org.junit.Test;

public class HexCoordinateTest {

    HexCoordinate first = new HexCoordinate(0,0,0);
    HexCoordinate second = new HexCoordinate(-2,0,2);
    HexCoordinate third = new HexCoordinate(2,2,-4);
    HexCoordinate fourth = new HexCoordinate(1,0,-1);
    HexCoordinate fifth = new HexCoordinate(2,0,-2);

/*
    //Wahrscheinlich zu niedrige version
    @Test
    public void shouldThrow_WhenCreatingNonExistentHexCoordinate(){
        Assert.assertThrows(RuntimeException.class, ()-> new HexCoordinate(0,1,1));
    }

 */

    @Test
    public void ShouldReturnTrue_WhenSubtractionOfHexCoordinatesAreCorrect(){
        HexCoordinate resultOfSubtraction = HexCoordinate.subtract(first,second);

        Assert.assertEquals(2,resultOfSubtraction.getX());
        Assert.assertEquals(0,resultOfSubtraction.getY());
        Assert.assertEquals(-2,resultOfSubtraction.getZ());
    }

    @Test
    public void ShouldReturnTrue_WhenSumOfAbsoluteDividedByTwoIsCorrect(){
        Assert.assertEquals(0,HexCoordinate.abs(first));
        Assert.assertEquals(2,HexCoordinate.abs(second));
        Assert.assertEquals(4,HexCoordinate.abs(third));
    }

    @Test
    public void ShouldReturnTrue_WhenHexCoordinatesAreNeighbours(){
        Assert.assertTrue(HexCoordinate.isNeighbour(first,fourth));
    }

    @Test
    public void ShouldReturnTrue_WhenHexCoordinatesAreNotNeighbours(){
        Assert.assertFalse(HexCoordinate.isNeighbour(first,second));
        Assert.assertFalse(HexCoordinate.isNeighbour(first,third));
        Assert.assertFalse(HexCoordinate.isNeighbour(second,third));
        Assert.assertFalse(HexCoordinate.isNeighbour(third,fourth));
    }

    @Test
    public void ShouldReturnTrue_WhenHexCoordinatesAreInLine(){
        Assert.assertTrue(HexCoordinate.isInLine(first,fourth,fifth));
    }

    @Test
    public void ShouldReturnTrue_WhenHexCoordinatesAreNotInLine(){
        Assert.assertFalse(HexCoordinate.isInLine(first,second,third));
        Assert.assertFalse(HexCoordinate.isInLine(first,fourth,second));
        Assert.assertFalse(HexCoordinate.isInLine(first,second,fifth));
        Assert.assertFalse(HexCoordinate.isInLine(first,third,fifth));
        Assert.assertFalse(HexCoordinate.isInLine(first,third,second));

    }
}