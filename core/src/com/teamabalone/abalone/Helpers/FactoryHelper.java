package com.teamabalone.abalone.Helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * Contains helper methods for easy creation of certain libGdx elements (e.g. Buttons).
 */
public class FactoryHelper {

    /**
     * Returns the default button skin, which for now is the skinny glass ui from:
     * https://github.com/czyzby/gdx-skins
     * @return
     */
    public static Skin GetDefaultButtonSkin() {
        return new Skin(Gdx.files.internal(GameConstants.SKIN_GLASSY_UI_JSON));
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
        TextButton button = new TextButton(text, GetDefaultButtonSkin());
        button.setWidth(width);
        button.setHeight(height);
        button.setX(xPos);
        button.setY(yPos);
        return button;
    }

    /**
     * Helper method to create a simple TextButton with the given parameters.
     * @param text
     * @return
     */
    public static TextButton CreateButtonWithText(String text) {
        TextButton button = new TextButton(text, GetDefaultButtonSkin());
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
