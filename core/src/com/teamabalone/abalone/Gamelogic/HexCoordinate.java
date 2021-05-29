package com.teamabalone.abalone.Gamelogic;

import java.util.Objects;

/**
 * Represents a specific point within a hexagonal field.
 * <p></p>
 * Contains three coordinates: x, y, z.
 * Consider the middle of the field as teh origin.
 */
public class HexCoordinate {
	private int x;
	private int y;
	private int z;

	/**
	 * Cosntructor of {@code HexCoordinate}.
	 *
	 * @param x  the x value
	 * @param y  the y value
	 * @param z  the z value
	 * @throws RuntimeException in case of an impossible position
	 */
	public HexCoordinate(int x, int y, int z) {
		if (x + y + z != 0) {
			throw new RuntimeException("Coordinate cannot exist");
		} else {
			this.x = x;
			this.y = y;
			this.z = z;
		}
	}

	/**
	 * Calculates the middle between to {@code HexCoordinates}.
	 *
	 * @param a  the first {@link HexCoordinate}
	 * @param b  the second {@code HexCoordinates}
	 * @return  a new {@code HexCoordinates} in the middle
	 */
	public static HexCoordinate subtract(HexCoordinate a, HexCoordinate b){
		int x = a.getX()-b.getX();
		int y = a.getY()-b.getY();
		int z = a.getZ()-b.getZ();

		return new HexCoordinate(x,y,z);
	}


	/**
	 * Returns an absolute value of the coordinates of a {@code HexCoordinate}.
	 * <p></p>
	 *
	 * @param a  the {@code HexCoordinate}
	 * @return  the absolute value of the argument
	 */
	public static int abs(HexCoordinate a){
		int x = Math.abs(a.x);
		int y = Math.abs(a.y);
		int z = Math.abs(a.z);

		return (x+y+z)/2;
	}

	/**
	 * Checks if two {@code HexCoordinate} arguemnts are neighbours
	 *
	 * @param a  the first {@link HexCoordinate}
	 * @param b  the second {@code HexCoordinates}
	 * @return  true if they are neighbours, false if not
	 */
	public static boolean isNeighbour(HexCoordinate a, HexCoordinate b){
		if(abs(HexCoordinate.subtract(a,b))==1){
			return true;
		}
		return false;
	}

	/**
	 * Checks if three {@code HexCoordinate} arguemnts are in straight line
	 *
	 * @param a  the first {@link HexCoordinate}
	 * @param b  the second {@code HexCoordinates}
	 * @param c  the third {@code HexCoordinates}
	 * @return  true if they are in a straight line, false if not
	 */
	public static boolean isInLine(HexCoordinate a, HexCoordinate b, HexCoordinate c){
		if(isNeighbour(a,b) && isNeighbour(b,c) || isNeighbour(a,c) && isNeighbour(a,b)){
			return a.x == b.x && b.x == c.x || a.y == b.y && b.y == c.y || a.z == b.z && b.z == c.z;
		}
		return false;
	}

	@Override
	public String toString() {
		return "[" + x + ", " + y + ", " + z + "]";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		HexCoordinate that = (HexCoordinate) o;
		return x == that.x && y == that.y && z == that.z;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y, z);
	}

	/**
	 * Gets the x coordinate.
	 *
	 * @return  the x coordinate
	 */
	public int getX() {
		return x;
	}

	/**
	 * Gets the y coordinate.
	 *
	 * @return  the y coordinate
	 */
	public int getY() {
		return y;
	}

	/**
	 * Gets the z coordinate.
	 *
	 * @return  the z coordinate
	 */
	public int getZ() {
		return z;
	}
}
