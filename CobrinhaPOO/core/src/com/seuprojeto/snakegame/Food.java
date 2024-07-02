package com.seuprojeto.snakegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

/**
 * Representa a comida (ou item) que aparece aleatoriamente no jogo Snake.
 * Responsável por renderizar a comida, reposicioná-la quando consumida e liberar recursos.
 */
public class Food {
    private static final int SIZE = 25; // Tamanho da textura da comida
    private Texture texture; // Textura da comida
    private int x, y; // Posição X e Y da comida na tela

    /**
     * Construtor da comida.
     *
     * @param startX Posição inicial X da comida
     * @param startY Posição inicial Y da comida
     */
    public Food(int startX, int startY) {
        texture = new Texture("food.png"); // Carrega a textura da comida a partir do arquivo "food.png"
        x = startX; // Define a posição X inicial da comida
        y = startY; // Define a posição Y inicial da comida
    }

    /**
     * Renderiza a comida na tela usando o objeto SpriteBatch fornecido.
     *
     * @param batch Objeto SpriteBatch para desenhar a comida
     */
    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y, SIZE, SIZE); // Desenha a textura da comida na posição (x, y) com o tamanho definido
    }

    /**
     * Reposiciona a comida em uma posição aleatória na tela.
     * A nova posição é um múltiplo do tamanho da comida para garantir que fique alinhada corretamente.
     */
    public void respawn() {
        x = MathUtils.random(0, (Gdx.graphics.getWidth() / SIZE) - 1) * SIZE; // Calcula uma posição X aleatória
        y = MathUtils.random(0, (Gdx.graphics.getHeight() / SIZE) - 1) * SIZE; // Calcula uma posição Y aleatória
    }

    /**
     * Libera os recursos da comida ao finalizar o jogo.
     */
    public void dispose() {
        texture.dispose(); // Libera a textura da comida
    }

    /**
     * Obtém a posição X atual da comida na tela.
     *
     * @return Posição X da comida
     */
    public int getX() {
        return x;
    }

    /**
     * Obtém a posição Y atual da comida na tela.
     *
     * @return Posição Y da comida
     */
    public int getY() {
        return y;
    }
}
