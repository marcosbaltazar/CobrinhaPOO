package com.seuprojeto.snakegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Tela de menu do jogo da cobrinha.
 * Permite ao jogador escolher entre modo single player e multiplayer.
 */
public class MenuScreen implements Screen {
    private SpriteBatch batch; // Batch para renderização
    private BitmapFont font; // Fonte para renderizar texto na tela
    private String singlePlayerText = "Single Player: Press 1"; // Texto para modo single player
    private String multiPlayerText = "Multiplayer: Press 2"; // Texto para modo multiplayer

    /**
     * Construtor da tela de menu.
     * Inicializa o batch e a fonte utilizados para renderização.
     */
    public MenuScreen() {
        batch = new SpriteBatch(); // Inicializa o SpriteBatch
        font = new BitmapFont(); // Inicializa a BitmapFont para renderizar texto
        font.setColor(Color.WHITE); // Define a cor do texto como branca
        font.getData().setScale(2); // Define a escala da fonte como 2 (tamanho maior)
    }

    @Override
    public void show() {
        // Método da interface Screen não utilizado nesta implementação
    }

    /**
     * Renderiza a tela de menu.
     *
     * @param delta Tempo passado desde o último render
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Limpa o buffer de cores da tela
        batch.begin(); // Inicia o batch para desenho

        // Calcula as posições para centralizar os textos na tela
        GlyphLayout singlePlayerLayout = new GlyphLayout(font, singlePlayerText);
        GlyphLayout multiPlayerLayout = new GlyphLayout(font, multiPlayerText);
        float singlePlayerX = (Gdx.graphics.getWidth() - singlePlayerLayout.width) / 2;
        float singlePlayerY = (Gdx.graphics.getHeight() / 2) + 50;
        float multiPlayerX = (Gdx.graphics.getWidth() - multiPlayerLayout.width) / 2;
        float multiPlayerY = (Gdx.graphics.getHeight() / 2) - 50;

        // Desenha os textos na tela
        font.draw(batch, singlePlayerText, singlePlayerX, singlePlayerY);
        font.draw(batch, multiPlayerText, multiPlayerX, multiPlayerY);

        batch.end(); // Finaliza o batch

        // Verifica se as teclas 1 ou 2 foram pressionadas para mudar para as respectivas telas de jogo
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_1)) {
            ((SnakeGame) Gdx.app.getApplicationListener()).setScreen(new GameScreenSinglePlayer(new SpriteBatch()));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_2)) {
            ((SnakeGame) Gdx.app.getApplicationListener()).setScreen(new GameScreenMultiplayer(new SpriteBatch()));
        }
    }

    @Override
    public void resize(int width, int height) {
        // Método da interface Screen não utilizado nesta implementação
    }

    @Override
    public void pause() {
        // Método da interface Screen não utilizado nesta implementação
    }

    @Override
    public void resume() {
        // Método da interface Screen não utilizado nesta implementação
    }

    @Override
    public void hide() {
        // Método da interface Screen não utilizado nesta implementação
    }

    /**
     * Libera os recursos utilizados pela tela de menu ao ser descartada.
     */
    @Override
    public void dispose() {
        batch.dispose(); // Libera o batch utilizado para renderização
        font.dispose(); // Libera a fonte utilizada para renderização de texto
    }
}