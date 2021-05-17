package com.teamabalone.abalone.Gamelogic;

import com.badlogic.gdx.Gdx;

import org.omg.CORBA.MARSHAL;

import java.util.*;

import static java.lang.Math.abs;

public class Field implements Iterable<Hexagon> {
    private HashMap<HexCoordinate, Hexagon> field;
    private int radius;
    private int hexFields;
    private boolean gotPushedOut = false;

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

    public List<Team> getMarbles(){
        List<Team> result = new ArrayList<Team>();
        for (HexCoordinate hex : iterateOverHexagons()) {
            if(getHexagon(hex).getMarble() == null){
                result.add(null);
            } else {
                result.add(getHexagon(hex).getMarble().getTeam());
            }
        }
        return result;
    }

    public void setGotPushedOut(){
        gotPushedOut = false;
    }
    //loads default marble positions into the hashmap for the game start
    public void fieldSetUp() {
        for (HexCoordinate hex : iterateOverHexagons()) {
            if (getHexagon(hex).getId() <= 11 || getHexagon(hex).getId() >= 14 && getHexagon(hex).getId() <= 16) {
                getHexagon(hex).setMarble(new Marble(Team.WHITE));
            } else if (getHexagon(hex).getId() >= 51 || getHexagon(hex).getId() >= 46 && getHexagon(hex).getId() <= 48) {
                getHexagon(hex).setMarble(new Marble(Team.BLACK));
            }
        }
    }

    //method for view that returns the content for all fields
    public int[] getWholeField() {
        int[] arr = new int[this.hexFields];
        for (int i = 1; i <= hexFields; i++) {
            for (HexCoordinate hex : iterateOverHexagons()) {
                if (getHexagon(hex).getId() == i) {
                    if (getHexagon(hex).getMarble() == null) {
                        arr[i - 1] = 0;
                    } else if (getHexagon(hex).getMarble().getTeam() == Team.BLACK) {
                        arr[i - 1] = 1;
                    } else if (getHexagon(hex).getMarble().getTeam() == Team.WHITE) {
                        arr[i - 1] = 2;
                    }
                }
            }
        }
        return arr;
    }

    //invalid move -> null
    //valid move but empty field ahead -> array{}
    //valid move and enemy marbles ahead -> array{enemy1, enemy2}

    public int[] checkMove(int[] ids, Directions direction) {  //return.length == 0 == false
        //TODO
        ArrayList<HexCoordinate> selectedItems = new ArrayList<>();
        Team playersTeam;
        gotPushedOut = false;
        int[] result = new int[0];
        //get the hexCoordinates so it's easier to navigate
        for (int i = 0; i < ids.length; i++) {
            for (HexCoordinate hex : iterateOverHexagons()) {
                if (getHexagon(hex).getId() == ids[i]) {
                    selectedItems.add(hex);
                }
            }
        }
        //check if the targeted fields are valid
        playersTeam = getHexagon(selectedItems.get(0)).getMarble().getTeam();
        for (int i = 0; i < ids.length; i++) {
            HexCoordinate neighbour = calcNeighbour(selectedItems.get(i), direction);
            if (getHexagon(neighbour) == null) {
                //in this case the neighbour field is not within the map and because we can't kill ourselves it's not legit
                //Gdx.app.log("Logic", "Method checkMove said out of bounds.");
                return null;
            }
            if (getHexagon(neighbour).getMarble() == null) {
                //this case is ok so we look further
            } else if (selectedItems.contains(neighbour)) {
                //this case is also ok because the blocking marble is in our selection and will be moved too
            } else if (getHexagon(neighbour).getMarble().getTeam() != playersTeam) {
                //this case will call isPushable because there is a enemy marble in our way which we can possibly push away
                result = isPushableMe(selectedItems, direction);
                if (result == null) {   //|| result == null
                    return null;  //the is pushable returns an empty array if it's not possible so our move is not legit
                } else {
                    //will push enemy marbles here
                    move(result, direction); //enemy
                    //Gdx.app.log("Logic", "Method checkMove said you will push an enemy marble.");
                }
            } else {
                //Gdx.app.log("Logic", "Method checkMove said ally marble is blocking the way");
                return null;  //this case will block the move because there is an allied non selected marble
            }
        }
        //Gdx.app.log("Logic", "Method checkMove said the move can be done");
        move(ids, direction);            //ally
        return result;
    }

	/*public int[] fuseIDS(int[] first, int[] second){
		int[] result = new int[first.length + second.length];
		for (int i = 0; i < first.length; i++) {
			result[i] = first[i];
		}
		for (int i = 0; i < second.length; i++) {
			result[i + first.length] = first[i];
		}
		return result;
	}*/

    public void move(int[] marbleID, Directions direction) {
        boolean localPushedOut = false;
        ArrayList<HexCoordinate> selectedItems = new ArrayList<>();
        //get the hexCoordinates so it's easier to navigate
        for (int i = 0; i < marbleID.length; i++) {                    //sorts the selection
            for (HexCoordinate hex : iterateOverHexagons()) {
                if (getHexagon(hex).getId() == marbleID[i]) {
                    selectedItems.add(hex);
                    break;
                }
            }
        }
        //push them
        Marble tempTarget = null;
        Marble tempMoving;
        //copy Marble to move in a safe var
        //move target temp in to move field
        //copy target marble into target temp
        //move moving temp into target field
        for (HexCoordinate hex : selectedItems) {
            HexCoordinate target = calcNeighbour(hex, direction);        //calc target field
            if (getHexagon(target) == null) {
                gotPushedOut = true;
                localPushedOut = true;
                if (marbleID.length == 1){                          //in this case only one marble is pushed out and we just delete it
                    getHexagon(hex).setMarble(null);
                }
                continue;                                    //target field is null -> it's out of bound so we skip this iteration
            }
            tempMoving = getHexagon(hex).getMarble();
            getHexagon(hex).setMarble(tempTarget);
            tempTarget = getHexagon(target).getMarble();
            getHexagon(target).setMarble(tempMoving);
            if(localPushedOut){                                 //we need to check if in the current call one marble got pushed out otherwise concurrent pushes will be buggy
                getHexagon(hex).setMarble(null);
            }
        }
    }

    public int[] isPushableMe(ArrayList<HexCoordinate> selectedItems, Directions direction){
        if(selectedItems.size() <= 1){
            return null;
        }
        Team currentTeam = getHexagon(selectedItems.get(0)).getMarble().getTeam();
        for (HexCoordinate hex : selectedItems) {
            HexCoordinate target = calcNeighbour(hex, direction);
            if(getHexagon(target).getMarble() == null || selectedItems.contains(target)){
                //in this case the target field is empty. we dond't need to check for ally marbles since the checkMove already does this
            } else{     //this case will have a enemy marble
                HexCoordinate behindAlly = calcNeighbour(hex, mirrorDirection(direction));
                if(!selectedItems.contains(behindAlly) ){       //in this case we push normal to the marble line and therefore can't push anything
                    return null;
                }
                int whiteRow = 1;                               //here we can actually check if we can legit move the marble
                int[] enemyMarbles = new int[3];
                while(whiteRow < selectedItems.size()){
                    HexCoordinate behindTarget = calcNeighbour(target, direction);
                    if(getHexagon(behindTarget) == null || getHexagon(behindTarget).getMarble() == null){               //marble have space behind it is free and can be pushed
                        enemyMarbles[whiteRow-1] = getHexagon(target).getId();
                        int[] result = new int[whiteRow];
                        for (int i = 0; i < result.length; i++) {                           //need to make a new array because i don't know how long it will be at the beginning
                            result[i] = enemyMarbles[i];
                        }
                        return result;
                    } else if (getHexagon(behindTarget).getMarble().getTeam() == currentTeam){      //ally marble blocks the push
                        return null;
                    } else{
                        enemyMarbles[whiteRow-1] = getHexagon(target).getId();      //enemy marble has another enemy marble behind it so we look again for that one
                        whiteRow++;
                        target = behindTarget;
                    }
                }
            }
        }
        return null;            //case will be reached when you have 3 iterations in the while loop which leads to 3+ enemy marbles which can never be pushed
    }


    public int[] isPushable(ArrayList<HexCoordinate> selectedItems, Directions direction) {
        //takes direction and selected items, looks for otherTeamMarbles in the direction
        int marbleCounter = 0;
        HexCoordinate temp;
        Team playersTeam = getHexagon(selectedItems.get(0)).getMarble().getTeam();

        int[] buffer = new int[3];
        boolean holdsMarble = true;

        for (int i = 0; i < radius * 2 && holdsMarble && marbleCounter <= 3; i++) {
            temp = getTemp(selectedItems, direction, i);
            if (getHexagon(temp) == null || getHexagon(temp).getMarble() == null) {
                holdsMarble = false;
            } else if (getHexagon(temp).getMarble().getTeam() != playersTeam) {
                if (marbleCounter < 3) {
                    buffer[marbleCounter] = getHexagon(temp).getId();
                }
                marbleCounter++;
            }
        }
        if (marbleCounter >= 3) {
            return new int[]{};
        }

        int[] result = new int[marbleCounter];
        for (int i = 0, k = 0; i < buffer.length; i++) { //don't return zeros
            if (buffer[i] != 0) {
                result[k++] = buffer[i];
            }
        }
        return result;
    }

    public HexCoordinate getTemp(ArrayList<HexCoordinate> selectedItems, Directions direction, int i) {
        switch (direction) {
            case LEFT:
                return new HexCoordinate(selectedItems.get(0).getX() - i, selectedItems.get(0).getY() + i, selectedItems.get(0).getZ());
            case RIGHT:
                return new HexCoordinate(selectedItems.get(0).getX() + i, selectedItems.get(0).getY() - i, selectedItems.get(0).getZ());
            case LEFTUP:
                return new HexCoordinate(selectedItems.get(0).getX(), selectedItems.get(0).getY() + i, selectedItems.get(0).getZ() - i);
            case RIGHTUP:
                return new HexCoordinate(selectedItems.get(0).getX() + i, selectedItems.get(0).getY(), selectedItems.get(0).getZ() - i);
            case LEFTDOWN:
                return new HexCoordinate(selectedItems.get(0).getX() - i, selectedItems.get(0).getY(), selectedItems.get(0).getZ() + i);
            case RIGHTDOWN:
                return new HexCoordinate(selectedItems.get(0).getX(), selectedItems.get(0).getY() - i, selectedItems.get(0).getZ() + i);
            default:
                throw new IllegalStateException("Unexpected Direction: " + direction);
        }
    }

    public boolean isPushedOutOfBound() {
        return gotPushedOut;
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
            if (one == null || two == null) {
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
                if (getHexagon(hex).getId() == ids[2]) {
                    three = hex;
                }
            }
            if (one == null || two == null || three == null) {
                throw new RuntimeException("Id doesn't exist");
            }
            return HexCoordinate.isInLine(one, two, three);
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
        return 3 * r * (r - 1) + 1;
    }

    @Override
    public Iterator<Hexagon> iterator() {
        return field.values().iterator();
    }

    public HexCoordinate calcNeighbour(HexCoordinate hex, Directions direction) {
        HexCoordinate neighbour;
        switch (direction) {
            case LEFT:
                neighbour = new HexCoordinate(hex.getX() - 1, hex.getY() + 1, hex.getZ());
                break;
            case RIGHT:
                neighbour = new HexCoordinate(hex.getX() + 1, hex.getY() - 1, hex.getZ());
                break;
            case LEFTUP:
                neighbour = new HexCoordinate(hex.getX(), hex.getY() + 1, hex.getZ() - 1);
                break;
            case RIGHTUP:
                neighbour = new HexCoordinate(hex.getX() + 1, hex.getY(), hex.getZ() - 1);
                break;
            case LEFTDOWN:
                neighbour = new HexCoordinate(hex.getX() - 1, hex.getY(), hex.getZ() + 1);
                break;
            case RIGHTDOWN:
                neighbour = new HexCoordinate(hex.getX(), hex.getY() - 1, hex.getZ() + 1);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + direction);
        }
        return neighbour;
    }

    public Directions mirrorDirection(Directions direction){
        Directions mirror;
        switch (direction) {
            case LEFT:
                mirror = Directions.RIGHT;
                break;
            case RIGHT:
                mirror = Directions.LEFT;
                break;
            case LEFTUP:
                mirror = Directions.RIGHTDOWN;
                break;
            case RIGHTUP:
                mirror = Directions.LEFTDOWN;
                break;
            case LEFTDOWN:
                mirror = Directions.RIGHTUP;
                break;
            case RIGHTDOWN:
                mirror = Directions.LEFTUP;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + direction);
        }
        return mirror;
    }
}
