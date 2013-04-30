package com.gamedev.ld26.goldenage.utils;

import com.badlogic.gdx.math.Rectangle;

public class Config {

	public static final String title = new String("Golden Age");
	public static final int window_width = 1280;
	public static final int window_height = 720;
	public static final int window_half_width = window_width / 2;
	public static final int window_half_height = window_height / 2;	
	public static final boolean use_gl2 = true;
	public static final boolean resizable = false;

	public static final int pong_paddle_size_x = 100;
	public static final int pong_paddle_size_y = 20;
	public static final Rectangle pong_window_bounds = new Rectangle(Config.window_half_width / 2, -40, Config.window_half_width, Config.window_height + 80);
	public static final Rectangle full_window_bounds = new Rectangle(0,0, Config.window_width, Config.window_height);
	public static final Rectangle g1942_window_bounds = new Rectangle(-20,-20, Config.window_width + 40, Config.window_height + 40);

}

