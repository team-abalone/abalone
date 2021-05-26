package com.teamabalone.abalone.Dialogs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.teamabalone.abalone.Abalone;
import com.teamabalone.abalone.Helpers.FactoryHelper;

/**
 * Dialog for the settings screen, enabling color selection and
 * sound effect / background music settings.
 * TODO: Not yet working properly.
 */
public class SettingsDialog extends Dialog {
    private ImageButton exitButton;
    //settings variables
    float bgMusicVolumeFactor;
    boolean sfxSoundActive;
    String marbleSkin;
    String boardSkin;
    boolean colorSetting;
    private Stage stage;
    //
    Preferences settings = Gdx.app.getPreferences("UserSettings");

    public SettingsDialog(String title, Skin skin, Abalone abalone) {
        super(title, skin);

        Table rootTable = getContentTable();
        rootTable.setFillParent(true);
        //rootTable.setDebug(true);

        //exit Button setup
        Label header = new Label("Settings", skin);
        exitButton = FactoryHelper.createImageButton(skin.get("exit-btn", ImageButton.ImageButtonStyle.class));
        exitButton.setHeight(100);
        exitButton.setWidth(100);

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("ClickListener", exitButton.toString() + " clicked");
                if (abalone != null) {
                    abalone.updateSettings();
                }
                remove();
            }

            ;
        });

        // Music Control setup
        Label musicVolume = new Label("Music Volume:", skin);
        final Slider slider = new Slider(0, 100, 1, false, skin);
        slider.setValue(settings.getFloat("bgMusicVolumeFactor", 1f) * 100);
        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                bgMusicVolumeFactor = slider.getValue() / 100;
                Gdx.app.log("ClickListener", bgMusicVolumeFactor + " was set");
                settings.putFloat("bgMusicVolumeFactor", bgMusicVolumeFactor);
                settings.flush();
                if (abalone != null) {
                    abalone.updateSettings();
                }
            }
        });

        //SFX Control setup
        Label sfxCheck = new Label("Disbale SFX?", skin);
        final CheckBox sfxBox = new CheckBox("", skin);
        sfxBox.setChecked(settings.getBoolean("sfxSoundActive", false));
        sfxBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (sfxBox.isChecked()) {
                    sfxSoundActive = false;
                } else {
                    sfxSoundActive = true;
                }
                settings.putBoolean("sfxSoundActive", sfxSoundActive);
                settings.flush();
                Gdx.app.log("ClickListener", "The SFX is: " + sfxSoundActive);
            }
        });

        //Marble visual setup
        Label marbleSkinLabel = new Label("Choose a Marble Skin:", skin);
        FileHandle[] directoryMarbles = Gdx.files.internal("marbles/").list();          //fetches the files in this directory
        Array<String> marbleSkinsList = new Array<String>();
        for (FileHandle i : directoryMarbles) {
            marbleSkinsList.add(i.name());                                  //saves the name of all files in this directory
        }
        final SelectBox<String> marbleSkins = new SelectBox<String>(skin);
        marbleSkins.setItems(marbleSkinsList);
        marbleSkins.setSelected(settings.getString("marbleSkin" + 1));
        marbleSkins.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                marbleSkin = marbleSkins.getSelected();
                Gdx.app.log("ClickListener", marbleSkin + " was set");
                settings.putString("marbleSkin" + 1, marbleSkin);
                settings.flush();
            }
        });

        //Marble RGB color selection
        Label marbleRGBLabel = new Label("Choose a Marble Color:", skin);
        final CheckBox colorBox = new CheckBox("", skin);
        colorBox.setChecked(settings.getBoolean("colorSetting", false));
        Button selectColor = FactoryHelper.createButtonWithText("Select Color");
        if(colorBox.isChecked()){
            selectColor.setVisible(true);
            marbleSkins.setVisible(false);
        } else{
            selectColor.setVisible(false);
            marbleSkins.setVisible(true);
        }
        colorBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (colorBox.isChecked()) {
                    colorSetting = true;
                    settings.putString("marbleSkin" + 1, "ball_white.png");
                    settings.flush();
                    marbleSkins.setSelected("ball_white.png");
                    marbleSkins.setVisible(false);
                    Gdx.app.log("ClickListener", "The marble skin for painting has ben set to ball_white.png");
                    selectColor.setVisible(true);
                } else {
                    colorSetting = false;
                    marbleSkins.setVisible(true);
                    selectColor.setVisible(false);
                }
                settings.putBoolean("colorSetting", colorSetting);
                settings.flush();
                Gdx.app.log("ClickListener", "The Color Setting is: " + sfxSoundActive);
            }
        });
        selectColor.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ColorPickerDialog colorPickerDialog = new ColorPickerDialog("", skin, abalone);
                colorPickerDialog.show(stage);

            }
        });





        //Gameboard visual setup
        Label boardSkinLabel = new Label("Chose a Board Skin:", skin);
        FileHandle[] directoryBoard = Gdx.files.internal("boards/").list();          //fetches the files in this directory
        Array<String> boardSkinList = new Array<String>();
        for (FileHandle i : directoryBoard) {
            boardSkinList.add(i.name());                                  //saves the name of all files in this directory
        }
        final SelectBox<String> boardSkins = new SelectBox<String>(skin);
        boardSkins.setItems(boardSkinList);
        boardSkins.setSelected(settings.getString("boardSkin"));
        boardSkins.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boardSkin = boardSkins.getSelected();
                Gdx.app.log("ClickListener", boardSkin + " was set");
                settings.putString("boardSkin", boardSkin);
                settings.flush();
            }
        });

        //Table that holds everything gets filled
        rootTable.add(header).left();
        rootTable.add();
        rootTable.add(exitButton).right().top().expandX();
        rootTable.row();
        rootTable.add(musicVolume);
        rootTable.add(slider).width(600);
        rootTable.row();
        rootTable.add(sfxCheck);
        rootTable.add(sfxBox).width(100).left().padLeft(50);
        rootTable.row();
        rootTable.add(marbleSkinLabel);
        rootTable.add(marbleSkins).center();
        rootTable.row();
        rootTable.add(marbleRGBLabel);
        rootTable.add(colorBox).left().padLeft(50);
        rootTable.add(selectColor).maxHeight(100f).left();
        rootTable.row();
        rootTable.add(boardSkinLabel);
        rootTable.add(boardSkins).center();
    }

    @Override
    public Dialog show(Stage stage) {
        this.stage = stage;
        return super.show(stage);
    }
}
