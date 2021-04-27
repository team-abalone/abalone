package com.teamabalone.abalone;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.awt.DisplayMode;

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

    public Abalone(GameImpl game) {
        this.game = game;
        batch = game.getBatch();

        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        tiledMap = new TmxMapLoader().load("abalone_map.tmx"); //set file paths accordingly
        tiledMapRenderer = new HexagonalTiledMapRenderer(tiledMap);

//        for (int i = 1; i < 60; i++) {
//            System.out.println("----" + i);
//            System.out.println(tiledMap.getTileSets().getTile(i).getOffsetX());
//            System.out.println(tiledMap.getTileSets().getTile(i).getOffsetY());
//        }


        System.out.println(tiledMap.getProperties().get("tilewidth", Integer.class));
        System.out.println(tiledMap.getProperties().get("tileheight", Integer.class));
        System.out.println(tiledMap.getProperties().get("width", Integer.class));
        System.out.println(tiledMap.getProperties().get("height", Integer.class));


        OrthographicCamera camera = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera); //sets worldWidth and worldHeight

        camera.setToOrtho(false, 0, 0); //centers camera projection at width/2 and height/2
        camera.zoom = 1;//0.5f;

//        camera.zoom = 1 + (float) Gdx.graphics.getWidth() / 2088; //Weiter durchtesten. TODO gleiche größe auf allen Geräten
        //TODO Warum zentriert das?
//        viewport.getCamera().translate(50,50, 0);

        blackBall = new Texture("ball.png");
        whiteBall = new Texture("ball_white.png");


        float[] positionsWhite = {
                //707, 908,
                0, 0,
                0, Gdx.graphics.getHeight(),
                Gdx.graphics.getWidth(), 0,
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
                //835, 908,
               //963, 908,
                //1091, 908,
                1219, 908,
                1155, 796,
                1283, 796,
                1027, 796,
                899, 796,
                771, 796,
                643, 796,
                835, 684,
                963, 684,
                Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f
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

        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false); //center option??
        tiledMapRenderer.setView((OrthographicCamera) viewport.getCamera()); //batch.setProjectionMatrix(viewport.getCamera().combined); is called here
        tiledMapRenderer.render();


        batch.begin();

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

            //debugging shows coordinates
            System.out.println("sx: " + Gdx.input.getX());
            System.out.println("sx: " + Gdx.input.getY());
            System.out.println("vx: " + viewport.getCamera().position.x);
            System.out.println("vx: " + viewport.getCamera().position.y);
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

    }

    @Override
    public void show() {

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