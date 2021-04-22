package com.teamabalone.abalone.Helpers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Helpers {
    public static Drawable TextureToDrawable(Texture texture) {
        return new TextureRegionDrawable(new TextureRegion(texture));
    }
}
