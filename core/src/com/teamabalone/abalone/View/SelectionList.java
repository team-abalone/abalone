package com.teamabalone.abalone.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ArrayList with a maximum capacity
 *
 * @param <T> type the Array list is supposed to manage
 */
public class SelectionList<T> {
    private final ArrayList<T> arrayList = new ArrayList<>();
    private final int maximum;

    /**
     * Constructor for this {@code SelectionList}.
     *
     * @param maximum the maximum size of this {@code SelectionList}
     */
    public SelectionList(int maximum) {
        this.maximum = maximum;
    }

    /**
     * Gets size of this {@code SelectionList}.
     *
     * @return the size of the {@link SelectionList#arrayList}
     */
    public int size() {
        return arrayList.size();
    }

    /**
     * Gets the {@code List}.
     *
     * @return this {@code List}
     */
    public List<T> getArrayList() {
        return arrayList;
    }

    /**
     * Gets value at given index.
     *
     * @param index the position in the {@code arrayList}
     * @return the value at the given position
     */
    public T get(int index) {
        return arrayList.get(index);
    }

    /**
     * Returns the index of the first occurrence of the specified element in this list, or -1 if this list does not contain the element.
     *
     * @param object the element to search for
     * @return the index of the argument in this arrayList, or -1 if it's not in the list
     */
    public int indexOf(T object) {
        return arrayList.indexOf(object);
    }

    /**
     * Returns if an object is within this arrayList.
     *
     * @param object the element to search for
     * @return true if it's in this list, false if not
     */
    public boolean contains(T object) {
        return arrayList.contains(object);
    }

    /**
     * Checks if this arrayList contains anything.
     *
     * @return true if at least one item is in this list, false if not
     */
    public boolean isEmpty() {
        return arrayList.isEmpty();
    }

    /**
     * Add an object to list, if list not full and object not already contained.
     *
     * @param object the object to add
     * @return true if successful
     */
    public boolean select(T object) {
        if (arrayList.size() < maximum && !arrayList.contains(object)) {
            return arrayList.add(object);
        }
        return false;
    }

    /**
     * Remove an object from the list.
     *
     * @param object the object to remove
     * @return true if successful
     */
    public boolean unselect(T object) {
        return getArrayList().remove(object);
    }

    /**
     * Clears the whole selection.
     */
    public void unselectAll() {
        arrayList.clear();
    }

    @Override
    public String toString() {
        return Arrays.toString(arrayList.toArray());
    }
}
