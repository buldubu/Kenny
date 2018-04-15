package com.buldu.kenny.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.buldu.kenny.Kenny;
import com.buldu.kenny.Scenes.Hud;
import com.buldu.kenny.Sprites.Ken;
import com.buldu.kenny.Sprites.Stan;
import com.buldu.kenny.Sprites.Wendy;
import com.buldu.kenny.Tools.B2WorldCreator;
import com.buldu.kenny.Tools.Controller;
import com.buldu.kenny.Tools.WorldContactListener;


public class PlayScreen implements Screen {
    private Kenny game;
    private Controller controller;
    private TextureAtlas atlas;
    private OrthographicCamera gamecam;
    private Viewport gamePort;
    private Hud hud;
    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private World world;
    private Box2DDebugRenderer b2dr;
    private Ken player;
    private Stan stan;
    private Wendy wendy;
    private B2WorldCreator creator;
    public static Sound sound = Gdx.audio.newSound(Gdx.files.internal("killedkenny.mp3"));
    public static Music music = Gdx.audio.newMusic(Gdx.files.internal("what.mp3"));
    public static boolean isKilled = false;


    public PlayScreen(Kenny game){
        atlas = new TextureAtlas("Kenny.pack");
        this.game = game;
        gamecam = new OrthographicCamera();
        gamePort = new FitViewport(Kenny.V_WIDTH /Kenny.PPM,Kenny.V_HEIGHT /Kenny.PPM,gamecam);
        hud = new Hud(game.batch);
        maploader = new TmxMapLoader();
        map = maploader.load("1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / Kenny.PPM);
        gamecam.position.set(gamePort.getWorldWidth() / 2  , gamePort.getWorldHeight() / 2, 0);
        world = new World(new Vector2(0,-10),true);
        b2dr = new Box2DDebugRenderer();
        creator = new B2WorldCreator(this);
        player = new Ken(world, this);
        stan = new Stan(world,this);
        wendy = new Wendy(world,this);
        world.setContactListener(new WorldContactListener());
        controller = new Controller(game.batch);
        music.play();

    }

    public TextureAtlas getAtlas(){
        return atlas;
    }

    @Override
    public void show() {

    }
    public void handleInput(float dt){
        /*if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
            player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2)
            player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2)
            player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
        if (Gdx.input.justTouched())
            player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);
            */
        if(!isKilled) {
            if (controller.isRightPressed())
                player.b2body.setLinearVelocity(new Vector2(1.5f, player.b2body.getLinearVelocity().y));
            else if (controller.isLeftPressed())
                player.b2body.setLinearVelocity(new Vector2(-1.5f, player.b2body.getLinearVelocity().y));
            else
                player.b2body.setLinearVelocity(new Vector2(0, player.b2body.getLinearVelocity().y));
            if (controller.isUpPressed() && player.b2body.getLinearVelocity().y == 0)
                player.b2body.applyLinearImpulse(new Vector2(0, 5f), player.b2body.getWorldCenter(), true);
        }

    }

    public void update(float dt){
        handleInput(dt);
        world.step(1/60f,6,2);
        player.update(dt);
        stan.update(dt);
        wendy.update(dt);
        gamecam.position.x = player.b2body.getPosition().x;
        gamecam.update();
        renderer.setView(gamecam);
        if(!music.isPlaying() && !isKilled)
            music.play();
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();
        b2dr.render(world, gamecam.combined);
        /*
        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        player.draw(game.batch);
        game.batch.end();
        */

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
        controller.draw();
        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        stan.draw(game.batch);
        wendy.draw(game.batch);
        player.draw(game.batch);
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width,height);
        controller.resize(width,height);

    }

    public TiledMap getMap(){
        return map;
    }

    public World getWorld(){
        return world;
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
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }
}
