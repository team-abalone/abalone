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
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.teamabalone.abalone.Abalone;
import com.teamabalone.abalone.Helpers.FactoryHelper;
import com.teamabalone.abalone.Helpers.GameConstants;

/**
 * Dialog for the settings screen, enabling color selection and
 * sound effect / background music settings.
 * TODO: Not yet working properly.
 */
public class SettingsDialog extends Dialog {
    private final int PAD_TOP = 30;
    private final int PAD_LEFT = 30;

    private ImageButton exitButton;

    //Settings variables
    private float bgMusicVolumeFactor;
    private boolean tiltingActivated;
    private String marbleSkin;
    private String boardSkin;
    private boolean colorSetting;
    private Stage stage;

    Preferences settings = Gdx.app.getPreferences("UserSettings");

    public SettingsDialog(String title, Skin skin, Abalone abalone) {
        super(title, skin);

        Table rootTable = getContentTable();
        rootTable.setFillParent(true);

        Table titleTable = getTitleTable();
        titleTable.padTop(80);

        //exit Button setup
        exitButton = FactoryHelper.createExitButton();

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (abalone != null) {
                    abalone.updateSettings();
                }
                remove();
            };
        });

        // Music control setup.
        Label lblMusicVolume = new Label("Music Volume:", skin);
        Slider slider = new Slider(0, 100, 1, false, skin);
        slider.setValue(settings.getFloat("bgMusicVolumeFactor", 1f) * 100);
        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                bgMusicVolumeFactor = slider.getValue() / 100;
                settings.putFloat("bgMusicVolumeFactor", bgMusicVolumeFactor);
                settings.flush();
                if (abalone != null) {
                    abalone.updateSettings();
                }
            }
        });

        //Tilting Control setup
        Label tiltCheck = new Label("Tilting Movement active?", skin);
        final CheckBox tiltBox = new CheckBox("", skin);
        tiltBox.setChecked(settings.getBoolean("TiltingActive", false));
        tiltBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                tiltingActivated = tiltBox.isChecked(); //toggle
                settings.putBoolean("TiltingActive", tiltingActivated);
                settings.flush();
                Gdx.app.log("ClickListener", "Tilting Movement is: " + tiltingActivated);
            }
        });

        //Marble visual setup
        Label lblMarbleSkins = new Label("Choose a marble skin:", skin);
        SelectBox<String> selectMarbleSkins = new SelectBox<String>(skin);

        selectMarbleSkins.setItems(GameConstants.MARBLE_SKINS.keySet().toArray(new String[GameConstants.MARBLE_SKINS.size()]));

        selectMarbleSkins.setSelected(settings.getString("marbleSkin" + 1));
        selectMarbleSkins.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                marbleSkin = selectMarbleSkins.getSelected();
                settings.putString("marbleSkin" + 1, GameConstants.getMarbleSkinPathPerName(marbleSkin));
                settings.flush();
            }
        });

        //Marble RGB color selection
        Label lblMarbleRgb = new Label("Choose a Marble Color:", skin);
        CheckBox colorBox = new CheckBox("", skin);
        colorBox.setChecked(settings.getBoolean("colorSetting", false));
        Button btnSelectColor = FactoryHelper.createButtonWithText("Select Color");

        if(colorBox.isChecked()){
            btnSelectColor.setVisible(true);
            selectMarbleSkins.setVisible(false);
        } else {
            btnSelectColor.setVisible(false);
            selectMarbleSkins.setVisible(true);
        }
        colorBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (colorBox.isChecked()) {
                    colorSetting = true;
                    settings.putString("marbleSkin" + 0, GameConstants.getMarbleSkinPathPerName("ball_white.png"));
                    settings.flush();
                    selectMarbleSkins.setSelected("ball_white.png");
                    selectMarbleSkins.setVisible(false);
                    Gdx.app.log("ClickListener", "The marble skin for painting has ben set to ball_white.png");
                    btnSelectColor.setVisible(true);
                } else {
                    colorSetting = false;
                    selectMarbleSkins.setVisible(true);
                    btnSelectColor.setVisible(false);
                }
                settings.putBoolean("colorSetting", colorSetting);
                settings.flush();
                Gdx.app.log("ClickListener", "The Color Setting is: " + tiltingActivated);
            }
        });

        btnSelectColor.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ColorPickerDialog colorPickerDialog = new ColorPickerDialog("", skin, abalone);
                colorPickerDialog.show(stage);
            }
        });

        // Board skin selection.
        Label lblBoardSkins = new Label("Chose a Board Skin:", skin);

        SelectBox<String> selectBoardSkins = new SelectBox<String>(skin);
        selectBoardSkins.setItems(GameConstants.BOARD_SKINS.keySet().toArray(new String[GameConstants.BOARD_SKINS.size()]));

        selectBoardSkins.setSelected(settings.getString("boardSkin"));
        selectBoardSkins.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boardSkin = selectBoardSkins.getSelected();
                settings.putString("boardSkin", GameConstants.getBoardSkinPathPerName(boardSkin));
                settings.flush();
            }
        });

        Label lblUserName = new Label("Username:", skin);

        TextField tfUsername = new TextField(settings.getString("UserName"), FactoryHelper.getDefaultSkin());
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

        // ExitButton needs to be added to titleTable.
        titleTable.add(exitButton).right().top().width(100).height(100);

        //Table that holds everything gets filled
        rootTable.row().padTop(100);
        rootTable.add(lblMusicVolume).left().width(800);
        rootTable.add(slider).width(800);

        rootTable.row().padTop(PAD_TOP);
        rootTable.add(tiltCheck).left();
        rootTable.add(tiltBox).width(100).left().padLeft(PAD_LEFT);

        rootTable.row().padTop(PAD_TOP);
        rootTable.add(lblMarbleSkins).left();
        rootTable.add(selectMarbleSkins).left().padLeft(PAD_LEFT);

        rootTable.row();
        rootTable.add(lblMarbleRgb).left();
        rootTable.add(colorBox).left().padLeft(PAD_LEFT).width(100);
        rootTable.add(btnSelectColor).maxHeight(100f).left().padLeft(-450);

        rootTable.row().padTop(PAD_TOP);
        rootTable.add(lblBoardSkins).left();
        rootTable.add(selectBoardSkins).left().padLeft(PAD_LEFT);

        rootTable.row().padTop(PAD_TOP);
        rootTable.add(lblUserName).left();
        rootTable.add(tfUsername).center().width(800).height(100).padLeft(PAD_LEFT);
    }

    @Override
    public Dialog show(Stage stage) {
        this.stage = stage;
        return super.show(stage);
    }
}
