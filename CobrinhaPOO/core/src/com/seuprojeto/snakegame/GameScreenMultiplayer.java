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
 * GameScreenMultiplayer é a tela principal do jogo multiplayer da cobrinha.
 * Esta classe implementa a interface Screen do LibGDX para gerenciar o ciclo de vida da tela.
 */
public class GameScreenMultiplayer implements Screen {

    private SpriteBatch batch;
    private Snake snake1;
    private Snake snake2;
    private Food food1;
    private Food food2;
    private BitmapFont font;
    private boolean isGameOver;
    private boolean isPaused;
    private boolean isResuming;
    private float resumeTimer;
    private String winnerText;

    /**
     * Construtor da tela de jogo multiplayer.
     *
     * @param batch O SpriteBatch usado para renderizar os elementos na tela.
     */
    public GameScreenMultiplayer(SpriteBatch batch) {
        this.batch = batch;
        Gdx.app.log("GameScreenMultiplayer", "Criando tela multiplayer");

        // Inicializa as cobras e comidas
        snake1 = new Snake(100, 100, Input.Keys.W, Input.Keys.D, Input.Keys.S, Input.Keys.A, false);
        snake2 = new Snake(300, 300, Input.Keys.UP, Input.Keys.RIGHT, Input.Keys.DOWN, Input.Keys.LEFT, true);
        food1 = new Food(200, 200);
        food2 = new Food(400, 400);

        // Gera a fonte para exibição na tela
        generateFont();

        // Inicializa variáveis de controle de jogo
        isGameOver = false;
        isPaused = false;
        isResuming = false;
        resumeTimer = 0;
        winnerText = "";

        // Configura o input processor para gerenciar entradas de teclado
        SnakeInputProcessor inputProcessor = new SnakeInputProcessor(snake1, snake2, this);
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
        snake1 = new Snake(100, 100, Input.Keys.W, Input.Keys.D, Input.Keys.S, Input.Keys.A, false);
        snake2 = new Snake(300, 300, Input.Keys.UP, Input.Keys.RIGHT, Input.Keys.DOWN, Input.Keys.LEFT, true);
        food1 = new Food(200, 200);
        food2 = new Food(400, 400);
        isGameOver = false;
        isPaused = false;
        isResuming = false;
        resumeTimer = 0;
        winnerText = "";
        SnakeInputProcessor inputProcessor = new SnakeInputProcessor(snake1, snake2, this);
        Gdx.input.setInputProcessor(inputProcessor);
    }

    /**
     * Verifica as colisões entre as cobras e processa o resultado do jogo.
     */
    private void checkCollisions() {
        Snake.BodyPart head1 = snake1.getHead();
        Snake.BodyPart head2 = snake2.getHead();

        // Verifica colisão de cabeça da cobra 1 com o corpo da cobra 2
        for (int i = 1; i < snake2.getBody().size(); i++) {
            Snake.BodyPart part = snake2.getBody().get(i);
            if (head1.x == part.x && head1.y == part.y) {
                isGameOver = true;
                winnerText = "Snake 2 Wins!";
                return;
            }
        }

        // Verifica colisão de cabeça da cobra 2 com o corpo da cobra 1
        for (int i = 1; i < snake1.getBody().size(); i++) {
            Snake.BodyPart part = snake1.getBody().get(i);
            if (head2.x == part.x && head2.y == part.y) {
                isGameOver = true;
                winnerText = "Snake 1 Wins!";
                return;
            }
        }

        // Verifica colisão de cabeça com cabeça
        if (head1.x == head2.x && head1.y == head2.y) {
            if (snake1.getBody().size() > snake2.getBody().size()) {
                isGameOver = true;
                winnerText = "Snake 1 Wins!";
            } else if (snake1.getBody().size() < snake2.getBody().size()) {
                isGameOver = true;
                winnerText = "Snake 2 Wins!";
            } else {
                isGameOver = true;
                winnerText = "Draw!";
            }
        }
    }

    @Override
    public void show() {
        Gdx.app.log("GameScreenMultiplayer", "Mostrando tela multiplayer");
    }

    @Override
    public void render(float delta) {
        Gdx.app.log("GameScreenMultiplayer", "Renderizando tela multiplayer");
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        snake1.render(batch);
        snake2.render(batch);
        food1.render(batch);
        food2.render(batch);

        // Exibe o tamanho das cobras na tela
        String point = Integer.toString(snake2.getBody().size());
        GlyphLayout layout = new GlyphLayout(font, point);
        float x = (Gdx.graphics.getWidth() - layout.width);
        float y = (Gdx.graphics.getHeight() - layout.height);
        font.draw(batch, point, x, y);

        point = Integer.toString(snake1.getBody().size());
        layout = new GlyphLayout(font, point);
        x = (Gdx.graphics.getWidth() - layout.width) / 192;
        y = (Gdx.graphics.getHeight() - layout.height);
        font.draw(batch, point, x, y);

        // Verifica se o jogo está pausado
        if (isPaused) {
            String pauseText = "Paused";
            layout = new GlyphLayout(font, pauseText);
            x = (Gdx.graphics.getWidth() - layout.width) / 2;
            y = (Gdx.graphics.getHeight() - layout.height) / 2;
            font.draw(batch, pauseText, x, y);
            batch.end();
            return;
        }

        // Verifica se o jogo está resumindo após a pausa
        if (isResuming) {
            resumeTimer -= delta;
            if (resumeTimer <= 0) {
                isResuming = false;
            } else {
                String resumeText = "Resuming in " + (int) Math.ceil(resumeTimer);
                layout = new GlyphLayout(font, resumeText);
                x = (Gdx.graphics.getWidth() - layout.width) / 2;
                y = (Gdx.graphics.getHeight() - layout.height) / 2;
                font.draw(batch, resumeText, x, y);
                batch.end();
                return;
            }
        }

        // Verifica se o jogo não terminou e atualiza as cobras e verifica colisões
        if (!isGameOver) {
            if (snake1.isAlive() && snake2.isAlive()) {
                snake1.update(food1, food2, delta);
                if (!snake1.isAlive()) {
                    winnerText = "Snake 2 Wins!";
                }
                snake2.update(food1, food2, delta);
                if (!snake2.isAlive()) {
                    winnerText = "Snake 1 Wins!";
                }
                checkCollisions();
            } else {
                isGameOver = true;
            }
        }

        // Exibe a tela de game over caso o jogo tenha terminado
        if (isGameOver) {
            String gameOverText = "Game Over";
            layout = new GlyphLayout(font, gameOverText);
            x = (Gdx.graphics.getWidth() - layout.width) / 2;
            y = (Gdx.graphics.getHeight() - layout.height) / 2;
            font.draw(batch, gameOverText, x, y);

            layout = new GlyphLayout(font, winnerText);
            x = (Gdx.graphics.getWidth() - layout.width) / 2;
            y = (Gdx.graphics.getHeight() - layout.height) / 2 - 50;
            font.draw(batch, winnerText, x, y);

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
        Gdx.app.log("GameScreenMultiplayer", "Escondendo tela multiplayer");
    }

    @Override
    public void dispose() {
        Gdx.app.log("GameScreenMultiplayer", "Descartando tela multiplayer");
        snake1.dispose();
        snake2.dispose();
        food1.dispose();
        food2.dispose();
        font.dispose();
    }
}
