package com.teamabalone.abalone.View;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * ArrayList with a maximum capacity
 *
 * @param <T> type the Array list is supposed to manage
 */
public class SelectionList<T> {
    private final ArrayList<T> arrayList = new ArrayList<>();
    private final int maximum;

    public SelectionList(int maximum) {
        this.maximum = maximum;
    }

    public int size() {
        return arrayList.size();
    }

    public ArrayList<T> getArrayList() {
        return arrayList;
    }

    public T get(int index) {
        return arrayList.get(index);
    }

    public int indexOf(T object){
        return arrayList.indexOf(object);
    }

    public boolean contains(T object) {
        return arrayList.contains(object);
    }

    public boolean isEmpty() {
        return arrayList.isEmpty();
    }

    /**
     * Add an object to list, if list not full and object not already contained.
     *
     * @param object object to add
     * @return true if successful
     */
    public boolean select(T object) {
        if (arrayList.size() < maximum) {
            if (!arrayList.contains(object)) {
                return arrayList.add(object);
            }
        }
        return false;
    }

    /**
     * Remove an object from the list.
     *
     * @param object object to remove
     * @return true if successful
     */
    public boolean unselect(T object) {
        return getArrayList().remove(object);
    }

    public void unselectAll() {
        arrayList.clear();
    }

    @Override
    public String toString() {
        return Arrays.toString(arrayList.toArray());
    }
}
