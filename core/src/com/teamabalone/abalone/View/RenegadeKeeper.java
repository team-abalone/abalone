package com.teamabalone.abalone.View;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class RenegadeKeeper {

    private boolean canPickRenegade = false;
    private int lastRenegadeId = -1;

    private boolean hasExposeAttempt = true;
    private boolean doubleTurn = false;

    public void setCanPickRenegade() {
        canPickRenegade = true;
    }

    public boolean isCanPickRenegade() {
        return canPickRenegade;
    }

    public void chooseRenegade(Sprite sprite, Texture texture, int currentPlayer) {
        GameSet.getInstance().removeMarble(sprite);
        sprite.setTexture(texture);
        GameSet.getInstance().getMarbleSets().get(currentPlayer).addMarble(sprite);
        canPickRenegade = false;
    }

    public void checkNewRenegade(int currentRenegadeId){
        if(lastRenegadeId != currentRenegadeId){
            lastRenegadeId = currentRenegadeId;
            hasExposeAttempt = true;
        }
    }

    public boolean hasDoubleTurn() {
        return doubleTurn;
    }

    public void takeDoubleTurn() {
        doubleTurn = false;
    }

    public void expose(int suspectedSpriteId) {
        if (hasExposeAttempt) {
            if (suspectedSpriteId == lastRenegadeId) {
                doubleTurn = true;
            }
            hasExposeAttempt = false;
        }
    }

}
