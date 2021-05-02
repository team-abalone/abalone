package com.teamabalone.abalone;

import java.util.ArrayList;
import java.util.function.Consumer;

public class SelectionList<t> {
    private final ArrayList<t> arrayList = new ArrayList<>();
    private final int maximum;

    public SelectionList(int maximum) {
        this.maximum = maximum;
    }

    public int size() {
        return arrayList.size();
    }

    public t get(int index){
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

    public boolean select(t object) {
        //TODO check if valid -> only marbles in a row, else unselect all | here?
        if (arrayList.size() < maximum) {
            if (!arrayList.contains(object)) {
                return arrayList.add(object);
            }
        }
        return false;
    }

    public boolean unselect(t object) {
        return getArrayList().remove(object);
    }

    public void unselectAll() {
        arrayList.clear();
    }

}
