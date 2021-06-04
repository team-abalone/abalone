package com.teamabalone.abalone.Gamelogic;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.teamabalone.abalone.Client.IResponseHandlerObserver;
import com.teamabalone.abalone.Client.RequestSender;
import com.teamabalone.abalone.Client.Requests.BaseRequest;
import com.teamabalone.abalone.Client.Requests.MakeMoveRequest;
import com.teamabalone.abalone.Client.ResponseHandler;
import com.teamabalone.abalone.Client.Responses.BaseResponse;
import com.teamabalone.abalone.Client.Responses.GameStartedResponse;
import com.teamabalone.abalone.Client.Responses.MadeMoveResponse;
import com.teamabalone.abalone.Client.Responses.ResponseCommandCodes;
import com.teamabalone.abalone.Client.Responses.StartGameResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.Iterator;
import java.util.List;

/**
 * This Class manages the data of the game field.
 * <p></p>
 * I holds a {@link HashMap} called field in which the {@link HexCoordinate Hexcoordinates} and {@link Marble Marbles} are stored.
 * Futhermore it has several methods to change the {@code field} and to communicate with a View.
 */
public class Field implements Iterable<Hexagon>, IResponseHandlerObserver, AbaloneQueries {

    private HashMap<HexCoordinate, Hexagon> field;
    private int radius;
    private int hexFields;
    private boolean gotPushedOut = false;
    private int renegade = -1;
    private UUID userId;

    private ResponseHandler responseHandler;

    /**
     * Constructor for Field.
     *
     * @param radius the side length of the Field.
     */
    public Field(int radius, GameStartedResponse gameStartedResponse) {
        this.radius = radius;
        this.userId = UUID.fromString(Gdx.app.getPreferences("UserPreferences").getString("UserId"));
        this.hexFields = getHexagonCount(radius);
        this.field = new HashMap<>(this.hexFields);
        int i = 1;
        for (HexCoordinate hex : iterateOverHexagons()) {
            this.setHexagon(hex, new Hexagon(hex, i++));
        }

        setInitialValues(gameStartedResponse);
        responseHandler = com.teamabalone.abalone.Client.ResponseHandler.newInstance();
        responseHandler.addObserver(this);

        System.out.println(iterateOverHexagons());
    }

    public Field(int radius) {
        this.radius = radius;
        this.hexFields = getHexagonCount(radius);
        this.field = new HashMap<>(this.hexFields);
        int i = 1;
        for (HexCoordinate hex : iterateOverHexagons()) {
            this.setHexagon(hex, new Hexagon(hex, i++));
        }

        fieldSetUp();
    }

    /**
     * Returns all Marbles in the {@code field}.
     * <p></p>
     * This Method is only used for Testing and should not be used in the real application.
     *
     * @return a {@link List} of teams from marbles. If a field has no marble it's null.
     */
    public List<Team> getMarbles() {
        List<Team> result = new ArrayList<Team>();
        for (HexCoordinate hex : iterateOverHexagons()) {
            if (getHexagon(hex).getMarble() == null) {
                result.add(null);
            } else {
                result.add(getHexagon(hex).getMarble().getTeam());
            }
        }
        return result;
    }


    /**
     * Sets the {@code gotPushedOut} boolean to false.
     */
    public void setGotPushedOut() {
        gotPushedOut = false;
    }

    /**
     * Loads default marble positions into the hashmap for the game start
     */
    public void fieldSetUp() {
        for (HexCoordinate hex : iterateOverHexagons()) {
            if (getHexagon(hex).getId() <= 11 || getHexagon(hex).getId() >= 14 && getHexagon(hex).getId() <= 16) {
                getHexagon(hex).setMarble(new Marble(Team.WHITE));
            } else if (getHexagon(hex).getId() >= 51 || getHexagon(hex).getId() >= 46 && getHexagon(hex).getId() <= 48) {
                getHexagon(hex).setMarble(new Marble(Team.BLACK));
            }
        }
    }

    public void setInitialValues(GameStartedResponse gameStartedResponse) {
        GameInfo gameInfo = GameInfo.getInstance();

        gameInfo.setPlayerId(gameStartedResponse.getPlayers().indexOf(userId));
        gameInfo.setNumberPlayers(gameStartedResponse.getNumberOfPlayers());

        ArrayList<String> names = new ArrayList<>();
        HashMap<UUID, String> map = gameStartedResponse.getPlayerMap();
        for (UUID uuid : gameStartedResponse.getPlayers()) {
            names.add(map.get(uuid));
        }
        gameInfo.setNames(names);

        fieldSetUpServer(toOneDimension(gameStartedResponse.getGameField()));
    }

    public int[] toOneDimension(int[][] array) {
        int elements = 0;
        for (int[] row : array) {
            elements += row.length;
        }

        int[] oneDimensionArray = new int[elements];
        int i = 0;
        for (int[] row : array) {
            for (int content : row) {
                oneDimensionArray[i++] = content;
            }
        }
        return oneDimensionArray;
    }

    //0 - 6
    //0 - 5
    public void fieldSetUpServer(int[] field) {
        if (field.length != this.field.size()) {
            throw new IllegalArgumentException("field size not matching hash map");
        }

        int i = 0;
        for (HexCoordinate hex : iterateOverHexagons()) {
            if (field[i] != 0) {
                getHexagon(hex).setMarble(new Marble(Team.values()[field[i] - 1]));
            }
            i++;
        }
    }

    //method for view that returns the content for all fields

    /**
     * Iterates over the whole game field and writes into an array what each hex holds.
     *
     * @return the content for all hexes in {@link Field#field}
     */
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

    @Override
    public void changeTo(int tileId, int playerId) {
        for (HexCoordinate hex : iterateOverHexagons()) {
            if (getHexagon(hex).getId() == tileId) {
                getHexagon(hex).getMarble().setTeam(Team.values()[playerId]); //returns the Team of the playerId
                renegade = tileId;
                break;
            }
        }
    }

    @Override
    public int idOfCurrentRenegade() {
        return renegade;
    }

    @Override
    public void resetRenegade() {
        renegade = -1;
    }

    //invalid move -> null
    //valid move but empty field ahead -> array{}
    //valid move and enemy marbles ahead -> array{enemy1, enemy2}

    /**
     * Checks if the selected marbles can be pushed into the direction.
     * <p></p>
     * This Method gets the id of the Marbles.
     * Then converts it into {@link HexCoordinate Hexcoordinates}.
     * It then checks for each marble if you can move the marble or not with following options:
     * <li>the move is not legit, the target is not within the field. returns null.
     * <li>the move is not legit, at least one ally marble blocks the move. returns null.
     * <li>the move is not legit, there are more enemy marbles than can be pushed away. returns null.
     * <li>the move is legit, all hexes are free. returns an empty array with zero length.
     * <li>the move is legit, enemy marbles can be pushed away, returns an array with the pushed enemy marbles.
     *
     * @param ids       the selected hexes, not null
     * @param direction the direction to push
     * @return an array of the id of the enemy marbles that got pushed, null if the push is not legit, empty if only allied got pushed
     */
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
                result = isPushable(selectedItems, direction);
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

    /**
     * Moves the marbles within the {@code field} data.
     * <p></p>
     * Please don't call the method on itself.
     * Let the {@link Field#checkMove(int[], Directions)} do the call.
     * If you call this method alone expect undefined behaviour.
     *
     * @param marbleID  the selected hexes, not null
     * @param direction the direction to push
     */
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
                if (marbleID.length == 1) {                          //in this case only one marble is pushed out and we just delete it
                    getHexagon(hex).setMarble(null);
                }
                continue;                                    //target field is null -> it's out of bound so we skip this iteration
            }
            tempMoving = getHexagon(hex).getMarble();
            getHexagon(hex).setMarble(tempTarget);
            tempTarget = getHexagon(target).getMarble();
            getHexagon(target).setMarble(tempMoving);
            if (localPushedOut) {                                 //we need to check if in the current call one marble got pushed out otherwise concurrent pushes will be buggy
                getHexagon(hex).setMarble(null);
            }
            if(!GameInfo.getInstance().getSingleDeviceMode()) {
                BaseRequest makeMoveRequest = new MakeMoveRequest(userId, marbleID, direction);
                try {
                    RequestSender rs = new RequestSender(makeMoveRequest);
                    ExecutorService executorService = Executors.newSingleThreadExecutor();
                    Future future = executorService.submit(rs);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * Checks if selected marbles can push the blocking marbles
     * <p></p>
     * Please don't call the method on itself.
     * Let the {@link Field#checkMove(int[], Directions)} do the call.
     * If you call this method alone expect undefined behaviour.
     *
     * @param selectedItems the hexes selected, null returns null
     * @param direction     the direction to push
     * @return the id of the pushable marbles, null if not pushable
     */
    public int[] isPushable(ArrayList<HexCoordinate> selectedItems, Directions direction) {
        if (selectedItems.size() <= 1) {
            return null;
        }
        Team currentTeam = getHexagon(selectedItems.get(0)).getMarble().getTeam();
        for (HexCoordinate hex : selectedItems) {
            HexCoordinate target = calcNeighbour(hex, direction);
            if (getHexagon(target).getMarble() == null || selectedItems.contains(target)) {
                //in this case the target field is empty. we dond't need to check for ally marbles since the checkMove already does this
            } else {     //this case will have a enemy marble
                HexCoordinate behindAlly = calcNeighbour(hex, mirrorDirection(direction));
                if (!selectedItems.contains(behindAlly)) {       //in this case we push normal to the marble line and therefore can't push anything
                    return null;
                }
                int enemyRow = 1;                               //here we can actually check if we can legit move the marble
                int[] enemyMarbles = new int[3];
                while (enemyRow < selectedItems.size()) {
                    HexCoordinate behindTarget = calcNeighbour(target, direction);
                    if (getHexagon(behindTarget) == null || getHexagon(behindTarget).getMarble() == null) {               //marble have space behind it is free and can be pushed
                        enemyMarbles[enemyRow - 1] = getHexagon(target).getId();
                        int[] result = new int[enemyRow];
                        for (int i = 0; i < result.length; i++) {                           //need to make a new array because i don't know how long it will be at the beginning
                            result[i] = enemyMarbles[i];
                        }
                        return result;
                    } else if (getHexagon(behindTarget).getMarble().getTeam() == currentTeam) {      //ally marble blocks the push
                        return null;
                    } else {
                        enemyMarbles[enemyRow - 1] = getHexagon(target).getId();      //enemy marble has another enemy marble behind it so we look again for that one
                        enemyRow++;
                        target = behindTarget;
                    }
                }
            }
        }
        return null;            //case will be reached when you have 3 iterations in the while loop which leads to 3+ enemy marbles which can never be pushed
    }

    /**
     * Returns the global gotPushedOut value
     *
     * @return {@code gotPushedOut} boolean
     */
    public boolean isPushedOutOfBound() {
        return gotPushedOut;
    }

    /**
     * Checks if an selection of marbles is in a line, no mather which direction
     *
     * @param ids the marbles to check
     * @return true if in line, false if not in line
     */
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

    /**
     * Creates a list which stores all available HexCoordinates for this Field.
     * <p></p>
     * This Method can be used to get the HexCoordinates to address the values stored in {@link Field#field}.
     *
     * @return an {@link ArrayList} of every {@link HexCoordinate} in the field
     */
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

    /**
     * Returns a specific hex
     *
     * @param coordinate a HexCoordinate for {@link Field#field}
     * @return the hex, null if not in {@link Field#field}
     */
    public Hexagon getHexagon(HexCoordinate coordinate) {
        return field.get(coordinate);
    }

    /**
     * Sets a specific hex
     *
     * @param coordinate a HexCoordinate for {@link Field#field}
     * @param hexagon    a specific {@link Hexagon}
     */
    private void setHexagon(HexCoordinate coordinate, Hexagon hexagon) {
        this.field.put(coordinate, hexagon);
    }

    /**
     * Counts how many hexes a field has
     *
     * @param r the radius
     * @return count of all hexes in a field
     */
    private int getHexagonCount(int r) {
        return 3 * r * (r - 1) + 1;
    }

    @Override
    public Iterator<Hexagon> iterator() {
        return field.values().iterator();
    }

    /**
     * Calculates the nearest hex in a certain direction.
     *
     * @param hex       the coordinates for a hex
     * @param direction the direction to aim for
     * @return the next {@code HexCoordinate} in the direction
     * @throws IllegalStateException in case of an invalid {@link Directions Direction}
     */
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


    /**
     * Turns the Direction 180° around.
     *
     * @param direction the direction
     * @return the turned direction
     */
    public Directions mirrorDirection(Directions direction) {
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

    @Override
    public void HandleResponse(BaseResponse response) {
        if (response instanceof MadeMoveResponse) {
            if (response.getCommandCode() == ResponseCommandCodes.MADE_MOVE.getValue()) {
                //Recreate opponents move. This will be broadcast by our api
                Directions direction = ((MadeMoveResponse) response).getDirection();
                int ids[] = ((MadeMoveResponse) response).getMarbles();
                //move ids
            }
            else if(response.getCommandCode() == ResponseCommandCodes.ROOM_EXCEPTION.getValue()){
                //Exception handling goes here : Maybe a small notification to be shown
            }
            else if(response.getCommandCode() == ResponseCommandCodes.SERVER_EXCEPTION.getValue()){
                //Exception handling goes here : Maybe a small notification to be shown
            }
            else if(response.getCommandCode() == ResponseCommandCodes.GAME_EXCEPTION.getValue()){
                //Exception handling goes here : Maybe a small notification to be shown
            }
        }
    }
}
