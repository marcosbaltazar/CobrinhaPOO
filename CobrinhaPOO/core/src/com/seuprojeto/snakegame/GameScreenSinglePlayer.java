package com.seuprojeto.snakegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class GameScreenSinglePlayer implements Screen {
    private SpriteBatch batch;
    private Snake snake;
    private Food food;
    private BitmapFont font;
    private boolean isGameOver;
    private boolean isPaused;
    private boolean isResuming;
    private float resumeTimer;

    public GameScreenSinglePlayer(SpriteBatch batch) {
        this.batch = batch;
        snake = new Snake(100, 100, Input.Keys.UP, Input.Keys.RIGHT, Input.Keys.DOWN, Input.Keys.LEFT, false); // Configurações do jogador
        food = new Food(200, 200); // Posição inicial da comida
        generateFont();
        isGameOver = false;
        isPaused = false;
        isResuming = false;
        resumeTimer = 0;

        // Configurar o input processor
        SnakeInputProcessor inputProcessor = new SnakeInputProcessor(snake, this);
        Gdx.input.setInputProcessor(inputProcessor);
    }

    private void generateFont() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("arial.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 48; // Tamanho da fonte
        parameter.color = Color.WHITE; // Cor da fonte
        parameter.borderWidth = 2;
        parameter.borderColor = Color.BLACK;
        font = generator.generateFont(parameter);
        generator.dispose(); // Libera o gerador de fontes
    }

    public void togglePause() {
        if (isPaused) {
            isPaused = false;
            isResuming = true;
            resumeTimer = 1; // 1 segundo
        } else {
            isPaused = true;
        }
    }

    public boolean isPaused() {
        return isPaused;
    }

    public boolean isResuming() {
        return isResuming;
    }

    private void resetGame() {
        snake = new Snake(100, 100, Input.Keys.UP, Input.Keys.RIGHT, Input.Keys.DOWN, Input.Keys.LEFT, false);
        food = new Food(200, 200);
        isGameOver = false;
        isPaused = false;
        isResuming = false;
        resumeTimer = 0;
    }

    @Override
    public void show() {
        Gdx.app.log("GameScreenSinglePlayer", "Showing single player screen");
    }

    @Override
    public void render(float delta) {
        Gdx.app.log("GameScreenSinglePlayer", "Rendering single player screen");
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        snake.render(batch);
        food.render(batch);

        if (isPaused) {
            String pauseText = "Paused";
            GlyphLayout layout = new GlyphLayout(font, pauseText);
            float x = (Gdx.graphics.getWidth() - layout.width) / 2;
            float y = (Gdx.graphics.getHeight() - layout.height) / 2;
            font.draw(batch, pauseText, x, y);
            batch.end();
            return;
        }

        if (isResuming) {
            resumeTimer -= delta;
            if (resumeTimer <= 0) {
                isResuming = false;
            } else {
                String resumeText = "Resuming in " + (int)Math.ceil(resumeTimer);
                GlyphLayout layout = new GlyphLayout(font, resumeText);
                float x = (Gdx.graphics.getWidth() - layout.width) / 2;
                float y = (Gdx.graphics.getHeight() - layout.height) / 2;
                font.draw(batch, resumeText, x, y);
                batch.end();
                return;
            }
        }

        if (snake.isAlive()) {
            snake.update(food, delta);
        } else {
            isGameOver = true;
        }

        if (isGameOver) {
            String gameOverText = "Game Over";
            GlyphLayout layout = new GlyphLayout(font, gameOverText);
            float x = (Gdx.graphics.getWidth() - layout.width) / 2;
            float y = (Gdx.graphics.getHeight() - layout.height) / 2;
            font.draw(batch, gameOverText, x, y);
        }

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        // Código para lidar com redimensionamento da tela
    }

    @Override
    public void pause() {
        isPaused = true;
    }

    @Override
    public void resume() {
        isPaused = false;
    }

    @Override
    public void hide() {
        Gdx.app.log("GameScreenSinglePlayer", "Hiding single player screen");
    }

    @Override
    public void dispose() {
        Gdx.app.log("GameScreenSinglePlayer", "Disposing single player screen");
        snake.dispose();
        food.dispose();
        font.dispose();
    }
}
