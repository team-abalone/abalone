package com.teamabalone.abalone.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.teamabalone.abalone.Helpers.FactoryHelper;


//Sollte einfach nur initialisiert werden  bei der abalone class und bei ne touch an ende das pop up zeigen

public class TurnAnnouncer extends ScreenAdapter {
    private Stage stage;
    private Skin skin;

    @Override
    public void show(){
        Gdx.input.setInputProcessor(stage = new Stage());
        skin = FactoryHelper.GetDefaultSkin();

        ExitDialog exitDia = new ExitDialog("TEsting", skin);

        exitDia.show(stage);
    }

    public static class ExitDialog extends Dialog{

        public ExitDialog(String title, Skin skin) {
            super(title, skin);
        }

        public ExitDialog(String title, Skin skin, String windowStyleName) {
            super(title, skin, windowStyleName);
        }

        public ExitDialog(String title, WindowStyle windowStyle) {
            super(title, windowStyle);
        }
        {
            //text("Testing");
            button("Yes");
        }

        @Override
        protected void result(Object object){

        }
    }

}
