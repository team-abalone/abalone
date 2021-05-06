package com.teamabalone.abalone.Gamelogic;

import java.util.Objects;

public class HexCoordinate {
	private int x;
	private int y;
	private int z;

	public HexCoordinate(int x, int y, int z) {
		if (x + y + z != 0) {
			throw new RuntimeException("Coordinate cannot exist");
		} else {
			this.x = x;
			this.y = y;
			this.z = z;
		}
	}

	public static HexCoordinate subtract(HexCoordinate a, HexCoordinate b){
		int x = a.getX()-b.getX();
		int y = a.getY()-b.getY();
		int z = a.getZ()-b.getZ();

		return new HexCoordinate(x,y,z);
	}

	public static int abs(HexCoordinate a){
		int x = Math.abs(a.x);
		int y = Math.abs(a.y);
		int z = Math.abs(a.z);

		return (x+y+z)/2;
	}

	public static boolean isNeighbour(HexCoordinate a, HexCoordinate b){
		if(abs(HexCoordinate.subtract(a,b))==1){
			return true;
		}
		return false;
	}

	public static boolean isInLine(HexCoordinate a, HexCoordinate b, HexCoordinate c){
		if(isNeighbour(a,b) && isNeighbour(b,c)){
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

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}
}
