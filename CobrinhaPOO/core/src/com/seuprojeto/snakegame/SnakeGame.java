package com.seuprojeto.snakegame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SnakeGame extends Game {
	public SpriteBatch batch;

	@Override
	public void create() {
		batch = new SpriteBatch();
		setScreen(new MenuScreen());
	}

	@Override
	public void render() {
		super.render(); // Delegate render method to the active screen
	}

	@Override
	public void dispose() {
		batch.dispose();
	}
}
