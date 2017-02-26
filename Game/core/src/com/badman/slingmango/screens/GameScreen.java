package com.badman.slingmango.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
import com.badlogic.gdx.utils.TimeUtils;
import com.badman.slingmango.data.Fruit;
import com.badman.slingmango.data.Mango;
import com.badman.slingmango.main.SlingMango;

import net.dermetfan.gdx.graphics.g2d.Box2DSprite;

/**
 * Created by roncon on 2/24/17.
 */

public class GameScreen implements Screen, GestureDetector.GestureListener, ContactListener
{
    private OrthographicCamera camera;

    private Box2DDebugRenderer renderer;

    private SpriteBatch batch;

    private BitmapFont font;

    private World world;

    private Array<Fruit> fruits = new Array<Fruit>();

    private Box2DSprite basketSprite;
    private Sprite mangoSprite;

    private Body basketBody;

    private Fixture conveyorBelt;
    private Fixture basketSensor;
    private Fixture basketLeftRim;
    private Fixture basketRightRim;

    @Override
    public void render (float delta)
    {
        // update the world with a fixed time step
        long startTime = TimeUtils.nanoTime();
        world.step(Gdx.app.getGraphics().getDeltaTime(), 3, 3);
        float updateTime = (TimeUtils.nanoTime() - startTime) / 1000000000.0f;

        startTime = TimeUtils.nanoTime();
        // clear the screen and setup the projection matrix
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();

        // render the world using the debug renderer
        renderer.render(world, camera.combined);
        float renderTime = (TimeUtils.nanoTime() - startTime) / 1000000000.0f;

        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        font.draw(batch, "fps:" + Gdx.graphics.getFramesPerSecond() + ", update: " + updateTime + ", render: " + renderTime, 0, 20);
        basketSprite.draw(batch, basketBody);

        for (Fruit fruit : fruits) {
            //mangoSprite.setPosition(fruit.body.getPosition().x, fruit.body.getPosition().y);
            //mangoSprite.draw(batch);
            fruit.draw(batch, fruit.body);
        }

        batch.end();
    }

    public GameScreen(SlingMango game)
    {
        // setup the camera. In Box2D we operate on a
        // meter scale, pixels won't do it. So we use
        // an orthographic camera with a viewport of
        // 48 meters in width and 32 meters in height.
        // We also position the camera so that it
        // looks at (0,16) (that's where the middle of the
        // screen will be located).
        camera = new OrthographicCamera(2, 2);
        camera.position.set(0, 0, 0);

        // create the debug renderer
        renderer = new Box2DDebugRenderer();

        // create the world
        world = new World(new Vector2(0, -5), true);

        // call abstract method to populate the world
        createWorld(world);

        batch = new SpriteBatch();
        font = new BitmapFont();

        //basketSprite = new Sprite(new Texture(Gdx.files.internal("basket.png")));
        //basketSprite.setScale(0.5f);
        //basketSprite.setPosition(235, 335);

        mangoSprite = new Sprite(new Texture(Gdx.files.internal("mango.png")));
        mangoSprite.setScale(0.25f);
        mangoSprite.setPosition(235, 335);

        Gdx.input.setInputProcessor(new GestureDetector(this));
    }

    @Override
    public void dispose ()
    {
        renderer.dispose();
        world.dispose();

        renderer = null;
        world = null;
    }

    /** another temporary vector **/
    Vector2 target = new Vector2();

    public void pause () {

    }

    public void resume () {

    }

    @Override
    public void hide() {

    }

    @Override
    public void show() {

    }

    public void resize (int width, int height) {

    }

    protected void createWorld (World world)
    {
        world.setContactListener(this);

        // Conveyor Belt
        BodyDef bd = new BodyDef();
        bd.position.set(10.4f, -0.5f);
        Body body = world.createBody(bd);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(10.0f, 0.1f);

        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        fd.friction = 0.8f;
        conveyorBelt = body.createFixture(fd);

        // Basket Rim
        BodyDef bd2 = new BodyDef();
        bd2.position.set(-0.12f, 0.8f);
        Body body2 = world.createBody(bd2);

        PolygonShape shape2 = new PolygonShape();
        shape.setAsBox(0.025f, 0.025f);

        FixtureDef fd2 = new FixtureDef();
        fd2.shape = shape;
        fd2.friction = 0.8f;
        basketLeftRim = body2.createFixture(fd2);

        bd2.position.set(0.12f, 0.8f);
        basketRightRim = world.createBody(bd2).createFixture(fd2);

        // Basket Sensor
        BodyDef bodyDef = new BodyDef();
        Body basketTrigger = world.createBody(bodyDef);

        EdgeShape shape3 = new EdgeShape();
        shape3.set(new Vector2(-20.0f, 1.2f), new Vector2(20.0f, 1.2f));
        basketSensor = basketTrigger.createFixture(shape3, 0.0f);
        basketSensor.setSensor(true);

        // Hoop Sprite
        BodyDef bd3 = new BodyDef();
        bd3.position.set(0f, 0.7f);
        basketBody = world.createBody(bd3);

        PolygonShape shape4 = new PolygonShape();
        shape.setAsBox(0.15f, 0.15f);

        FixtureDef fd3 = new FixtureDef();
        fd3.shape = shape;
        fd3.isSensor = true;
        basketBody.createFixture(fd3);

        basketSprite = new Box2DSprite(new Texture(Gdx.files.internal("basket.png")));

        // Boxes
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
            mango.body.setTransform(2.0f + 2.0f * i, 1.0f, 0);

            fruits.add(mango);
        }
    }

    public void preSolve (Contact contact, Manifold oldManifold)
    {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if (fixtureA == conveyorBelt || fixtureB == conveyorBelt)
        {
            if (fixtureA == conveyorBelt)
            {
                Fruit fruit = (Fruit) fixtureB.getBody().getUserData();

                if (fruit.falling)
                    contact.setEnabled(false);
            }

            else if (fixtureB == conveyorBelt)
            {
                Fruit fruit = (Fruit) fixtureA.getBody().getUserData();

                if (fruit.falling)
                    contact.setEnabled(false);
            }

            contact.setTangentSpeed(-1.0f);
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
                fruit.body.setAngularVelocity(0);
                fruit.body.applyForceToCenter(velocityX / 1000, 11, true);
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
}
