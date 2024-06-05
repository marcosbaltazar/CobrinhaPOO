package com.seuprojeto.snakegame;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public class SnakeInputProcessor extends InputAdapter {
    private Snake snake1;
    private Snake snake2;
    private GameScreenMultiplayer gameScreenMultiplayer;
    private GameScreenSinglePlayer gameScreenSinglePlayer;

    public SnakeInputProcessor(Snake snake1, Snake snake2, GameScreenMultiplayer gameScreenMultiplayer) {
        this.snake1 = snake1;
        this.snake2 = snake2;
        this.gameScreenMultiplayer = gameScreenMultiplayer;
    }

    public SnakeInputProcessor(Snake snake, GameScreenSinglePlayer gameScreenSinglePlayer) {
        this.snake1 = snake;
        this.gameScreenSinglePlayer = gameScreenSinglePlayer;
    }

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
