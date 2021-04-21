package com.teamabalone.abalone;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

public class Abalone extends ApplicationAdapter {
    SpriteBatch batch;
    Texture img;

    TiledMap tiledMap;
    HexagonalTiledMapRenderer tiledMapRenderer;
    FitViewport viewport;

    Texture blackBall;
    Texture whiteBall;

    //branch

    @Override
    public void create() {
		batch = new SpriteBatch();
        tiledMap = new TmxMapLoader().load("abalone_map.tmx"); //set file paths accordingly
        tiledMapRenderer = new HexagonalTiledMapRenderer(tiledMap);
        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, 288, 288);
        viewport = new FitViewport(Gdx.graphics.getWidth()/4, Gdx.graphics.getHeight()/4, camera);
        camera.zoom += (float) 1.0;
        viewport.getCamera().translate(280,230,0);

        blackBall = new Texture("ball.png");
        whiteBall = new Texture("ball_white.png");

    }

    @Override
    public void render() {
		Gdx.gl.glClearColor(120/255f,120/255f,120/255f,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        //viewport.getCamera().translate(0.1f, 0, 0); //potential movement
        tiledMapRenderer.setView((OrthographicCamera) viewport.getCamera());
        tiledMapRenderer.render();

        batch.begin();
        //Black Balls
        //1 Zeile
        batch.draw(blackBall,707,18);
        batch.draw(blackBall,835,18);
        batch.draw(blackBall,963,18);
        batch.draw(blackBall,1091,18);
        batch.draw(blackBall,1219,18);
        batch.draw(blackBall,1219,18);
        //2 Zeile
        batch.draw(blackBall,1155,130);
        batch.draw(blackBall,1283,130);
        batch.draw(blackBall,1027,130);
        batch.draw(blackBall,899,130);
        batch.draw(blackBall,771,130);
        batch.draw(blackBall,643,130);
        //3 Zeile
        batch.draw(blackBall,835,242);
        batch.draw(blackBall,963,242);
        batch.draw(blackBall,1091,242);

        //White Balls
        //1 Zeile
        batch.draw(whiteBall,707,908);
        batch.draw(whiteBall,835,908);
        batch.draw(whiteBall,963,908);
        batch.draw(whiteBall,1091,908);
        batch.draw(whiteBall,1219,908);
        batch.draw(whiteBall,1219,908);
        //2 Zeile
        batch.draw(whiteBall,1155,796);
        batch.draw(whiteBall,1283,796);
        batch.draw(whiteBall,1027,796);
        batch.draw(whiteBall,899,796);
        batch.draw(whiteBall,771,796);
        batch.draw(whiteBall,643,796);
        //3 Zeile
        batch.draw(whiteBall,835,684);
        batch.draw(whiteBall,963,684);
        batch.draw(whiteBall,1091,684);

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        blackBall.dispose();
        whiteBall.dispose();
    }

}


//rendering tile map: https://www.youtube.com/watch?v=0RGdjnHtpXg