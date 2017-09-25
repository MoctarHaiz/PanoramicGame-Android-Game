package com.MoctarHaiz.framework;

public interface Jeu {
	public Audio getAudio();
	public Input getInput();
	public FileIO getFileIO();
	public Graphics getGraphics();
	public void setScreen(Screen screen);
	public Screen setCurrentScreen();
	public Screen setInitScreen();
	Screen getInitScreen();

}
