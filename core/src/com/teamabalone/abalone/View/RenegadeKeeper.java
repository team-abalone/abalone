package com.teamabalone.abalone.View;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class RenegadeKeeper {

    private boolean canPickRenegade = false;
    private Sprite recentRenegade = null;

    private int[] exposeAttempts;
    private boolean doubleTurn = false;

    public RenegadeKeeper(int numberPlayers) {
        exposeAttempts = new int[numberPlayers];
    }

    public void mayChooseRenegade() {
        canPickRenegade = true;
    }

    public boolean isCanPickRenegade(){
        return canPickRenegade;
    }

    public void chooseRenegade(Sprite sprite) {
        recentRenegade = sprite;
        canPickRenegade = false;
    }

}
