package ru.geekbrains.stargame.pool;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.geekbrains.stargame.base.SpritesPool;
import ru.geekbrains.stargame.sprites.Explosion;

public class ExposionPool extends SpritesPool<Explosion> {

    private TextureAtlas atlas;
    private Sound sound;

    public ExposionPool(TextureAtlas atlas, Sound sound) {
        this.atlas = atlas;
        this.sound = sound;
    }

    @Override
    protected Explosion newObject() {
        return new Explosion(atlas, sound);
    }
}
