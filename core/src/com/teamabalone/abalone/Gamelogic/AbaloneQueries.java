package com.teamabalone.abalone.Gamelogic;

/**
 * Interface to be able to change various field classes which the Abalone class uses.
 *
 * This reduces dependency of the Abalone class to the Fields.
 * In further implementation you can then change the underlying class without the need to make changes in Abalone.
 */
public interface AbaloneQueries {

    /**
     * Checks if an selection of marbles is in a line, no mather which direction
     *
     * @param ids the marbles to check
     * @return true if in line, false if not in line
     */
    boolean isInLine(int[] ids);

    /**
     * Returns a gotPushedOut value
     *
     * @return a boolean
     */
    boolean isPushedOutOfBound();

    /**
     * Checks if the selected marbles can be pushed into the direction.
     *
     * @param ids  the selected hexes, not null
     * @param direction  the direction to push
     * @return  an array of the id of the enemy marbles that got pushed, null if the push is not legit, empty if only allied got pushed
     */
    int[] checkMove(int[] ids, Directions direction);

    /**
     * Iterates over the whole game field and writes into an array what each hex holds.
     *
     * @return the content for all hexes in {@link Field#field}
     */
    int[] getWholeField();

    /**
     * Changes the color of a certain marble.
     * <p></p>
     * A specific marble which a player chooses will be changed to his color.
     * This is only possible for marbles which don't belong to the player.
     *
     * @param tileId  the index of the marble that has to be changed
     * @param playerId  the id of the player that changes the marble
     */
    void changeTo(int tileId, int playerId);

    /**
     * Returns the ID of the current changed marble (renegade).
     *
     * @return  the ID of the marble, if none then -1
     */
    int idOfCurrentRenegade();

    /**
     * Resets the current renegade.
     */
    void resetRenegade();
}
