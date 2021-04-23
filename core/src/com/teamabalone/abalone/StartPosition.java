package com.teamabalone.abalone;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.ArrayList;

public class StartPosition {

    private final ArrayList<Sprite> sprites = new ArrayList<>();

    //coordinates are read alternately x1, y1, x2, y2, x3...
    public StartPosition(Texture texture, float... coordinates) {
        if (coordinates.length % 2 == 0) {
            Sprite sprite;
            for (int i = 0; i < coordinates.length; i += 2) {
                sprite = new Sprite(texture);
                sprite.setPosition(coordinates[i], coordinates[i + 1]);
                sprites.add(sprite);
            }
        } else {
            //System.out.println("invalid start position parameter count");
        }
    }

    public ArrayList<Sprite> getSprites() {
        return sprites;
    }

}
