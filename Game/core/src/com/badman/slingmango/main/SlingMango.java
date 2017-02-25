package com.badman.slingmango.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badman.slingmango.screens.GameScreen;

public class SlingMango extends Game
{
	public SpriteBatch batch;
	public BitmapFont font;

	@Override
	public void create()
	{
		batch = new SpriteBatch();
		//Use LibGDX's default Arial font.
		font = new BitmapFont();

		setScreen(new GameScreen(this));
	}

	@Override
	public void render() {
		super.render(); //important!
	}

	@Override
	public void dispose()
	{
		batch.dispose();
		font.dispose();
	}
}
