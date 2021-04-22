package com.teamabalone.abalone;

import com.badlogic.gdx.Game;
import com.teamabalone.abalone.Screens.MenuScreen;

public class AbaloneGame extends Game {
    public AbaloneGame() {
    }

    @Override
    public void create() {
        setScreen(new MenuScreen(this));
    }
}
