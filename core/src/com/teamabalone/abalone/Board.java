package com.teamabalone.abalone;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

public class Board {
    private static Board board;
    private final ArrayList<Vector2> fields = new ArrayList<>();

    private Viewport viewport;
    private TiledMapTileLayer tileLayer;

    private float boardWidth;
    private float boardHeight;
    private float topHight;
    private float bottomHight;
    private float edge;

    private Board(Viewport viewport, TiledMapTileLayer tileLayer) {
        this.viewport = viewport;
        this.tileLayer = tileLayer;

        boardWidth = tileLayer.getWidth() * tileLayer.getTileWidth();
        //height needs to take overlap in account (55,5 + 18,5 = 74)
        boardHeight = 55.5f * (tileLayer.getHeight() - 1) + tileLayer.getTileHeight();
        topHight = (float) Math.round(Math.tan(Math.toRadians(30)) * tileLayer.getTileWidth() / 2f * 10f) / 10f;
        bottomHight = tileLayer.getTileHeight() - topHight;
        edge = bottomHight - topHight;
    }

    public static Board getInstance(Viewport viewport, TiledMapTileLayer tileLayer) {
        if (board == null) {
            board = new Board(viewport, tileLayer);
            board.instantiate();
        }
        return board;
    }

    private void instantiate() {
        fields.add(toMapCoordinates(boardWidth/2f, boardHeight/2f));
        fields.add(toMapCoordinates(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f));
    }

    private Vector2 toMapCoordinates(float x, float y) {
        Vector3 v = new Vector3(x, y, 0f);
        viewport.unproject(v);
        return new Vector2(Math.round(v.x), Math.round(v.y));
    }

    public Vector2 get(int index) {
        return fields.get(index);
    }

    public float getBoardWidth() {
        return boardWidth;
    }

    public float getBoardHeight() {
        return boardHeight;
    }
}
