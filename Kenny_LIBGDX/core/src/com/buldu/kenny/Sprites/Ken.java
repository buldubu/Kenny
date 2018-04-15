package com.buldu.kenny.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.buldu.kenny.Kenny;
import com.buldu.kenny.Screens.PlayScreen;

public class Ken extends Sprite {
    public enum State {STANDING, RUNNING, JUMPING, FALLING};
    public State currenState;
    public State previousState;
    public World world;
    public Body b2body;
    private TextureRegion kennyStand;
    private Animation <TextureRegion>kennyRun;
    private Animation <TextureRegion>kennyJump;
    private float stateTimer;
    private boolean runningRight;


    public Ken(World world, PlayScreen screen){
        super(screen.getAtlas().findRegion("kenny"));
        this.world = world;
        currenState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;
        Array<TextureRegion> frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(getTexture(),261,295,120,160));
        kennyRun = new Animation<TextureRegion>(0.1f,frames);
        kennyJump = new Animation<TextureRegion>(0.1f,frames);
        frames.clear();
        defineKen();
        kennyStand = new TextureRegion(getTexture(),381,295,120,160);
        setBounds(0,0,48/Kenny.PPM, 48/Kenny.PPM);
        setRegion(kennyStand);
    }
    public void update(float dt){
        setPosition(b2body.getPosition().x - getWidth() / 2,b2body.getPosition().y - getHeight() / 2 );
        setRegion(getFrame(dt));
    }

    public TextureRegion getFrame(float dt){
        currenState = getState();

        TextureRegion region;
        switch (currenState){
            case JUMPING:
                region = (TextureRegion) kennyJump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                //region = (TextureRegion) kennyJump.getKeyFrame(stateTimer, true);
                region = kennyStand;
                break;
            case FALLING:
            case STANDING:
            default:
                region = kennyStand;
                break;
        }
        if ((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
            region.flip(true, false);
            runningRight = false;
        }else if((b2body.getLinearVelocity().x > 0 || runningRight)&& region.isFlipX()){
            region.flip(true,false);
            runningRight = true;
        }
        stateTimer = currenState == previousState ? stateTimer + dt :0;
        previousState = currenState;
        return region;
    }

    public State getState(){
        if(b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING))
            return State.JUMPING;
        else if(b2body.getLinearVelocity().y < 0)
            return State.FALLING;
        else if(b2body.getLinearVelocity().x != 0)
            return State.RUNNING;
        else
            return State.STANDING;
    }

    public void defineKen(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(32/Kenny.PPM,32/Kenny.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(23/Kenny.PPM);

        fdef.shape = shape;
        b2body.createFixture(fdef);

        EdgeShape foot = new EdgeShape();
        foot.set(new Vector2(0/Kenny.PPM, -23/Kenny.PPM), new Vector2(0/Kenny.PPM, 23/Kenny.PPM));
        fdef.shape = foot;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData("foot");
    }
}
