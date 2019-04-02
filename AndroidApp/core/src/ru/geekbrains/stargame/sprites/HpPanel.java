package ru.geekbrains.stargame.sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;


import ru.geekbrains.stargame.base.Sprite;
import ru.geekbrains.stargame.math.Rect;

public class HpPanel extends Sprite {



    public HpPanel(TextureAtlas atlas) {
        super(atlas.findRegion("heart"));
        setProportion(3f);
    }


    @Override
    public void resize(Rect worldBounds) {
        setBottom(worldBounds.getBottom() + 2f);
        setLeft(worldBounds.getLeft() + 2f);
    }
}
