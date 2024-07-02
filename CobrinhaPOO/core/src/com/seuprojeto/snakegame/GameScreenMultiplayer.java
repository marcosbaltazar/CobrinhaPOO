package com.seuprojeto.snakegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
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

    private SpriteBatch batch; // Batch usado para renderizar os elementos na tela
    private Snake snake1; // Primeira cobra
    private Snake snake2; // Segunda cobra
    private Food food1; // Primeira comida
    private Food food2; // Segunda comida
    private BitmapFont font; // Fonte usada para exibir texto na tela
    private boolean isGameOver; // Indica se o jogo acabou
    private boolean isPaused; // Indica se o jogo está pausado
    private boolean isResuming; // Indica se o jogo está retomando após uma pausa
    private float resumeTimer; // Temporizador usado para retomar o jogo
    private String winnerText; // Texto exibido quando o jogo termina, indicando o vencedor
    private Music gameMusic; // Música de fundo do jogo

    /**
     * Construtor da tela de jogo multiplayer.
     *
     * @param batch O SpriteBatch usado para renderizar os elementos na tela.
     */
    public GameScreenMultiplayer(SpriteBatch batch) {
        this.batch = batch;
        Gdx.app.log("GameScreenMultiplayer", "Criando tela multiplayer");

        // Inicializa as cobras e as comidas
        snake1 = new Snake(100, 100, Input.Keys.W, Input.Keys.D, Input.Keys.S, Input.Keys.A, false);
        snake2 = new Snake(300, 300, Input.Keys.UP, Input.Keys.RIGHT, Input.Keys.DOWN, Input.Keys.LEFT, true);
        food1 = new Food(200, 200);
        food2 = new Food(400, 400);

        // Gera a fonte usada para exibir texto na tela
        generateFont();

        // Inicializa variáveis de controle de estado do jogo
        isGameOver = false;
        isPaused = false;
        isResuming = false;
        resumeTimer = 0;
        winnerText = "";

        // Carrega a música do jogo
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("game_music.mp3"));
        gameMusic.setLooping(true); // Define para tocar em loop
        gameMusic.play(); // Inicia a reprodução da música

        // Configura o input processor para lidar com entradas do jogador
        SnakeInputProcessor inputProcessor = new SnakeInputProcessor(snake1, snake2, this);
        Gdx.input.setInputProcessor(inputProcessor);
    }

    /**
     * Gera a fonte usada para exibir texto na tela.
     */
    private void generateFont() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("arial.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 24;
        parameter.color = Color.WHITE;
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
     * Verifica se o jogo está retomando após uma pausa.
     *
     * @return true se o jogo estiver retomando, false caso contrário.
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

        // Verifica colisão da cabeça da cobra 1 com o corpo da cobra 2
        for (int i = 1; i < snake2.getBody().size(); i++) {
            Snake.BodyPart part = snake2.getBody().get(i);
            if (head1.x == part.x && head1.y == part.y) {
                isGameOver = true;
                winnerText = "Cobra vermelha vence!";
                return;
            }
        }

        // Verifica colisão da cabeça da cobra 2 com o corpo da cobra 1
        for (int i = 1; i < snake1.getBody().size(); i++) {
            Snake.BodyPart part = snake1.getBody().get(i);
            if (head2.x == part.x && head2.y == part.y) {
                isGameOver = true;
                winnerText = "Cobra amarela vence!";
                return;
            }
        }

        // Verifica colisão de cabeça com cabeça
        if (head1.x == head2.x && head1.y == head2.y) {
            if (snake1.getBody().size() > snake2.getBody().size()) {
                isGameOver = true;
                winnerText = "Cobra vermelha vence!";
            } else if (snake1.getBody().size() < snake2.getBody().size()) {
                isGameOver = true;
                winnerText = "Cobra amarela vence!";
            } else {
                isGameOver = true;
                winnerText = "Empate!";
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

        // Renderiza a pontuação da cobra amarela
        String point = Integer.toString(snake2.getBody().size());
        GlyphLayout layout = new GlyphLayout(font, point);
        float x = (Gdx.graphics.getWidth() - layout.width);
        float y = (Gdx.graphics.getHeight() - layout.height);
        font.setColor(Color.YELLOW);
        font.draw(batch, "Cobra amarela: " + point, x - 250, y);

        // Renderiza a pontuação da cobra vermelha
        point = Integer.toString(snake1.getBody().size());
        layout = new GlyphLayout(font, point);
        x = (Gdx.graphics.getWidth() - layout.width) / 192;
        y = (Gdx.graphics.getHeight() - layout.height);
        font.setColor(Color.RED);
        font.draw(batch, "Cobra vermelha: " + point, x, y);
        font.setColor(Color.WHITE);

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
                String resumeText = "Resuming in " + (int) Math.ceil(resumeTimer);
                layout = new GlyphLayout(font, resumeText);
                x = (Gdx.graphics.getWidth() - layout.width) / 2;
                y = (Gdx.graphics.getHeight() - layout.height) / 2;
                font.draw(batch, resumeText, x, y);
                batch.end();
                return;
            }
        }

        if (!isGameOver) {
            if (snake1.isAlive() && snake2.isAlive()) {
                snake1.update(food1, food2, delta);
                if (!snake1.isAlive()) {
                    winnerText = "Cobra amarela vence!";
                }
                snake2.update(food1, food2, delta);
                if (!snake2.isAlive()) {
                    winnerText = "Cobra vermelha vence!";
                }
                checkCollisions();
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
            gameMusic.stop(); // Para a música quando voltar ao menu
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
        gameMusic.dispose(); // Libera a música do jogo
    }
}
