package com.seuprojeto.snakegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Input;

import java.util.ArrayList;
import java.util.List;

public class Snake {
    private static final int TEXTURE_SIZE = 25; // Tamanho da textura
    private static final float MOVE_TIME = 0.2f; // Tempo entre os movimentos da cobra em segundos

    private List<BodyPart> body;
    private Texture headTextureUp;
    private Texture headTextureDown;
    private Texture headTextureLeft;
    private Texture headTextureRight;
    private Texture bodyTexture;
    private int direction;
    private float timer;
    private boolean isAlive;
    private int keyUp, keyRight, keyDown, keyLeft;

    public Snake() {
        this(100, 100, Input.Keys.UP, Input.Keys.RIGHT, Input.Keys.DOWN, Input.Keys.LEFT, false);
    }

    public Snake(int startX, int startY, int keyUp, int keyRight, int keyDown, int keyLeft, boolean isSecondPlayer) {
        body = new ArrayList<>();
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
        body.add(new BodyPart(startX, startY));
        direction = 1; // Começa indo para a direita
        timer = 0;
        isAlive = true;
        this.keyUp = keyUp;
        this.keyRight = keyRight;
        this.keyDown = keyDown;
        this.keyLeft = keyLeft;
    }

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

    public void render(SpriteBatch batch) {
        Texture currentHeadTexture;
        switch (direction) {
            case 0:
                currentHeadTexture = headTextureUp;
                break;
            case 1:
                currentHeadTexture = headTextureRight;
                break;
            case 2:
                currentHeadTexture = headTextureDown;
                break;
            case 3:
                currentHeadTexture = headTextureLeft;
                break;
            default:
                currentHeadTexture = headTextureRight; // Padrão para direita
                break;
        }

        for (int i = 0; i < body.size(); i++) {
            BodyPart part = body.get(i);
            if (i == 0) {
                batch.draw(currentHeadTexture, part.x, part.y, TEXTURE_SIZE, TEXTURE_SIZE);
            } else {
                batch.draw(bodyTexture, part.x, part.y, TEXTURE_SIZE, TEXTURE_SIZE);
            }
        }
    }

    public void dispose() {
        headTextureUp.dispose();
        headTextureDown.dispose();
        headTextureLeft.dispose();
        headTextureRight.dispose();
        bodyTexture.dispose();
    }

    public boolean isAlive() {
        return isAlive;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

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

    public BodyPart getHead() {
        return body.get(0);
    }

    public List<BodyPart> getBody() {
        return body;
    }

    public class BodyPart {
        int x, y;

        BodyPart(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
