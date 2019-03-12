package ru.geekbrains.stargame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.base.Base2dScreen;


public class MenuScreen extends Base2dScreen {
    private SpriteBatch batch;
    private Texture img;
    private Texture spaceShip;
    private Vector2 touch;
    private Vector2 pos;
    private Vector2 v;
    private Vector2 v2;
    private Vector2 route;
    private float speed = 4.0f;
    private int key = 0;



    @Override
    public void show() {
        super.show();
        batch = new SpriteBatch();
        img = new Texture("background.jpg");
        spaceShip = new Texture("spaceShip.png");
        touch = new Vector2();
        route = new Vector2();
        pos = new Vector2(0, 0);
        v = new Vector2(0f, 0f);
        v2 = new Vector2(0f, 0f);
    }




    @Override
    public void render(float delta) {

        super.render(delta);
        Gdx.gl.glClearColor(1, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(img, 0, 0);
        batch.draw(spaceShip, pos.x, pos.y);
        batch.end();
        route.set(touch);

        if (v2.x == 0 && v2.y == 0) {
            if (route.sub(pos).len() <= speed) {
                pos.set(touch);
            } else {
                pos.add(v);
            }
        }pos.add(v2);











    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
        spaceShip.dispose();
        super.dispose();
    }

    public void moveWithKeyDown () {

    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touch.set(screenX, Gdx.graphics.getHeight() - screenY);
        v.set(touch.cpy().sub(pos)).setLength(speed);
        System.out.println("touch x = " + touch.x + " touch.y = " + touch.y);
        return super.touchDown(screenX, screenY, pointer, button);
    }

    /**
     * немного криво, дальше обязательно додумаю по лучше((
     * @param keycode
     * @return
     */

    @Override
    public boolean keyDown(int keycode) {
        key = keycode;
        switch (key) {
            case (22):
                v2.set(1,0).setLength(speed);
                break;
            case (21):
                v2.set(-1,0).setLength(speed);
                break;
            case (19):
                v2.set(0,1).setLength(speed);
                break;
            case (20):
                v2.set(0,-1).setLength(speed);
                break;
        }
        System.out.println("keyDown keycode = " + keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        key = keycode;
        switch (key) {
            case (22):
                v2.set(0,0);
               // key = 0;
                break;
            case (21):
                v2.set(0,0);
                //key = 0;
                break;
            case (19):
                v2.set(0,0);
               // key = 0;
                break;
            case (20):
                v2.set(0,0);
               // key = 0;
                break;
        }
        System.out.println("keyDown keycode = " + keycode);
        return false;
    }
}
