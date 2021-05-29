package com.teamabalone.abalone.logic.test;

import com.teamabalone.abalone.Gamelogic.HexCoordinate;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class HexCoordinateTest {

    HexCoordinate first = new HexCoordinate(0, 0, 0);
    HexCoordinate second = new HexCoordinate(-2, 0, 2);
    HexCoordinate third = new HexCoordinate(2, 2, -4);
    HexCoordinate fourth = new HexCoordinate(1, 0, -1);
    HexCoordinate fifth = new HexCoordinate(2, 0, -2);

    //Wahrscheinlich zu niedrige version
    @Test
    public void shouldThrow_WhenCreatingNonExistentHexCoordinate() {

        assertThrows(RuntimeException.class, () -> new HexCoordinate(0, 1, 1));
    }

    @Test
    public void ShouldReturnTrue_WhenSubtractionOfHexCoordinatesAreCorrect() {
        HexCoordinate resultOfSubtraction = HexCoordinate.subtract(first, second);


        assertEquals(2, resultOfSubtraction.getX());

        assertEquals(0, resultOfSubtraction.getY());

        assertEquals(-2, resultOfSubtraction.getZ());
    }

    @Test
    public void ShouldReturnTrue_WhenSumOfAbsoluteDividedByTwoIsCorrect() {

        assertEquals(0, HexCoordinate.abs(first));

        assertEquals(2, HexCoordinate.abs(second));

        assertEquals(4, HexCoordinate.abs(third));
    }

    @Test
    public void ShouldReturnTrue_WhenHexCoordinatesAreNeighbours() {

        assertTrue(HexCoordinate.isNeighbour(first, fourth));
    }

    @Test
    public void ShouldReturnTrue_WhenHexCoordinatesAreNotNeighbours() {

        assertFalse(HexCoordinate.isNeighbour(first, second));

        assertFalse(HexCoordinate.isNeighbour(first, third));

        assertFalse(HexCoordinate.isNeighbour(second, third));

        assertFalse(HexCoordinate.isNeighbour(third, fourth));
    }

    @Test
    public void ShouldReturnTrue_WhenHexCoordinatesAreInLine() {

        assertTrue(HexCoordinate.isInLine(first, fourth, fifth));
    }

    @Test
    public void ShouldReturnTrue_WhenHexCoordinatesAreNotInLine() {

        assertFalse(HexCoordinate.isInLine(first, second, third));

        assertFalse(HexCoordinate.isInLine(first, fourth, second));

        assertFalse(HexCoordinate.isInLine(first, second, fifth));

        assertFalse(HexCoordinate.isInLine(first, third, fifth));

        assertFalse(HexCoordinate.isInLine(first, third, second));

    }
}