package com.teamabalone.abalone.Helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
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
     *
     * @return
     */
    public static Skin getDefaultSkin() {
        return new Skin(Gdx.files.internal(GameConstants.CUSTOM_UI_JSON));
    }


    /**
     * Returns the default textureAtlas.
     *
     * @return
     */
    public static TextureAtlas getAtlas() {
        return new TextureAtlas(Gdx.files.internal(GameConstants.CUSTOM_UI_ATLAS));
    }

    /**
     * Returns the region with the given name in the atlas.
     *
     * @param name
     * @return
     */
    public static TextureAtlas.AtlasRegion getAtlasRegion(String name) {
        return getAtlas().findRegion(name);
    }

    /**
     * Helper method to create a simple TextButton with the given parameters.
     *
     * @param text
     * @param width
     * @param height
     * @param xPos
     * @param yPos
     * @return
     */
    public static TextButton createButtonWithText(String text, float width, float height, float xPos, float yPos) {
        TextButton button = new TextButton(text, getDefaultSkin());
        button.setWidth(width);
        button.setHeight(height);
        button.setX(xPos);
        button.setY(yPos);
        return button;
    }

    /**
     * Helper method to create a simple TextButton with the given parameters.
     *
     * @param text
     * @param width
     * @param height
     * @return
     */
    public static TextButton createButtonWithText(String text, float width, float height) {
        TextButton button = new TextButton(text, getDefaultSkin());
        button.setWidth(width);
        button.setHeight(height);
        return button;
    }

    /**
     * Helper method to create a simple TextButton with the given parameters.
     *
     * @param text
     * @return
     */
    public static TextButton createButtonWithText(String text) {
        TextButton button = new TextButton(text, getDefaultSkin());
        return button;
    }

    //TODO document and set skin
    public static Label createLabelWithText(String text, float width, float height) {
        Label label = new Label(text, getDefaultSkin());
        label.setWidth(width);
        label.setHeight(height);
        return label;
    }

    /**
     * Creates a simple Table with the given parameters.
     *
     * @param width
     * @param height
     * @param xPos
     * @param yPos
     * @return
     */
    public static Table createTable(float width, float height, float xPos, float yPos) {
        Table table = new Table();
        table.setWidth(width);
        table.setHeight(height);
        table.setX(xPos);
        table.setY(yPos);

        return table;
    }

    /**
     * Creates a simple ImageButton with the given parameters.
     *
     * @param imageUp
     * @param imageDown
     * @param background
     * @param width
     * @param height
     * @param xPos
     * @param yPos
     * @return
     */
    public static ImageButton createImageButton(Drawable imageUp, Drawable imageDown, Drawable background,
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
     *
     * @param width
     * @param height
     * @param xPos
     * @param yPos
     * @return
     */
    public static ImageButton createImageButton(ImageButton.ImageButtonStyle style, float width, float height, float xPos, float yPos) {
        ImageButton imageButton = new ImageButton(style);
        imageButton.setWidth(width);
        imageButton.setHeight(height);
        imageButton.setX(xPos);
        imageButton.setY(yPos);

        return imageButton;
    }

    /**
     * Creates a simple ImageButton with the given parameters.
     *
     * @return
     */
    public static ImageButton createImageButton(ImageButton.ImageButtonStyle style) {
        ImageButton imageButton = new ImageButton(style);
        return imageButton;
    }


    /**
     * Creates a simple ImageButton with the given parameters.
     *
     * @param imageUp
     * @param imageDown
     * @param background
     * @param xPos
     * @param yPos
     * @return
     */
    public static ImageButton createImageButton(Drawable imageUp, Drawable imageDown, Drawable background,
                                                float xPos, float yPos) {
        ImageButton imageButton = new ImageButton(imageUp, imageDown, background);
        imageButton.setX(xPos);
        imageButton.setY(yPos);

        return imageButton;
    }
}
