package ru.geekbrains.stargame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.base.Base2dScreen;
import ru.geekbrains.stargame.math.Rect;
import ru.geekbrains.stargame.sprites.Background;


public class MenuScreen extends Base2dScreen {

    private Vector2 pos;

    private Background background;
    private Texture backgroundTexture;



    @Override
    public void show() {
        super.show();

        backgroundTexture = new Texture("background.jpg");
        background = new Background(new TextureRegion(backgroundTexture));
        pos = new Vector2(0, 0);

    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
    }



    @Override
    public void render(float delta) {
        super.render(delta);

        Gdx.gl.glClearColor(0, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        batch.end();

    }

    @Override
    public void dispose() {
        super.dispose();
        backgroundTexture.dispose();
    }

}
