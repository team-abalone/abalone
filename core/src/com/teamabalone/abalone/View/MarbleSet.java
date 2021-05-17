package com.teamabalone.abalone.View;

import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.ArrayList;

/**
 * Class representing a set of marbles of one team holding all it's marble sprites
 */
public class MarbleSet {
    private final ArrayList<Sprite> marbles = new ArrayList<>();

    public MarbleSet(ArrayList<Sprite> sprites) {
        if (sprites == null) {
            throw new IllegalArgumentException("no sprites were passed to marble set");
        }

        marbles.addAll(sprites);
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
        return marbles.contains(sprite);
    }

    public boolean remove(Sprite sprite) {
        return marbles.remove(sprite);
    }

    public int size() {
        return marbles.size();
    }
}
