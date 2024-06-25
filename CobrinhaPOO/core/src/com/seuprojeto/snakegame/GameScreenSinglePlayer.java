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

/**
 * GameScreenSinglePlayer é a tela principal do jogo single player da cobrinha.
 * Esta classe implementa a interface Screen do LibGDX para gerenciar o ciclo de vida da tela.
 */
public class GameScreenSinglePlayer implements Screen {

    private SpriteBatch batch;
    private Snake snake;
    private Food food;
    private BitmapFont font;
    private boolean isGameOver;
    private boolean isPaused;
    private boolean isResuming;
    private float resumeTimer;

    /**
     * Construtor da tela de jogo single player.
     *
     * @param batch O SpriteBatch usado para renderizar os elementos na tela.
     */
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

    /**
     * Gera a fonte usada para exibir texto na tela.
     */
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

    /**
     * Alterna o estado de pausa do jogo.
     */
    public void togglePause() {
        if (isPaused) {
            isPaused = false;
            isResuming = true;
            resumeTimer = 1; // 1 segundo
        } else {
            isPaused = true;
        }
    }

    /**
     * Verifica se o jogo está pausado.
     *
     * @return true se o jogo estiver pausado, false caso contrário.
     */
    public boolean isPaused() {
        return isPaused;
    }

    /**
     * Verifica se o jogo está resumindo após a pausa.
     *
     * @return true se o jogo estiver resumindo, false caso contrário.
     */
    public boolean isResuming() {
        return isResuming;
    }

    /**
     * Reinicia o jogo, redefinindo todas as variáveis e elementos do jogo.
     */
    private void resetGame() {
        snake = new Snake(100, 100, Input.Keys.UP, Input.Keys.RIGHT, Input.Keys.DOWN, Input.Keys.LEFT, false);
        food = new Food(200, 200);
        isGameOver = false;
        isPaused = false;
        isResuming = false;
        resumeTimer = 0;
        SnakeInputProcessor inputProcessor = new SnakeInputProcessor(snake, this);
        Gdx.input.setInputProcessor(inputProcessor);
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
        String point = Integer.toString(snake.getBody().size());
        GlyphLayout layout = new GlyphLayout(font, point);
        float x= (Gdx.graphics.getWidth() - layout.width)/192;
        float y = (Gdx.graphics.getHeight() - layout.height);
        font.draw(batch, point, x, y);

        if (isPaused) {
            String pauseText = "Paused";
            layout = new GlyphLayout(font, pauseText);
            x = (Gdx.graphics.getWidth() - layout.width) / 2;
            y = (Gdx.graphics.getHeight() - layout.height) / 2;
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
                layout = new GlyphLayout(font, resumeText);
                x = (Gdx.graphics.getWidth() - layout.width) / 2;
                y = (Gdx.graphics.getHeight() - layout.height) / 2;
                font.draw(batch, resumeText, x, y);
                batch.end();
                return;
            }
        }
        if(!isGameOver) {
            if (snake.isAlive()) {
                snake.update(food, delta);
            } else {
                isGameOver = true;
            }
        }

        if (isGameOver) {
            String gameOverText = "Game Over";
            layout = new GlyphLayout(font, gameOverText);
            x = (Gdx.graphics.getWidth() - layout.width) / 2;
            y = (Gdx.graphics.getHeight() - layout.height) / 2;
            font.draw(batch, gameOverText, x, y);

            String restartText = "Press SPACE to restart";
            layout = new GlyphLayout(font, restartText);
            x = (Gdx.graphics.getWidth() - layout.width) / 2;
            y = (Gdx.graphics.getHeight() - layout.height) / 2 - 100;
            font.draw(batch, restartText, x, y);

            String menuText = "Press ESC to go to menu";
            layout = new GlyphLayout(font, menuText);
            x = (Gdx.graphics.getWidth() - layout.width) / 2;
            y = (Gdx.graphics.getHeight() - layout.height) / 2 - 150;
            font.draw(batch, menuText, x, y);

            handleGameOverInput();
        }
        batch.end();

    }

    /**
     * Processa as entradas do jogador na tela de game over.
     * Permite reiniciar o jogo ou voltar ao menu principal.
     */
    private void handleGameOverInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            resetGame();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            // Código para voltar ao menu inicial
            ((SnakeGame) Gdx.app.getApplicationListener()).setScreen(new MenuScreen());
        }
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
