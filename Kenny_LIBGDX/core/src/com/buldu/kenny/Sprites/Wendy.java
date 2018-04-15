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

public class Wendy extends Sprite{

    public World world;
    public Body b2body;
    private TextureRegion wendy;


    public Wendy(World world, PlayScreen screen){
        super(screen.getAtlas().findRegion("kenny"));
        this.world = world;
        defineWendy();
        wendy = new TextureRegion(getTexture(),0,0,261,383);
        setBounds(0,0,28/Kenny.PPM,37/Kenny.PPM);
        setRegion(wendy);
    }
    public void update(float dt){
        setPosition(b2body.getPosition().x - getWidth() / 2,b2body.getPosition().y - getHeight() / 2 );
        //setRegion(getFrame(dt));
    }

    public void defineWendy(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(100/Kenny.PPM,400/Kenny.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(14/Kenny.PPM);

        fdef.shape = shape;
        b2body.createFixture(fdef);
    }
}

