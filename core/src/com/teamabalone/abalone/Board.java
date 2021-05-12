package com.teamabalone.abalone;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

public class Board { //singleton
    private static Board board;
    private final ArrayList<Vector2> fields = new ArrayList<>();

    private Viewport viewport;
    private TiledMapTileLayer tileLayer;

    private float boardWidth;
    private float boardHeight;
    private float topHight;
    private float bottomHight;
    private float edge;

    private float tileWidth;
    private float tileHeight;
    private int numberEdgeTiles;

    private Board(Viewport viewport, TiledMapTileLayer tileLayer, int mapSize) {
        this.viewport = viewport;
        this.tileLayer = tileLayer;

        boardWidth = tileLayer.getWidth() * tileLayer.getTileWidth();
        //height needs to take overlap in account (55,5 + 18,5 = 74)
        boardHeight = 55.5f * (tileLayer.getHeight() - 1) + tileLayer.getTileHeight();
        topHight = (float) Math.round(Math.tan(Math.toRadians(30)) * tileLayer.getTileWidth() / 2f * 10f) / 10f;
        bottomHight = tileLayer.getTileHeight() - topHight;
        edge = bottomHight - topHight;

        tileWidth = tileLayer.getTileWidth();
        tileHeight = tileLayer.getTileHeight();
        numberEdgeTiles = mapSize; //TODO generalize
    }

    public static Board getInstance(Viewport viewport, TiledMapTileLayer tileLayer, int mapSize) {
        if (board == null) {
            board = new Board(viewport, tileLayer, mapSize);
            board.instantiate();
        }
        return board;
    }

    public static Board getInstance() { //TODO proper implementation of singleton?
        return board;
    }

    private void instantiate() {
        fields.add(new Vector2(boardWidth / 2f + 1.5f, boardHeight / 2f - 0.5f)); //center; manually centered (+1.5f -0.5f)
        final Vector2 center = fields.get(0);

        //starting in left upper corner, indexing row after row going down
        int maxTilesPerRow = ((numberEdgeTiles * 2) - 1);
        for (int a = numberEdgeTiles - 1; a >= (-numberEdgeTiles) + 1; a--) {
            Vector2 leftmostMarble = shiftLeft((a >= 0 ? shiftLeftUp(center, a) : shiftLeftDown(center, -a)), (numberEdgeTiles - 1) - Math.abs(a));

            //indexes row starting left
            for (int k = 0; k < maxTilesPerRow - Math.abs(a); k++) {
                fields.add(shiftRight(leftmostMarble, k));
            }
        }
    }

    //navigation methods
    public Vector2 shiftLeftUp(Vector2 point) {
        return shiftLeftUp(point, 1);
    }

    public Vector2 shiftLeftUp(Vector2 point, int howManyTimes) {
        Vector2 vector = new Vector2(point.x, point.y);
        for (int i = 0; i < howManyTimes; i++) {
            vector.x -= tileWidth / 2f;
            vector.y += bottomHight;
        }
        return vector;
    }

    public Vector2 shiftLeft(Vector2 point) {
        return shiftLeft(point, 1);
    }

    public Vector2 shiftLeft(Vector2 point, int howManyTimes) {
        Vector2 vector = new Vector2(point.x, point.y);
        for (int i = 0; i < howManyTimes; i++) {
            vector.x -= tileWidth;
        }
        return vector;
    }

    public Vector2 shiftLeftDown(Vector2 point) {
        return shiftLeftDown(point, 1);

    }

    public Vector2 shiftLeftDown(Vector2 point, int howManyTimes) {
        Vector2 vector = new Vector2(point.x, point.y);
        for (int i = 0; i < howManyTimes; i++) {
            vector.x -= tileWidth / 2f;
            vector.y -= bottomHight;
        }
        return vector;
    }

    public Vector2 shiftRightUp(Vector2 point) {
        return shiftRightUp(point, 1);
    }

    public Vector2 shiftRightUp(Vector2 point, int howManyTimes) {
        Vector2 vector = new Vector2(point.x, point.y);
        for (int i = 0; i < howManyTimes; i++) {
            vector.x += tileWidth / 2f;
            vector.y += bottomHight;
        }
        return vector;
    }

    public Vector2 shiftRight(Vector2 point) {
        return shiftRight(point, 1);
    }

    public Vector2 shiftRight(Vector2 point, int howManyTimes) {
        Vector2 vector = new Vector2(point.x, point.y);
        for (int i = 0; i < howManyTimes; i++) {
            vector.x += tileWidth;
        }
        return vector;
    }

    public Vector2 shiftRightDown(Vector2 point) {
        return shiftRightDown(point, 1);
    }

    public Vector2 shiftRightDown(Vector2 point, int howManyTimes) {
        Vector2 vector = new Vector2(point.x, point.y);
        for (int i = 0; i < howManyTimes; i++) {
            vector.x += tileWidth / 2f;
            vector.y -= bottomHight;
        }
        return vector;
    }

    public Vector2 get(int index) {
        return fields.get(index);
    }

    public int getFieldId(Sprite sprite) {
        Vector2 center = getCenter(sprite);
        for (int i = 1; i < fields.size(); i++) {
            if (fields.get(i).x == center.x && fields.get(i).y == center.y) {
                return i;
            }
        }
        return -1;
    }

    public Vector2 getCenter(Sprite sprite) {
        return new Vector2(sprite.getX() + sprite.getWidth() / 2, sprite.getY() + sprite.getHeight() / 2);
    }

    public void set(int index, Vector2 vector) {
        fields.set(index, vector);
    }

    public float getBoardWidth() {
        return boardWidth;
    }

    public float getBoardHeight() {
        return boardHeight;
    }
}
