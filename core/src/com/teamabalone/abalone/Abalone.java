package com.teamabalone.abalone;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
import com.badlogic.gdx.math.Frustum;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Sphere;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.teamabalone.abalone.Client.IResponseHandlerObserver;
import com.teamabalone.abalone.Client.RequestSender;
import com.teamabalone.abalone.Client.Requests.BaseRequest;
import com.teamabalone.abalone.Client.Requests.SurrenderRequest;
import com.teamabalone.abalone.Client.Responses.BaseResponse;
import com.teamabalone.abalone.Client.Responses.MadeMoveResponse;
import com.teamabalone.abalone.Client.Responses.ResponseCommandCodes;
import com.teamabalone.abalone.Dialogs.SettingsDialog;
import com.teamabalone.abalone.Gamelogic.AbaloneQueries;
import com.teamabalone.abalone.Gamelogic.Directions;
import com.teamabalone.abalone.Gamelogic.Field;
import com.teamabalone.abalone.Gamelogic.GameInfo;
import com.teamabalone.abalone.Gamelogic.GameInfos;
import com.teamabalone.abalone.Helpers.FactoryHelper;
import com.teamabalone.abalone.Helpers.GameConstants;
import com.teamabalone.abalone.Screens.MenuScreen;
import com.teamabalone.abalone.View.Board;
import com.teamabalone.abalone.View.GameSet;
import com.teamabalone.abalone.View.MarbleSet;
import com.teamabalone.abalone.View.RenegadeKeeper;
import com.teamabalone.abalone.View.SelectionList;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Abalone implements Screen, IResponseHandlerObserver {
    private final GameInfos gameInfos = GameInfo.getInstance();

    private final int MAX_TEAMS = 6;
    private final int NUMBER_CAPTURES_TO_WIN = 2;
    private final int SWIPE_SENSITIVITY = 40;
    private final double TILT_SENSITIVITY = 2.5;
    private boolean tiltActive = false;
    private final boolean SINGLE_DEVICE_MODE = gameInfos.getSingleDeviceMode();
    private final int MAX_SELECT = gameInfos.getMaximalSelectableMarbles();
    private final int NUMBER_PLAYERS = gameInfos.getNumberPlayers();
    private final int MAP_SIZE = gameInfos.getMapSize(); //TODO make only setting valid value possible?
    private final int PLAYER_ID = gameInfos.getPlayerId();

    private Music bgMusic;
    private final Preferences settings;
    private final ArrayList<Texture> playerTextures = new ArrayList<>();

    private final GameImpl game;
    private final SpriteBatch batch;

    private TiledMap tiledMap;
    private HexagonalTiledMapRenderer tiledMapRenderer;
    private FitViewport viewport;

    private Texture background;

    private Board board;

    private final float screenWidth = Gdx.graphics.getWidth();
    private final float screenHeight = Gdx.graphics.getHeight();
    private final Stage stage;
    private Label nextLabel;
    private Label deadBlackMarbleLabel;
    private Label deadWhiteMarbleLabel;
    private ImageButton settingsButton;

    SelectionList<Sprite> selectedSprites = new SelectionList<>(MAX_SELECT);
    ArrayList<ArrayList<Sprite>> deletedSpritesLists = new ArrayList<>();

    boolean justTouched = false; //makes one touch only one operation (-> select)
    float firstTouchX;
    float firstTouchY;
    float lastTouchX;
    float lastTouchY;

    AbaloneQueries queries;

    Directions lastDirection = Directions.NOTSET;

    private float boardWidth;
    private float boardHeight;

    private int currentPlayer = 0;
    private int winner = -1;

    private final RenegadeKeeper[] renegadeKeepers = new RenegadeKeeper[SINGLE_DEVICE_MODE ? NUMBER_PLAYERS : 1];
    private Label renegadeLabels = null;

    private final float min_zoom = 0.3f;
    private final float max_zoom = 0.7f;

    public Abalone(GameImpl game, Field field) {
        queries = field;
        field.setAbalone(this);
        settings = Gdx.app.getPreferences("UserSettings");

        this.game = game;
        batch = game.batch;
        stage = game.gameStage; //stage not attached, so it moves with screen
        stage.getActors().clear();
        Gdx.input.setInputProcessor(stage);

        board();
        camera();
        textures();

        instantiateBoard();

        //initialize renegadeKeeper array
        for (int i = 0; i < renegadeKeepers.length; i++) {
            renegadeKeepers[i] = new RenegadeKeeper();
        }
    }

    private void textures() {
        background = new Texture("boards/" + settings.getString("boardSkin", "Laminat.png"));

        settings.putString("marbleSkin" + 0, "ball_white.png"); //TODO putString only temporary. should be in settings later on.
//        settings.putString("marbleSkin" + 1, "ball.png"); //TODO something happened in SETTINGS?
        settings.flush();
        playerTextures.add(new Texture("marbles/ball_white.png"));
        playerTextures.add(new Texture("marbles/" + settings.getString("marbleSkin" + 1)));
        //playerTextures.add(new Texture("marbles/" + settings.getString("marbleSkin" + 1)));
    }

    private void board() {
        tiledMap = new TmxMapLoader().load("abalone_map" + MAP_SIZE + ".tmx");
        tiledMapRenderer = new HexagonalTiledMapRenderer(tiledMap, batch);

        TiledMapTileLayer tileLayer = (TiledMapTileLayer) tiledMap.getLayers().get(0);
        boardWidth = tileLayer.getWidth() * tileLayer.getTileWidth();
        boardHeight = 55.5f * (tileLayer.getHeight() - 1) + tileLayer.getTileHeight(); //take overlap into account (55,5 + 18,5 = 74)

        board = new Board(tileLayer, MAP_SIZE, boardWidth, boardHeight); //call after camera has been set!
    }

    private void camera() {
        OrthographicCamera camera = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);

        camera.setToOrtho(false, boardWidth, boardHeight); //centers camera projection at width/2 and height/2
        camera.zoom = 0.5f;
//        camera.rotate((360 / (float) NUMBER_PLAYERS) * PLAYER_ID); //player orientation //not working!
    }

    private void instantiateBoard() {
        int[] fieldMatrix = queries.getWholeField();
        int[] teams = new int[MAX_TEAMS + 1]; //also storing empty field

        for (int initialPosition : fieldMatrix) { //get count of teams
            teams[initialPosition]++;
        }

        ArrayList<float[]> positionArrays = new ArrayList<>();
        for (int i = 1; i < teams.length; i++) { //initialize position arrays
            if (teams[i] > 0) {
                positionArrays.add(new float[teams[i] * 2]); //size of team x2 for x and y float
            }
        }

        int[] teamIndices = new int[MAX_TEAMS + 1]; //for looping to fill id arrays
        for (int i = 0; i < fieldMatrix.length; i++) {
            int team = fieldMatrix[i];
            if (team > 0) {
                int nextIndex = teamIndices[team];
                positionArrays.get(team - 1)[nextIndex] = board.get(i + 1).x;
                positionArrays.get(team - 1)[nextIndex + 1] = board.get(i + 1).y;
                teamIndices[team] += 2;
            }
        }

        for (int i = 0; i < positionArrays.size(); i++) {
            if (settings.getBoolean("colorSetting") && i == PLAYER_ID) {
                GameSet.getInstance().register(playerTextures.get(i), settings.getInteger("rgbaValue"), positionArrays.get(i));
            } else {
                GameSet.getInstance().register(playerTextures.get(i), positionArrays.get(i));
            }
            deletedSpritesLists.add(new ArrayList<>()); //add delete list for every team
        }

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(139 / 255f, 58 / 255f, 58 / 255f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false); //would center the camera to the center of the world

        background();

        tiledMapRenderer.setView((OrthographicCamera) viewport.getCamera()); //batch.setProjectionMatrix(viewport.getCamera().combined); is called here
        tiledMapRenderer.render();

        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();

        for (MarbleSet m : GameSet.getInstance().getMarbleSets()) {
            for (int i = 0; i < m.size(); i++) {
                m.getMarble(i).setScale(0.5f); //old: (screenWidth - 15 * 64) / (screenWidth-100)
                m.getMarble(i).draw(batch);
            }
        }

        for (ArrayList<Sprite> arrayList : deletedSpritesLists) {
            for (Sprite sprite : arrayList) {
                sprite.setScale(0.5f);
                sprite.draw(batch);
            }
        }

        batch.end();


        stage.act();
        stage.draw();

        boolean firstFingerTouching = Gdx.input.isTouched(0);
        boolean secondFingerTouching = Gdx.input.isTouched(1);
        boolean thirdFingerTouching = Gdx.input.isTouched(2);

        //translate
        if (firstFingerTouching && secondFingerTouching && thirdFingerTouching) {

            OrthographicCamera camera = ((OrthographicCamera) viewport.getCamera());

            float deltaX = -Gdx.input.getDeltaX(0) * camera.zoom;
            float deltaY = Gdx.input.getDeltaY(0) * camera.zoom;

//            Vector3 v = new Vector3(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0);
//            viewport.unproject(v);
//
//            float halfCameraWidth = v.x / 2f * camera.zoom;
//            float halfCameraHeight = v.y / 2f * camera.zoom;
//
//            float viewBoarderLeft = camera.position.x - halfCameraWidth;
//            float viewBoarderRight = camera.position.x + halfCameraWidth;
//            float viewBoarderUp = camera.position.y + halfCameraHeight;
//            float viewBoarderLow = camera.position.y - halfCameraHeight;
//
//            //empirically determined
//            float leftBound = -120;
//            float upperBound = 810;
//            float rightBound = 1100;
//            float lowerBound = 35;
//
//            float translateX = 0;
//            float translateY = 0;
//
//            if (deltaX < 0) {
//                translateX = (leftBound < (viewBoarderLeft + deltaX) ? deltaX : (viewBoarderLeft - leftBound));
//            } else if (deltaX > 0) {
//                translateX = (rightBound > (viewBoarderRight + deltaX) ? deltaX : (rightBound - viewBoarderRight));
//            }
//
//            if (deltaY < 0) {
//                translateY = (lowerBound < (viewBoarderLow + deltaY) ? deltaY : (viewBoarderLow - lowerBound));
//            } else if (deltaX > 0) {
//                translateY = (upperBound > (viewBoarderUp + deltaY) ? deltaY : (upperBound - viewBoarderUp));
//            }
//
//            viewport.getCamera().translate(translateX, translateY, 0);

            viewport.getCamera().translate(deltaX, deltaY, 0);

        }

        if (firstFingerTouching)
            System.out.println("point: ---" + Gdx.input.getX() + ", " + Gdx.input.getY());

        //zoom
        if (firstFingerTouching && secondFingerTouching && !thirdFingerTouching) {
            zoom();
            firstTouchX = Gdx.input.getX(); //set anew, since position might have changed due to zooming
            firstTouchY = Gdx.input.getY();
        }

        if (firstFingerTouching && !secondFingerTouching && !thirdFingerTouching && !justTouched) {  //on touch begins
            firstTouchX = Gdx.input.getX();
            firstTouchY = Gdx.input.getY();
            justTouched = true;
        }

        if (tiltActive) { //move via tilting phone
            float accelX = -Gdx.input.getAccelerometerX();
            float accelY = Gdx.input.getAccelerometerY();
            if (Math.abs(accelX) > TILT_SENSITIVITY || Math.abs(accelY) > TILT_SENSITIVITY) {
                lastDirection = Directions.calculateDirectionFromAcceleration(accelY, accelX);

                if (SINGLE_DEVICE_MODE || PLAYER_ID == currentPlayer) { //waiting for turn if multiple devices
                    if (lastDirection != Directions.NOTSET && !selectedSprites.isEmpty()) {
                        makeMove();
                    }
                }
            }
        }

        if (justTouched && !firstFingerTouching) { //on touch ends
            lastTouchX = Gdx.input.getX();
            lastTouchY = Gdx.input.getY();

            lastDirection = Directions.calculateDirection(SWIPE_SENSITIVITY, firstTouchX, firstTouchY, lastTouchX, lastTouchY); //only sets direction if sensitivity is exceeded

            if (SINGLE_DEVICE_MODE || PLAYER_ID == currentPlayer) { //waiting for turn if multiple devices
                if (lastDirection != Directions.NOTSET && !selectedSprites.isEmpty()) {
                    makeMove();
                } else {
                    selectMarbleIfTouched();
                }
            }

            justTouched = false;
        }

    }

    public void background() {
        batch.begin();
        batch.draw(background, (boardWidth - background.getWidth()) / 2f, (boardHeight - background.getHeight()) / 2f);
        batch.end();
    }

    private void makeMove() {
        int[] marblesToCheck = new int[selectedSprites.size()];
        for (int i = 0; i < selectedSprites.size(); i++) {
            marblesToCheck[i] = board.getTileId(selectedSprites.get(i));
        }

        int[] enemyMarbles = queries.checkMove(marblesToCheck, lastDirection);
        boolean validMove = enemyMarbles != null;
        if (validMove) {
            boolean marblesToPush = enemyMarbles.length > 0;

            SelectionList<Sprite> selectedEnemySprites = new SelectionList<>(MAX_SELECT);
            if (marblesToPush) {
                for (int enemyMarble : enemyMarbles) { //add enemy marbles for move
                    Vector2 field = board.get(enemyMarble);
                    selectedEnemySprites.select(GameSet.getInstance().getMarble(field.x, field.y));
                }
            }

            moveSelectedMarbles(selectedSprites); //move
            moveSelectedMarbles(selectedEnemySprites);

            unselectCompleteList();

            //unmark renegade
            if (renegadeLabels != null) {
                stage.getActors().pop();
                renegadeLabels = null;
            }

            if (queries.isPushedOutOfBound()) {
                Sprite capturedMarble = selectedEnemySprites.get(selectedEnemySprites.size() - 1); //always the last one

                //choose renegade = true
                renegadeKeepers[GameSet.getInstance().getTeamIndex(capturedMarble)].setCanPickRenegadeTrue(); //TODO make it work

                GameSet.getInstance().removeMarble(capturedMarble);

                ArrayList<Sprite> deletionList = deletedSpritesLists.get(currentPlayer); //TODO show captured marbles
                deletionList.add(capturedMarble);
                deadBlackMarbleLabel.setText(deletedSpritesLists.get(0).size());
                deadWhiteMarbleLabel.setText(deletedSpritesLists.get(1).size());

                if (currentPlayer == 1) {
                    capturedMarble.setCenter(780, 140 + (60 * (deletionList.size() - 1)));
                } else {
                    capturedMarble.setCenter(60, 580 - (60 * (deletionList.size() - 1)));
                }

                if (deletionList.size() == NUMBER_CAPTURES_TO_WIN) {
                    winner = currentPlayer;
                    winnerLabel();
                }
            }

            lastDirection = Directions.NOTSET;

            if (SINGLE_DEVICE_MODE && renegadeKeepers[currentPlayer].hasDoubleTurn()) {
                renegadeKeepers[currentPlayer].takeDoubleTurn();
            } else if (winner != -1) {
                nextLabel.setText("");
            } else {
                nextPlayer();
            }
        }
    }

    public void makeRemoteMove(int[] ids, Directions direction, boolean enemy, boolean enemySecondTurn) {
        lastDirection = direction;
        SelectionList<Sprite> marblesToMove = new SelectionList<>(MAX_SELECT);
        for (int id : ids) {
            Vector2 field = board.get(id);
            marblesToMove.select(GameSet.getInstance().getMarble(field.x, field.y));
        }

        moveSelectedMarbles(marblesToMove);

        if (enemy && queries.isPushedOutOfBound()) {
            Sprite capturedMarble = marblesToMove.get(marblesToMove.size() - 1); //always the last one

            GameSet.getInstance().removeMarble(capturedMarble);

            ArrayList<Sprite> deletionList = deletedSpritesLists.get(currentPlayer);
            deletionList.add(capturedMarble);
            deadBlackMarbleLabel.setText(deletedSpritesLists.get(0).size());
            deadWhiteMarbleLabel.setText(deletedSpritesLists.get(1).size());

            if (currentPlayer == 1) {
                capturedMarble.setCenter(780, 140 + (60 * (deletionList.size() - 1)));
            } else {
                capturedMarble.setCenter(60, 580 - (60 * (deletionList.size() - 1)));
            }

            if (deletionList.size() == NUMBER_CAPTURES_TO_WIN) {
                winner = currentPlayer;
                winnerLabel();
            }
        }

        if (winner != -1) {
            nextLabel.setText("");
        } else if (!enemy && !enemySecondTurn) {
            nextPlayer();
        }
    }

    public int nextPlayer() { //TODO has to be called if server sends move of opponent
        currentPlayer = (currentPlayer + 1) % NUMBER_PLAYERS;
        nextLabel.setText(GameInfo.getInstance().getNames().get(currentPlayer));

        RenegadeKeeper renegadeKeeper = null;
        if (SINGLE_DEVICE_MODE) {
            renegadeKeeper = renegadeKeepers[currentPlayer];
        } else if (currentPlayer == GameInfo.getInstance().getPlayerId()) {
            renegadeKeeper = renegadeKeepers[0];
        }

        if (renegadeKeeper != null) {
            nextLabel.setText(nextLabel.getText() + (renegadeKeeper.isCanPickRenegade() ? "*" : ""));
            renegadeKeeper.checkNewRenegade(queries.idOfCurrentRenegade()); //update renegade id -> has expose attempt
        }

        //online game.. you cant see renegade status of other player yet.

        return currentPlayer;
    }

    public void createRenegadeMark(Vector2 center) {
        Label mark = FactoryHelper.createLabelWithText("R", 20, 20);
        mark.setAlignment(Align.center);
        mark.setColor(Color.GOLD);
        renegadeLabels = mark;

        stage.addActor(mark);
        Actor label = stage.getActors().peek();

        Vector3 vector = new Vector3(center.x, center.y, 0f);
        viewport.project(vector);
        label.setX(vector.x - mark.getWidth() / 2);
        label.setY(vector.y - mark.getHeight() / 2);
    }

    public void updateRemoteRenegade(int renegadeId) {
        Vector2 spriteCenter = board.get(renegadeId);
        Sprite renegadeSprite = GameSet.getInstance().getMarble(spriteCenter.x, spriteCenter.y);

        if (renegadeSprite == null) {
            throw new IllegalArgumentException("no valid renegade");
        }

        Sprite marbleOfCurrentPlayer = GameSet.getInstance().getMarbleSets().get(currentPlayer).getMarble(0);
        chooseRenegade(renegadeSprite, marbleOfCurrentPlayer.getTexture(), marbleOfCurrentPlayer.getColor()); //playerTextures.get(currentPlayer));
        queries.changeTo(renegadeId, currentPlayer); //updates logic that marble changed team
//        nextLabel.setText(GameInfo.getInstance().getNames().get(currentPlayer) + (renegadeKeepers[currentPlayer].isCanPickRenegade() ? "*" : ""));
    }

    public void chooseRenegade(Sprite renegade, Texture texture, Color color) {
        transitionRenegade(renegade, texture, color);
        if (SINGLE_DEVICE_MODE) {
            renegadeKeepers[currentPlayer].setRenegade(board.getTileId(renegade));
        }
    }

    public void transitionRenegade(Sprite renegade, Texture texture, Color color) {
        GameSet.getInstance().removeMarble(renegade);
        renegade.setTexture(texture); //set texture
        renegade.setColor(color);
        GameSet.getInstance().getMarbleSets().get(currentPlayer).addMarble(renegade);
    }

    private void selectMarbleIfTouched() {
        //touch coordinates have to be translate to map coordinates
        Vector3 v = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0f);
        viewport.unproject(v);
        Sprite potentialSprite = GameSet.getInstance().getMarble(v.x, v.y); //returns null if no marble matches coordinates
        if (potentialSprite != null && GameSet.getInstance().getTeamIndex(potentialSprite) != currentPlayer) {

            if (renegadeKeepers[currentPlayer].expose(board.getTileId(potentialSprite))) { //first expose
                queries.resetRenegade();
                createRenegadeMark(board.getCenter(potentialSprite));
            } else if (renegadeKeepers[currentPlayer].isCanPickRenegade()) { //then pick
                Sprite marbleOfCurrentPlayer = GameSet.getInstance().getMarbleSets().get(currentPlayer).getMarble(0);
                chooseRenegade(potentialSprite, marbleOfCurrentPlayer.getTexture(), marbleOfCurrentPlayer.getColor());
                queries.changeTo(board.getTileId(potentialSprite), currentPlayer);
                nextLabel.setText(GameInfo.getInstance().getNames().get(currentPlayer) + (renegadeKeepers[currentPlayer].isCanPickRenegade() ? "*" : ""));
            }

            //else nothing

            return;
        }

        int marbleCounter = 0;
        if (potentialSprite != null) {
            int[] buffer = new int[selectedSprites.size() + 1]; //previously selected + 1
            for (int i = 0; i < selectedSprites.size(); i++) {
                buffer[i] = board.getTileId(selectedSprites.get(i));
                marbleCounter++;
            }
            if (!selectedSprites.contains(potentialSprite)) {
                buffer[buffer.length - 1] = board.getTileId(potentialSprite);
                marbleCounter++;
            }

            int[] marblesToCheck = new int[marbleCounter];
            for (int i = 0, k = 0; i < buffer.length; i++) { //don't deliver zeros
                if (buffer[i] != 0) {
                    marblesToCheck[k++] = buffer[i];
                }
            }

            if (queries.isInLine(marblesToCheck)) {
                boolean alreadySelected = !select(potentialSprite);
                if (alreadySelected) {
                    int tileIndexPotentialSprite = board.getTileId(potentialSprite);
                    boolean isMax = true;
                    boolean isMin = true;
                    for (int i = 0; i < selectedSprites.size(); i++) {
                        Sprite currentSprite = selectedSprites.get(i);
                        if (board.getTileId(currentSprite) > tileIndexPotentialSprite) {
                            isMax = false;
                        }
                        if (board.getTileId(currentSprite) < tileIndexPotentialSprite) {
                            isMin = false;
                        }
                    }

                    if (isMax || isMin) { //first or last marble
                        unselect(potentialSprite);
                    } else {
                        unselectCompleteList();
                    }
                }
            }
        } else {
            unselectCompleteList();
        }
    }

    private boolean select(Sprite sprite) {
        if (selectedSprites.select(sprite)) {
            sprite.setColor(Color.GRAY);
            return true;
        }
        return false;
    }

    private void unselect(Sprite sprite) {
        selectedSprites.unselect(sprite);
        if (settings.getBoolean("colorSetting") && currentPlayer == PLAYER_ID) {
            sprite.setColor(new Color(settings.getInteger("rgbaValue")));
        } else {
            sprite.setColor(Color.WHITE);
        }

    }

    private void unselectCompleteList() {
        for (int i = 0; i < selectedSprites.size(); i++) {
            Sprite s = selectedSprites.get(i);
            if (s != null) {
                if (settings.getBoolean("colorSetting") && currentPlayer == PLAYER_ID) {
                    s.setColor(new Color(settings.getInteger("rgbaValue")));
                } else {
                    s.setColor(Color.WHITE);
                }
            }
        }
        selectedSprites.unselectAll();
    }

    private void zoom() {
        boolean zeroLeftFinger = Gdx.input.getX(0) < Gdx.input.getX(1); //set left/right finger to make touch sequence irrelevant
        int indexLeftFinger = zeroLeftFinger ? 0 : 1;
        int indexRightFinger = !zeroLeftFinger ? 0 : 1;

        OrthographicCamera camera = ((OrthographicCamera) viewport.getCamera());

        if ((Gdx.input.getDeltaX(indexLeftFinger) < 0 && Gdx.input.getDeltaX(indexRightFinger) > 0)) { //delta left finger neg. -> zoom in (make zoom smaller)
            if (camera.zoom > min_zoom) {
                camera.zoom -= 0.02;
            }
        }
        if ((Gdx.input.getDeltaX(indexLeftFinger) > 0 && Gdx.input.getDeltaX(indexRightFinger) < 0)) { //zoom out
            if (camera.zoom < max_zoom) {
                camera.zoom += 0.02;
            }
        }
    }

    private void moveSelectedMarbles(SelectionList<Sprite> list) {
        for (int i = 0; i < list.size(); i++) {
            board.move(list.get(i), lastDirection);
        }
    }

    @Override
    public void show() {
        playerLabel();
        deadBlackMarblesLabel();
        deadWhiteMarblesLabel();
        settingsButton();
        exitButton();

        music();
    }

    public void playerLabel() {
        nextLabel = FactoryHelper.createLabelWithText(GameInfo.getInstance().getNames().get(currentPlayer), 200, 60);
        nextLabel.setAlignment(Align.right);
        stage.addActor(nextLabel);
        Actor label = stage.getActors().peek();
        label.setX(screenWidth - (label.getWidth() + 220));
        label.setY(screenHeight - (label.getHeight() + 60));
    }

    public void deadBlackMarblesLabel() {
        deadBlackMarbleLabel = FactoryHelper.createLabelWithText("" + deletedSpritesLists.get(0).size(), 100, 60);

        stage.addActor(deadBlackMarbleLabel);
        Actor label = stage.getActors().peek();
        label.setX((label.getWidth() + 220));
        label.setY(screenHeight - (label.getHeight() + 60));
    }

    public void deadWhiteMarblesLabel() {
        deadWhiteMarbleLabel = FactoryHelper.createLabelWithText("" + deletedSpritesLists.get(1).size(), 100, 60);

        stage.addActor(deadWhiteMarbleLabel);
        Actor label = stage.getActors().peek();
        label.setX(screenWidth - (label.getWidth() + 220));
        label.setY((label.getHeight() + 60));
    }

    public void winnerLabel() {
        //TODO proper style?
        Label winLabel = FactoryHelper.createLabelWithText(GameInfo.getInstance().getNames().get(currentPlayer) + " won", screenWidth, screenHeight);
        winLabel.setAlignment(Align.center);

        Abalone currentGame = this;
        winLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // TODO: Open settings overlay.
                Gdx.app.log("ClickListener", winLabel.toString() + " clicked");
                currentGame.exitCurrentGame();
            }
        });

        stage.addActor(winLabel);
    }

    public void surrenderLabel() {
        //TODO proper style?
        Label surrenderLabel = FactoryHelper.createLabelWithText(GameInfo.getInstance().getNames().get(currentPlayer) + " surrenders", screenWidth, screenHeight);
        surrenderLabel.setAlignment(Align.center);

        Abalone currentGame = this;
        surrenderLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // TODO: Open settings overlay.
                Gdx.app.log("ClickListener", surrenderLabel.toString() + " clicked");
                currentGame.exitCurrentGame();
            }
        });

        stage.addActor(surrenderLabel);
    }

    private void exitCurrentGame() {
        resetView();
        bgMusic.stop();

        MenuScreen menuScreen = game.menuScreen;
        menuScreen.setField(null);
        game.setScreen(menuScreen);
        Gdx.input.setInputProcessor(game.menuStage);
    }

    public void resetView() {
        board = null;
        GameSet.reset();
    }

    public void settingsButton() { //TODO copied code from SettingsDialog; make it not repetitive
        Skin skin = FactoryHelper.getDefaultSkin();
        settingsButton = FactoryHelper.createImageButton(
                skin.get("settings-btn", ImageButton.ImageButtonStyle.class),
                150, 150, 0, 0); //TODO method without x/y parameters? want to make it width/height dependent

        Abalone currentGame = this;
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // TODO: Open settings overlay.
                Gdx.app.log("ClickListener", settingsButton.toString() + " clicked");
                Skin uiSkin = new Skin(Gdx.files.internal(GameConstants.CUSTOM_UI_JSON));
                SettingsDialog settingsDialog = new SettingsDialog("", uiSkin, currentGame);
                settingsDialog.show(stage);
            }
        });

        stage.addActor(settingsButton);
        Actor button = stage.getActors().peek();
        button.setX(screenWidth - (button.getWidth() + 20));
        button.setY(screenHeight - (button.getHeight() + 20));

        // TODO updating setting changes
    }

    public void exitButton() { //TODO copied code from SettingsDialog; make it not repetitive
        Skin skin = FactoryHelper.getDefaultSkin();
        ImageButton exitButton = FactoryHelper.createImageButton(
                skin.get("exit-btn", ImageButton.ImageButtonStyle.class),
                150, 150, 0, 0); //TODO method without x/y parameters? want to make it width/height dependent

        Abalone currentGame = this;
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // TODO: Open settings overlay.
                Gdx.app.log("ClickListener", exitButton.toString() + " clicked");
                surrenderLabel();
                if (!SINGLE_DEVICE_MODE) {
                    BaseRequest surrenderRequest = new SurrenderRequest(UUID.fromString(Gdx.app.getPreferences("UserPreferences").getString("UserId")));
                    try {
                        RequestSender rs = new RequestSender(surrenderRequest);
                        ExecutorService executorService = Executors.newSingleThreadExecutor();
                        Future future = executorService.submit(rs);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        stage.addActor(exitButton);
        Actor button = stage.getActors().peek();
        button.setX(20);
        button.setY(screenHeight - (button.getHeight() + 20));
    }

    public void music() {
        bgMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds\\background.wav"));
        bgMusic.play();
        bgMusic.setLooping(true);
        bgMusic.setVolume(settings.getFloat("bgMusicVolumeFactor", 1f));
    }

    public void updateSettings() {
        bgMusic.setVolume(settings.getFloat("bgMusicVolumeFactor", 1f));

        String boardTexturePath = "boards/" + settings.getString("boardSkin");
        if (!background.toString().equals(boardTexturePath)) {
            background = new Texture(boardTexturePath);
        }

        for (int k = 0; k < playerTextures.size(); k++) { //TODO clearer code
            Texture oldTexture = playerTextures.get(k);
            playerTextures.set(k, new Texture("marbles/" + settings.getString("marbleSkin" + k)));
            Texture newTexture = playerTextures.get(k);
            if (!newTexture.toString().equals(oldTexture.toString())) { //if texture type changed
                MarbleSet marbleSet = GameSet.getInstance().getMarbleSets().get(k);
                if (marbleSet.getMarble(0) != null) {
                    for (int i = 0; i < marbleSet.size(); i++) {
                        marbleSet.getMarble(i).setTexture(newTexture);
                    }
                }

            }
        }

        if (settings.getBoolean("colorSetting")) {
            GameSet.getInstance().colorMarbleSet(settings.getInteger("rgbaValue"), PLAYER_ID);
        } else {
            GameSet.getInstance().colorMarbleSet(-197377, PLAYER_ID);
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        exitCurrentGame();
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() { //TODO dispose of everything
        bgMusic.dispose();

        batch.dispose();
        stage.dispose();

        tiledMap.dispose();
        tiledMapRenderer.dispose();

        background.dispose();
        for (int i = 0; i < playerTextures.size(); i++) {
            playerTextures.get(i).dispose();
        }

        exitCurrentGame();
    }

    @Override
    public void HandleResponse(BaseResponse response) {
        if (!GameInfo.getInstance().getSingleDeviceMode()) {
            if (response instanceof MadeMoveResponse) {
                if (response.getCommandCode() == ResponseCommandCodes.MADE_MOVE.getValue()) {
                    surrenderLabel();
                } else if (response.getCommandCode() == ResponseCommandCodes.ROOM_EXCEPTION.getValue()) {
                    //Exception handling goes here : Maybe a small notification to be shown
                } else if (response.getCommandCode() == ResponseCommandCodes.SERVER_EXCEPTION.getValue()) {
                    //Exception handling goes here : Maybe a small notification to be shown
                } else if (response.getCommandCode() == ResponseCommandCodes.GAME_EXCEPTION.getValue()) {
                    //Exception handling goes here : Maybe a small notification to be shown
                }
            }
        }
    }
}