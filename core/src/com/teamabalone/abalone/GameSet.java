package com.teamabalone.abalone;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
        StartPosition startPosition = new StartPosition(viewport, texture, positions);
        MarbleSet marbleSet = new MarbleSet(startPosition.getSprites());
        marbleSets.add(marbleSet);
        return marbleSet;
    }

    public ArrayList<MarbleSet> getMarbleSets() {
        return marbleSets;
    }

    public Sprite getMarble(float x, float y) {
        for (int i = 0; i < marbleSets.size(); i++) {
            MarbleSet marbleSet = marbleSets.get(i);
            for (int k = 0; k < marbleSet.size(); k++) {
                Sprite sprite = marbleSet.getMarble(k);
                float xdiff = x - sprite.getX();
                float ydiff = y - sprite.getY();
                if ( 0 <= xdiff && xdiff <= sprite.getWidth() && 0 <= ydiff && ydiff <= sprite.getHeight()) {
                    return sprite;
                }
            }
        }
        return null;
    }
}
