package com.buldu.kenny.Sprites.TileObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Rectangle;
import com.buldu.kenny.Kenny;
import com.buldu.kenny.Scenes.Hud;
import com.buldu.kenny.Screens.PlayScreen;
import com.buldu.kenny.Sprites.Ken;


public class Lav extends InteractiveTileObject {
    public Lav(PlayScreen screen, MapObject object){
        super(screen, object);
        fixture.setUserData(this);
    }

    @Override
    public void onFoot(Ken kenny) {
        Gdx.app.log("2","a");

    }

}