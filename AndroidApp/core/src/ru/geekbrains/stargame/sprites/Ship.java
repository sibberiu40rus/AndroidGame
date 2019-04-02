package ru.geekbrains.stargame.sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.base.Sprite;
import ru.geekbrains.stargame.math.Rect;
import ru.geekbrains.stargame.pool.BulletPool;
import ru.geekbrains.stargame.pool.ExposionPool;


public class Ship extends Sprite {

    protected Rect worldBounds;

    protected Vector2 v = new Vector2();
    protected Vector2 bulletV = new Vector2();
    protected float bulletHeight;

    protected BulletPool bulletPool;
    protected ExposionPool exposionPool;
    protected TextureRegion bulletRegion;
    protected int damage;
    protected int hp;

    protected Sound shootSound;

    protected float reloadInterval;
    protected float reloadTimer;

    private float damageAnimateInterval = 0.1f;
    private float damageAnimateTimer;


    public Ship() {
    }

    public Ship(TextureRegion region, int rows, int cols, int frames) {
        super(region, rows, cols, frames);
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
    }

    @Override
    public void update(float delta) {
        pos.mulAdd(v, delta);
        damageAnimateTimer += delta;
        if (damageAnimateTimer >= damageAnimateInterval) {
            frame = 0;
        }
    }

    public void damage (int damage) {
        frame = 1;
        damageAnimateTimer = 0f;
        hp -= damage;
        if (hp <= 0) {
            hp = 0;
            destroy();
        }
    }

    public void shoot() {
        Bullet bullet = bulletPool.obtain();
        bullet.set(this, bulletRegion, pos, bulletV, bulletHeight, worldBounds, damage);
        shootSound.play();
    }

    public void boom() {
        Explosion explosion = exposionPool.obtain();
        explosion.set(this.getHeight(), this.pos);

    }

    public int getHp() {
        return hp;
    }

    @Override
    public void destroy() {
        super.destroy();
        boom();
    }

    public Vector2 getV() {
        return v;
    }
}
