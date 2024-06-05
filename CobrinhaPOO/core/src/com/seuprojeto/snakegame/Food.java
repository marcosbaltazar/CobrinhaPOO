package com.seuprojeto.snakegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class Food {
    private static final int SIZE = 25;
    private Texture texture;
    private int x, y;

    public Food(int startX, int startY) {
        texture = new Texture("food.png");
        x = startX;
        y = startY;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y, SIZE, SIZE);
    }

    public void respawn() {
        x = MathUtils.random(0, (Gdx.graphics.getWidth() / SIZE) - 1) * SIZE;
        y = MathUtils.random(0, (Gdx.graphics.getHeight() / SIZE) - 1) * SIZE;
    }

    public void dispose() {
        texture.dispose();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
