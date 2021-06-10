package com.teamabalone.abalone.Dialogs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.teamabalone.abalone.Abalone;
import com.teamabalone.abalone.Helpers.FactoryHelper;

public class ColorPickerDialog extends Dialog {
    private ImageButton exitButton;
    private Pixmap colorTable;
    private int rgbaValue;
    private Stage stage;
    private Preferences settings = Gdx.app.getPreferences("UserSettings");

    public ColorPickerDialog(String title, Skin skin, Abalone abalone) {
        super(title, skin);

        Table rootTable = getContentTable();
        rootTable.setFillParent(true);

        Table titleTable = getTitleTable();
        titleTable.padTop(80);
        rgbaValue = settings.getInteger("rgbaValue", -197377);

        // Exit Button setup
        exitButton = FactoryHelper.createExitButton();

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (abalone != null) {
                    abalone.updateSettings();
                }
                colorTable.dispose();
                settings.putInteger("rgbaValue", rgbaValue);
                settings.flush();
                remove();
            }
        });

        //The Preview Marble
        Table marbleTable = new Table();
        Texture previewMarble = new Texture("marbles/ball_white.png");
        Image preview = new Image();
        preview.setDrawable(new TextureRegionDrawable(new TextureRegion(previewMarble)));  //wtf is this shit???
        preview.scaleBy(1.5f);
        preview.setColor(new Color(rgbaValue));

        marbleTable.add(new Label("Preview:", skin)).height(400).width(300).padLeft(70);
        marbleTable.row();
        marbleTable.add(preview).maxWidth(300).maxHeight(300).left();

        //The ColorMap
        colorTable = new Pixmap(Gdx.files.internal("images/colorTable.png"));
        Image color = new Image();
        color.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture(colorTable))));  //wtf is this shit???
        color.addListener(new DragListener() {
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                rgbaValue = colorTable.getPixel((int) x, 800 - (int) y);
                if (rgbaValue != 0) {
                    preview.setColor(new Color(rgbaValue));
                }
                Gdx.app.log("ClickListener", rgbaValue + " was clicked");
                Gdx.app.log("ClickListener", x + ", " + y + " was clicked");
                super.touchDragged(event, x, y, pointer);
            }
        });

        rootTable.add();
        rootTable.add().width(700);
        titleTable.add(exitButton).width(100).height(100).top().right();
        rootTable.row();
        rootTable.add(color);
        rootTable.add(marbleTable).center();
    }

    @Override
    public Dialog show(Stage stage) {
        this.stage = stage;
        return super.show(stage);
    }

}
