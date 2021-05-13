package com.teamabalone.abalone;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.teamabalone.abalone.Gamelogic.Directions;

import java.util.ArrayList;

public class SelectionList<t> {
    private final ArrayList<t> arrayList = new ArrayList<>();
    private final int maximum;

    public SelectionList(int maximum) {
        this.maximum = maximum;
    }

    public int size() {
        return arrayList.size();
    }

    public t get(int index) {
        return arrayList.get(index);
    }

    public ArrayList<t> getArrayList() {
        return arrayList;
    }

    public boolean isSelected(t object) {
        return arrayList.contains(object);
    }

    public boolean isEmpty() {
        return arrayList.isEmpty();
    }

    public boolean select(t object) { //TODO only select marbles of specific color
        //TODO check if valid -> only marbles in a row, else unselect all | here?
        if (arrayList.size() < maximum) {
            if (!arrayList.contains(object)) {
                return arrayList.add(object);
            }
        }
        return false;
    }

    public Vector2 getCenter(Sprite sprite) { //TODO method here?
        return new Vector2(sprite.getX() + sprite.getWidth() / 2f, sprite.getY() + sprite.getHeight() / 2f);
    }

    public void move(int index, Directions direction) { //not generic!
        Sprite sprite = (Sprite) arrayList.get(index);
        Board board = Board.getInstance();

        if(sprite == null || board == null){ //TODO proper handling
            return;
        }

        Vector2 vector = getCenter(sprite);

        switch (direction) {
            case RIGHT:
                vector = board.shiftRight(vector);
                break;
            case RIGHTUP:
                vector = board.shiftRightUp(vector);
                break;
            case RIGHTDOWN:
                vector = board.shiftRightDown(vector);
                break;
            case LEFT:
                vector = board.shiftLeft(vector);
                break;
            case LEFTUP:
                vector = board.shiftLeftUp(vector);
                break;
            case LEFTDOWN:
                vector = board.shiftLeftDown(vector);
                break;
        }

        sprite.setCenter(vector.x, vector.y);
    }

    public boolean unselect(t object) {
        return getArrayList().remove(object);
    }

    public void unselectAll() {
        arrayList.clear();
    }

    public boolean contains(Sprite sprite){
        return arrayList.contains(sprite);
    }

}
