package com.teamabalone.abalone;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TideMapLoader;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Abalone extends ApplicationAdapter {
    SpriteBatch batch;
    Texture img;

    TiledMap tiledMap;
    HexagonalTiledMapRenderer tiledMapRenderer;
    FitViewport viewport;

    //branch

    @Override
    public void create() {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
        tiledMap = new TmxMapLoader().load("abalone_map.tmx"); //set file paths accordingly
        tiledMapRenderer = new HexagonalTiledMapRenderer(tiledMap);
        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, 288, 288);
        viewport = new FitViewport(Gdx.graphics.getWidth()/4, Gdx.graphics.getHeight()/4, camera);
        camera.zoom += (float) 1.0;
        viewport.getCamera().translate(280,230,0);

    }

    @Override
    public void render() {
		Gdx.gl.glClearColor(0.9f,0.9f,0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.draw(img, 500, 300);
		batch.end();

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//        viewport.getCamera().translate(1, 0, 0); //potential movement
        tiledMapRenderer.setView((OrthographicCamera) viewport.getCamera());
        tiledMapRenderer.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
    }
}


//rendering tile map: https://www.youtube.com/watch?v=0RGdjnHtpXg