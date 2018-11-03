package com.ru.tgra.lab1.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ru.tgra.shapes.Maze3D;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = "Mindcraft"; // or whatever you like
		config.width = 1920;  //experiment with
		config.height = 1080;  //the window size
		config.x = 0;
		config.y = -1;
		new LwjglApplication(new Maze3D(), config);
	}
}
