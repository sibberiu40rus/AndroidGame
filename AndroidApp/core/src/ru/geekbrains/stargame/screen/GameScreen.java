package ru.geekbrains.stargame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

import java.util.List;

import ru.geekbrains.stargame.base.Font;
import ru.geekbrains.stargame.math.Rect;
import ru.geekbrains.stargame.pool.BulletPool;
import ru.geekbrains.stargame.pool.EnemyPool;
import ru.geekbrains.stargame.pool.ExposionPool;
import ru.geekbrains.stargame.sprites.Background;
import ru.geekbrains.stargame.base.Base2dScreen;
import ru.geekbrains.stargame.sprites.Bullet;
import ru.geekbrains.stargame.sprites.ButtonExit;
import ru.geekbrains.stargame.sprites.ButtonNewGame;
import ru.geekbrains.stargame.sprites.ButtonPause;
import ru.geekbrains.stargame.sprites.Enemy;
import ru.geekbrains.stargame.sprites.GameOver;
import ru.geekbrains.stargame.sprites.HpPanel;
import ru.geekbrains.stargame.sprites.MainShip;
import ru.geekbrains.stargame.sprites.Star;
import ru.geekbrains.stargame.sprites.TrackingStar;
import ru.geekbrains.stargame.utils.EnemiesEmitter;

public class GameScreen extends Base2dScreen {

    private static final int STAR_COUNT = 64;
    private static final float FONT_SIZE = 2f;
    private static final String FRAGS = "Frags: ";
    private static final String HP = "Hp: ";
    private static final String LEVEL = "Level: ";

    private enum State {PLAYING, GAME_OVER, PAUSE}

    private Background background;
    private Texture backgroundTexture;
    private TextureAtlas atlas;
    private TextureAtlas atlasExitAndPlay;
    private TextureAtlas atlasSettings;
    private TextureAtlas atlasHealthPanel;

    private TrackingStar starList[];
    private MainShip mainShip;

    private BulletPool bulletPool;
    private EnemyPool enemyPool;
    private ExposionPool exposionPool;

    private EnemiesEmitter enemiesEmitter;

    private Music music;
    private Sound laserSound;
    private Sound bulletSound;
    private Sound explosionSound;

    private ButtonNewGame buttonNewGame;
    private ButtonExit buttonExit;
    private ButtonPause buttonPause;

    private HpPanel hpPanel;
    private GameOver gameOver;


    private int frags;

    private State state;
    private State stateBuf;

    private Font font;
    private StringBuilder sbFrags;
    private StringBuilder sbHp;
    private StringBuilder sbLevel;


    @Override
    public void show() {
        super.show();

        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        music.setLooping(true);
        music.play();

        laserSound = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));
        bulletSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet.wav"));
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));

        backgroundTexture = new Texture("background.jpg");
        background = new Background(new TextureRegion(backgroundTexture));

        atlas = new TextureAtlas("textures/mainAtlas.tpack");
        atlasExitAndPlay = new TextureAtlas("textures/menuAtlas.tpack");
        atlasSettings = new TextureAtlas("textures/settings.pack");
        atlasHealthPanel = new TextureAtlas("textures/health.pack");

        starList = new TrackingStar[STAR_COUNT];

        bulletPool = new BulletPool();
        exposionPool = new ExposionPool(atlas, explosionSound);
        enemyPool = new EnemyPool(bulletPool, exposionPool, worldBounds, bulletSound);

        hpPanel = new HpPanel(atlasHealthPanel);
        gameOver = new GameOver(atlas);

        mainShip = new MainShip(atlas,atlasHealthPanel, exposionPool, bulletPool, laserSound);
        enemiesEmitter = new EnemiesEmitter(atlas, worldBounds, enemyPool);

        buttonNewGame = new ButtonNewGame(atlasExitAndPlay, this);
        buttonPause = new ButtonPause(atlasSettings,this);
        buttonExit = new ButtonExit(atlasExitAndPlay);



        font = new Font("font/font.fnt", "font/font.png");
        font.setSize(FONT_SIZE);
        sbFrags = new StringBuilder();
        sbHp = new StringBuilder();
        sbLevel = new StringBuilder();
        for (int i = 0; i < starList.length; i++) {
            starList[i] = new TrackingStar(atlas, mainShip.getV());
        }
        startNewGame();
    }

    @Override
    public void pause() {
        super.pause();
        stateBuf = state;
        state = State.PAUSE;
    }

    @Override
    public void resume() {
        super.resume();
        state = stateBuf;
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        buttonNewGame.resize(worldBounds);
        buttonExit.resize(worldBounds);
        buttonPause.resize(worldBounds);
        hpPanel.resize(worldBounds);
        for (Star star : starList) {
            star.resize(worldBounds);
        }
        if (state == State.PLAYING) {
            mainShip.resize(worldBounds);
        }

    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        deleteAllDestroyed();
        draw();
    }

    private void update(float delta) {
        for (Star star : starList) {
            star.update(delta);
        }
        exposionPool.updateActiveSprites(delta);
        if (state == State.PLAYING) {
            mainShip.update(delta);
            bulletPool.updateActiveSprites(delta);
            enemyPool.updateActiveSprites(delta);
            enemiesEmitter.generate(delta, frags);
        }
        checkCollisions();
    }

    private void checkCollisions() {
        if (state == State.GAME_OVER) {
            return;
        }

        List<Enemy> enemyList = enemyPool.getActiveObjects();
        for (Enemy enemy : enemyList) {
            if (enemy.isDestroyed()) {
                continue;
            }
            float minDistance = enemy.getHalfWidth() + mainShip.getHalfWidth();
            if (enemy.pos.dst(mainShip.pos) < minDistance) {
                enemy.destroy();
                mainShip.damage(mainShip.getHp());
                state = State.GAME_OVER;
                return;
            }
        }

        List<Bullet> bulletList = bulletPool.getActiveObjects();
        for (Enemy enemy : enemyList) {
            if (enemy.isDestroyed()) {
                continue;
            }
            for (Bullet bullet : bulletList) {
                if (bullet.getOwner() != mainShip || bullet.isDestroyed()) {
                    continue;
                }
                if (enemy.isBulletCollision(bullet)) {
                    enemy.damage(bullet.getDamage());
                    bullet.destroy();
                    if (enemy.isDestroyed()) {
                        frags++;
                    }
                }
            }
        }


        for (Bullet bullet : bulletList) {
            if (bullet.getOwner() == mainShip || bullet.isDestroyed()) {
                continue;
            }
            if (mainShip.isBulletCollision(bullet)) {
                mainShip.damage(bullet.getDamage());
                bullet.destroy();
                if (mainShip.isDestroyed()) {
                    state = State.GAME_OVER;
                }
            }
        }
    }

    private void deleteAllDestroyed() {
        bulletPool.freeAllDestroyedActiveSprites();
        exposionPool.freeAllDestroyedActiveSprites();
        enemyPool.freeAllDestroyedActiveSprites();
    }

    private void draw() {
        Gdx.gl.glClearColor(0.51f, 0.34f, 0.64f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();

        background.draw(batch);
        for (Star star : starList) {
            star.draw(batch);
        }
        exposionPool.drawActiveSprites(batch);
        hpPanel.draw(batch);
        buttonPause.draw(batch);
        if (state == State.PLAYING) {
            mainShip.draw(batch);
            bulletPool.drawActiveSprites(batch);
            enemyPool.drawActiveSprites(batch);
        }else if (state == State.GAME_OVER){
            buttonExit.draw(batch);
            buttonNewGame.draw(batch);
            gameOver.draw(batch);
        }
        printInfo();
        batch.end();
    }

    public void printInfo() {
        sbFrags.setLength(0);
        sbHp.setLength(0);
        sbLevel.setLength(0);
        font.draw(batch,sbFrags.append(FRAGS).append(frags), worldBounds.getLeft() + 2f , worldBounds.getTop() - 2f);
        font.draw(batch,sbHp.append(HP).append(mainShip.getHp()), worldBounds.pos.x, worldBounds.getTop()-2f, Align.center);
        font.draw(batch,sbLevel.append(LEVEL).append(enemiesEmitter.getLevel()), worldBounds.getLeft() + 2f , worldBounds.getTop() - 6f, Align.left);
    }




    @Override
    public void dispose() {
        backgroundTexture.dispose();

        atlas.dispose();
        atlasExitAndPlay.dispose();
        atlasSettings.dispose();
        atlasHealthPanel.dispose();

        music.dispose();
        laserSound.dispose();
        bulletSound.dispose();
        explosionSound.dispose();

        exposionPool.dispose();
        bulletPool.dispose();
        enemyPool.dispose();

        super.dispose();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        buttonPause.touchDown(touch,pointer);
        if (state == State.PLAYING) {
            mainShip.touchDown(touch, pointer);
        }else if (state == State.GAME_OVER) {
            buttonNewGame.touchDown(touch,pointer);
            buttonExit.touchDown(touch,pointer);
        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        buttonPause.touchUp(touch,pointer);
        if (state == State.PLAYING) {
            mainShip.touchUp(touch, pointer);
        }else if (state == State.GAME_OVER) {
            buttonNewGame.touchUp(touch,pointer);
            buttonExit.touchUp(touch,pointer);
        }
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (state == State.PLAYING) {
            mainShip.keyDown(keycode);
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (state == State.PLAYING) {
            mainShip.keyUp(keycode);
        }
        return false;
    }

    public void startNewGame() {
        state = State.PLAYING;
        frags = 0;

        mainShip.startNewGame(worldBounds);

        bulletPool.freeAllActiveObjects();
        enemyPool.freeAllActiveObjects();
        exposionPool.freeAllActiveObjects();

    }
}