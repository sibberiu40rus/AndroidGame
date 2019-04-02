package ru.geekbrains.stargame.sprites;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.math.Rect;
import ru.geekbrains.stargame.pool.BulletPool;
import ru.geekbrains.stargame.pool.ExposionPool;
import ru.geekbrains.stargame.utils.Regions;

public class MainShip extends Ship {

    private static final int INVALID_POINTER = -1;
    private static final int HP = 100;
    private int hpEdge = 90;
    private int decrimHp = 10;
    private int decrimRegion = 36;
    private int widthOfRegion = 360;

    private TextureRegion hpPoints;

    private Vector2 v0 = new Vector2(50f, 0);



    private boolean isPressedRight;
    private boolean isPressedLeft;

    private int leftPointer = INVALID_POINTER;
    private int rightPointer = INVALID_POINTER;

    public MainShip(TextureAtlas atlas,TextureAtlas atlasHeart, ExposionPool exposionPool, BulletPool bulletPool, Sound shootSound) {
        super(atlas.findRegion("main_ship"), 1, 2, 2);
        setProportion(7f);
        hpPoints = atlasHeart.findRegion("heart");
        this.bulletPool = bulletPool;
        this.exposionPool = exposionPool;
        this.bulletRegion = atlas.findRegion("bulletMainShip");
        this.bulletHeight = 1f;
        this.bulletV.set(0, 50f);
        this.damage = 1;
        this.hp = HP;
        this.reloadInterval = 0.2f;
        this.shootSound = shootSound;

    }

    public void startNewGame (Rect worldBounds) {
        this.hp = HP;
        pos.x = worldBounds.pos.x;
        widthOfRegion = 360;
        hpEdge = 90;
        flushDestroy();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        setBottom(worldBounds.getBottom() + 5f);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        reloadTimer += delta;
        hpPoints.setRegionWidth(widthOfRegion);

        if (hp <= 0) {
            hpPoints.setRegionWidth(0);
        }

        if (hp <= hpEdge){
            hpPoints.setRegionWidth(widthOfRegion);
            widthOfRegion = widthOfRegion - decrimRegion;
            hpEdge = hpEdge - decrimHp;
        }
        if (reloadTimer >= reloadInterval) {
            reloadTimer = 0f;
            shoot();
        }
        if (getRight() > worldBounds.getRight()) {
            setRight(worldBounds.getRight());
            stop();
        }
        if (getLeft() < worldBounds.getLeft()) {
            setLeft(worldBounds.getLeft());
            stop();
        }
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        if (touch.x < worldBounds.pos.x) {
            if (leftPointer != INVALID_POINTER) {
                return false;
            }
            leftPointer = pointer;
            moveLeft();
        } else {
            if (rightPointer != INVALID_POINTER) {
                return false;
            }
            rightPointer = pointer;
            moveRight();
        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        if (pointer == leftPointer) {
            leftPointer = INVALID_POINTER;
            if (rightPointer != INVALID_POINTER) {
                moveRight();
            } else {
                stop();
            }
        } else if (pointer == rightPointer) {
            rightPointer = INVALID_POINTER;
            if (leftPointer != INVALID_POINTER) {
                moveLeft();
            } else {
                stop();
            }
        }
        return false;
    }

    public void keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.LEFT:
            case Input.Keys.A:
                isPressedLeft = true;
                moveLeft();
                break;
            case Input.Keys.RIGHT:
            case Input.Keys.D:
                isPressedRight = true;
                moveRight();
                break;
        }
    }

    public void keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.LEFT:
            case Input.Keys.A:
                isPressedLeft = false;
                if (isPressedRight) {
                    moveRight();
                } else {
                    stop();
                }
                break;
            case Input.Keys.RIGHT:
            case Input.Keys.D:
                isPressedRight = false;
                if (isPressedLeft) {
                    moveLeft();
                } else {
                    stop();
                }
                break;
        }
    }

    public boolean isBulletCollision(Rect bullet) {
        return !(
                bullet.getRight() < getLeft()
                || bullet.getLeft() > getRight()
                || bullet.getBottom() > pos.y
                || bullet.getTop() < getBottom()
        );
    }


    private void moveRight() {
        v.set(v0);
    }

    private void moveLeft() {
        v.set(v0).rotate(180);
    }

    private void stop() {
        v.setZero();
    }
}