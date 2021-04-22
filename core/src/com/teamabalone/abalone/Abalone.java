package com.teamabalone.abalone;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
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

public class Abalone extends ApplicationAdapter {
    SpriteBatch batch;

    TiledMap tiledMap;
    HexagonalTiledMapRenderer tiledMapRenderer;
    FitViewport viewport;

    Texture blackBall;
    Texture whiteBall;

    MarbleSet whiteMarbleSet;
    MarbleSet blackMarbleSet;
    Sprite currentSprite;

    @Override
    public void create() {
        batch = new SpriteBatch();
        tiledMap = new TmxMapLoader().load("abalone_map.tmx"); //set file paths accordingly
        tiledMapRenderer = new HexagonalTiledMapRenderer(tiledMap);

        OrthographicCamera camera = new OrthographicCamera();
        //TODO Warum 288?
        camera.setToOrtho(false, 288, 288);

        viewport = new FitViewport(Gdx.graphics.getWidth() / 4f, Gdx.graphics.getHeight() / 4f, camera);

        System.out.println(Gdx.graphics.getWidth());

        camera.zoom += (float) Gdx.graphics.getWidth() / 2088; //Weiter durchtesten. TODO gleiche größe auf allen Geräten
        //TODO Warum zentriert das?
        viewport.getCamera().translate(280, 230, 0);
        viewport.setScaling(Scaling.fillY);

        blackBall = new Texture("ball.png");
        whiteBall = new Texture("ball_white.png");

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
                1091, 684
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
    public void render() {
        Gdx.gl.glClearColor(120 / 255f, 120 / 255f, 120 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        tiledMapRenderer.setView((OrthographicCamera) viewport.getCamera());
        tiledMapRenderer.render();

        batch.begin();

        //TODO Ball resize! & Koordinaten rework!

        for (MarbleSet m : GameSet.getInstance().getMarbleSets()) {
            for (int i = 0; i < m.size(); i++) {
                //TODO Marble scaling Aufpassen:Minimal=0

                //Dividiert durch 2088 weil das Ausgangshandy diese Breite hat und somit wurde alles gescaled.
                m.getMarble(i).setScale(Gdx.graphics.getWidth() / 2088f, Gdx.graphics.getWidth() / 2088f);
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
                currentSprite.setPosition(Gdx.input.getX() - 50, Gdx.graphics.getHeight() - Gdx.input.getY() - 50);
            }
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
    public void dispose() {
        batch.dispose();
        blackBall.dispose();
        whiteBall.dispose();
    }

}


//rendering tile map: https://www.youtube.com/watch?v=0RGdjnHtpXg