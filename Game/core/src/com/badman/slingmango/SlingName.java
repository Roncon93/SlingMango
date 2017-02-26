package com.badman.slingmango;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SlingName extends Game {
	SpriteBatch batch;
	Texture img;
    OrthographicCamera camera;
	@Override
	public void create () {

		batch = new SpriteBatch();
		img = new Texture("BadMan.png");
		this.setScreen(new SplashScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
