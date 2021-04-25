package com.teamabalone.abalone.Dialogs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class TurnAnnouncerTwo extends Dialog {

    public TurnAnnouncerTwo(String title, Skin skin) {
        super(title, skin);
        Table contentTable = getContentTable();
        contentTable.setDebug(true);

        contentTable.setWidth(Gdx.graphics.getWidth());
        contentTable.setWidth(Gdx.graphics.getWidth());
    }

    @Override
    public Dialog show(Stage stage) {
        return super.show(stage);
    }
}

