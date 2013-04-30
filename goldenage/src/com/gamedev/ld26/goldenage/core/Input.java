package com.gamedev.ld26.goldenage.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.gamedev.ld26.goldenage.utils.Config;
import com.gamedev.ld26.goldenage.utils.Utils;

public class Input extends InputAdapter {
	
	private final Vector2 prevMouse= new Vector2(0,0);
	private final Vector2 currMouse = new Vector2(0,0);
	private final boolean[] keys = new boolean[256];
	private final boolean[] buttons = new boolean[3];
	
	public Input() {
		super();
		reset();
	}
	
	public void reset() {
		resetKeys();
		resetButtons();
	}
	
	public void resetKeys() {
		for(int i = 0; i < keys.length; ++i) {
			keys[i] = false;
		}
	}
	
	public void resetButtons() {
		for(int i = 0; i < buttons.length; ++i) {
			buttons[i] = false;
		}
	}
	
	public boolean isKeyDown(int keycode) {
		return keys[keycode];
	}
	
	public boolean isKeyUp(int keycode) {
		return !keys[keycode];
	}
	
	public boolean isButtonDown(int button) {
		return buttons[button];
	}
	
	public boolean isButtonUp(int button) {
		return !buttons[button];
	}
	
	public Vector2 getCurrMouse() { return currMouse; }
	public Vector2 getPrevMouse() { return prevMouse; }
	
	@Override
	public boolean keyDown(int keycode) {
		keys[keycode] = true;
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		keys[keycode] = false;
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		prevMouse.set(currMouse);
		currMouse.set(screenX, screenY);
		buttons[button] = true;
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		prevMouse.set(currMouse);
		currMouse.set(screenX, screenY);
		buttons[button] = false;
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		prevMouse.set(currMouse);
		currMouse.set(screenX, screenY);
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		prevMouse.set(currMouse);
		currMouse.set(screenX, screenY);
		Gdx.input.setCursorPosition((int)Utils.clamp(screenX, 0, Config.window_width),
									 (int)Utils.clamp(Config.window_height - screenY, 0, Config.window_height));
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
