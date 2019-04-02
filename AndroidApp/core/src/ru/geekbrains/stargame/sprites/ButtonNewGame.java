package ru.geekbrains.stargame.sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;


import ru.geekbrains.stargame.base.ScaledButton;
import ru.geekbrains.stargame.math.Rect;
import ru.geekbrains.stargame.screen.GameScreen;

public class ButtonNewGame extends ScaledButton {

    private GameScreen gameScreen;

    public ButtonNewGame(TextureAtlas atlas, GameScreen gameScreen) {
        super(atlas.findRegion("btPlay"));
        setProportion(15f);
//        setBottom(-48f);
//        setLeft(13f);
        this.gameScreen = gameScreen;
    }
    @Override
    public void resize(Rect worldBounds) {
        setBottom(worldBounds.getBottom() + 2f);
        setRight(worldBounds.getRight() - 2f);
    }

    @Override
    protected void action() {
        gameScreen.startNewGame();
    }
}
