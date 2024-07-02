package com.seuprojeto.snakegame;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

/**
 * SnakeInputProcessor é responsável por processar os eventos de entrada do jogador.
 * Ele detecta quando teclas são pressionadas e controla o comportamento das cobras nos modos de jogo multiplayer e single player.
 */
public class SnakeInputProcessor extends InputAdapter {
    private Snake snake1;
    private Snake snake2;
    private GameScreenMultiplayer gameScreenMultiplayer;
    private GameScreenSinglePlayer gameScreenSinglePlayer;

    /**
     * Construtor para SnakeInputProcessor usado no modo multiplayer.
     *
     * @param snake1                A instância da cobra do jogador 1.
     * @param snake2                A instância da cobra do jogador 2.
     * @param gameScreenMultiplayer A tela do jogo multiplayer associada.
     */
    public SnakeInputProcessor(Snake snake1, Snake snake2, GameScreenMultiplayer gameScreenMultiplayer) {
        this.snake1 = snake1;
        this.snake2 = snake2;
        this.gameScreenMultiplayer = gameScreenMultiplayer;
    }

    /**
     * Construtor para SnakeInputProcessor usado no modo single player.
     *
     * @param snake                 A instância da cobra do jogador.
     * @param gameScreenSinglePlayer A tela do jogo single player associada.
     */
    public SnakeInputProcessor(Snake snake, GameScreenSinglePlayer gameScreenSinglePlayer) {
        this.snake1 = snake;
        this.gameScreenSinglePlayer = gameScreenSinglePlayer;
    }

    /**
     * Método chamado quando uma tecla é pressionada.
     * Controla a pausa do jogo (tecla P) e envia comandos de movimento para as cobras ativas.
     *
     * @param keycode Código da tecla pressionada.
     * @return true se a entrada foi processada, false caso contrário.
     */
    @Override
    public boolean keyDown(int keycode) {
        if (gameScreenMultiplayer != null) {
            if (keycode == Input.Keys.P) {
                gameScreenMultiplayer.togglePause();
            } else {
                if (!gameScreenMultiplayer.isPaused() && !gameScreenMultiplayer.isResuming()) {
                    snake1.handleInput();
                    snake2.handleInput();
                }
            }
        } else if (gameScreenSinglePlayer != null) {
            if (keycode == Input.Keys.P) {
                gameScreenSinglePlayer.togglePause();
            } else {
                if (!gameScreenSinglePlayer.isPaused() && !gameScreenSinglePlayer.isResuming()) {
                    snake1.handleInput();
                }
            }
        }
        return true;
    }
}
