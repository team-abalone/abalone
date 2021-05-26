package com.teamabalone.abalone.View;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.ArrayList;

/**
 * Singleton instance containing all marble sets of a game in one collection.
 */
public class GameSet { //singleton
    private static GameSet gameSet;
    private final ArrayList<MarbleSet> marbleSets = new ArrayList<>();

    private GameSet() {
        //private Constructor
    }

    public static GameSet getInstance() {
        if (gameSet == null) {
            gameSet = new GameSet();
        }
        return gameSet;
    }

    /**
     * Set singleton instance to null, so the next getInstance() call will create a new instance. Possible way to delete the current GameSet.
     *
     * @return old GameSet
     */
    public static GameSet reset() {
        GameSet oldGameSet = gameSet;
        gameSet = null;
        return oldGameSet;
    }

    /**
     * Create and add a new marble set to GameSet instance.
     *
     * @param texture   marble team texture
     * @param positions center coordinates of marbles
     * @return created marble set
     */
    public MarbleSet register(Texture texture, int rgbaValue, float[] positions ) {
        MarbleSet marbleSet = new MarbleSet(createMarbles(texture, rgbaValue, positions));
        marbleSets.add(marbleSet);
        return marbleSet;
    }

    public MarbleSet register(Texture texture, float[] positions ) {
        return register(texture, 0 , positions);
    }

    /**
     * Creates sprite instances with given texture and center coordinates. The coordinates are read successively in pairs (x1, y1, x2, y2, x3...) each providing the x and teh y-coordinate for the center of a marble.
     *
     * @param texture     texture for sprites, not null
     * @param coordinates coordinates to center the sprites
     * @param rgbaValue  the possibly desired coloring, not null
     * @return list of created sprites
     * @throws IllegalArgumentException  in case of an coordinate that's not even
     */
    public ArrayList<Sprite> createMarbles(Texture texture, int rgbaValue,  float... coordinates) {
        if (texture == null) {
            throw new IllegalArgumentException("no texture passed");
        }

        if (coordinates.length % 2 != 0) {
            throw new IllegalArgumentException("field coordinates not an even number");
        }

        ArrayList<Sprite> sprites = new ArrayList<>();
        Sprite sprite;

        for (int i = 0; i < coordinates.length; i += 2) {
            sprite = new Sprite(texture);
            if(rgbaValue != 0){
                sprite.setColor(new Color(rgbaValue));
            }
            sprite.setCenter(coordinates[i], coordinates[i + 1]);
            sprites.add(sprite);
        }

        return sprites;
    }

    public ArrayList<Sprite> createMarbles(Texture texture,  float... coordinates) {
        return createMarbles(texture, 0, coordinates);
    }

    public ArrayList<MarbleSet> getMarbleSets() {
        return marbleSets;
    }

    /**
     * Get index of marble set that contains the specified sprite.
     *
     * @param sprite sprite  of interest
     * @return marble set index
     */
    public int getTeamIndex(Sprite sprite) {
        for (int i = 0; i < marbleSets.size(); i++) {
            if (marbleSets.get(i).contains(sprite)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Find marble at specific coordinates (should be map coordinates)
     *
     * @param x x-component
     * @param y y-component
     * @return sprite in GameSet
     */
    public Sprite getMarble(float x, float y) {
        Sprite sprite;

        for (int i = 0; i < marbleSets.size(); i++) {
            MarbleSet marbleSet = marbleSets.get(i);

            for (int k = 0; k < marbleSet.size(); k++) {
                sprite = marbleSet.getMarble(k);
                float xdiff = x - (sprite.getX() + 32); //32 = half of tile width
                float ydiff = y - (sprite.getY() + 32);

                if (0 <= xdiff && xdiff <= sprite.getWidth() * sprite.getScaleX() &&
                        0 <= ydiff && ydiff <= sprite.getHeight() * sprite.getScaleY()) {
                    return sprite;
                }
            }
        }
        return null;
    }

    /**
     * Remove marble from it's marble set
     *
     * @param sprite marble to remove
     * @return true on success
     */
    public boolean removeMarble(Sprite sprite) {
        for (MarbleSet marbleSet : marbleSets) {
            if (marbleSet.remove(sprite)) {
                return true;
            }
        }
        return false;
    }

    public void colorMarbleSet(int rgbaValue, int index){
        for (int i = 0; i < marbleSets.get(index).size(); i++) {
            marbleSets.get(index).getMarble(i).setColor(new Color(rgbaValue));
        }
    }
}
