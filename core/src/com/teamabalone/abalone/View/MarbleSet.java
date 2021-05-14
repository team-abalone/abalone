package com.teamabalone.abalone.View;

import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class representing a set of marbles of one team holding all it's marble sprites
 */
public class MarbleSet {
    private final HashMap<Integer, Sprite> marbles = new HashMap<>();

    public MarbleSet(ArrayList<Sprite> sprites) {
        if (sprites == null) {
            throw new IllegalArgumentException("no sprites were passed to marble set");
        }

        for (int i = 0; i < sprites.size(); i++) {
            marbles.put(i, sprites.get(i));
        }
    }

    /**
     * Get marble of marble set
     *
     * @param key hash map key to finde mable
     * @return marble
     */
    public Sprite getMarble(int key) {
        if (key >= 0 && key < marbles.size()) {
            return marbles.get(key);
        }

        throw new IllegalArgumentException("key for marble out of range");
    }

    public boolean contains(Sprite sprite) {
        return marbles.containsValue(sprite);
    }

    public int size() {
        return marbles.size();
    }
}
