package com.teamabalone.abalone.Dialogs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.teamabalone.abalone.Helpers.FactoryHelper;

/**
 * Dialog for the settings screen, enabling color selection and
 * sound effect / background music settings.
 * TODO: Not yet working properly.
 */
public class SettingsDialog extends Dialog {
    private final int PAD_TOP = 40;
    private ImageButton exitButton;

    //settings variables
    private float bgMusicVolumeFactor;
    private boolean sfxSoundActive;
    private String marbleSkin;
    private String boardSkin;

    private final TextField tfUsername;
    private final Label lblUserName;

    //
    Preferences settings = Gdx.app.getPreferences("UserSettings");

    public SettingsDialog(String title, Skin skin) {
        super(title, skin);

        Table rootTable = getContentTable();
        rootTable.setFillParent(true);

        //exit Button setup
        Label header = new Label("Settings", skin);
        exitButton = FactoryHelper.CreateImageButton(skin.get("exit-btn", ImageButton.ImageButtonStyle.class));
        exitButton.setHeight(100);
        exitButton.setWidth(100);

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                remove();
            };
        });

        // Music Controll setup
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
            }
        });

        //SFX Controll setup
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
        marbleSkins.setSelected(settings.getString("marbleSkin"));
        marbleSkins.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                marbleSkin = marbleSkins.getSelected();
                Gdx.app.log("ClickListener", marbleSkin + " was set");
                settings.putString("marbleSkin", marbleSkin);
                settings.flush();
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

        lblUserName = new Label("Username:", skin);

        tfUsername = new TextField(settings.getString("UserName"), FactoryHelper.GetDefaultSkin());
        TextField.TextFieldStyle style = tfUsername.getStyle();
        style.background.setLeftWidth(60);
        tfUsername.setStyle(style);

        tfUsername.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settings.putString("UserName", tfUsername.getText());
                settings.flush();
            }
        });

        //Table that holds everything gets filled
        rootTable.add(header).left();
        rootTable.add(exitButton).right().top().expandX();

        rootTable.row().padTop(100);
        rootTable.add(musicVolume).left();
        rootTable.add(slider).width(600);

        rootTable.row().padTop(PAD_TOP);
        rootTable.add(sfxCheck).left();
        rootTable.add(sfxBox).width(100).center().padLeft(50);

        rootTable.row().padTop(PAD_TOP);
        rootTable.add(marbleSkinLabel).left();
        rootTable.add(marbleSkins).center();

        rootTable.row().padTop(PAD_TOP);
        rootTable.add(boardSkinLabel).left();
        rootTable.add(boardSkins).center();

        rootTable.row().padTop(PAD_TOP);
        rootTable.add(lblUserName).left();
        rootTable.add(tfUsername).center().width(600);
    }

    @Override
    public Dialog show(Stage stage) {
        return super.show(stage);
    }
}
