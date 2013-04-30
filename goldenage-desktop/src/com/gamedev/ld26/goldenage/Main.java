package com.gamedev.ld26.goldenage;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.gamedev.ld26.goldenage.utils.Config;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = Config.title;
		cfg.useGL20 = Config.use_gl2;
		cfg.width = Config.window_width;
		cfg.height = Config.window_height;
		cfg.resizable = Config.resizable;
		
		new LwjglApplication(new GoldenAgeGame(), cfg);
		
	}
}
