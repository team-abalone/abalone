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
}
