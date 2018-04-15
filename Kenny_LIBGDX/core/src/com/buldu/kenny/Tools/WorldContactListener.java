package com.buldu.kenny.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Timer;
import com.buldu.kenny.Kenny;
import com.buldu.kenny.Sprites.Ken;
import com.buldu.kenny.Sprites.TileObjects.InteractiveTileObject;
import com.buldu.kenny.Screens.PlayScreen;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if(fixA.getUserData() == "foot" || fixB.getUserData() == "foot"){
            Fixture foot = fixA.getUserData() == "foot" ? fixA : fixB;
            Fixture object = foot == fixA ? fixB : fixA;
            if(object.getUserData() != null && InteractiveTileObject.class.isAssignableFrom(object.getUserData().getClass())){
                PlayScreen.music.stop();
                PlayScreen.sound.play();
                PlayScreen.isKilled = true;
                Timer.schedule(new Timer.Task(){
                    @Override
                    public void run() {
                        Gdx.app.exit();
                    }
                }, 4);
            }
        }
    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
