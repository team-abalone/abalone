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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.teamabalone.abalone.Dialogs.SettingsDialog;

import com.teamabalone.abalone.Gamelogic.AbaloneQueries;
import com.teamabalone.abalone.Gamelogic.Field;
import com.teamabalone.abalone.Gamelogic.Directions;

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

public class Abalone implements Screen {
    private final GameInfos gameInfos = GameInfo.getInstance();

    private final int MAX_TEAMS = 6;
    private final int SWIPE_SENSITIVITY = 40;
    private final int NUMBER_CAPTURES_TO_WIN = 6;
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

    public Abalone(GameImpl game, Field field) {
        queries = field;
        settings = Gdx.app.getPreferences("UserSettings");

        this.game = game;
        batch = game.batch;
        stage = game.gameStage;
//        stage = new Stage(); //stage not attached, so it moves with screen
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
            viewport.getCamera().translate(-Gdx.input.getDeltaX(1), Gdx.input.getDeltaY(1), 0);
        }

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
        float backgroundWidth = background.getWidth();
        float backgroundHeight = background.getHeight();
        float startX = boardWidth / 2 - backgroundWidth;
        float startY = boardHeight / 2 - backgroundHeight;

        batch.begin();
        batch.draw(background, startX, startY);
        batch.draw(background, startX, startY + backgroundHeight);
        batch.draw(background, startX, startY + backgroundHeight * 2);
        batch.draw(background, startX, startY - backgroundHeight);

        batch.draw(background, startX + backgroundWidth, startY);
        batch.draw(background, startX + backgroundWidth, startY + backgroundHeight);
        batch.draw(background, startX + backgroundWidth, startY + backgroundHeight * 2);
        batch.draw(background, startX + backgroundWidth, startY - backgroundHeight);

        batch.draw(background, startX + backgroundWidth * 2, startY);
        batch.draw(background, startX + backgroundWidth * 2, startY + backgroundHeight);
        batch.draw(background, startX + backgroundWidth * 2, startY + backgroundHeight * 2);
        batch.draw(background, startX + backgroundWidth * 2, startY - backgroundHeight);

        batch.draw(background, startX - backgroundWidth, startY);
        batch.draw(background, startX - backgroundWidth, startY + backgroundHeight);
        batch.draw(background, startX - backgroundWidth, startY + backgroundHeight * 2);
        batch.draw(background, startX - backgroundWidth, startY - backgroundHeight);
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
                renegadeKeepers[GameSet.getInstance().getTeamIndex(capturedMarble)].setCanPickRenegade();

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
                    exitLabel();
                }
            }

            lastDirection = Directions.NOTSET;
            nextPlayer();
        }
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

    private void selectMarbleIfTouched() {
        //touch coordinates have to be translate to map coordinates
        Vector3 v = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0f);
        viewport.unproject(v);
        Sprite potentialSprite = GameSet.getInstance().getMarble(v.x, v.y); //returns null if no marble matches coordinates
        if (potentialSprite != null && GameSet.getInstance().getTeamIndex(potentialSprite) != currentPlayer) {

            //TODO first turns marble before expose enemy marble
            if (renegadeKeepers[currentPlayer].expose(board.getTileId(potentialSprite))) {
                queries.resetRenegade();
                createRenegadeMark(board.getCenter(potentialSprite));
            } else if (renegadeKeepers[currentPlayer].isCanPickRenegade()) {
                renegadeKeepers[currentPlayer].chooseRenegade(potentialSprite, playerTextures.get(currentPlayer), currentPlayer, board);
                queries.changeTo(board.getTileId(potentialSprite), currentPlayer);
                nextLabel.setText((currentPlayer == 0 ? "White" : "Black") + (renegadeKeepers[currentPlayer].isCanPickRenegade() ? "*" : ""));
            }

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

        if ((Gdx.input.getDeltaX(indexLeftFinger) < 0 && Gdx.input.getDeltaX(indexRightFinger) > 0)) { //delta left finger neg. -> zoom in (make zoom smaller)
            ((OrthographicCamera) viewport.getCamera()).zoom -= 0.02;
        }
        if ((Gdx.input.getDeltaX(indexLeftFinger) > 0 && Gdx.input.getDeltaX(indexRightFinger) < 0)) { //zoom out
            ((OrthographicCamera) viewport.getCamera()).zoom += 0.02;
        }
    }

    private void moveSelectedMarbles(SelectionList<Sprite> list) {
        for (int i = 0; i < list.size(); i++) {
            board.move(list.get(i), lastDirection);
        }
    }

    public int nextPlayer() { //TODO has to be called if server sends move of opponent
        if (renegadeKeepers[currentPlayer].hasDoubleTurn()) {
            renegadeKeepers[currentPlayer].takeDoubleTurn();
        } else {
            currentPlayer = (currentPlayer + 1) % NUMBER_PLAYERS;

            if (SINGLE_DEVICE_MODE) {
                nextLabel.setText((currentPlayer == 0 ? "White" : "Black") + (renegadeKeepers[currentPlayer].isCanPickRenegade() ? "*" : ""));
                renegadeKeepers[currentPlayer].checkNewRenegade(queries.idOfCurrentRenegade()); //update renegade id -> has expose attempt
            } else {
                //TODO change label
            }

            //TODO player communication server wont work that easily
        }
        return currentPlayer;
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
        //TODO proper style?
        nextLabel = FactoryHelper.createLabelWithText(currentPlayer == 0 ? "White" : "Black", 100, 60);
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

    public void exitLabel() {
        //TODO proper style?
        Label winLabel = FactoryHelper.createLabelWithText(currentPlayer == 0 ? "White won" : "Black won", screenWidth, screenHeight);
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

    private void exitCurrentGame() {
        resetView();
        MenuScreen.field = null;
        bgMusic.stop();

        game.setScreen(game.menuScreen);
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
                currentGame.exitCurrentGame();
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
}