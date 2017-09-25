package com.MoctarHaiz.PanoramicGame;

import com.MoctarHaiz.framework.Graphics;
import com.MoctarHaiz.framework.Jeu;
import com.MoctarHaiz.framework.Graphics.ImageFormat;
import com.MoctarHaiz.framework.Screen;

public class SplashLoadingScreen extends Screen {
	public SplashLoadingScreen(Jeu game) {
		super(game);
	}

	@Override
	public void update(float deltaTime) {
		Graphics g = game.getGraphics();
		Assets.splash = g.newImage("HaizStudioSplash.png", ImageFormat.RGB565);

		game.setScreen(new LoadingScreen(game));

	}

	@Override
	public void paint(float deltaTime) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}

	@Override
	public void backButton() {

	}
}
