package com.teamabalone.abalone;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

public class StartPosition {

    private final ArrayList<Sprite> sprites = new ArrayList<>();

    //coordinates are read alternately x1, y1, x2, y2, x3...
    //screen coordinates have to be converted to map coordinates
    public StartPosition(Viewport viewport, Texture texture, float... coordinates) {
        if (coordinates.length % 2 == 0) {
            Sprite sprite;

            for (int i = 0; i < coordinates.length; i += 2) {
                sprite = new Sprite(texture);
                sprite.setCenter(coordinates[i], coordinates[i + 1]);
                sprites.add(sprite);
            }
        } else {
            System.out.println("invalid start position parameter count");
        }
    }

    public ArrayList<Sprite> getSprites() {
        return sprites;
    }

}
