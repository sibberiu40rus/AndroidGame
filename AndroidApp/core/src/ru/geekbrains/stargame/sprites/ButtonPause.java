package ru.geekbrains.stargame.sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.geekbrains.stargame.base.ScaledButton;
import ru.geekbrains.stargame.math.Rect;
import ru.geekbrains.stargame.screen.GameScreen;

public class ButtonPause extends ScaledButton {

    private GameScreen gameScreen;
    private boolean gameState = true;

    public ButtonPause(TextureAtlas atlas, GameScreen gameScreen) {
        super(atlas.findRegion("pause"));
        setProportion(7f);
        setTop(49f);
        setRight(27f);
        this.gameScreen = gameScreen;
    }

    @Override
    public void resize(Rect worldBounds) {
        setTop(worldBounds.getTop() - 1f);
        setRight(worldBounds.getRight() - 1f);
    }

    @Override
    protected void action() {
        if (gameState == true) {
            gameScreen.pause();
            gameState = false;
        }else if (gameState == false) {
            gameScreen.resume();
            gameState = true;
        }

    }
}

