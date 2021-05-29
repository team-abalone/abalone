package com.teamabalone.abalone.View;

import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.ArrayList;

/**
 * Class representing a set of marbles of one team holding all it's marble sprites.
 */
public class MarbleSet {
    private final ArrayList<Sprite> marbles = new ArrayList<>();

    /**
     * Constructor for this {@code MarbleSet}.
     * <p></p>
     * Takes the given argument and if it's not null adds the whole ArrayList into it's own.
     *
     * @param sprites  an {@link ArrayList} of {@link Sprite Sprites}, not null
     */
    public MarbleSet(ArrayList<Sprite> sprites) {
        if (sprites == null) {
            throw new IllegalArgumentException("no sprites were passed to marble set");
        }

        marbles.addAll(sprites);
    }

    /**
     * Get marble of marble set.
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

    /**
     * Returns if an object is within this arrayList.
     *
     * @param sprite  the element to search for
     * @return  true if it's in this list, false if not
     */
    public boolean contains(Sprite sprite) {
        return marbles.contains(sprite);
    }

    /**
     * Removes given {@code Sprite} from this arrayList.
     *
     * @param sprite  the element to search for
     * @return  true if anything was removed, false if not
     */
    public boolean remove(Sprite sprite) {
        return marbles.remove(sprite);
    }

    public boolean addMarble(Sprite sprite) {
        return marbles.add(sprite);
    }

    /**
     * Returns the number of the current {@code Sprites} in this arrayList.
     *
     * @return the number of items
     */
    public int size() {
        return marbles.size();
    }
}
