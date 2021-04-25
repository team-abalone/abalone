package com.teamabalone.abalone;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.teamabalone.abalone.Dialogs.TurnAnnouncerTwo;
import com.teamabalone.abalone.Helpers.FactoryHelper;

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
    Sprite currentSprite;

    float mapWidth = 15 * 64;
    float mapHeight = 4.5f * 76;
    float screenWidth;
    float screenHeight;

    //changes
    boolean yourTurn = true;
    TurnAnnouncerTwo nextPlayer = new TurnAnnouncerTwo("Next Players Turn", FactoryHelper.GetDefaultSkin());

    private Stage stage;
    private TextButton next;
    //

    public Abalone(GameImpl game){
        this.game = game;
        batch = game.getBatch();

        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        tiledMap = new TmxMapLoader().load("abalone_map.tmx"); //set file paths accordingly
        tiledMapRenderer = new HexagonalTiledMapRenderer(tiledMap);

        OrthographicCamera camera = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);

        camera.setToOrtho(false, Gdx.graphics.getWidth() - mapWidth, Gdx.graphics.getHeight() - mapHeight);
        camera.zoom = 0.5f;

//        camera.zoom = 1 + (float) Gdx.graphics.getWidth() / 2088; //Weiter durchtesten. TODO gleiche größe auf allen Geräten
        //TODO Warum zentriert das?
//        viewport.getCamera().translate(50,50, 0);

        blackBall = new Texture("ball.png");
        whiteBall = new Texture("ball_white.png");

        textureWidth = blackBall.getWidth();
        textureHeight = blackBall.getHeight();

        float[] positionsWhite = {
                707, 908,
                835, 908,
                963, 908,
                1091, 908,
                1219, 908,
                1155, 796,
                1283, 796,
                1027, 796,
                899, 796,
                771, 796,
                643, 796,
                835, 684,
                963, 684,
                Gdx.graphics.getWidth() / 2f - textureWidth / 2f, Gdx.graphics.getHeight() / 2f - textureHeight / 2f //showing center
//                1091, 684
        };

        float[] positionsBlack = {
                707, 18,
                835, 18,
                963, 18,
                1091, 18,
                1219, 18,
                1155, 130,
                1283, 130,
                1027, 130,
                899, 130,
                771, 130,
                643, 130,
                835, 242,
                963, 242,
                1091, 242
        };

        GameSet gameSet = GameSet.getInstance();
        whiteMarbleSet = gameSet.register(whiteBall, positionsWhite);
        blackMarbleSet = gameSet.register(blackBall, positionsBlack);
    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(120 / 255f, 120 / 255f, 120 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        tiledMapRenderer.setView((OrthographicCamera) viewport.getCamera());
        tiledMapRenderer.render();

        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();
        //changes
        stage.act();
        stage.draw();
        //

        //TODO Ball resize! & Koordinaten rework!

        for (MarbleSet m : GameSet.getInstance().getMarbleSets()) {
            for (int i = 0; i < m.size(); i++) {
                //TODO Marble scaling Aufpassen:Minimal=0

                //Dividiert durch 2088 weil das Ausgangshandy diese Breite hat und somit wurde alles gescaled.
                //-mapWidth wegen setProjectionMatrix und screenWidth handyspezifisch (?)
                m.getMarble(i).setScale((Gdx.graphics.getWidth() - mapWidth) / screenWidth, (Gdx.graphics.getWidth() - mapWidth) / screenWidth);
                m.getMarble(i).draw(batch);
            }
        }

        batch.end();


        boolean firstFingerTouching = Gdx.input.isTouched(0);
        boolean secondFingerTouching = Gdx.input.isTouched(1);
        boolean thirdFingerTouching = Gdx.input.isTouched(2);

        if (firstFingerTouching && !secondFingerTouching && !thirdFingerTouching) {
            Sprite potentialSprite = GameSet.getInstance().getMarble(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
            if (potentialSprite != null) {
                currentSprite = potentialSprite;
            }
            if (currentSprite != null) {
                currentSprite.setCenter(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
            }
            yourTurn = false;
            Gdx.app.log("Marble Movement","Turn ended");
        }

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

        if (firstFingerTouching && secondFingerTouching && thirdFingerTouching) {
            viewport.getCamera().translate(-Gdx.input.getDeltaX(1), Gdx.input.getDeltaY(1), 0);
        }

        //debugging shows coordinates
        /*
        System.out.println("sx: " + Gdx.input.getX());
        System.out.println("sx: " + Gdx.input.getY());
        System.out.println("vx: " + viewport.getCamera().position.x);
        System.out.println("vx: " + viewport.getCamera().position.y);
         */
    }

    @Override
    public void show() {
        // changes
        Table buttonTable = FactoryHelper.CreateTable(
                15,
                15,
                Gdx.graphics.getWidth() - 250,
                Gdx.graphics.getHeight() -100);
        next = FactoryHelper.CreateButtonWithText("Next Player");
        next.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, float x, float y) {
                Gdx.app.log("ClickListener", next.toString() + " clicked");
                nextPlayer.show(stage);
                final Timer t = new Timer();
                t.schedule(new TimerTask() {
                    public void run() {
                        nextPlayer.hide();
                        t.cancel();
                    }}, 1000);//time in milliseconds
            }
        });

        buttonTable.row().fillX().expandX();
        buttonTable.add(next);

        stage = new Stage(new ScreenViewport());
        stage.addActor(buttonTable);
        Gdx.input.setInputProcessor(stage);
        //

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
        batch.dispose();
        blackBall.dispose();
        whiteBall.dispose();
    }

}


//rendering tile map: https://www.youtube.com/watch?v=0RGdjnHtpXg