package com.seuprojeto.snakegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MenuScreen implements Screen {
    private SpriteBatch batch;
    private BitmapFont font;
    private String singlePlayerText = "Single Player: Press 1";
    private String multiPlayerText = "Multiplayer: Press 2";

    public MenuScreen() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(2);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();

        GlyphLayout singlePlayerLayout = new GlyphLayout(font, singlePlayerText);
        GlyphLayout multiPlayerLayout = new GlyphLayout(font, multiPlayerText);

        float singlePlayerX = (Gdx.graphics.getWidth() - singlePlayerLayout.width) / 2;
        float singlePlayerY = (Gdx.graphics.getHeight() / 2) + 50;
        float multiPlayerX = (Gdx.graphics.getWidth() - multiPlayerLayout.width) / 2;
        float multiPlayerY = (Gdx.graphics.getHeight() / 2) - 50;

        font.draw(batch, singlePlayerText, singlePlayerX, singlePlayerY);
        font.draw(batch, multiPlayerText, multiPlayerX, multiPlayerY);

        batch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.NUM_1)) {
            ((SnakeGame) Gdx.app.getApplicationListener()).setScreen(new GameScreenSinglePlayer(new SpriteBatch()));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_2)) {
            ((SnakeGame) Gdx.app.getApplicationListener()).setScreen(new GameScreenMultiplayer(new SpriteBatch()));
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
