package com.teamabalone.abalone.view;


import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.teamabalone.abalone.Gamelogic.Directions;
import com.teamabalone.abalone.View.Board;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class testBoard {

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
        Assert.assertEquals(vector2, board.getCenter(sprite));
    }

    @Test
    public void testMoveRightUp() {
        Vector2 targetPosition = new Vector2(sprite.getX() + 32, sprite.getY() + 55.5f);
        Vector2 spritePosition = new Vector2(sprite.getX(), sprite.getY());

        Assert.assertNotEquals(targetPosition, spritePosition);

        board.move(sprite, Directions.RIGHTUP);
        spritePosition = new Vector2(sprite.getX(), sprite.getY());

        Assert.assertEquals(targetPosition, spritePosition);
    }

    @Test
    public void testTileLayerNull(){
        Assert.assertThrows(IllegalArgumentException.class, () -> new Board(null, MAP_SIZE, 832, 740));
    }

}
