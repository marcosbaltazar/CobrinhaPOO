package com.seuprojeto.snakegame;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

/**
 * Classe responsável por iniciar a versão desktop do jogo Snake.
 */
public class DesktopLauncher {

	/**
	 * Método principal que inicia a aplicação desktop.
	 *
	 * @param arg Argumentos de linha de comando (não são utilizados neste caso).
	 */
	public static void main(String[] arg) {
		// Configuração da aplicação desktop usando a biblioteca LibGDX
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("Snake Game"); // Define o título da janela do jogo
		config.setWindowedMode(800, 600); // Define o modo janela com largura e altura específicas
		new Lwjgl3Application(new SnakeGame(), config); // Cria uma nova aplicação usando a classe SnakeGame e a configuração fornecida
	}
}

