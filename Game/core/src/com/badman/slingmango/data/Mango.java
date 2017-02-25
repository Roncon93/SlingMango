package com.badman.slingmango.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by roncon on 2/25/17.
 */

public class Mango extends Fruit
{
    public Mango(World world) {
        super(new Texture(Gdx.files.internal("mango.png")), world);
    }
}
