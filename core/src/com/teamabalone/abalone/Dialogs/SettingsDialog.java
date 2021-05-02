package com.teamabalone.abalone.Dialogs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.teamabalone.abalone.Helpers.FactoryHelper;

/**
 * Dialog for the settings screen, enabling color selection and
 * sound effect / background music settings.
 * TODO: Not yet working properly.
 */
public class SettingsDialog extends Dialog {
    private ImageButton exitButton;
    float bgMusicVolumeFactor;
    boolean sfxSoundActive;
    Preferences settings = Gdx.app.getPreferences("UserSettings");

    public SettingsDialog(String title, Skin skin) {
        super(title, skin);

        Table rootTable = getContentTable();
        rootTable.setFillParent(true);
        //rootTable.setDebug(true);

        //exit Button setup
        exitButton = FactoryHelper.CreateImageButton(skin.get("exit-btn", ImageButton.ImageButtonStyle.class));
        exitButton.setHeight(100);
        exitButton.setWidth(100);

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("ClickListener", exitButton.toString() + " clicked");
                remove();

            };
        });

        // Music Controll setup
        Label  musicVolume = new Label("Music Volume:", skin);
        final Slider slider = new Slider(0, 100, 1, false, skin);
        slider.setValue(settings.getFloat("bgMusicVolumeFactor")*100);
        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                bgMusicVolumeFactor = slider.getValue() /100;
                Gdx.app.log("ClickListener", bgMusicVolumeFactor + " was set");
                settings.putFloat("bgMusicVolumeFactor", bgMusicVolumeFactor);
                settings.flush();
            }
        });

        //SFX Controll setup
        Label sfxCheck = new Label("Disbale SFX?", skin);
        final CheckBox sfxBox = new CheckBox("", skin);
        sfxBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                 if(sfxBox.isChecked()){
                     sfxSoundActive = false;
                 } else{
                     sfxSoundActive = true;
                 }
                Gdx.app.log("ClickListener", "The SFX is: "+ sfxSoundActive);
            }
        });

        //Marble visual setup
        Label marbleSkinLabel = new Label("Choose a Skin:", skin);



        rootTable.add(exitButton).right().top().expandX();
        rootTable.row();
        rootTable.add(musicVolume);
        rootTable.add(slider).width(600);
        rootTable.row();
        rootTable.add(sfxCheck);
        rootTable.add(sfxBox).width(100).left().padLeft(50);






        /*Table contentTable = getContentTable();
        contentTable.setDebug(true);

        contentTable.setWidth(Gdx.graphics.getWidth());
        contentTable.setHeight(Gdx.graphics.getHeight());

        exitButton = FactoryHelper.CreateImageButton(skin.get("exit-btn", ImageButton.ImageButtonStyle.class));
        exitButton.setHeight(100);
        exitButton.setWidth(100);

        exitButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Gdx.app.log("ClickListener", exitButton.toString() + " clicked");
                    remove();
                };
        });
        contentTable.add(exitButton);*/
    }
    @Override
    public Dialog show(Stage stage) {
        return super.show(stage);
    }
}
