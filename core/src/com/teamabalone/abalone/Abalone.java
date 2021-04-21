package com.teamabalone.abalone;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Abalone extends ApplicationAdapter {
    SpriteBatch batch;
    Texture img;

    TiledMap tiledMap;
    HexagonalTiledMapRenderer tiledMapRenderer;
    FitViewport viewport;

    Texture blackBall;
    Texture whiteBall;

    MarbleSet whiteMarbleSet;
    MarbleSet blackMarbleSet;
    Sprite sprite;
    float start = 507;

    //branch

    @Override
    public void create() {
        batch = new SpriteBatch();
        tiledMap = new TmxMapLoader().load("abalone_map.tmx"); //set file paths accordingly
        tiledMapRenderer = new HexagonalTiledMapRenderer(tiledMap);

        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, 288, 288);

        viewport = new FitViewport(Gdx.graphics.getWidth() / 4f, Gdx.graphics.getHeight() / 4f, camera);
        camera.zoom += (float) 1.0;
        viewport.getCamera().translate(280, 230, 0);

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
                1091, 684,
                start, 1000
        };

        float[] positionsBlack ={
                707, 18,
                835, 1,
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

        StartPosition startPositionBlack = new StartPosition(blackBall, positionsBlack);
        blackMarbleSet = new MarbleSet(startPositionBlack.getSprites());

        StartPosition startPositionWhite = new StartPosition(whiteBall, positionsWhite);
        whiteMarbleSet = new MarbleSet(startPositionWhite.getSprites());
        sprite = whiteMarbleSet.getMarble(14);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(120 / 255f, 120 / 255f, 120 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        //viewport.getCamera().translate(0.1f, 0, 0); //potential movement
        tiledMapRenderer.setView((OrthographicCamera) viewport.getCamera());
        tiledMapRenderer.render();

        sprite.setPosition(start++, 1000);

        batch.begin();

        sprite.draw(batch);

        for (int i = 0; i < blackMarbleSet.size(); i++) {
            blackMarbleSet.getMarble(i).draw(batch);
        }

        for (int i = 0; i < whiteMarbleSet.size(); i++) {
            whiteMarbleSet.getMarble(i).draw(batch);
        }

        batch.end();


        //sprite.setPosition(Gdx.input.getX(), Gdx.input.getY());
    }

    @Override
    public void dispose() {
        batch.dispose();
        blackBall.dispose();
        whiteBall.dispose();
    }

}


//rendering tile map: https://www.youtube.com/watch?v=0RGdjnHtpXg