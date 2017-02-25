package com.badman.slingmango.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badman.slingmango.data.ConveyorBelt;
import com.badman.slingmango.data.Fruit;
import com.badman.slingmango.data.Mango;
import com.badman.slingmango.main.SlingMango;

/**
 * Created by roncon on 2/24/17.
 */

public class GameScreen implements Screen, GestureDetector.GestureListener, ContactListener
{
    public static final float GRAVITY = -15.0f;
    public static final int CAM_VIEWPORT = 800/5;
    public static final int GAME_WIDTH = 800/10;
    public static final int GAME_HEIGHT = 480/10;
    public static final float BELT_X = 1500.0f/10;
    public static final float BELT_Y = 450.0f/10;
    public static final float FRUIT_X = 750.0f/10;
    public static final float FRUIT_Y = 270.0f/10;

    private SlingMango game;
    private Sprite basketSprite;
    private ConveyorBelt belt;
    private World world;
    private Camera camera;
    private Viewport viewport;
    private Box2DDebugRenderer debugRenderer;
    private Array<Fruit> fruits;
    Fixture m_platform;
    Fixture basketSensor;
    Fixture basketLeftRim;
    Fixture basketRightRim;

    public GameScreen(SlingMango game)
    {
        this.game = game;

        // setup the camera. In Box2D we operate on a
        // meter scale, pixels won't do it. So we use
        // an orthographic camera with a viewport of
        // 48 meters in width and 32 meters in height.
        // We also position the camera so that it
        // looks at (0,16) (that's where the middle of the
        // screen will be located).
        camera = new OrthographicCamera(48, 32);
        camera.position.set(0, 15, 0);

        // create the debug renderer
        debugRenderer = new Box2DDebugRenderer();

        // create the world
        world = new World(new Vector2(0, -10), true);
        world.setContactListener(this);

        // Conveyor Belt
        BodyDef bd = new BodyDef();
        bd.position.set(15.0f, -10.0f);
        Body body = world.createBody(bd);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(10.0f, 0.5f);

        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        fd.friction = 0.8f;
        m_platform = body.createFixture(fd);

        // Basket
        BodyDef bd2 = new BodyDef();
        bd2.position.set(-3f, 9.0f);
        Body body2 = world.createBody(bd2);

        PolygonShape shape2 = new PolygonShape();
        shape.setAsBox(0.1f, 0.1f);

        FixtureDef fd2 = new FixtureDef();
        fd2.shape = shape;
        fd2.friction = 0.8f;
        basketLeftRim = body2.createFixture(fd2);

        bd2.position.set(3f, 9.0f);
        basketRightRim = world.createBody(bd2).createFixture(fd2);

        // Basket Sensor
        BodyDef bodyDef = new BodyDef();
        Body basketTrigger = world.createBody(bodyDef);

        EdgeShape shape3 = new EdgeShape();
        shape3.set(new Vector2(-20.0f, 10.0f), new Vector2(20.0f, 10.0f));
        basketSensor = basketTrigger.createFixture(shape3, 0.0f);
        basketSensor.setSensor(true);

        fruits = new Array<Fruit>();

        for (int i = 0; i < 5; ++i)
        {
			/*BodyDef bd = new BodyDef();
			bd.type = BodyType.DynamicBody;
			bd.position.set(-10.0f + 2.0f * i, 7.0f);
			Body body = world.createBody(bd);

			PolygonShape shape = new PolygonShape();
			shape.setAsBox(0.5f, 0.5f);
			body.createFixture(shape, 20.0f);*/

            Mango mango = new Mango(world);
            mango.body.setTransform(15.0f + 2.0f * i, -9.0f, 0);

            fruits.add(mango);
        }

        Gdx.input.setInputProcessor(new GestureDetector(this));
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();

        game.batch.end();

        debugRenderer.render(world, camera.combined);
        world.step(Gdx.app.getGraphics().getDeltaTime(), 3, 3);
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
        basketSprite.getTexture().dispose();
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button)
    {
        Vector3 testPoint = new Vector3();

        camera.unproject(testPoint.set(x, y, 0));

        for (Fruit fruit : fruits)
        {
            Body body = fruit.body;

            if (testPoint.x > body.getPosition().x - 1.0f && testPoint.x <= body.getPosition().x + 1.0f)
            {
                if (testPoint.y > body.getPosition().y - 1.0f && testPoint.y <= body.getPosition().y + 1.0f)
                {
                    if (body.getLinearVelocity().y < -1 && !fruit.slinged)
                        fruit.touchedDown = true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button)
    {
        for (Fruit fruit : fruits)
            fruit.touchedDown = false;

        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button)
    {
        for (Fruit fruit : fruits)
        {
            if (fruit.touchedDown && !fruit.slinged)
            {
                fruit.body.setLinearVelocity(Vector2.Zero);
                fruit.body.applyForceToCenter(velocityX, 5000, true);
                fruit.slinged = true;
            }
        }

        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }

    public void preSolve (Contact contact, Manifold oldManifold)
    {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if (fixtureA == m_platform || fixtureB == m_platform)
        {
            if (fixtureA == m_platform)
            {
                Fruit fruit = (Fruit) fixtureB.getBody().getUserData();

                if (fruit.falling)
                    contact.setEnabled(false);
            }

            else if (fixtureB == m_platform)
            {
                Fruit fruit = (Fruit) fixtureA.getBody().getUserData();

                if (fruit.falling)
                    contact.setEnabled(false);
            }
            
            contact.setTangentSpeed(-5.0f);
        }

        else if (fixtureA == basketLeftRim || fixtureB == basketRightRim
                || fixtureA == basketRightRim || fixtureB == basketLeftRim)
        {
            Fruit fruit = (Fruit) fixtureB.getBody().getUserData();

            if (!fruit.falling)
                contact.setEnabled(false);
        }

        else
        {
            Fruit fruitA = (Fruit) fixtureA.getBody().getUserData();
            Fruit fruitB = (Fruit) fixtureB.getBody().getUserData();

            if (fruitA.falling && !fruitB.falling)
                contact.setEnabled(false);

            else if (fruitB.falling && !fruitA.falling)
                contact.setEnabled(false);
        }
    }

    @Override
    public void beginContact (Contact contact)
    {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if (fixtureA == basketSensor)
        {
            Fruit fruit = (Fruit) fixtureB.getBody().getUserData();
            fruit.falling = true;
        }

        else if (fixtureB == basketSensor)
        {
            Fruit fruit = (Fruit) fixtureB.getBody().getUserData();
            fruit.falling = true;
        }
    }

    @Override
    public void endContact (Contact contact) {
    }

    @Override
    public void postSolve (Contact contact, ContactImpulse impulse) {
    }
}
