package com.seuprojeto.snakegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Input;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa a entidade Snake (cobra) no jogo.
 * Responsável por controlar o movimento, colisões, renderização e estado da cobra.
 */
public class Snake {
    private static final int TEXTURE_SIZE = 25; // Tamanho da textura
    private static final float MOVE_TIME = 0.2f; // Tempo entre os movimentos da cobra em segundos

    private List<BodyPart> body; // Lista de partes do corpo da cobra
    private Texture headTextureUp; // Textura da cabeça da cobra olhando para cima
    private Texture headTextureDown; // Textura da cabeça da cobra olhando para baixo
    private Texture headTextureLeft; // Textura da cabeça da cobra olhando para a esquerda
    private Texture headTextureRight; // Textura da cabeça da cobra olhando para a direita
    private Texture bodyTexture; // Textura do corpo da cobra
    private int direction; // Direção atual da cobra (0=cima, 1=direita, 2=baixo, 3=esquerda)
    private float timer; // Contador de tempo para controle dos movimentos da cobra
    private boolean isAlive; // Indica se a cobra está viva
    private int keyUp, keyRight, keyDown, keyLeft; // Teclas de controle para cima, direita, baixo e esquerda

    /**
     * Construtor padrão da cobra.
     * Inicia a cobra na posição padrão (100, 100) e define as teclas de controle padrão.
     */
    public Snake() {
        this(100, 100, Input.Keys.UP, Input.Keys.RIGHT, Input.Keys.DOWN, Input.Keys.LEFT, false);
    }

    /**
     * Construtor personalizado da cobra.
     *
     * @param startX        Posição inicial X da cobra
     * @param startY        Posição inicial Y da cobra
     * @param keyUp         Tecla para mover a cobra para cima
     * @param keyRight      Tecla para mover a cobra para a direita
     * @param keyDown       Tecla para mover a cobra para baixo
     * @param keyLeft       Tecla para mover a cobra para a esquerda
     * @param isSecondPlayer Indica se é a segunda cobra (para carregar texturas diferentes)
     */
    public Snake(int startX, int startY, int keyUp, int keyRight, int keyDown, int keyLeft, boolean isSecondPlayer) {
        body = new ArrayList<>();

        // Carrega as texturas da cabeça e do corpo da cobra dependendo se é o primeiro ou segundo jogador
        if (isSecondPlayer) {
            headTextureUp = new Texture("snakeheadU2.png");
            headTextureDown = new Texture("snakeheadD2.png");
            headTextureLeft = new Texture("snakeheadL2.png");
            headTextureRight = new Texture("snakeheadR2.png");
            bodyTexture = new Texture("snakebody2.png");
        } else {
            headTextureUp = new Texture("snakeheadU.png");
            headTextureDown = new Texture("snakeheadD.png");
            headTextureLeft = new Texture("snakeheadL.png");
            headTextureRight = new Texture("snakeheadR.png");
            bodyTexture = new Texture("snakebody.png");
        }

        // Adiciona a primeira parte do corpo da cobra na posição inicial
        body.add(new BodyPart(startX, startY));

        direction = 1; // Começa indo para a direita
        timer = 0;
        isAlive = true;
        this.keyUp = keyUp;
        this.keyRight = keyRight;
        this.keyDown = keyDown;
        this.keyLeft = keyLeft;
    }

    /**
     * Atualiza o estado da cobra.
     * Verifica movimento, colisões com o próprio corpo e comidas, e atualiza o timer de movimento.
     *
     * @param food  Instância de Food (comida) para detecção de colisão
     * @param delta Tempo decorrido desde a última atualização
     */
    public void update(Food food, float delta) {
        if (!isAlive) return;

        timer += delta;

        if (timer >= MOVE_TIME) {
            timer = 0;

            // Movimento da cabeça da cobra
            BodyPart head = body.get(0);
            int newX = head.x;
            int newY = head.y;

            switch (direction) {
                case 0:
                    newY += TEXTURE_SIZE; // Cima
                    if (newY >= Gdx.graphics.getHeight()) newY = 0; // Sai pela parte de cima e volta por baixo
                    break;
                case 1:
                    newX += TEXTURE_SIZE; // Direita
                    if (newX >= Gdx.graphics.getWidth()) newX = 0; // Sai pela direita e volta pela esquerda
                    break;
                case 2:
                    newY -= TEXTURE_SIZE; // Baixo
                    if (newY < 0) newY = Gdx.graphics.getHeight() - TEXTURE_SIZE; // Sai por baixo e volta por cima
                    break;
                case 3:
                    newX -= TEXTURE_SIZE; // Esquerda
                    if (newX < 0) newX = Gdx.graphics.getWidth() - TEXTURE_SIZE; // Sai pela esquerda e volta pela direita
                    break;
            }

            // Adiciona nova posição da cabeça e remove a última parte do corpo
            body.add(0, new BodyPart(newX, newY));

            // Verifica colisão com o próprio corpo
            for (int i = 1; i < body.size(); i++) {
                BodyPart part = body.get(i);
                if (part.x == newX && part.y == newY) {
                    isAlive = false;
                    break;
                }
            }

            if (isAlive) {
                body.remove(body.size() - 1);

                // Detecção de colisão com a comida
                if (head.x == food.getX() && head.y == food.getY()) {
                    body.add(new BodyPart(newX, newY)); // Adiciona uma nova parte ao corpo
                    food.respawn(); // Reposiciona a comida
                }
            }
        }
    }

    /**
     * Atualiza o estado da cobra para dois jogadores.
     *
     * @param food1 Instância de Food (comida) para detecção de colisão com o primeiro jogador
     * @param food2 Instância de Food (comida) para detecção de colisão com o segundo jogador
     * @param delta Tempo decorrido desde a última atualização
     */
    public void update(Food food1, Food food2, float delta) {
        if (!isAlive) return;

        timer += delta;

        if (timer >= MOVE_TIME) {
            timer = 0;

            // Movimento da cabeça da cobra
            BodyPart head = body.get(0);
            int newX = head.x;
            int newY = head.y;

            switch (direction) {
                case 0:
                    newY += TEXTURE_SIZE; // Cima
                    if (newY >= Gdx.graphics.getHeight()) newY = 0; // Sai pela parte de cima e volta por baixo
                    break;
                case 1:
                    newX += TEXTURE_SIZE; // Direita
                    if (newX >= Gdx.graphics.getWidth()) newX = 0; // Sai pela direita e volta pela esquerda
                    break;
                case 2:
                    newY -= TEXTURE_SIZE; // Baixo
                    if (newY < 0) newY = Gdx.graphics.getHeight() - TEXTURE_SIZE; // Sai por baixo e volta por cima
                    break;
                case 3:
                    newX -= TEXTURE_SIZE; // Esquerda
                    if (newX < 0) newX = Gdx.graphics.getWidth() - TEXTURE_SIZE; // Sai pela esquerda e volta pela direita
                    break;
            }

            // Adiciona nova posição da cabeça e remove a última parte do corpo
            body.add(0, new BodyPart(newX, newY));

            // Verifica colisão com o próprio corpo
            for (int i = 1; i < body.size(); i++) {
                BodyPart part = body.get(i);
                if (part.x == newX && part.y == newY) {
                    isAlive = false;
                    break;
                }
            }

            if (isAlive) {
                body.remove(body.size() - 1);

                // Detecção de colisão com as comidas
                if ((head.x == food1.getX() && head.y == food1.getY()) || (head.x == food2.getX() && head.y == food2.getY())) {
                    body.add(new BodyPart(newX, newY)); // Adiciona uma nova parte ao corpo
                    if (head.x == food1.getX() && head.y == food1.getY()) {
                        food1.respawn(); // Reposiciona a comida 1
                    }
                    if (head.x == food2.getX() && head.y == food2.getY()) {
                        food2.respawn(); // Reposiciona a comida 2
                    }
                }
            }
        }
    }

    /**
     * Renderiza a cobra na tela.
     *
     * @param batch Objeto SpriteBatch para desenhar as texturas da cobra
     */
    public void render(SpriteBatch batch) {
        Texture currentHeadTexture;
        switch (direction) {
            case 0:
                currentHeadTexture = headTextureUp;// cabeça para cima
                break;
            case 1:
                currentHeadTexture = headTextureRight; // cabeça para a direita
                break;
            case 2:
                currentHeadTexture = headTextureDown;// cabeça para baixo
                break;
            case 3:
                currentHeadTexture = headTextureLeft;// cabeça para a esquerda
                break;
            default:
                currentHeadTexture = headTextureRight; // Padrão para direita
                break;
        }

        // Desenha todas as partes da cobra (cabeça e corpo)
        for (int i = 0; i < body.size(); i++) {
            BodyPart part = body.get(i);
            if (i == 0) {
                batch.draw(currentHeadTexture, part.x, part.y, TEXTURE_SIZE, TEXTURE_SIZE);
            } else {
                batch.draw(bodyTexture, part.x, part.y, TEXTURE_SIZE, TEXTURE_SIZE);
            }
        }
    }

    /**
     * Libera os recursos da cobra ao finalizar o jogo.
     */
    public void dispose() {
        headTextureUp.dispose();
        headTextureDown.dispose();
        headTextureLeft.dispose();
        headTextureRight.dispose();
        bodyTexture.dispose();
    }

    /**
     * Verifica se a cobra está viva.
     *
     * @return true se a cobra está viva, false caso contrário
     */
    public boolean isAlive() {
        return isAlive;
    }

    /**
     * Obtém a direção atual da cobra.
     *
     * @return Direção atual da cobra (0=cima, 1=direita, 2=baixo, 3=esquerda)
     */
    public int getDirection() {
        return direction;
    }

    /**
     * Define a direção da cobra.
     *
     * @param direction Nova direção da cobra (0=cima, 1=direita, 2=baixo, 3=esquerda)
     */
    public void setDirection(int direction) {
        this.direction = direction;
    }

    /**
     * Verifica as teclas pressionadas pelo jogador para alterar a direção da cobra.
     *
     * @see com.badlogic.gdx.Input
     */
    public void handleInput() {
        if (Gdx.input.isKeyPressed(keyUp)) {
            if (direction != 2) direction = 0;
        }
        if (Gdx.input.isKeyPressed(keyRight)) {
            if (direction != 3) direction = 1;
        }
        if (Gdx.input.isKeyPressed(keyDown)) {
            if (direction != 0) direction = 2;
        }
        if (Gdx.input.isKeyPressed(keyLeft)) {
            if (direction != 1) direction = 3;
        }
    }

    /**
     * Obtém a parte da cabeça da cobra.
     *
     * @return Objeto BodyPart representando a parte da cabeça da cobra
     */
    public BodyPart getHead() {
        return body.get(0);
    }

    /**
     * Obtém a lista de partes do corpo da cobra.
     *
     * @return Lista de objetos BodyPart representando as partes do corpo da cobra
     */
    public List<BodyPart> getBody() {
        return body;
    }

    /**
     * Classe interna que representa uma parte do corpo da cobra.
     */
    public class BodyPart {
        int x, y;

        /**
         * Construtor de uma parte do corpo da cobra.
         *
         * @param x Posição X da parte do corpo
         * @param y Posição Y da parte do corpo
         */
        BodyPart(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
