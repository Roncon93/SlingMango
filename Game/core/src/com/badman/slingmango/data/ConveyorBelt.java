package com.badman.slingmango.data;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;

/**
 * Created by roncon on 2/25/17.
 */

public class ConveyorBelt extends Sprite implements ContactListener
{
    private final static float SPEED = -200.0f/10;
    private final static float WIDTH = 300.0f/10;
    private final static float HEIGHT = 20.0f/10;
    private final static float FRUIT_XOFFSET = 30.0f/10;
    private final static float FRUIT_YOFFSET = 30.0f/10;

    private Queue<Fruit> queuedFruits;
    private Array<Fruit> movingFruits;

    Fixture m_platform;

    public ConveyorBelt(World world, float x, float y)
    {
        world.setContactListener(this);

        queuedFruits = new Queue<Fruit>();
        movingFruits = new Array<Fruit>();

        BodyDef bd = new BodyDef();
        bd.position.set(x, y);
        Body body = world.createBody(bd);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(WIDTH, HEIGHT);

        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        fd.friction = 0.8f;
        m_platform = body.createFixture(fd);

        setPosition(x, y);
    }

    public void update(float delta)
    {

    }

    @Override
    public void draw(Batch batch)
    {
        //super.draw(batch);

        for (Fruit fruit : movingFruits) {
            fruit.draw(batch);
        }
    }

    public void addMango(World world)
    {
        Mango mango = new Mango(world);
        mango.setScale(0.3f, 0.3f);
        mango.body.setTransform(getX() + FRUIT_XOFFSET, getY() + FRUIT_YOFFSET, 0);

        movingFruits.add(mango);
    }

    public void preSolve (Contact contact, Manifold oldManifold) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if (fixtureA == m_platform || fixtureB == m_platform) {
            contact.setTangentSpeed(SPEED);
        }
    }

    @Override
    public void beginContact (Contact contact) {

    }

    @Override
    public void endContact (Contact contact) {
    }

    @Override
    public void postSolve (Contact contact, ContactImpulse impulse) {
    }
}
