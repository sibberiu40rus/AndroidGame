package ru.geekbrains.stargame.pool;

import com.badlogic.gdx.audio.Sound;

import ru.geekbrains.stargame.base.SpritesPool;
import ru.geekbrains.stargame.math.Rect;
import ru.geekbrains.stargame.sprites.Enemy;


public class EnemyPool extends SpritesPool<Enemy> {

    private BulletPool bulletPool;
    private ExposionPool exposionPool;
    private Rect worldBounds;
    private Sound shootSound;

    public EnemyPool(BulletPool bulletPool,ExposionPool exposionPool, Rect worldBounds, Sound shootSound) {
        this.bulletPool = bulletPool;
        this.exposionPool = exposionPool;
        this.worldBounds = worldBounds;
        this.shootSound = shootSound;
    }

    @Override
    protected Enemy newObject() {
        return new Enemy(bulletPool, exposionPool, shootSound, worldBounds);
    }
}
