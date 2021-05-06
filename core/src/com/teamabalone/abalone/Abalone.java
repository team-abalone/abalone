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
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.teamabalone.abalone.Dialogs.TurnAnnouncerTwo;
import com.teamabalone.abalone.Helpers.FactoryHelper;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Abalone implements Screen {
    final GameImpl game;
    SpriteBatch batch;

    TiledMap tiledMap;
    HexagonalTiledMapRenderer tiledMapRenderer;
    FitViewport viewport;

    Texture background;
    Texture blackBall;
    Texture whiteBall;

    MarbleSet whiteMarbleSet;
    MarbleSet blackMarbleSet;

    float screenWidth;
    float screenHeight;

    int mapSize;

    //changes
    Music bgMusic;
    Preferences settings;
    boolean yourTurn = true;
    boolean wasTouched = false;
    TurnAnnouncerTwo nextPlayerCard;

    private Stage stage;
    private TextButton next;

    SelectionList<Sprite> selectedSprites = new SelectionList<>(3);

    boolean justTouched = false; //makes one touch only one operation (-> select)
    float firstTouchX;
    float firstTouchY;
    float lastTouchX;
    float lastTouchY;

    enum Direction {RIGHT, RIGHTUP, LEFTUP, LEFT, LEFTDOWN, RIGHTDOWN, NOTSET} //index starts with 0 (?)

    Direction lastDirection = Direction.NOTSET;
    int swipeSensitivity = 40;

    private final float boardWidth;
    private final float boardHeight;


    public Abalone(GameImpl game) {
        this.game = game;
        settings = Gdx.app.getPreferences("UserSettings");
        batch = game.getBatch();

        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        mapSize = 5; //make only setting valid value possible?
        tiledMap = new TmxMapLoader().load("abalone_map" + mapSize + ".tmx"); //set file paths accordingly
        TiledMapTileLayer tileLayer = (TiledMapTileLayer) tiledMap.getLayers().get(0); //instantiate tiled layer
        tiledMapRenderer = new HexagonalTiledMapRenderer(tiledMap); //additional parameter unitScale possible

        OrthographicCamera camera = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        //not attaching stage, so it moves with screen
        stage = new Stage();

        boardWidth = tileLayer.getWidth() * tileLayer.getTileWidth();
        //height needs to take overlap in account (55,5 + 18,5 = 74)
        boardHeight = 55.5f * (tileLayer.getHeight() - 1) + tileLayer.getTileHeight();

        camera.setToOrtho(false, boardWidth, boardHeight); //centers camera projection at width/2 and height/2
        camera.zoom = 0.5f;//0.5f;

        Board board = Board.getInstance(viewport, tileLayer, mapSize); //call after camera has been set!
        board.get(0);

        background = new Texture("boards/"+settings.getString("boardSkin", "Laminat.png"));
        blackBall = new Texture("marbles/"+settings.getString("marbleSkin", "ball.png"));
        whiteBall = new Texture("marbles/ball_white.png");

        float[] positionsWhite = {
                board.get(1).x, board.get(1).y,
                board.get(2).x, board.get(2).y,
                board.get(3).x, board.get(3).y,
                board.get(4).x, board.get(4).y,
                board.get(5).x, board.get(5).y,
//                board.get(6).x, board.get(6).y,
//                board.get(7).x, board.get(7).y,
//                board.get(8).x, board.get(8).y,
//                board.get(9).x, board.get(9).y,
//                board.get(10).x, board.get(10).y,
//                board.get(11).x, board.get(11).y,
//                board.get(14).x, board.get(14).y,
//                board.get(15).x, board.get(15).y,
//                board.get(16).x, board.get(16).y
        };

        float[] positionsBlack = {
//                board.get(61).x, board.get(61).y,
//                board.get(60).x, board.get(60).y,
//                board.get(59).x, board.get(59).y,
//                board.get(58).x, board.get(58).y,
//                board.get(57).x, board.get(57).y,
//                board.get(56).x, board.get(56).y,
//                board.get(55).x, board.get(55).y,
//                board.get(54).x, board.get(54).y,
//                board.get(53).x, board.get(53).y,
//                board.get(52).x, board.get(52).y,
//                board.get(51).x, board.get(51).y,
//                board.get(48).x, board.get(48).y,
//                board.get(47).x, board.get(47).y,
//                board.get(46).x, board.get(46).y
        };

        GameSet gameSet = GameSet.getInstance();
        blackMarbleSet = gameSet.register(viewport, blackBall, positionsBlack);
        whiteMarbleSet = gameSet.register(viewport, whiteBall, positionsWhite);
    }

    private Vector2 toMapCoordinates(float x, float y) {
        Vector3 v = new Vector3(x, y, 0f);
        viewport.unproject(v);
        return new Vector2(Math.round(v.x), Math.round(v.y));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(139 / 255f, 58 / 255f, 58 / 255f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false); //would center the camera to the center of the world

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

        batch.end();
        //changes
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

            lastDirection = calculateDirection(firstTouchX, firstTouchY, lastTouchX, lastTouchY); //only sets direction if sensitivity is exceeded

            if (lastDirection != Direction.NOTSET && !selectedSprites.isEmpty()) {
                moveSelectedMarbles(); //move
                unselectList();
                lastDirection = Direction.NOTSET;
            } else {
                selectMarbleIfTouched(); //select
            }

            justTouched = false;
        }
    }

    private void selectMarbleIfTouched() {
        //touch coordinates have to be translate to map coordinates
        Vector3 v = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0f);
        viewport.unproject(v);
        Sprite potentialSprite = GameSet.getInstance().getMarble(v.x, v.y); //returns null if no marble matches coordinates

        if (potentialSprite != null) {
            boolean alreadySelected = !select(potentialSprite);
            if (alreadySelected) {
                unselect(potentialSprite);
            }
        } else {
            unselectList();
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

    private void unselectList() {
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

    private void moveSelectedMarbles() {
        for (int i = 0; i < selectedSprites.size(); i++) {
            selectedSprites.move(i, lastDirection);
        }
    }

    private Direction calculateDirection(float startX, float startY, float endX, float endY) {
        float adjacentLeg = endX - startX;
        float oppositeLeg = startY - endY; //screen coordinates: (0,0) left UPPER corner

        if (Math.abs(adjacentLeg) < swipeSensitivity && Math.abs(oppositeLeg) < swipeSensitivity) { //continue selection mode
            return Direction.NOTSET;
        }

        //to get 360°
        boolean olneg = oppositeLeg < 0;
        boolean alneg = adjacentLeg < 0;
        double offset = 0;
        if (alneg) {
            offset = 180;
        }
        if (!alneg && olneg) {
            offset = 360;
        }

        double degrees = Math.toDegrees(Math.atan(oppositeLeg / adjacentLeg)) + offset; //atan() returns only -pi/2 - pi/2 (circle half)
        int index = ((int) ((degrees) / 30)); //get sector
        Direction direction;

        switch (index) {
            case 0:
            case 11:
                direction = Direction.RIGHT;
                break;
            case 1:
            case 2:
                direction = Direction.RIGHTUP;
                break;
            case 3:
            case 4:
                direction = Direction.LEFTUP;
                break;
            case 5:
            case 6:
                direction = Direction.LEFT;
                break;
            case 7:
            case 8:
                direction = Direction.LEFTDOWN;
                break;
            case 9:
            case 10:
                direction = Direction.RIGHTDOWN;
                break;
            default:
                direction = Direction.NOTSET;
        }

        return direction;
    }

    @Override
    public void show() {
        next = FactoryHelper.CreateButtonWithText("Next Player");
        next.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, float x, float y) {
                Gdx.app.log("ClickListener", next.toString() + " clicked");
                playerTransition("Opponent");
                simulatingOpponent();
            }
        });

        //Stage stage = new Stage(viewport, batch);
        stage.addActor(next);
        Actor button = stage.getActors().get(0);
        //set coordinates of actor/button
        button.setX(Gdx.graphics.getWidth() - button.getWidth() - 10);
        button.setY(Gdx.graphics.getHeight() - button.getHeight() - 10);

        Gdx.input.setInputProcessor(stage);
        bgMusic  = Gdx.audio.newMusic(Gdx.files.internal("sounds\\background.wav"));
        bgMusic.play();
        bgMusic.setLooping(true);
        bgMusic.setVolume(settings.getFloat("bgMusicVolumeFactor", 1f));
    }

    public void simulatingOpponent() {
        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                int randX = new Random().nextInt(15);
                Sprite potentialSprite = whiteMarbleSet.getMarble(randX);
                if (potentialSprite != null) {
                    selectedSprites.select(potentialSprite);
                }
//                if (currentSprite != null) {
//                    currentSprite.setCenter(new Random().nextInt((int) screenWidth), new Random().nextInt((int) screenHeight));
//                } TODO change to new selection mode
                wasTouched = false;
                yourTurn = true;
                t.cancel();
                playerTransition("Your");
            }
        }, 5000);//time in milliseconds
    }

    public void playerTransition(String sayWhichPlayerTransTo) {
        nextPlayerCard = new TurnAnnouncerTwo(sayWhichPlayerTransTo + " Turn", FactoryHelper.GetDefaultSkin());
        nextPlayerCard.show(stage);
        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                nextPlayerCard.hide();
                t.cancel();
            }
        }, 1000);//time in milliseconds
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
    public void dispose() {
        tiledMap.dispose();
        batch.dispose();
        blackBall.dispose();
        whiteBall.dispose();
        bgMusic.dispose();
    }

}


//rendering tile map: https://www.youtube.com/watch?v=0RGdjnHtpXg