package ru.geekbrains.stargame.sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;


import ru.geekbrains.stargame.base.ScaledButton;
import ru.geekbrains.stargame.screen.GameScreen;

public class ButtonNewGame extends ScaledButton {

    private GameScreen gameScreen;

    public ButtonNewGame(TextureAtlas atlas, GameScreen gameScreen) {
        super(atlas.findRegion("button_new_game"));
        setProportion(3f);
        setTop(-4f);
        this.gameScreen = gameScreen;
    }

    @Override
    protected void action() {
        gameScreen.startNewGame();
    }
}
