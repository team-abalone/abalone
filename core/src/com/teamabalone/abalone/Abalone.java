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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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
import com.teamabalone.abalone.Client.Responses.SurrenderResponse;
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
    private final int MAP_SIZE = gameInfos.getMapSize();
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

    private Label currentPlayerLabel;
    private Label deadBlackMarbleLabel;
    private Label deadWhiteMarbleLabel;

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
    private Label renegadeLabel = null;

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

        drawSprites();
        stage.act();
        stage.draw();

        boolean firstFingerTouching = Gdx.input.isTouched(0);
        boolean secondFingerTouching = Gdx.input.isTouched(1);
        boolean thirdFingerTouching = Gdx.input.isTouched(2);

        //translate
        if (firstFingerTouching && secondFingerTouching && thirdFingerTouching) {
            float deltaX = -Gdx.input.getDeltaX(0) * ((OrthographicCamera) viewport.getCamera()).zoom;
            float deltaY = Gdx.input.getDeltaY(0) * ((OrthographicCamera) viewport.getCamera()).zoom;
            viewport.getCamera().translate(deltaX, deltaY, 0);
        }

        //zoom
        if (firstFingerTouching && secondFingerTouching && !thirdFingerTouching) {
            zoom();
            firstTouchX = Gdx.input.getX(); //set anew, since position might have changed due to zooming
            firstTouchY = Gdx.input.getY();
        }

        if (firstFingerTouching && !secondFingerTouching && !thirdFingerTouching && !justTouched) {
            onTouchBeginns();
        }

        if (tiltActive) { //move via tilting phone
            checkTiltMove();
        }

        if (justTouched && !firstFingerTouching) {
            onTouchEnds();
        }
    }

    private void drawSprites() {
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        for (MarbleSet m : GameSet.getInstance().getMarbleSets()) {
            for (int i = 0; i < m.size(); i++) {
                m.getMarble(i).setScale(0.5f); //old: (screenWidth - 15 * 64) / (screenWidth-100)
                m.getMarble(i).draw(batch);
            }
        }

//        for (ArrayList<Sprite> arrayList : deletedSpritesLists) {
//            for (Sprite sprite : arrayList) {
//                sprite.setScale(0.5f);
//                sprite.draw(batch);
//            }
//        }

        batch.end();
    }

    private void checkTiltMove() {
        float accelerationX = -Gdx.input.getAccelerometerX();
        float accelerationY = Gdx.input.getAccelerometerY();
        if (Math.abs(accelerationX) > TILT_SENSITIVITY || Math.abs(accelerationY) > TILT_SENSITIVITY) {
            lastDirection = Directions.getDirection(accelerationY, accelerationX);

            if (SINGLE_DEVICE_MODE || PLAYER_ID == currentPlayer) { //waiting for turn if multiple devices
                if (lastDirection != Directions.NOTSET && !selectedSprites.isEmpty()) {
                    makeMove();
                }
            }
        }
    }

    private void onTouchBeginns() {
        firstTouchX = Gdx.input.getX();
        firstTouchY = Gdx.input.getY();
        justTouched = true;
    }

    private void onTouchEnds() {
        lastTouchX = Gdx.input.getX();
        lastTouchY = Gdx.input.getY();

        lastDirection = Directions.calculateDirection(SWIPE_SENSITIVITY, firstTouchX, firstTouchY, lastTouchX, lastTouchY); //only sets direction if sensitivity is exceeded

        if (SINGLE_DEVICE_MODE || PLAYER_ID == currentPlayer) { //waiting for turn if multiple devices
            if (lastDirection != Directions.NOTSET && !selectedSprites.isEmpty()) {
                makeMove();
            } else {
                checkSelection();
            }
        }

        justTouched = false;
    }

    private void background() {
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
        if (enemyMarbles != null) {
            SelectionList<Sprite> selectedEnemySprites = new SelectionList<>(MAX_SELECT);

            if (enemyMarbles.length > 0) {
                for (int enemyMarble : enemyMarbles) {
                    Vector2 field = board.get(enemyMarble);
                    selectedEnemySprites.select(GameSet.getInstance().getMarble(field.x, field.y));
                }
            }

            moveSelectedMarbles(selectedSprites);
            moveSelectedMarbles(selectedEnemySprites);

            unselectAllSelectedSprites();
            unmarkRenegade();
            lastDirection = Directions.NOTSET;

            if (queries.isPushedOutOfBound()) {
                Sprite pushedOutMarble = selectedEnemySprites.get(selectedEnemySprites.size() - 1); //always the last one
//                renegadeKeepers[GameSet.getInstance().getTeamIndex(pushedOutMarble)].setCanPickRenegadeTrue(); //TODO make it work
                capture(pushedOutMarble);
            }

            if (SINGLE_DEVICE_MODE && renegadeKeepers[currentPlayer].hasDoubleTurn()) {
                renegadeKeepers[currentPlayer].takeDoubleTurn();
            } else if (winner != -1) {
                stage.getActors().removeValue(currentPlayerLabel, true);
            } else {
                nextPlayer();
            }
        }
    }

    private void unmarkRenegade() {
        if (renegadeLabel != null) {
            stage.getActors().pop();
            renegadeLabel = null;
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
            Sprite pushedOutMarble = marblesToMove.get(marblesToMove.size() - 1); //always the last one
            capture(pushedOutMarble);
        }

        if (winner != -1) {
            stage.getActors().removeValue(currentPlayerLabel, true);
        } else if (!enemy && !enemySecondTurn) {
            nextPlayer();
        }
    }

    private void capture(Sprite sprite) {
        GameSet.getInstance().removeMarble(sprite);

        ArrayList<Sprite> deletionList = deletedSpritesLists.get(currentPlayer);
        deletionList.add(sprite);
        deadBlackMarbleLabel.setText(deletedSpritesLists.get(0).size());
        deadWhiteMarbleLabel.setText(deletedSpritesLists.get(1).size());

        if (currentPlayer == 1) {
            sprite.setCenter(780, 140 + (60 * (deletionList.size() - 1)));
        } else {
            sprite.setCenter(60, 580 - (60 * (deletionList.size() - 1)));
        }

        if (deletionList.size() == NUMBER_CAPTURES_TO_WIN) {
            winner = currentPlayer;
            createWinnerLabel();
        }
    }

    private void nextPlayer() {
        currentPlayer = (currentPlayer + 1) % NUMBER_PLAYERS;
        currentPlayerLabel.setText(GameInfo.getInstance().getNames().get(currentPlayer));

        RenegadeKeeper renegadeKeeper = null;
        if (SINGLE_DEVICE_MODE) {
            renegadeKeeper = renegadeKeepers[currentPlayer];
        } else if (currentPlayer == GameInfo.getInstance().getPlayerId()) {
            renegadeKeeper = renegadeKeepers[0];
        }

        if (renegadeKeeper != null) {
            currentPlayerLabel.setText(currentPlayerLabel.getText() + (renegadeKeeper.isCanPickRenegade() ? "*" : ""));
            renegadeKeeper.checkNewRenegade(queries.idOfCurrentRenegade()); //update renegade id -> has expose attempt
        }

        //online game.. you cant see renegade status of other player yet.
    }

    private void createRenegadeMark(Vector2 center) {
        renegadeLabel = FactoryHelper.createLabelWithText("R", 20, 20);
        renegadeLabel.setAlignment(Align.center);
        renegadeLabel.setColor(Color.GOLD);

        stage.addActor(renegadeLabel);
        Actor label = stage.getActors().peek();

        Vector3 vector = new Vector3(center.x, center.y, 0f);
        viewport.project(vector);
        label.setX(vector.x - renegadeLabel.getWidth() / 2);
        label.setY(vector.y - renegadeLabel.getHeight() / 2);
    }

    public void updateRemoteRenegade(int fieldId) {
        Vector2 spriteCenter = board.get(fieldId);
        Sprite renegadeSprite = GameSet.getInstance().getMarble(spriteCenter.x, spriteCenter.y);

        if (renegadeSprite == null) {
            throw new IllegalArgumentException("no valid renegade");
        }

        Sprite marbleOfCurrentPlayer = GameSet.getInstance().getMarbleSets().get(currentPlayer).getMarble(0);
        selectRenegade(renegadeSprite, marbleOfCurrentPlayer.getTexture(), marbleOfCurrentPlayer.getColor()); //playerTextures.get(currentPlayer));
        queries.changeTo(fieldId, currentPlayer); //updates logic that marble changed team
//        nextLabel.setText(GameInfo.getInstance().getNames().get(currentPlayer) + (renegadeKeepers[currentPlayer].isCanPickRenegade() ? "*" : ""));
    }

    private void selectRenegade(Sprite renegade, Texture texture, Color color) {
        changeTeam(renegade, texture, color);
        if (SINGLE_DEVICE_MODE) {
            renegadeKeepers[currentPlayer].setRenegade(board.getTileId(renegade));
        }
    }

    private void changeTeam(Sprite sprite, Texture texture, Color color) {
        GameSet.getInstance().removeMarble(sprite);
        sprite.setTexture(texture); //set texture
        sprite.setColor(color);
        GameSet.getInstance().getMarbleSets().get(currentPlayer).addMarble(sprite);
    }

    private void checkSelection() {
        //touch coordinates have to be translate to map coordinates
        Vector3 v = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0f);
        viewport.unproject(v);
        Sprite potentialSprite = GameSet.getInstance().getMarble(v.x, v.y); //returns null if no marble matches coordinates

        if (potentialSprite != null) {
            handleSelection(potentialSprite);
        } else {
            unselectAllSelectedSprites();
        }
    }

    private void handleSelection(Sprite sprite) {
        if (GameSet.getInstance().getTeamIndex(sprite) != currentPlayer) {

            if (renegadeKeepers[SINGLE_DEVICE_MODE ? currentPlayer : 0].expose(board.getTileId(sprite))) { //first expose
                queries.resetRenegade();
                createRenegadeMark(board.getCenter(sprite));

            } else if (renegadeKeepers[SINGLE_DEVICE_MODE ? currentPlayer : 0].isCanPickRenegade()) { //then pick
                Sprite marbleOfCurrentPlayer = GameSet.getInstance().getMarbleSets().get(currentPlayer).getMarble(0);
                selectRenegade(sprite, marbleOfCurrentPlayer.getTexture(), marbleOfCurrentPlayer.getColor());
                queries.changeTo(board.getTileId(sprite), currentPlayer);
                currentPlayerLabel.setText(GameInfo.getInstance().getNames().get(currentPlayer) + (renegadeKeepers[SINGLE_DEVICE_MODE ? currentPlayer : 0].isCanPickRenegade() ? "*" : ""));
            }

            return;
        }

        if (queries.isInLine(selectedPlus(sprite))) {
            if (!select(sprite)) {
                unselectOneOrAll(sprite);
            }
        }
    }

    private int[] selectedPlus(Sprite sprite) {
        int[] marblesToCheck = new int[selectedSprites.contains(sprite) ? selectedSprites.size() : selectedSprites.size() + 1]; //previously selected + 1

        for (int i = 0; i < selectedSprites.size(); i++) {
            marblesToCheck[i] = board.getTileId(selectedSprites.get(i));
        }

        if (!selectedSprites.contains(sprite)) {
            marblesToCheck[marblesToCheck.length - 1] = board.getTileId(sprite);
        }

        return marblesToCheck;
    }

    private void unselectOneOrAll(Sprite sprite) {
        boolean hasSmallestId = true;
        boolean hasHighestId = true;
        int tileIndexSprite = board.getTileId(sprite);
        Sprite currentSprite;

        for (int i = 0; i < selectedSprites.size(); i++) {
            currentSprite = selectedSprites.get(i);
            if (board.getTileId(currentSprite) > tileIndexSprite) {
                hasSmallestId = false;
            }
            if (board.getTileId(currentSprite) < tileIndexSprite) {
                hasHighestId = false;
            }
        }

        if (hasSmallestId || hasHighestId) {
            unselect(sprite);
        } else {
            unselectAllSelectedSprites();
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
        decolorSprite(sprite);
        selectedSprites.unselect(sprite);
    }

    private void unselectAllSelectedSprites() {
        for (int i = 0; i < selectedSprites.size(); i++) {
            decolorSprite(selectedSprites.get(i));
        }
        selectedSprites.unselectAll();
    }

    private void decolorSprite(Sprite sprite) {
        if (currentPlayer == PLAYER_ID && settings.getBoolean("colorSetting")) {
            sprite.setColor(new Color(settings.getInteger("rgbaValue")));
        } else {
            sprite.setColor(Color.WHITE);
        }
    }

    private void zoom() {
        //set left/right finger to make touch sequence irrelevant
        boolean zeroLeftFinger = Gdx.input.getX(0) < Gdx.input.getX(1);
        int indexLeftFinger = zeroLeftFinger ? 0 : 1;
        int indexRightFinger = !zeroLeftFinger ? 0 : 1;

        OrthographicCamera camera = ((OrthographicCamera) viewport.getCamera());

        //delta left finger neg. -> zoom in (make zoom smaller)
        if ((Gdx.input.getDeltaX(indexLeftFinger) < 0 && Gdx.input.getDeltaX(indexRightFinger) > 0)) {
            if (camera.zoom > min_zoom) {
                camera.zoom -= 0.02;
            }
        }
        //zoom out
        if ((Gdx.input.getDeltaX(indexLeftFinger) > 0 && Gdx.input.getDeltaX(indexRightFinger) < 0)) {
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
        createCurrentPlayerLabel();
        deadBlackMarblesLabel();
        deadWhiteMarblesLabel();
        setUpSettingsButton();
        setUpExitButton();
        startMusic();
    }

    private void createCurrentPlayerLabel() {
        currentPlayerLabel = FactoryHelper.createLabelWithText(GameInfo.getInstance().getNames().get(currentPlayer), 200, 60);
        currentPlayerLabel.setAlignment(Align.right);
        currentPlayerLabel.setColor(Color.RED);

        stage.addActor(currentPlayerLabel);
        Actor label = stage.getActors().peek();
        label.setX(screenWidth - (label.getWidth() + currentPlayerLabel.getWidth()));
        label.setY(screenHeight - (label.getHeight() + currentPlayerLabel.getHeight()));
    }

    public void deadBlackMarblesLabel() {
        deadBlackMarbleLabel = FactoryHelper.createLabelWithText("" + deletedSpritesLists.get(0).size(), 110, 65);

        Vector2 position = new Vector2(screenWidth * 0.1f, screenHeight * 0.85f);

        Image imageOfBlackMarble = new Image(playerTextures.get(1));
        imageOfBlackMarble.setPosition(position.x, position.y);
        stage.addActor(imageOfBlackMarble);

        stage.addActor(deadBlackMarbleLabel);
        Actor label = stage.getActors().peek();
        label.setX(position.x + label.getWidth() / 2f);
        label.setY(position.y + label.getHeight() / 2f);
    }

    public void deadWhiteMarblesLabel() {
        deadWhiteMarbleLabel = FactoryHelper.createLabelWithText("" + deletedSpritesLists.get(1).size(), 110, 65);
        //deadWhiteMarbleLabel.setColor(Color.RED);

        Vector2 position = new Vector2(screenWidth * 0.85f, screenHeight * 0.05f);

        Image imageOfWhiteMarble = new Image(playerTextures.get(0));
        imageOfWhiteMarble.setPosition(position.x, position.y);
        stage.addActor(imageOfWhiteMarble);

        stage.addActor(deadWhiteMarbleLabel);
        Actor label = stage.getActors().peek();
        label.setX(position.x + label.getWidth() / 2f);
        label.setY(position.y + label.getHeight() / 2f);
    }

    private void createWinnerLabel() {
        Label winLabel = FactoryHelper.createLabelWithText(GameInfo.getInstance().getNames().get(currentPlayer) + " won", screenWidth, screenHeight);
        winLabel.setAlignment(Align.center);
        currentPlayerLabel.setColor(Color.RED);

        Abalone currentGame = this;
        winLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("ClickListener", winLabel.toString() + " clicked");
                currentGame.exitCurrentGame();
            }
        });

        stage.addActor(winLabel);
    }

    private void createSurrenderLabel() {
        Label surrenderLabel = FactoryHelper.createLabelWithText("opponent surrenders", screenWidth, screenHeight);
        surrenderLabel.setAlignment(Align.center);

        Abalone currentGame = this;
        surrenderLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("ClickListener", surrenderLabel.toString() + " clicked");
                currentGame.exitCurrentGame();
            }
        });

        stage.addActor(surrenderLabel);
    }

    private void exitCurrentGame() {
        resetViewData();
        bgMusic.stop();

        //switch to menu screen
        MenuScreen menuScreen = game.menuScreen;
        menuScreen.setField(null);
        game.setScreen(menuScreen);
        Gdx.input.setInputProcessor(game.menuStage);
    }

    private void resetViewData() {
        board = null;
        GameSet.reset();
    }

    private void setUpSettingsButton() {
        Skin skin = FactoryHelper.getDefaultSkin();
        ImageButton settingsButton = FactoryHelper.createImageButton(
                skin.get("settings-btn", ImageButton.ImageButtonStyle.class),
                150, 150, 0, 0);

        Abalone currentGame = this;
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
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
    }

    private void setUpExitButton() {
        Skin skin = FactoryHelper.getDefaultSkin();
        ImageButton exitButton = FactoryHelper.createImageButton(
                skin.get("exit-btn", ImageButton.ImageButtonStyle.class),
                150, 150, 0, 0);

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("ClickListener", exitButton.toString() + " clicked");
                if (!SINGLE_DEVICE_MODE) {
                    BaseRequest surrenderRequest = new SurrenderRequest(UUID.fromString(Gdx.app.getPreferences("UserPreferences").getString("UserId")));
                    try {
                        RequestSender rs = new RequestSender(surrenderRequest);
                        ExecutorService executorService = Executors.newSingleThreadExecutor();
                        executorService.submit(rs);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                createSurrenderLabel();
            }
        });

        stage.addActor(exitButton);
        Actor button = stage.getActors().peek();
        button.setX(20);
        button.setY(screenHeight - (button.getHeight() + 20));
    }

    private void startMusic() {
        bgMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds\\background.wav"));
        bgMusic.play();
        bgMusic.setLooping(true);
        bgMusic.setVolume(settings.getFloat("bgMusicVolumeFactor", 1f));
    }

    /**
     * Update values that have been changed in the settings menu during the game, so they take effect.
     */
    public void updateSettings() {

        //volume
        bgMusic.setVolume(settings.getFloat("bgMusicVolumeFactor", 1f));

        //background
        String boardTexturePath = "boards/" + settings.getString("boardSkin");
        if (!background.toString().equals(boardTexturePath)) {
            background = new Texture(boardTexturePath);
        }

        //player texture
        for (int k = 0; k < playerTextures.size(); k++) {
            Texture oldTexture = playerTextures.get(k);
            playerTextures.set(k, new Texture("marbles/" + settings.getString("marbleSkin" + k)));
            Texture newTexture = playerTextures.get(k);

            if (!newTexture.toString().equals(oldTexture.toString())) {
                GameSet.getInstance().setTextureOfMarbleSet(newTexture, k);
            }
        }

        //player color
        if (settings.getBoolean("colorSetting")) {
            GameSet.getInstance().colorMarbleSet(settings.getInteger("rgbaValue"), PLAYER_ID);
        } else {
            GameSet.getInstance().colorMarbleSet(-197377, PLAYER_ID); //no color
        }

        //update sensor control
        tiltActive = settings.getBoolean("TiltingActive");
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
    public void dispose() {
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
            if (response instanceof SurrenderResponse) {
                if (response.getCommandCode() == ResponseCommandCodes.SURRENDERED.getValue()) {
                    createSurrenderLabel();
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