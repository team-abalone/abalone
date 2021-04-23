package com.teamabalone.abalone.Dialogs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.teamabalone.abalone.Helpers.FactoryHelper;

/**
 * Dialog for the settings screen, enabling color selection and
 * sound effect / background music settings.
 * TODO: Not yet working properly.
 */
public class SettingsDialog extends Dialog {
    private ImageButton ExitButton;

    public SettingsDialog(String title, Skin skin) {
        super(title, skin);
        Table contentTable = getContentTable();
        contentTable.setDebug(true);

        contentTable.setWidth(Gdx.graphics.getWidth());
        contentTable.setWidth(Gdx.graphics.getWidth());

        ExitButton = FactoryHelper.CreateImageButton(skin.get("exit-btn", ImageButton.ImageButtonStyle.class));
        ExitButton.setHeight(100);
        ExitButton.setWidth(100);

        ExitButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Gdx.app.log("ClickListener", ExitButton.toString() + " clicked");
                    remove();

                };
        });

        contentTable.add(ExitButton);
    }

    @Override
    public Dialog show(Stage stage) {
        return super.show(stage);
    }
}
