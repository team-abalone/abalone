package com.teamabalone.abalone.Gamelogic;

/**
 * Represents a single hex in the whole field.
 * <p></p>
 * This class holds:
 * <li> a {@link Marble}
 * <li> a {@link HexCoordinate}
 * <li> a id for the view
 */
public class Hexagon {
	private Marble marble;
	private HexCoordinate position;
	private int id;

	/**
	 * Constructor for this Hexagon
	 *
	 * @param coordinate  the coordinate within the {@link Field}
	 * @param id  the unique id of this
	 */
	public Hexagon(HexCoordinate coordinate, int id) {
		this.position = coordinate;
		this.id = id;
	}

	/**
	 * Gets the marble.
	 *
	 * @return  the marble
	 */
	public Marble getMarble() {
		return marble;
	}

	/**
	 * Sets the marble.
	 *
	 * @param marble  the marble
	 */
	public void setMarble(Marble marble) {
		this.marble = marble;
	}

	/**
	 * Gets the HexCoordinate.
	 *
	 * @return the {@link HexCoordinate} of this
	 */
	public HexCoordinate getPosition() {
		return position;
	}

	/**
	 * Gets the id.
	 *
	 * @return  the id
	 */
	public int getId() {
		return id;
	}
}
