package com.teamabalone.abalone.Gamelogic;

import java.util.*;

import static java.lang.Math.abs;

public class Field implements Iterable<Hexagon> {
	private HashMap<HexCoordinate, Hexagon> field;
	private int radius;
	private int hexFields;

	public Field(int radius) {
		this.radius = radius;
		this.hexFields = getHexagonCount(radius);
		this.field = new HashMap<>(this.hexFields);
		int i = 0;
		for (HexCoordinate hex : iterateOverHexagons()) {
			this.setHexagon(hex, new Hexagon(hex, i++));
		}
		System.out.println(iterateOverHexagons());
	}

	public boolean isInLine(int[] ids) {
		if (ids.length == 0) {
			throw new RuntimeException("No ids given");
		} else if (ids.length == 1) {
			return true;
		} else if (ids.length == 2) {
			HexCoordinate one = null;
			HexCoordinate two = null;
			for (HexCoordinate hex : iterateOverHexagons()) {
				if (getHexagon(hex).getId() == ids[0]) {
					one = hex;
				}
				if (getHexagon(hex).getId() == ids[1]) {
					two = hex;
				}
			}
			if(one == null || two == null){
				throw new RuntimeException("Id doesn't exist");
			}
			return HexCoordinate.isNeighbour(one, two);
		} else if (ids.length == 3) {
			HexCoordinate one = null;
			HexCoordinate two = null;
			HexCoordinate three = null;
			for (HexCoordinate hex : iterateOverHexagons()) {
				if (getHexagon(hex).getId() == ids[0]) {
					one = hex;
				}
				if (getHexagon(hex).getId() == ids[1]) {
					two = hex;
				}
				if(getHexagon(hex).getId() == ids[2]){
					three = hex;
				}
			}
			if(one == null || two == null || three ==  null){
				throw new RuntimeException("Id doesn't exist");
			}
			return HexCoordinate.isInLine(one,two,three);
		} else {
			return false;
		}
	}

	public Iterable<HexCoordinate> iterateOverHexagons() {
		ArrayList<HexCoordinate> resultList = new ArrayList<>(this.hexFields);
		/*
		for (int x = -radius + 1; x < radius; x++) {
			for (int y = -radius + 1; y < radius; y++) {
				for (int z = -radius + 1; z < radius; z++) {
					if (x + y + z == 0) {
						resultList.add(new HexCoordinate(x, y, z));
					} else {

					}
				}
			}
		}
		 */
		for (int z = -radius + 1; z < radius; z++) {
			for (int y = radius - 1; y > -radius; y--) {
				for (int x = radius - 1; x > -radius; x--) {
					if (x + y + z == 0) {
						resultList.add(new HexCoordinate(x, y, z));
					}
				}
			}
		}
		return resultList;
	}

	public Hexagon getHexagon(HexCoordinate coordinate) {
		return field.get(coordinate);
	}

	private void setHexagon(HexCoordinate coordinate, Hexagon hexagon) {
		this.field.put(coordinate, hexagon);
	}

	private int getHexagonCount(int r) {
		return 3*r*(r+1)+1;
	}

	@Override
	public Iterator<Hexagon> iterator() {
		return field.values().iterator();
	}
}
