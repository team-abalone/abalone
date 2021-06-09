package com.teamabalone.abalone.View;

public class RenegadeKeeper {

    private boolean canPickRenegade = false;
    private int lastRenegadeId = -1;

    private boolean hasExposeAttempt = false;
    private boolean doubleTurn = false;

    public void setRenegade(int renegadeId) {
        lastRenegadeId = renegadeId;
        canPickRenegade = true;
    }

    public boolean isCanPickRenegade() {
        return canPickRenegade;
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
                return true;
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

    public void setCanPickRenegadeTrue() {
        canPickRenegade = true;
    }
}
