package ru.geekbrains.stargame.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.geekbrains.stargame.base.ScaledButton;
import ru.geekbrains.stargame.math.Rect;

public class ButtonExit extends ScaledButton {

    public ButtonExit(TextureAtlas atlas) {
        super(atlas.findRegion("btExit"));
        setProportion(13f);
    }

    @Override
    public void resize(Rect worldBounds) {
        setBottom(worldBounds.getBottom() + 2f);
        setLeft(worldBounds.getLeft() + 2f);
    }


    @Override
    protected void action() {
        Gdx.app.exit();
    }
}

