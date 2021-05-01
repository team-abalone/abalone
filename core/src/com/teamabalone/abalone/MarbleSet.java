package com.teamabalone.abalone;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.HashMap;

public class MarbleSet {
    private final HashMap<Integer, Sprite> marbles = new HashMap<>();

    public MarbleSet(ArrayList<Sprite> sprites) {
        if (sprites != null) {
            for (int i = 0; i < sprites.size(); i++) {
                marbles.put(i, sprites.get(i));
            }
        } else {
            System.out.println("no sprites passed to marble set");
        }
    }

    public Sprite getMarble(int key) {
        if (key >= 0 && key < marbles.size()) {
            return marbles.get(key);
        }
        return null;
    }

    public int size() {
        return marbles.size();
    }
}
