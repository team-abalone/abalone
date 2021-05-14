package com.teamabalone.abalone.View;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

public class GameSet { //singleton
    private static GameSet gameSet;
    private final ArrayList<MarbleSet> marbleSets = new ArrayList<>();

    private GameSet() {

    }

    public static GameSet getInstance() {
        if (gameSet == null) {
            gameSet = new GameSet();
        }
        return gameSet;
    }

    public MarbleSet register(Viewport viewport, Texture texture, float[] positions) {
        MarbleSet marbleSet = new MarbleSet(forgeMarbles(texture, positions));
        marbleSets.add(marbleSet);
        return marbleSet;
    }


    //coordinates are read alternately x1, y1, x2, y2, x3...
    //screen coordinates have to be converted to map coordinates
    public ArrayList<Sprite> forgeMarbles(Texture texture, float... coordinates) {
        if (coordinates.length % 2 != 0) {
            throw new IllegalArgumentException("field coordinates not an even number");
        }

        ArrayList<Sprite> sprites = new ArrayList<>();
        Sprite sprite;

        for (int i = 0; i < coordinates.length; i += 2) {
            sprite = new Sprite(texture);
            sprite.setCenter(coordinates[i], coordinates[i + 1]);
            sprites.add(sprite);
        }

        return sprites;
    }

    public ArrayList<MarbleSet> getMarbleSets() {
        return marbleSets;
    }

    public int getTeam(Sprite sprite) {
        for (int i = 0; i < marbleSets.size(); i++) {
            if (marbleSets.get(i).contains(sprite)) {
                return i;
            }
        }
        return -1;
    }

    //TODO depending on order of set the first set will be searched first, making it dominating in selecting a marble and is drawn first
    public Sprite getMarble(float x, float y) {
        for (int i = 0; i < marbleSets.size(); i++) {
            MarbleSet marbleSet = marbleSets.get(i);
            for (int k = 0; k < marbleSet.size(); k++) {
                Sprite sprite = marbleSet.getMarble(k);
                float xdiff = x - 32 - sprite.getX();
                float ydiff = y - 32 - sprite.getY();

                if (0 <= xdiff && xdiff <= sprite.getWidth() * sprite.getScaleX() && 0 <= ydiff && ydiff <= sprite.getHeight() * sprite.getScaleY()) {
                    return sprite;
                }
            }
        }
        return null;
    }

    public Sprite captureMarble(GameSet gameSet, Board board, int fieldId) { //TODO necessary?
        if (fieldId == -1) {
            return null;
        }

        Vector2 fieldCoordinates = board.get(fieldId);
        Sprite sprite = gameSet.getMarble(fieldCoordinates.x, fieldCoordinates.y);
        gameSet.removeMarble(sprite);
        return sprite;
    }

    public boolean removeMarble(Sprite sprite) {
//        for (int i = 0; i < marbleSets.size(); i++) {
//            MarbleSet marbleSet = marbleSets.get(i);
//            for (int k = 0; k < marbleSet.size(); k++) {
        return marbleSets.remove(sprite);
//            }
//        }
//        return false;
    }

}
