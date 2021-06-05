package com.teamabalone.abalone.View;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class RenegadeKeeper {

    private boolean canPickRenegade = false;
    private int lastRenegadeId = -1;

    private boolean hasExposeAttempt = false;
    private boolean doubleTurn = false;

    public void setCanPickRenegade() {
        canPickRenegade = true;
    }

    public boolean isCanPickRenegade() {
        return canPickRenegade;
    }

    public void chooseRenegade(Sprite sprite, Texture texture, int currentPlayer, Board board) {
        GameSet.getInstance().removeMarble(sprite);
        sprite.setTexture(texture); //set texture
        GameSet.getInstance().getMarbleSets().get(currentPlayer).addMarble(sprite);
        lastRenegadeId = board.getTileId(sprite);
        canPickRenegade = false;
    }

    public void checkNewRenegade(int currentRenegadeId) { //only one expose attempt per renegade
        if (currentRenegadeId == -1 && lastRenegadeId != -1) { //reset
            lastRenegadeId = -1;
            hasExposeAttempt = false;
        }
        if (lastRenegadeId != currentRenegadeId) {
            lastRenegadeId = currentRenegadeId;
            hasExposeAttempt = true;
        }
    }

    public boolean expose(int suspectedSpriteId) {
        if (hasExposeAttempt) {
            if (suspectedSpriteId == lastRenegadeId) {
                doubleTurn = true;
                return doubleTurn;
            }
            hasExposeAttempt = false;
        }
        return false;
    }

    public boolean hasDoubleTurn() {
        return doubleTurn;
    }

    public void takeDoubleTurn() {
        doubleTurn = false;
    }
}
