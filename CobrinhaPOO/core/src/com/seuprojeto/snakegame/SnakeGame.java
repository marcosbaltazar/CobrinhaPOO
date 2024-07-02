package com.seuprojeto.snakegame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Classe principal do jogo Snake, que estende a classe Game do LibGDX.
 * Responsável por gerenciar o ciclo de vida da aplicação e as transições entre telas.
 */
public class SnakeGame extends Game {
	public SpriteBatch batch; // O batch usado para renderizar sprites na tela

	/**
	 * Método chamado quando o jogo é inicializado.
	 * Aqui, cria-se o batch para desenho e define a tela inicial como MenuScreen.
	 */
	@Override
	public void create() {
		batch = new SpriteBatch(); // Inicializa o batch para desenho de sprites
		setScreen(new MenuScreen()); // Define a tela inicial como MenuScreen
	}

	/**
	 * Método chamado a cada quadro (frame) para renderizar a tela ativa.
	 * Chama o método render da tela ativa (MenuScreen, GameScreen, etc.).
	 */
	@Override
	public void render() {
		super.render(); // Delega o método de renderização para a tela ativa
	}

	/**
	 * Método chamado quando o jogo está sendo fechado ou finalizado.
	 * É utilizado para liberar recursos, como o batch de sprites.
	 */
	@Override
	public void dispose() {
		batch.dispose(); // Libera o batch de sprites ao finalizar o jogo
	}
}
