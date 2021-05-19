package com.teamabalone.abalone.view;


import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.teamabalone.abalone.Gamelogic.Directions;
import com.teamabalone.abalone.View.Board;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;

@RunWith(MockitoJUnitRunner.class)
public class TestBoard {

    private static final int MAP_SIZE = 5;
    private Board board;

    @Spy
    private TiledMapTileLayer tileLayerMock = new TiledMapTileLayer(13, 13, 64, 74);

    @Spy
    Sprite sprite = new Sprite();

    @Before
    public void setup() {
        board = new Board(tileLayerMock, MAP_SIZE, 832, 740);
        sprite.setBounds(20, 20, 136, 136);
    }

    @Test
    public void testGetCenter() {
        Vector2 vector2 = new Vector2(88, 88);
        assertEquals(vector2, board.getCenter(sprite));
    }

    @Test
    public void testMoveRightUp() {
        Vector2 targetPosition = new Vector2(sprite.getX() + 32, sprite.getY() + 55.5f);
        Vector2 spritePosition = new Vector2(sprite.getX(), sprite.getY());

        assertNotEquals(targetPosition, spritePosition);

        board.move(sprite, Directions.RIGHTUP);
        spritePosition = new Vector2(sprite.getX(), sprite.getY());

        assertEquals(targetPosition, spritePosition);
    }

    @Test
    public void testMoveRight() {
        Vector2 targetPosition = new Vector2(sprite.getX() + 64, sprite.getY());
        Vector2 spritePosition = new Vector2(sprite.getX(), sprite.getY());

        assertNotEquals(targetPosition, spritePosition);

        board.move(sprite, Directions.RIGHT);
        spritePosition = new Vector2(sprite.getX(), sprite.getY());

        assertEquals(targetPosition, spritePosition);
    }

    @Test
    public void testMoveRightDown() {
        Vector2 targetPosition = new Vector2(sprite.getX() + 32, sprite.getY() - 55.5f);
        Vector2 spritePosition = new Vector2(sprite.getX(), sprite.getY());

        assertNotEquals(targetPosition, spritePosition);

        board.move(sprite, Directions.RIGHTDOWN);
        spritePosition = new Vector2(sprite.getX(), sprite.getY());

        assertEquals(targetPosition, spritePosition);
    }

    @Test
    public void testMoveLeftDown() {
        Vector2 targetPosition = new Vector2(sprite.getX() - 32, sprite.getY() - 55.5f);
        Vector2 spritePosition = new Vector2(sprite.getX(), sprite.getY());

        assertNotEquals(targetPosition, spritePosition);

        board.move(sprite, Directions.LEFTDOWN);
        spritePosition = new Vector2(sprite.getX(), sprite.getY());

        assertEquals(targetPosition, spritePosition);
    }

    @Test
    public void testMoveLeft() {
        Vector2 targetPosition = new Vector2(sprite.getX() - 64, sprite.getY());
        Vector2 spritePosition = new Vector2(sprite.getX(), sprite.getY());

        assertNotEquals(targetPosition, spritePosition);

        board.move(sprite, Directions.LEFT);
        spritePosition = new Vector2(sprite.getX(), sprite.getY());

        assertEquals(targetPosition, spritePosition);
    }

    @Test
    public void testMoveLeftUp() {
        Vector2 targetPosition = new Vector2(sprite.getX() - 32, sprite.getY() + 55.5f);
        Vector2 spritePosition = new Vector2(sprite.getX(), sprite.getY());

        assertNotEquals(targetPosition, spritePosition);

        board.move(sprite, Directions.LEFTUP);
        spritePosition = new Vector2(sprite.getX(), sprite.getY());

        assertEquals(targetPosition, spritePosition);
    }

    @Test
    public void testMoveNullDirection() {
        assertThrows(IllegalArgumentException.class, () -> board.move(sprite, null));
    }

    @Test
    public void testShiftLeftUpNegativeAmount() {
        Vector2 spritePosition = new Vector2(sprite.getX(), sprite.getY());
        assertEquals(spritePosition, board.shiftLeft(spritePosition, -1));
    }

    @Test
    public void testMoveNotSet() {
        assertThrows(IllegalArgumentException.class, () -> board.move(sprite, Directions.NOTSET));
    }

    @Test
    public void testMoveNull() {
        assertThrows(IllegalArgumentException.class, () -> board.move(null, Directions.RIGHT));
    }

    @Test
    public void testTileLayerNull() {
        assertThrows(IllegalArgumentException.class, () -> new Board(null, MAP_SIZE, 832, 740));
    }

    @Test
    public void testMapSizeNegative() {
        assertThrows(IllegalArgumentException.class, () -> new Board(tileLayerMock, -1, 832, 740));
    }

    @Test
    public void testGetTileIdNullArgument() {
        assertThrows(IllegalArgumentException.class, () -> board.getTileId(null));
    }

    @Test
    public void testGetTileId() {
        Sprite spriteLeft = new Sprite();
        spriteLeft.setCenter(289.5f, 369.5f);
        assertEquals(29, board.getTileId(spriteLeft));
    }

    @Test
    public void testGet() {
        Sprite spriteLeft = new Sprite();
        spriteLeft.setCenter(289.5f, 369.5f);
        Vector2 centerSprite = board.get(29);
        assertEquals(spriteLeft.getX(), centerSprite.x, 0.0001);
        assertEquals(spriteLeft.getY(), centerSprite.y, 0.0001);
    }

    @Test
    public void testGetCenterOfNUll() {
        assertThrows(IllegalArgumentException.class, () -> board.getCenter(null));
    }

    @Test
    public void testMoveLeftNoPointGiven() {
        assertThrows(IllegalArgumentException.class, () -> board.shiftLeft(null, 3));
    }

    @Test
    public void testGetTileIdNoSuchCenter() {
        assertEquals(-1, board.getTileId(sprite));
    }

}
