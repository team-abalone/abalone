package com.teamabalone.abalone;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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

    Texture blackBall;
    Texture whiteBall;
    float textureWidth;
    float textureHeight;

    MarbleSet whiteMarbleSet;
    MarbleSet blackMarbleSet;

    float screenWidth;
    float screenHeight;

    //changes
    boolean yourTurn = true;
    boolean wasTouched = false;
    TurnAnnouncerTwo nextPlayerCard;

    private Stage stage;
    private TextButton next;

    Sprite currentSprite;
    SelectionList<Sprite> selectedSprites = new SelectionList<>(3);

    boolean justTouched = false;

    public Abalone(GameImpl game) {
        this.game = game;
        batch = game.getBatch();

        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        tiledMap = new TmxMapLoader().load("abalone_map.tmx"); //set file paths accordingly
        TiledMapTileLayer tileLayer = (TiledMapTileLayer) tiledMap.getLayers().get(0); //instantiate tiled layer
        tiledMapRenderer = new HexagonalTiledMapRenderer(tiledMap); //additional parameter unitScale possible

        OrthographicCamera camera = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        //not attaching stage, so it moves with screen
        stage = new Stage();

        float boardWidth = tileLayer.getWidth() * tileLayer.getTileWidth();
        //height needs to take overlap in account (55,5 + 18,5 = 74)
        float boardHeight = 55.5f * (tileLayer.getHeight() - 1) + tileLayer.getTileHeight();

        camera.setToOrtho(false, boardWidth, boardHeight); //centers camera projection at width/2 and height/2
        camera.zoom = 0.5f;//0.5f;


        Board board = Board.getInstance(viewport, tileLayer); //call after camera has been set!
        board.get(0);

        blackBall = new Texture("ball.png");
        whiteBall = new Texture("ball_white.png");

        float[] positionsWhite = {
                board.get(1).x, board.get(1).y,
                board.get(2).x, board.get(2).y,
                board.get(3).x, board.get(3).y,
                board.get(4).x, board.get(4).y,
                board.get(5).x, board.get(5).y,
                board.get(6).x, board.get(6).y,
                board.get(7).x, board.get(7).y,
                board.get(8).x, board.get(8).y,
                board.get(9).x, board.get(9).y,
                board.get(10).x, board.get(10).y,
                board.get(11).x, board.get(11).y,
                board.get(14).x, board.get(14).y,
                board.get(15).x, board.get(15).y,
//                board.get(27).x, board.get(27).y,
//                board.get(30).x, board.get(30).y,
//                board.get(31).x, board.get(31).y,
//                board.get(32).x, board.get(32).y,
//                board.get(35).x, board.get(35).y,
                board.get(16).x, board.get(16).y
        };

        float[] positionsBlack = {
                board.get(61).x, board.get(61).y,
                board.get(60).x, board.get(60).y,
                board.get(59).x, board.get(59).y,
                board.get(58).x, board.get(58).y,
                board.get(57).x, board.get(57).y,
                board.get(56).x, board.get(56).y,
                board.get(55).x, board.get(55).y,
                board.get(54).x, board.get(54).y,
                board.get(53).x, board.get(53).y,
                board.get(52).x, board.get(52).y,
                board.get(51).x, board.get(51).y,
                board.get(48).x, board.get(48).y,
                board.get(47).x, board.get(47).y,
                board.get(46).x, board.get(46).y
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
        Gdx.gl.glClearColor(120 / 255f, 120 / 255f, 120 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false); //would center the camera to the center of the world
        tiledMapRenderer.setView((OrthographicCamera) viewport.getCamera()); //batch.setProjectionMatrix(viewport.getCamera().combined); is called here
        tiledMapRenderer.render();

        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();


        //TODO Ball resize! & Koordinaten rework!

        for (MarbleSet m : GameSet.getInstance().getMarbleSets()) {
            for (int i = 0; i < m.size(); i++) {
                //TODO Marble scaling Aufpassen:Minimal=0

                //Dividiert durch 2088 weil das Ausgangshandy diese Breite hat und somit wurde alles gescaled.
                //-mapWidth wegen setProjectionMatrix und screenWidth handyspezifisch (?)
                m.getMarble(i).setScale(0.5f); //old: (screenWidth - 15 * 64) / (screenWidth-100)
                m.getMarble(i).draw(batch);
            }
        }

        batch.end();
        //changes
        stage.act();
        stage.draw();
        //


        boolean firstFingerTouching = Gdx.input.isTouched(0);
        boolean secondFingerTouching = Gdx.input.isTouched(1);
        boolean thirdFingerTouching = Gdx.input.isTouched(2);

        //select
        if (firstFingerTouching && !secondFingerTouching && !thirdFingerTouching && !justTouched) {
            //touch coordinates have to be translate to map coordinates
            Vector3 v = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0f);
            viewport.unproject(v);
            Sprite potentialSprite = GameSet.getInstance().getMarble(v.x, v.y);

//            if (potentialSprite != null) {
//                currentSprite = potentialSprite;
//            }
//            if (currentSprite != null) {
//                currentSprite.setCenter(v.x, v.y);
//            }

            if (potentialSprite != null) {
                if (selectedSprites.select(potentialSprite)) { //toggle
                    potentialSprite.setColor(Color.GRAY);
                } else {
                    selectedSprites.unselect(potentialSprite);
                    potentialSprite.setColor(Color.WHITE);
                }
            } else {
                for (int i = 0; i < selectedSprites.size(); i++) {
                    Sprite s = selectedSprites.get(i);
                    if (s != null) {
                        s.setColor(Color.WHITE);
                    }
                }
                selectedSprites.unselectAll();
            }
//            if (currentSprite != null) {
//                currentSprite.setCenter(v.x, v.y);
//            }
        }

        //moving
        if (!selectedSprites.isEmpty() && (Gdx.input.getDeltaX() > 0.1 || Gdx.input.getDeltaY() > 0.1)) {
            //do moving marbles
        }

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

        //zoom
        if (firstFingerTouching && secondFingerTouching && !thirdFingerTouching) {
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

        //translate
        if (firstFingerTouching && secondFingerTouching && thirdFingerTouching) {
            viewport.getCamera().translate(-Gdx.input.getDeltaX(1), Gdx.input.getDeltaY(1), 0);
        }

        //makes one touch only one operation (-> select)
        if (!justTouched && firstFingerTouching) {
            justTouched = true;
        }
        if (justTouched && !firstFingerTouching) {
            justTouched = false;
        }

    }

    @Override
    public void show() {

        // changes

        /*Table buttonTable = FactoryHelper.CreateTable(
                15,
                15,
                mapWidth - 250,
                mapHeight -100);*/
        next = FactoryHelper.CreateButtonWithText("Next Player");

        next.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, float x, float y) {
                Gdx.app.log("ClickListener", next.toString() + " clicked");
                playerTransition("Opponent");
                simulatingOpponent();
            }
        });

        //buttonTable.row().fillX().expandX();
        //buttonTable.add(next);

        //Stage stage = new Stage(viewport, batch);
        stage.addActor(next);
        Actor button = stage.getActors().get(0);
        //set coordinates of actor/button
        button.setX(Gdx.graphics.getWidth() - button.getWidth() - 10);
        button.setY(Gdx.graphics.getHeight() - button.getHeight() - 10);

        Gdx.input.setInputProcessor(stage);

        //


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
//                }
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
    }

}


//rendering tile map: https://www.youtube.com/watch?v=0RGdjnHtpXg