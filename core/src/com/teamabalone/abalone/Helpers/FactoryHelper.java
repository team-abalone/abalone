package com.teamabalone.abalone.Helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * Contains helper methods for easy creation of certain libGdx elements (e.g. Buttons).
 */
public class FactoryHelper {
    public static final String FONT_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;,{}\"´`'<>";

    /**
     * Returns the default button skin, which for now is the skinny glass ui from:
     * https://github.com/czyzby/gdx-skins
     * @return
     */
    public static Skin GetDefaultSkin() {
        return new Skin(Gdx.files.internal(GameConstants.CUSTOM_UI_JSON));
    }


    /**
     * Returns the default textureAtlas.
     * @return
     */
    public static TextureAtlas GetAtlas() {
        return new TextureAtlas(Gdx.files.internal(GameConstants.CUSTOM_UI_ATLAS));
    }

    /**
     * Returns the region with the given name in the atlas.
     * @param name
     * @return
     */
    public static TextureAtlas.AtlasRegion GetAtlasRegion(String name) {
        return GetAtlas().findRegion(name);
    }

    /**
     * Helper method to create a simple TextButton with the given parameters.
     * @param text
     * @param width
     * @param height
     * @param xPos
     * @param yPos
     * @return
     */
    public static TextButton CreateButtonWithText(String text, float width, float height, float xPos, float yPos) {
        TextButton button = new TextButton(text, GetDefaultSkin());
        button.setWidth(width);
        button.setHeight(height);
        button.setX(xPos);
        button.setY(yPos);
        return button;
    }

    /**
     * Helper method to create a simple TextButton with the given parameters.
     * @param text
     * @param width
     * @param height
     * @return
     */
    public static TextButton CreateButtonWithText(String text, float width, float height) {
        TextButton button = new TextButton(text, GetDefaultSkin());
        button.setWidth(width);
        button.setHeight(height);
        return button;
    }

    /**
     * Helper method to create a simple TextButton with the given parameters.
     * @param text
     * @return
     */
    public static TextButton CreateButtonWithText(String text) {
        TextButton button = new TextButton(text, GetDefaultSkin());
        return button;
    }

    /**
     * Creates a simple Table with the given parameters.
     * @param width
     * @param height
     * @param xPos
     * @param yPos
     * @return
     */
    public static Table CreateTable(float width, float height, float xPos, float yPos) {
        Table table = new Table();
        table.setWidth(width);
        table.setHeight(height);
        table.setX(xPos);
        table.setY(yPos);

        return table;
    }

    /**
     * Creates a simple ImageButton with the given parameters.
     * @param imageUp
     * @param imageDown
     * @param background
     * @param width
     * @param height
     * @param xPos
     * @param yPos
     * @return
     */
    public static ImageButton CreateImageButton(Drawable imageUp, Drawable imageDown, Drawable background,
                                          float width, float height, float xPos, float yPos) {
        ImageButton imageButton = new ImageButton(imageUp, imageDown, background);
        imageButton.setWidth(width);
        imageButton.setHeight(height);
        imageButton.setX(xPos);
        imageButton.setY(yPos);

        return imageButton;
    }

    /**
     * Creates a simple ImageButton with the given parameters.#
     * @param width
     * @param height
     * @param xPos
     * @param yPos
     * @return
     */
    public static ImageButton CreateImageButton(ImageButton.ImageButtonStyle style, float width, float height, float xPos, float yPos) {
        ImageButton imageButton = new ImageButton(style);
        imageButton.setWidth(width);
        imageButton.setHeight(height);
        imageButton.setX(xPos);
        imageButton.setY(yPos);

        return imageButton;
    }

    /**
     * Creates a simple ImageButton with the given parameters.
     * @return
     */
    public static ImageButton CreateImageButton(ImageButton.ImageButtonStyle style) {
        ImageButton imageButton = new ImageButton(style);
        return imageButton;
    }


    /**
     * Creates a simple ImageButton with the given parameters.
     * @param imageUp
     * @param imageDown
     * @param background
     * @param xPos
     * @param yPos
     * @return
     */
    public static ImageButton CreateImageButton(Drawable imageUp, Drawable imageDown, Drawable background,
                                          float xPos, float yPos) {
        ImageButton imageButton = new ImageButton(imageUp, imageDown, background);
        imageButton.setX(xPos);
        imageButton.setY(yPos);

        return imageButton;
    }
}
