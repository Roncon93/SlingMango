package com.badman.slingmango;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by kaija on 2/25/2017.
 */
public class SlingGame implements Screen {

    final SlingName slingName;

    private Texture img;

    public SlingGame(SlingName slingName) {

        this.slingName = slingName;

        img = new Texture("badlogic.jpg");

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 1 , 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
       // slingName.batch.begin();
       //  slingName.batch.draw(img, 0, 0);
       // slingName.batch.end();
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

    }
}
