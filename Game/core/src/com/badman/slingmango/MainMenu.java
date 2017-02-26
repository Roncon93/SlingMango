package com.badman.slingmango;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.awt.Label;

/**
 * Created by kaija on 2/25/2017.
 */
public class MainMenu implements Screen {

    final SlingName slingName;
    //private final Rectangle title;

    Texture img;
    private Stage stage;
    private OrthographicCamera camera;


    public MainMenu(final SlingName slingName) {
        this.slingName = slingName;


        img = new Texture("Logo6.png");


        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        Skin mySkin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        ImageButton button3 = new ImageButton(mySkin);

        button3.setSize(250,100);
        button3.getStyle().imageUp = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("mangoSetUp.png"))));
       // button3.getStyle().imageDown = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("mangoSetUp3.png"))));
        button3.setPosition((Gdx.app.getGraphics().getWidth()/2)-125,Gdx.app.getGraphics().getHeight()/2 - 50 );

        button3.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.print("Thiss");
                slingName.setScreen(new SlingGame(slingName));
                //dispose();
            }
        });

    /*    button3.addListener(new InputListener(){

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
               // outputLabel.setText("Press a Button");
            }

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                System.out.print("Thiss");
                slingName.setScreen(new SlingGame(slingName));
                dispose();
                return true;
            }
        });*/


        stage.addActor(button3);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {


        Gdx.gl.glClearColor(1, 0.655f, 0.149f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

       // Gdx.app.getGraphics().getWidth();



        slingName.batch.begin();
        slingName.batch.draw(img,(Gdx.app.getGraphics().getWidth()/2)- 175,Gdx.app.getGraphics().getHeight()- 210,350,200);//x y w h
        slingName.batch.end();

        stage.act();
        stage.draw();


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
        stage.dispose();
        //batch.dispose();
        //img.dispose();
    }

}
