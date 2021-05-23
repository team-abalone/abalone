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
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.teamabalone.abalone.Dialogs.SettingsDialog;
import com.teamabalone.abalone.Dialogs.TurnAnnouncerTwo;

import com.teamabalone.abalone.Gamelogic.AbaloneQueries;
import com.teamabalone.abalone.Gamelogic.Field;
import com.teamabalone.abalone.Gamelogic.Directions;

import com.teamabalone.abalone.Gamelogic.GameInfo;
import com.teamabalone.abalone.Gamelogic.GameInfos;
import com.teamabalone.abalone.Helpers.FactoryHelper;
import com.teamabalone.abalone.Helpers.GameConstants;
import com.teamabalone.abalone.View.Board;
import com.teamabalone.abalone.View.GameSet;
import com.teamabalone.abalone.View.MarbleSet;
import com.teamabalone.abalone.View.SelectionList;

import java.util.ArrayList;

public class Abalone implements Screen {
    private final GameInfos gameInfos = GameInfo.getInstance();

    private final int MAX_TEAMS = 6;
    private final int SWIPE_SENSITIVITY = 40;
    private final boolean SINGLE_DEVICE_MODE = gameInfos.singleDeviceMode();
    private final int MAX_SELECT = gameInfos.maximalSelectableMarbles();
    private final int NUMBER_PLAYERS = gameInfos.numberPlayers();
    private final int MAP_SIZE = gameInfos.mapSize(); //TODO make only setting valid value possible?

    private Music bgMusic;
    private Preferences settings;

    private final GameImpl game;
    private final SpriteBatch batch;

    private TiledMap tiledMap;
    private HexagonalTiledMapRenderer tiledMapRenderer;
    private FitViewport viewport;

    private Texture background;
    private Texture blackBall;
    private Texture whiteBall;

    private Board board;

    private final float screenWidth = Gdx.graphics.getWidth();
    private final float screenHeight = Gdx.graphics.getHeight();

    boolean yourTurn = true;
    boolean wasTouched = false;
    TurnAnnouncerTwo nextPlayerCard;

    private Stage stage;
    private Label nextLabel;
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

    public Abalone(GameImpl game) {
        settings = Gdx.app.getPreferences("UserSettings");

        this.game = game;
        batch = game.getBatch();

        stage = new Stage(); //stage not attached, so it moves with screen
        Gdx.input.setInputProcessor(stage);

        board();
        camera();
        textures();

        instantiateBoard();
    }

    private void textures() {
        background = new Texture("boards/" + settings.getString("boardSkin", "Laminat.png"));
        blackBall = new Texture("marbles/" + settings.getString("marbleSkin", "ball.png"));
        whiteBall = new Texture("marbles/ball_white.png");
    }

    private void board() {
        tiledMap = new TmxMapLoader().load("abalone_map" + MAP_SIZE + ".tmx");
        tiledMapRenderer = new HexagonalTiledMapRenderer(tiledMap);

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
        queries = new Field(5);
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
            GameSet.getInstance().register(i == 0 ? whiteBall : blackBall, positionArrays.get(i)); //TODO set chosen color
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

        /*
        Gdx.app.log("Click Listener", "Your Turn = " + yourTurn);
        if(yourTurn == true){      //solange true kann bewegt werden
            if (firstFingerTouching && !secondFingerTouching && !thirdFingerTouching) {
                Sprite potentialSprite = GameSet.getInstance().getMarble(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
                if (potentialSprite != null) {
                    currentSprite = potentialSprite;
                }
                if (currentSprite != null) {
                    currentSprite.setCenter(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
                    wasTouched = true;
                    Gdx.app.log("Click Listener", "Turn ended");
                }
            }
            if(Gdx.input.isTouched() != true && wasTouched == true){
                yourTurn = false;
            }
        }
         */

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

            if (lastDirection != Directions.NOTSET && !selectedSprites.isEmpty()) {
                makeMove();
            } else {
                selectMarbleIfTouched();
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
//            playerTransition((currentPlayer + 1) % NUMBER_PLAYERS == 0 ? "White's" : "Black's");

            unselectCompleteList();

            if (queries.isPushedOutOfBound()) {
                Sprite capturedMarble = selectedEnemySprites.get(selectedEnemySprites.size() - 1);
                GameSet.getInstance().removeMarble(capturedMarble);

                ArrayList<Sprite> deletionList = deletedSpritesLists.get(currentPlayer); //TODO show captured marbles
                deletionList.add(capturedMarble);
                if (currentPlayer == 1) {
                    capturedMarble.setCenter(780, 140 + (60 * (deletionList.size() - 1)));
                } else {
                    capturedMarble.setCenter(60, 580 - (60 * (deletionList.size() - 1)));
                }
            }

            lastDirection = Directions.NOTSET;
            nextPlayer();
        }
    }

    private void selectMarbleIfTouched() {
        //touch coordinates have to be translate to map coordinates
        Vector3 v = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0f);
        viewport.unproject(v);
        Sprite potentialSprite = GameSet.getInstance().getMarble(v.x, v.y); //returns null if no marble matches coordinates
        if (potentialSprite != null && GameSet.getInstance().getTeamIndex(potentialSprite) != currentPlayer) {
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
        sprite.setColor(Color.WHITE);
    }

    private void unselectCompleteList() {
        for (int i = 0; i < selectedSprites.size(); i++) {
            Sprite s = selectedSprites.get(i);
            if (s != null) {
                s.setColor(Color.WHITE);
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

    public int nextPlayer() {
        currentPlayer = (currentPlayer + 1) % NUMBER_PLAYERS;
        nextLabel.setText(currentPlayer == 0 ? "White" : "Black");
        return currentPlayer;
    }

    @Override
    public void show() {
        playerLabel();
        settingsButton();
        exitButton();

        music();
    }

    public void playerLabel() {
        //TODO proper style?
        nextLabel = FactoryHelper.CreateLabelWithText(currentPlayer == 0 ? "White" : "Black", 100, 60);
        stage.addActor(nextLabel);
        Actor label = stage.getActors().peek();
        label.setX(screenWidth - (label.getWidth() + 220));
        label.setY(screenHeight - (label.getHeight() + 60));
    }

    public void settingsButton() { //TODO copied code from SettingsDialog; make it not repetitive
        Skin skin = FactoryHelper.GetDefaultSkin();
        settingsButton = FactoryHelper.CreateImageButton(
                skin.get("settings-btn", ImageButton.ImageButtonStyle.class),
                150, 150, 0, 0); //TODO method without x/y parameters? want to make it width/height dependent

        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // TODO: Open settings overlay.
                Gdx.app.log("ClickListener", settingsButton.toString() + " clicked");
                Skin uiSkin = new Skin(Gdx.files.internal(GameConstants.CUSTOM_UI_JSON));
                SettingsDialog settingsDialog = new SettingsDialog("", uiSkin);
                settingsDialog.show(stage);
            }
        });

        stage.addActor(settingsButton);
        Actor button = stage.getActors().peek();
        button.setX(screenWidth - (button.getWidth() + 20));
        button.setY(screenHeight - (button.getHeight() + 20));

        // TODO updating setting changes
    }

    public void exitButton() {
    }

    public void music() {
        bgMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds\\background.wav"));
        bgMusic.play();
        bgMusic.setLooping(true);
        bgMusic.setVolume(settings.getFloat("bgMusicVolumeFactor", 1f));
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
        //TODO you get more murbles if you close the up and start it up again
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
        blackBall.dispose();
        whiteBall.dispose();
    }
}