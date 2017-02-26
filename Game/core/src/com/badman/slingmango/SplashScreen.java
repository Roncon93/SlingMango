package com.badman.slingmango;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Timer;

/**
 * Created by kaija on 2/25/2017.
 */

public class SplashScreen implements Screen {

    final SlingName slingName;
    private OrthographicCamera camera;

    public SplashScreen(SlingName slingName) {
        this.slingName = slingName;

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Timer timer = new Timer();
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

      //  slingName.batch.begin();
      //  slingName.batch.draw(slingName.img, 0, 0,Gdx.app.getGraphics().getWidth(),Gdx.app.getGraphics().getHeight());
      //  slingName.batch.end();

        boolean timerIsOn = false;
        if(!timerIsOn) {
            timerIsOn = true;

            Timer.schedule(new Timer.Task() {

                @Override
                public void run() {

                    slingName.setScreen(new MainMenu(slingName));
                    dispose();
                }

            }, 6);
        }

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
