package com.teamabalone.abalone.View;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.teamabalone.abalone.Gamelogic.Directions;

import java.util.ArrayList;

/**
 * The Board class holds the center coordinates of all field tiles of a hexagon map, also providing methods to move marbles in each direction. A vertical row oriented hexagon tile can be separated into a rectangle in the middle and two triangles, one on top and one below. Accordingly, the top Height represents the vertical height of one of those triangles and the bottom height the sum of the vertical height of the rectangle and the vertical triangle height. Each map has a specific edge length expressed in the number of edge tiles.
 */
public class Board {
    private final ArrayList<Vector2> tiles = new ArrayList<>();

    private final float boardWidth;
    private final float boardHeight;
    private final float topHeight;
    private final float bottomHeight;

    private final float tileWidth;
    private final float tileHeight;
    private final int numberEdgeTiles;

    public Board(TiledMapTileLayer tileLayer, int mapSize, float boardWidth, float boardHeight) {
        if (tileLayer == null) {
            throw new IllegalArgumentException("no tileLayer has been passed to board");
        }
        if (mapSize < 0) {
            throw new IllegalArgumentException("negative tile map size");
        }

        tileWidth = tileLayer.getTileWidth();
        tileHeight = tileLayer.getTileHeight();
        numberEdgeTiles = mapSize;

        topHeight = (float) Math.round(Math.tan(Math.toRadians(30)) * tileWidth / 2f * 10f) / 10f;
        bottomHeight = tileHeight - topHeight;
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;

        this.instantiate();
    }

    private void instantiate() {
        //the center coordinates will be stored two times: on index 0 and in the middle of the remaining array, basically making tiles 1-indexed and allowing quick access to center tile coordinates
        tiles.add(new Vector2(boardWidth / 2f + 1.5f, boardHeight / 2f - 0.5f)); //center; manually centered (+1.5f -0.5f)
        final Vector2 center = tiles.get(0);

        //starting in left upper corner, indexing row after row going down
        int maxTilesPerRow = ((numberEdgeTiles * 2) - 1);
        for (int a = numberEdgeTiles - 1; a >= (-numberEdgeTiles) + 1; a--) {
            Vector2 leftmostMarble = shiftLeft((a >= 0 ? shiftLeftUp(center, a) : shiftLeftDown(center, -a)), (numberEdgeTiles - 1) - Math.abs(a));

            //indexes row starting left
            for (int k = 0; k < maxTilesPerRow - Math.abs(a); k++) {
                tiles.add(shiftRight(leftmostMarble, k));
            }
        }
    }

    /**
     * Move marble one tile in left-up direction
     *
     * @param point current center
     * @return center after move
     */
    public Vector2 shiftLeftUp(Vector2 point) {
        return shiftLeftUp(point, 1);
    }

    /**
     * Move marble n tiles in left-up direction
     *
     * @param point        current center
     * @param howManyTimes number of tiles to move
     * @return center after move
     */
    public Vector2 shiftLeftUp(Vector2 point, int howManyTimes) {
        Vector2 vector = createVector(point);
        for (int i = 0; i < howManyTimes; i++) {
            vector.x -= tileWidth / 2f;
            vector.y += bottomHeight;
        }
        return vector;
    }

    public Vector2 shiftLeft(Vector2 point) {
        return shiftLeft(point, 1);
    }

    public Vector2 shiftLeft(Vector2 point, int howManyTimes) {
        Vector2 vector = createVector(point);
        for (int i = 0; i < howManyTimes; i++) {
            vector.x -= tileWidth;
        }
        return vector;
    }

    public Vector2 shiftLeftDown(Vector2 point) {
        return shiftLeftDown(point, 1);
    }

    public Vector2 shiftLeftDown(Vector2 point, int howManyTimes) {
        Vector2 vector = createVector(point);
        for (int i = 0; i < howManyTimes; i++) {
            vector.x -= tileWidth / 2f;
            vector.y -= bottomHeight;
        }
        return vector;
    }

    public Vector2 shiftRightUp(Vector2 point) {
        return shiftRightUp(point, 1);
    }

    public Vector2 shiftRightUp(Vector2 point, int howManyTimes) {
        Vector2 vector = createVector(point);
        for (int i = 0; i < howManyTimes; i++) {
            vector.x += tileWidth / 2f;
            vector.y += bottomHeight;
        }
        return vector;
    }

    public Vector2 shiftRight(Vector2 point) {
        return shiftRight(point, 1);
    }

    public Vector2 shiftRight(Vector2 point, int howManyTimes) {
        Vector2 vector = createVector(point);
        for (int i = 0; i < howManyTimes; i++) {
            vector.x += tileWidth;
        }
        return vector;
    }

    public Vector2 shiftRightDown(Vector2 point) {
        return shiftRightDown(point, 1);
    }

    public Vector2 shiftRightDown(Vector2 point, int howManyTimes) {
        Vector2 vector = createVector(point);
        for (int i = 0; i < howManyTimes; i++) {
            vector.x += tileWidth / 2f;
            vector.y -= bottomHeight;
        }
        return vector;
    }

    public Vector2 get(int index) {
        return tiles.get(index);
    }

    /**
     * Null check
     *
     * @param point old Vector2
     * @return new Vector2
     */
    private Vector2 createVector(Vector2 point) {
        if (point == null) {
            throw new IllegalArgumentException("no point passed");
        }
        return new Vector2(point.x, point.y);
    }

    /**
     * Get index of board tile representing its id. The board is indexed start with 1 in left upper corner going down row after row.
     *
     * @param sprite sprite on the tile of interest
     * @return index of board tile
     */
    public int getTileId(Sprite sprite) {
        if (sprite == null) {
            throw new IllegalArgumentException("no sprite passed");
        }

        Vector2 center = getCenter(sprite);
        for (int i = 1; i < tiles.size(); i++) {
            if (tiles.get(i).x == center.x && tiles.get(i).y == center.y) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Get center of tile under the sprite (= sprite center)
     *
     * @param sprite sprite of which the coordinates are used to calculate the center of the tile
     * @return center coordinates
     */
    public Vector2 getCenter(Sprite sprite) {
        if (sprite == null) {
            throw new IllegalArgumentException("no sprite passed");
        }

        return new Vector2(sprite.getX() + sprite.getWidth() / 2, sprite.getY() + sprite.getHeight() / 2);
    }

    /**
     * Move Sprite one tile in any direction by setting it's center to the center of the target tile
     * @param sprite sprite to move
     * @param direction direction to move in
     */
    public void move(Sprite sprite, Directions direction) {
        if (sprite == null || direction == null || direction == Directions.NOTSET) {
            throw new IllegalArgumentException("no sprite and/or direction passed");
        }

        Vector2 center = getCenter(sprite);

        switch (direction) {
            case RIGHT:
                center = shiftRight(center);
                break;
            case RIGHTUP:
                center = shiftRightUp(center);
                break;
            case RIGHTDOWN:
                center = shiftRightDown(center);
                break;
            case LEFT:
                center = shiftLeft(center);
                break;
            case LEFTUP:
                center = shiftLeftUp(center);
                break;
            case LEFTDOWN:
                center = shiftLeftDown(center);
        }

        sprite.setCenter(center.x, center.y);
    }
}
