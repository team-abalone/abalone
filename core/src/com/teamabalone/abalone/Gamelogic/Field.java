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
		int i = 1;
		for (HexCoordinate hex : iterateOverHexagons()) {
			this.setHexagon(hex, new Hexagon(hex, i++));
		}
		System.out.println(iterateOverHexagons());
	}

	//returns the whole field as an array. 0 = none, 1 = black, 2 = white;
	public int[] getWholeField(){
		int[] arr = new int[this.hexFields];
		for (int i = 1; i < hexFields ; i++) {
			for (HexCoordinate hex : iterateOverHexagons()) {
				if (getHexagon(hex).getId() == i) {
					if(getHexagon(hex).getMarble() == null){
						arr[i-1] = 0;
					}
					else if(getHexagon(hex).getMarble().getTeam()==Team.BLACK){
						arr[i-1] = 1;
					}
					else if(getHexagon(hex).getMarble().getTeam()==Team.WHITE){
						arr[i-1] = 2;
					}
				}
			}
		}
		return arr;
	}

	public boolean checkMove(int[]ids, Directions direction){
		//TODO

		return false;
	}

	public boolean isThereAMarble(){
		//TODO kontrollieren ob da ein Marble ist

		return false;
	}

	public int[] isPushable(){
		//TODO

		return null;
	}

	public boolean isPushedOutOfBound(ArrayList<HexCoordinate> coordinates){
		for (int i = 0; i < coordinates.size(); i++) {
			if(coordinates.get(i).getX()>=radius || coordinates.get(i).getY()>=radius || coordinates.get(i).getZ()>=radius){
				return true;
			}
		}
		return false;
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
		return 3*r*(r-1)+1;
	}

	@Override
	public Iterator<Hexagon> iterator() {
		return field.values().iterator();
	}
}
