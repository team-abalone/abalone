package com.teamabalone.abalone.view;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.teamabalone.abalone.View.Board;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class testBoard {

    private static final int MAP_SIZE = 5;
    private Board board;

    @Mock
    private TiledMapTileLayer tileLayerMock;

    @Mock
    Sprite sprite;

    @Before
    public void setup() {
        board = new Board(tileLayerMock, MAP_SIZE, 122, 130);
        when(tileLayerMock.getTileWidth()).thenReturn(64);
        when(tileLayerMock.getTileHeight()).thenReturn(74);
        when(sprite.getX()).thenReturn((float) 20);
        when(sprite.getY()).thenReturn((float) 20);
        when(sprite.getWidth()).thenReturn((float) 136);
        when(sprite.getHeight()).thenReturn((float) 136);
    }

    @Test
    public void testGetCenter() {
        Vector2 vector2 = new Vector2(88,88);
        Assert.assertEquals(vector2, board.getCenter(sprite));
    }
}
