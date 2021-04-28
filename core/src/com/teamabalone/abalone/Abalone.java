package com.teamabalone.abalone;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.files.FileHandle;

public class Abalone implements Screen {
    final GameImpl game;
    SpriteBatch batch;

    TiledMap tiledMap;
    HexagonalTiledMapRenderer tiledMapRenderer;
    FitViewport viewport;

    Texture blackBall;
    Texture whiteBall;

    MarbleSet whiteMarbleSet;
    MarbleSet blackMarbleSet;
    Sprite currentSprite;

    float mapWidth = 15 * 64;
    float mapHeight = 4.5f * 76;
    float screenWidth;
    float screenHeight;

    Stage stage;

    public Abalone(GameImpl game) {

//        MapCreater.createMap();
        //Gdx.files.local("create_map.tmx");

        this.game = game;
        batch = game.getBatch();

        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        tiledMap = new TmxMapLoader().load("abalone_map.tmx"); //set file paths accordingly
        TiledMapTileLayer tileLayer = (TiledMapTileLayer) tiledMap.getLayers().get(0); //instantiate tiled layer
//        tiledMap.getTileSets().getTile(7).setOffsetX(50);
        tiledMapRenderer = new HexagonalTiledMapRenderer(tiledMap); //additional parameter unitScale possible
//        tileLayer.getCell(7,7).getTile().getTextureRegion();

//        tiledMapRenderer.getViewBounds()

        System.out.println(tiledMap.getProperties().get("tilewidth", Integer.class));
        System.out.println(tiledMap.getProperties().get("tileheight", Integer.class));
        System.out.println(tiledMap.getProperties().get("width", Integer.class));
        System.out.println(tiledMap.getProperties().get("height", Integer.class));


        OrthographicCamera camera = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera); //sets worldWidth and worldHeight

        stage = new Stage(viewport, batch);
        Gdx.input.setInputProcessor(stage);

        //height needs to take overlap in account (55,5 + 18,5 = 74)
        camera.setToOrtho(false, tileLayer.getWidth() * tileLayer.getTileWidth(), 55.5f * (tileLayer.getHeight() - 1) + tileLayer.getTileHeight()); //centers camera projection at width/2 and height/2
        camera.zoom = 0.5f;//0.5f;

//        camera.translate(tileLayer.getWidth() * tileLayer.getTileWidth(), tileLayer.getHeight() * tileLayer.getTileHeight());


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
                200, 200,
//                1219, 908,
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
        whiteMarbleSet = gameSet.register(viewport, whiteBall, positionsWhite);
        blackMarbleSet = gameSet.register(viewport, blackBall, positionsBlack);
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

//                Vector3 v = new Vector3(m.getMarble(i).getX(), m.getMarble(i).getY(), 0f);
//                viewport.unproject(v);
//                System.out.println(m.getMarble(i).getX() + " +++++ " + m.getMarble(i).getY());
//                System.out.println(v.x + " ---- " + v.y);
//                m.getMarble(i).setX(v.x);
//                m.getMarble(i).setY(v.y);

                m.getMarble(i).setScale((Gdx.graphics.getWidth() - mapWidth) / screenWidth, (Gdx.graphics.getWidth() - mapWidth) / screenWidth);
                m.getMarble(i).draw(batch);
            }
        }

        batch.end();


        boolean firstFingerTouching = Gdx.input.isTouched(0);
        boolean secondFingerTouching = Gdx.input.isTouched(1);
        boolean thirdFingerTouching = Gdx.input.isTouched(2);

        //move
        if (firstFingerTouching && !secondFingerTouching && !thirdFingerTouching) {
            Vector3 v = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0f);
            viewport.unproject(v);
            Sprite potentialSprite = GameSet.getInstance().getMarble(v.x, v.y);
            if (potentialSprite != null) {
                currentSprite = potentialSprite;
            }
            if (currentSprite != null) {
                currentSprite.setCenter(v.x, v.y);
            }

            //debugging shows coordinates
//            System.out.println("sx: " + Gdx.input.getX());
//            System.out.println("sy: " + Gdx.input.getY());
//            System.out.println("vx: " + viewport.getCamera().position.x);
//            System.out.println("vy: " + viewport.getCamera().position.y);

                    System.out.println(Gdx.input.getX() + " +++++ " + Gdx.input.getY());
                    System.out.println(v.x + " ---- " + v.y);

        }

        //zoom
        if (firstFingerTouching && secondFingerTouching && !thirdFingerTouching) {
            boolean zeroLeftFinger = Gdx.input.getX(0) < Gdx.input.getX(1); //set left/right finger to make touch sequence irrelevant
            int indexLeftFinger = zeroLeftFinger ? 0 : 1;
            int indexRightFinger = !zeroLeftFinger ? 0 : 1;
            if ((Gdx.input.getDeltaX(indexLeftFinger) < 0 && Gdx.input.getDeltaX(indexRightFinger) > 0)) { //delta left finger neg. -> zoom in (make zoom smaller)
                ((OrthographicCamera) viewport.getCamera()).zoom -= 0.02;

//                for (MarbleSet m : GameSet.getInstance().getMarbleSets()) {
//                    for (int i = 0; i < m.size(); i++) {
//                        m.getMarble(i).setSize(m.getMarble(i).getWidth() + 2, m.getMarble(i).getHeight() + 2);
//                    }
//                }
            }
            if ((Gdx.input.getDeltaX(indexLeftFinger) > 0 && Gdx.input.getDeltaX(indexRightFinger) < 0)) { //zoom out
                ((OrthographicCamera) viewport.getCamera()).zoom += 0.02;

//                for (MarbleSet m : GameSet.getInstance().getMarbleSets()) {
//                    for (int i = 0; i < m.size(); i++) {
//                        Sprite marbel = m.getMarble(i);
//                        m.getMarble(i).setSize(m.getMarble(i).getWidth() - 2, m.getMarble(i).getHeight() - 2);
//
//                    }
//                }
            }

        }

        //translate
        if (firstFingerTouching && secondFingerTouching && thirdFingerTouching) {
            viewport.getCamera().translate(-Gdx.input.getDeltaX(1), Gdx.input.getDeltaY(1), 0);

            //makes sprites stay on map position
//            for (MarbleSet m : GameSet.getInstance().getMarbleSets()) {
//                for (int i = 0; i < m.size(); i++) {
//                    m.getMarble(i).setX(m.getMarble(i).getX() + Gdx.input.getDeltaX(1) / ((OrthographicCamera) viewport.getCamera()).zoom);
//                    m.getMarble(i).setY(m.getMarble(i).getY() - Gdx.input.getDeltaY(1) / ((OrthographicCamera) viewport.getCamera()).zoom);
//                }
//            }
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
        tiledMap.dispose();
        batch.dispose();
        blackBall.dispose();
        whiteBall.dispose();
    }

}


//rendering tile map: https://www.youtube.com/watch?v=0RGdjnHtpXg