package com.teamabalone.abalone.Gamelogic;

import com.badlogic.gdx.Gdx;

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
		fieldSetUp();
		System.out.println(iterateOverHexagons());
	}

	//loads default marble positions into the hashmap for the game start
	public void fieldSetUp(){
		for (HexCoordinate hex : iterateOverHexagons()) {
			if(getHexagon(hex).getId() <= 11 || getHexagon(hex).getId() >= 14 && getHexagon(hex).getId() <= 16){
				getHexagon(hex).setMarble(new Marble(Team.WHITE));
			} else if(getHexagon(hex).getId() >= 51 || getHexagon(hex).getId() >= 46 && getHexagon(hex).getId() <= 48){
				getHexagon(hex).setMarble(new Marble(Team.BLACK));
			}
		}
	}

	//method for view that returns the content for all fields
	public int[] getWholeField(){
		int[] arr = new int[this.hexFields];

		//TODO getHexagons von 0-Hexfields in int[]

		return null;
	}

	public boolean checkMove(int[]ids, Directions direction){
		//TODO
		ArrayList<HexCoordinate> selectedItems = new ArrayList<>();
		Marble playersTeam;
		//get the hexCoordinates so it's easier to navigate
		for (int i = 0; i < ids.length; i++) {
			for (HexCoordinate hex: iterateOverHexagons()) {
				if(getHexagon(hex).getId() == ids[i]){
					selectedItems.add(hex);
				}
			}
		}
		//check if the targeted fields are valid
		playersTeam = getHexagon(selectedItems.get(0)).getMarble();
		for (int i = 0; i < ids.length ; i++) {
			HexCoordinate neighbour = calcNeighbour(selectedItems.get(i), direction);
			if(getHexagon(neighbour) == null){
				//in this case the neighbour field is not within the map and because we can't kill ourselves it's not legit
				Gdx.app.log("Logic", "Method checkMove said out of bounds.");
				return false;

			}
			if(getHexagon(neighbour).getMarble() == null ){
				//this case is ok so we look further
			}else if(selectedItems.contains(neighbour)){
				//this case is also ok because the blocking marble is in our selection and will be moved too
			}else if(getHexagon(neighbour).getMarble() != playersTeam){
				//this case will call isPushable because there is a enemy marble in our way which we can possibly push away
				int[] result = isPushable(selectedItems);
				if(result == null){
					return false;  //the is pushable returns an empty array if it's not possible so our move is not legit
				}
				//TODO manage the push of the marbles
			} else{
				Gdx.app.log("Logic", "Method checkMove said ally marble is blocking the way");
				return false;  //this case will block the move because there is an allied non selected marble
			}
		}
		Gdx.app.log("Logic", "Method checkMove said the move can be done");
		return true;
	}

	/*	//Allready handled in checkMove
	public boolean isThereAMarble(){
		//TODO kontrollieren ob da ein Marble ist

		return false;
	}
	 */

	public int[] isPushable(ArrayList<HexCoordinate> selectedItems){
		//TODO

		return null;
	}

	public boolean isPushedOutOfBound(){
		//TODO ist die Kugel außerhalb?
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
		return 3*r*(r+1)+1;
	}

	@Override
	public Iterator<Hexagon> iterator() {
		return field.values().iterator();
	}

	public HexCoordinate calcNeighbour (HexCoordinate hex, Directions direction){
		HexCoordinate neighbour;
		switch (direction){
			case LEFT:
				neighbour = new HexCoordinate(hex.getX()-1, hex.getY()+1, hex.getZ());
				break;
			case RIGHT:
				neighbour = new HexCoordinate(hex.getX()+1, hex.getY()-1, hex.getZ());
				break;
			case LEFTUP:
				neighbour = new HexCoordinate(hex.getX(), hex.getY()+1, hex.getZ()-1);
				break;
			case RIGHTUP:
				neighbour = new HexCoordinate(hex.getX()+1, hex.getY(), hex.getZ()-1);
				break;
			case LEFTDOWN:
				neighbour = new HexCoordinate(hex.getX()-1, hex.getY(), hex.getZ()+1);
				break;
			case RIGHTDOWN:
				neighbour = new HexCoordinate(hex.getX(), hex.getY()-1, hex.getZ()+1);
				break;
			default:
				throw new IllegalStateException("Unexpected value: " + direction);
		}
		return neighbour;
	}
}
